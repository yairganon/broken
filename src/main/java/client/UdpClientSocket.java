package client;

import data.Offer;
import data.Request;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;


public class UdpClientSocket {

    private DatagramSocket clientSocket;
    byte[] receiveData;
    byte[] sendData;
    public Socket clientTCPSocket;

    public UdpClientSocket() throws Exception {
        clientSocket = new DatagramSocket();
        sendData = new Request().getBytes();
        receiveData = new byte[26];
        clientSocket.setSoTimeout(1000);

    }

    public void sendReceiveAndHandleMessage() throws IOException {
        InetAddress IPAddress = InetAddress.getByName("255.255.255.255");
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 6000);
        clientSocket.send(sendPacket);
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);
        Offer offerRequest = new Offer(receivePacket.getData());
        System.out.println("FROM SERVER:" + offerRequest);
        clientSocket.close();
        System.out.printf("hefffre");
        openTcpConnection(offerRequest);
    }

    private void openTcpConnection(Offer offerRequest) throws IOException {
        clientTCPSocket = new Socket(offerRequest.getIpString(), offerRequest.getPort());
    }

    public void sendUserMessage() throws IOException {
        String sentence;
        BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));
        DataOutputStream outToServer = new DataOutputStream(clientTCPSocket.getOutputStream());
        System.out.println("Hey There Send A message!!!!");
        sentence = inFromUser.readLine();
        outToServer.writeBytes(sentence + '\n');
    }

    public void sendServerMessage(String clientSentence) throws IOException {
        DataOutputStream outToServer = new DataOutputStream(clientTCPSocket.getOutputStream());
        outToServer.writeBytes(clientSentence + '\n');
    }

    public boolean isTcpClose() {
        return clientTCPSocket == null ||  clientTCPSocket.isClosed();
    }


}
