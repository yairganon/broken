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
            if(tcpServerSocket.close()){
                try{
                    tcpServerSocket.receiveAndHandle();
                }catch (SocketTimeoutException e) {
                    System.out.println("TCP Time out");
                }
            }

            try{
                udpServerSocket.receiveAndHandle();
            }catch (SocketTimeoutException e) {
                System.out.println("UDP Time out");
            }

            if(udpClientSocket.isTcpClose()) {
                try {
                    udpClientSocket.sendReceiveAndHandleMessage();
                    sendMessageToTcpServer(tcpServerSocket, udpClientSocket);
                } catch (SocketTimeoutException e) {
                    System.out.println("UDP Client Time out");
                }
            }else{
                sendMessageToTcpServer(tcpServerSocket, udpClientSocket);
            }

        }
    }

    private static void sendMessageToTcpServer(TcpServerSocket tcpServerSocket, UdpClientSocket udpClientSocket) throws IOException {
        if(tcpServerSocket.close()){
            udpClientSocket.sendUserMessage();
        }else{
            udpClientSocket.sendServerMessage(tcpServerSocket.clientSentence);
        }
    }
}
