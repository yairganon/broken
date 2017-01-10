import client.UdpClientSocket;
import server.TcpServerSocket;
import server.UdpServerSocket;

import java.io.IOException;
import java.net.*;

public class Main {

    public static void main (String [] args) throws Exception {
        TcpServerSocket tcpServerSocket = new TcpServerSocket();
        UdpServerSocket udpServerSocket = new UdpServerSocket(tcpServerSocket.getPort());
        UdpClientSocket udpClientSocket = new UdpClientSocket();

        while (true) {
            handleTcpServer(tcpServerSocket);
            handleUdpServer(tcpServerSocket, udpServerSocket, udpClientSocket);
            handleClient(tcpServerSocket, udpClientSocket);
        }
    }

    private static void handleClient(TcpServerSocket tcpServerSocket, UdpClientSocket udpClientSocket) throws IOException {
        if(udpClientSocket.isTcpClose() && tcpServerSocket.isClose()) {
            try {
                udpClientSocket.sendReceiveAndHandleMessage();
                sendMessageToTcpServer(tcpServerSocket, udpClientSocket);
            } catch (SocketTimeoutException e) {
                System.out.println("UDP Client Time out");
                udpClientSocket.closeUdp();
            }
        }else if(!udpClientSocket.isTcpClose()){
            sendMessageToTcpServer(tcpServerSocket, udpClientSocket);
        }
    }

    private static void handleUdpServer(TcpServerSocket tcpServerSocket, UdpServerSocket udpServerSocket, UdpClientSocket udpClientSocket) throws IOException {
        if(udpClientSocket.isUdpClose() && tcpServerSocket.isClose()) {
            try {
                udpServerSocket.receiveAndHandle();
                udpServerSocket.close();
            } catch (SocketTimeoutException e) {
                System.out.println("UDP Time out");
                udpServerSocket.close();
            }
        }
    }

    private static void handleTcpServer(TcpServerSocket tcpServerSocket) throws IOException {
        if(tcpServerSocket.isClose()){
            try{
                tcpServerSocket.receiveAndHandle();
            }catch (SocketTimeoutException e) {
                System.out.println("TCP Time out");
            }
        }else{
            tcpServerSocket.receiveMessage();
        }
    }

    private static void sendMessageToTcpServer(TcpServerSocket tcpServerSocket, UdpClientSocket udpClientSocket) throws IOException {
        if(tcpServerSocket.isClose()){
            udpClientSocket.sendUserMessage();
        }else{
            udpClientSocket.sendServerMessage(tcpServerSocket.clientSentence);
        }
    }
}
