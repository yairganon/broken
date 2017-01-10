package server;

import data.Offer;
import data.Request;

import java.io.IOException;
import java.net.*;

public class UdpServerSocket {

    private byte[] receiveData;
    private byte[] sendData;
    private DatagramSocket serverSocket;
    private int tcpPort;

    public UdpServerSocket(int port) throws Exception{
        receiveData = new byte[20];
        sendData = new byte[1024];
        tcpPort = port;
    }

    public void receiveAndHandle() throws IOException {
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        serverSocket = new DatagramSocket(6000 , InetAddress.getByName("0.0.0.0"));
        serverSocket.setSoTimeout(2000);
        serverSocket.receive(receivePacket);

        receiveConnectionRequest(receivePacket);
    }

    private void receiveConnectionRequest(DatagramPacket receivePacket) throws IOException {
        Request requestMessage = new Request(receivePacket.getData());
        if(requestMessage.isValidRequest(receivePacket.getAddress())){
            System.out.println("contain");
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            sendData = new Offer(requestMessage, tcpPort).offerBytes();
            DatagramPacket sendPacket =
                    new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
        }else{
            System.out.println("Bed Request Or Self BroadCast");
        }
    }

    public void close() {
        if(serverSocket != null)
            serverSocket.close();
        serverSocket = null;
    }
}
