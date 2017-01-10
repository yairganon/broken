package client;

import data.Offer;
import data.Request;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;


public class UdpClientSocket {

    private DatagramSocket clientSocket;
    byte[] receiveData;
    byte[] sendData;
    public Socket clientTCPSocket;

    public UdpClientSocket() throws Exception {
        sendData = new Request().getBytes();
        receiveData = new byte[26];

    }

    public void sendReceiveAndHandleMessage() throws IOException {
        clientSocket = new DatagramSocket();
        clientSocket.setSoTimeout(1000);
        InetAddress IPAddress = InetAddress.getByName("255.255.255.255");
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 6000);
        clientSocket.send(sendPacket);
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);
        Offer offerRequest = new Offer(receivePacket.getData());
        System.out.println("FROM SERVER:" + offerRequest);
        clientSocket.close();
        openTcpConnection(offerRequest);
    }

    private void openTcpConnection(Offer offerRequest) throws IOException {
        System.out.println("Try to open clientTCPSocket");
        System.out.println("IP :" + offerRequest.getIpString() + ":" +  offerRequest.getPort());
        while(isTcpClose()) {
            try {
                clientTCPSocket = new Socket();
                clientTCPSocket.connect(new InetSocketAddress(offerRequest.getIpString(), offerRequest.getPort()), 200);
            }catch (SocketTimeoutException e) {

            }
        }
        System.out.println("clientTCPSocket open ");
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
        return clientTCPSocket == null ||  clientTCPSocket.isBound();
    }

    public void closeUdp(){
        if(clientSocket != null)
            clientSocket.close();
        clientSocket = null;
    }
}
