package netwerking;


import logs.Logger;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

//This class will wrap up the network interface into
//some handy methods. We also implement this class
//using the singleton design pattern to make sure
//there never exists more than 1 instance of the class

public class networkUtils
{
    private DatagramSocket socket = null;
    private InetAddress address = null;

    private String host = "localhost";
    private int port = 8274;

    private int FNV_32_PRIME = 0x01000193;
    private int FNV1_32_INIT = 0x811c9dc5;
    private int timeout = 4000;


    private static networkUtils networkUtils = null;


    private networkUtils() {}

    public static networkUtils getInstance()
    {
        if(networkUtils == null)
        {
            Logger.getInstance().logMessage("Instantiated network interface");
            networkUtils = new networkUtils();
        }

        return networkUtils;
    }


    //Return codes:
    //0 = Normal connection established
    //-1 = Failed to bind to open port. connection failed
    //-2 = Failed to resolve host. connection failed
    public int establishControlComputerConnection()
    {
        if(socket != null)
        {
            Logger.getInstance().logErrorMessage("Failed to create socket (Socket is already open)");
            return 0;
        }

        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(timeout);
            Logger.getInstance().logMessage("Created socket to " + host + " on port " + String.valueOf(port));

        } catch (SocketException e)
        {
            //Socket Exception is thrown when the constructor
            //fails to bind to an open port on local host so
            //set socket to null to enforce no connection

            socket = null;
            Logger.getInstance().logErrorMessage("Socket Exception: Could not bind to open port");

            return -1;
        }
        try
        {
            address = InetAddress.getByName(host);

        }catch (UnknownHostException e)
        {
            //Failed to resolve host
            address = null;
            Logger.getInstance().logErrorMessage("Unknown Host Exception: Failed to resolve host");
            return -2;
        }

        return 0;
    }

    private boolean isConnected()
    {
        return socket != null && address != null;

    }

    //This is a comment!

    //fnv Hash is used as a checksum
    private byte[] fnvHashMessage(String message)
    {
        int hval = FNV1_32_INIT;

        byte msgArr[] = message.getBytes();

        for(int i=0; i<message.length(); i++)
        {
            hval ^= message.getBytes()[i];
            hval *= FNV_32_PRIME;
        }

        byte hash[] = ByteBuffer.allocate(4).putInt(hval).array();


        byte[] c = new byte[msgArr.length + hash.length];
        System.arraycopy(msgArr, 0, c, 0, msgArr.length);
        System.arraycopy(hash, 0, c, msgArr.length, hash.length);

        return c;

    }



    public String sendRequest(String message) {

        if(!isConnected())
        {
            Logger.getInstance().logErrorMessage("Failed to send request (Connection has not been established) \nRequest: " + message);
            return "Not Connected";
        }

        byte[] data = fnvHashMessage(message);

        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);


        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        packet = new DatagramPacket(data, data.length);

        try {
            socket.receive(packet);

        } catch (SocketTimeoutException e)
        {
            Logger.getInstance().logErrorMessage("Socket Timeout: Failed to send request");
        } catch (IOException e)
        {
           System.out.println("Generic Catch for receive");
        }
        return new String(packet.getData(), 0, packet.getLength());
    }



    public void closeControlComputerConnection()
    {
        if(socket.isClosed())
        {
            Logger.getInstance().logErrorMessage("Failed to close socket (socket is already closed)");
            return;
        }
        socket.close();
        Logger.getInstance().logMessage("Socket closed");
    }
}
