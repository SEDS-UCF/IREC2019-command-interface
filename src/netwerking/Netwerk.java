package netwerking;

import logs.Logger;
import msgPack.Yam;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;
import org.msgpack.value.ImmutableArrayValue;
import org.msgpack.value.Value;
import org.msgpack.value.ValueFactory;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;


//This class is responsible for creating and managing the TCP interface
//for all incoming and outgoing commands. We implement a thread safe
//input and output queue that act as the access point to send and retrieve
//data sent over the network. All data sent over the network conforms to the
//message packing protocol we implement in the 'Yam' class. The general
//format of a 'Yam' message is [int cmd, int dest, Value[] args] where
//Value[] args is an array of dynamic data types that can be any length.

//We use the singleton design pattern since we only ever want 1 network
//interface running at anytime. This class also exposes methods for
//interfacing with the input and output queue for retrieving and sending
//data.

public class Netwerk
{
    public ConcurrentLinkedQueue<Yam> outQueue;
    public ConcurrentLinkedQueue<Yam> inQueue;

    private Socket sock;
    private DataInputStream dis;
    private DataOutputStream dos;
    private MessageUnpacker unpack;
    private MessagePacker pack;
    private Thread readThread;
    private Thread writeThread;

    private static Netwerk netwerk;

    private Netwerk()
    {
        outQueue = new ConcurrentLinkedQueue<>();
        inQueue = new ConcurrentLinkedQueue<>();
    }

    public static Netwerk getInstance()
    {
        if(netwerk == null)
        {
            netwerk = new Netwerk();
            Logger.getInstance().logMessage("Instantiated Network Interface");
        }

        return netwerk;
    }

    //Because we want to concurrent read and write functionality from
    //the network interface, we implement a read and write thread to
    //wait for incoming and outgoing traffic. For the read thread, the
    //output stream is passed directly to the message pack library to
    //create a new 'yam' object which is then added to the input queue
    class NetwerkReadThread extends Thread
    {
        @Override
        public void run()
        {
            Logger.getInstance().logMessage("Starting read thread");

            while (!sock.isClosed())
            {
                try {
                    if(unpack.hasNext()) {
                        ImmutableArrayValue msg = unpack.unpackValue().asArrayValue();
                        Yam newYam = new Yam(
                                msg.get(0).asIntegerValue().asInt(),
                                msg.get(1).asIntegerValue().asInt(),
                                msg.get(2).asArrayValue().list()
                        );
                        inQueue.add(newYam);
                    }
                } catch (SocketTimeoutException e) {
                } catch (SocketException e) {
                    Logger.getInstance().logErrorMessage("The socket was closed during a read operation.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Logger.getInstance().logMessage("Read thread was killed");
        }
    }


    //Because we want to concurrent read and write functionality from
    //the network interface, we implement a read and write thread to
    //wait for incoming and outgoing traffic. For the write thread,
    //we simply check to see if the output queue contains any 'Yam'
    //objects and if there are, we pass it to the output stream and
    //pop that 'Yam' from the output queue.
    class NetwerkWriteThread extends Thread {
        @Override
        public void run() {

            Logger.getInstance().logMessage("Starting write thread");

            while(!sock.isClosed()) {
                if (!outQueue.isEmpty()) {
                    try {
                        pack.packValue(outQueue.poll().getYam());
                        pack.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            Logger.getInstance().logMessage("Write thread was killed");
        }
    }

    //This method must be called before any data can be sent or received.
    //Handles starting the TCP connection with the specified host on the
    //specified port. First we check to see if the socket is already open
    //and if not we begin instantiating a new socket, input/output stream,
    //and the read/write threads. While the socket maintains a connection
    //the read/write threads continue running and are killed once the socket
    //is closed.
    public void connect(String host, int port) {

        if(sock != null && !sock.isClosed())
        {
            return;
        }

        try {
            sock = new Socket(host, port);
            sock.setSoTimeout(5000);

            Logger.getInstance().logMessage("Socket has been connected at " + host + ":" + port);

            dis = new DataInputStream(sock.getInputStream());
            dos = new DataOutputStream(sock.getOutputStream());
            unpack = MessagePack.newDefaultUnpacker(dis);
            pack = MessagePack.newDefaultPacker(dos);

            inQueue.clear();
            outQueue.clear();

            readThread = new NetwerkReadThread();
            writeThread = new NetwerkWriteThread();
            readThread.start();
            writeThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Before the socket is closed we have to close the IO streams.
    //To do this we explicitly close both the message pack
    //wrappers for the IO streams as well as calling the
    //close method directly on the streams in case the wrapper
    //can't close them implicitly. Once all of the IO streams
    //have been closed, we can close the socket and finally
    //kill the read/write threads.
    public void closeConnection()
    {
        if(sock != null && !sock.isClosed())
        {
            try {
                pack.close();
                unpack.close();
                dis.close();
                dos.close();
                sock.close();

                readThread.join();
                writeThread.join();

                Logger.getInstance().logMessage("All networking threads dead, connection closed.");
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Logger.getInstance().logErrorMessage("Connection is already closed");
        }
    }



    //NOT PERMANENT METHOD
    public void test() {
        List<Value> args = new ArrayList<>();
        args.add(ValueFactory.newString("testing lmao"));
        args.add(ValueFactory.newBoolean(false));
        args.add(ValueFactory.newFloat(3.141592651));

        //Yam empty = new Yam(1,2, new Value[] {});

        for(int i = 0; i < 10; i++) {
            Yam testYam = new Yam(1, i, args);
            //this.outQueue.add(empty);
            this.outQueue.add(testYam);
        }
    }
}
