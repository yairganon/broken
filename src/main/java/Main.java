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
            if(tcpServerSocket.isClose()){
                try{
                    tcpServerSocket.receiveAndHandle();
                }catch (SocketTimeoutException e) {
                    System.out.println("TCP Time out");
                }
            }else{
                tcpServerSocket.receiveMessage();
            }

            if(udpClientSocket.isUdpClose() && tcpServerSocket.isClose()) {
                try {
                    udpServerSocket.receiveAndHandle();
                    udpServerSocket.close();
                } catch (SocketTimeoutException e) {
                    System.out.println("UDP Time out");
                    udpServerSocket.close();
                }
            }

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
    }

    private static void sendMessageToTcpServer(TcpServerSocket tcpServerSocket, UdpClientSocket udpClientSocket) throws IOException {
        if(tcpServerSocket.isClose()){
            udpClientSocket.sendUserMessage();
        }else{
            udpClientSocket.sendServerMessage(tcpServerSocket.clientSentence);
        }
    }
}
