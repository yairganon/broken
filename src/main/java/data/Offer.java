package data;

import org.apache.commons.lang3.ArrayUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class Offer {
    private byte[] prefix;
    private byte[] requestNumber;
    private byte[] ip;
    private byte[] port;

    public Offer(Request requestMessage, int tcpPort) throws UnknownHostException {
        prefix = "##Networking17##".getBytes();
        requestNumber = requestMessage.randomNumber;
        this.port = ByteBuffer.allocate(2).putShort((short) tcpPort).array();
        ip = InetAddress.getLocalHost().getAddress();
    }

    public Offer(byte[] data) throws UnknownHostException {
        prefix = ArrayUtils.subarray(data , 0, 16);
        requestNumber = ArrayUtils.subarray(data , 16, 20);
        ip = ArrayUtils.subarray(data , 20, 24);
        port = ArrayUtils.subarray(data , 24, 26);
        System.out.println(InetAddress.getByAddress(ip).getHostAddress());
    }


    public byte[] offerBytes(){
        return ArrayUtils
                .addAll(ArrayUtils
                                .addAll(prefix, requestNumber),
                        ArrayUtils
                                .addAll(ip , port));
    }

    public int getPort(){
        return ByteBuffer.wrap(port).getShort();
    }

    @Override
    public String toString() {
        return new String(prefix) + " " +
                ByteBuffer.wrap(requestNumber).getInt() + " " +
                getIpString() + ":" + ByteBuffer.wrap(port).getShort();
    }

    public String getIpString() {
        return (ip[0] & 0xFF) + "." + (ip[1] & 0xFF) + "." + (ip[2] & 0xFF) + "." + (ip[3] & 0xFF);
    }
}
