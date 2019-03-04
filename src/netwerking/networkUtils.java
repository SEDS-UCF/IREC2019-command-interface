package netwerking;


import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

public class networkUtils
{
    private DatagramSocket socket;
    private InetAddress address;

    private String host = "lax.benrstraw.xyz";
    private int port = 8274;

    private int FNV_32_PRIME = 0x01000193;
    private int FNV1_32_INIT = 0x811c9dc5;


    public boolean establishControlComputerConnection()
    {
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(1000);
        } catch (SocketException e) {
            e.printStackTrace();
            return false;
        }
        try {
            address = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

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

        } catch (IOException e)
        {
           System.out.println("Generic Catch for receive");
        }
        return new String(packet.getData(), 0, packet.getLength());
    }



    public void closeControlComputerConnection()
    {
        socket.close();
        System.out.println("We closed");
    }
}
