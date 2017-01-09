package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServerSocket {

    private ServerSocket serverSocket;
    public String clientSentence;
    public TcpServerSocket() throws IOException {
        serverSocket = new ServerSocket(6789);
        System.out.println(InetAddress.getLocalHost().getHostAddress()+ ":6789");
        serverSocket.setSoTimeout(1000);
    }

    public void receiveAndHandle() throws IOException {
        System.out.println("Server TCP looking for connect");
        Socket connectionSocket = serverSocket.accept();
        System.out.println("Server recive TCP connect");
        receiveConnectionRequest(connectionSocket);
    }

    private void receiveConnectionRequest(Socket connectionSocket) throws IOException {
        BufferedReader inFromClient =
                new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        clientSentence = inFromClient.readLine();
        System.out.println("My TCP server Received: " + clientSentence);
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    public boolean close() {
        return serverSocket == null || serverSocket.isBound();
    }
}
