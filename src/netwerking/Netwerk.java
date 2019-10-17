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

public class Netwerk {
    private Socket sock;
    private DataInputStream dis;
    private DataOutputStream dos;
    private MessageUnpacker unpack;
    private MessagePacker pack;

    public ConcurrentLinkedQueue<Yam> outQueue;
    public ConcurrentLinkedQueue<Yam> inQueue;

    private static Netwerk netwerk;
    private Thread readThread;
    private Thread writeThread;

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

    class NetwerkReadThread extends Thread {
        @Override
        public void run() {
            Logger.getInstance().logMessage("Starting read thread");

            while (!sock.isClosed()) {
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

    class NetwerkWriteThread extends Thread {
        @Override
        public void run() {

            Logger.getInstance().logMessage("Starting write thread");

            while(!sock.isClosed()) {
                if (!outQueue.isEmpty()) {
                    try {
                        pack.packValue(outQueue.poll().getMsg());
                        pack.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            Logger.getInstance().logMessage("Write thread was killed");
        }
    }

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

            readThread = new NetwerkReadThread();
            writeThread = new NetwerkWriteThread();
            readThread.start();
            writeThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
    }

    public void test() {
        List<Value> args = new ArrayList<>();
        args.add(ValueFactory.newString("testing lmao"));
        args.add(ValueFactory.newBoolean(false));
        args.add(ValueFactory.newFloat(3.141592651));
        for(int i = 0; i < 10; i++) {
            Yam testYam = new Yam(1, i, args);
            this.outQueue.add(testYam);
        }
    }
}
