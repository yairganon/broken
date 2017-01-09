package data;

import org.apache.commons.lang3.ArrayUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class Offer {
    private byte[] prefix;
    private byte[] requestNumber;
    private byte[] port;
    private byte[] ip;

    public Offer(Request requestMessage, int tcpPort) throws UnknownHostException {
        prefix = "##Networking17##".getBytes();
        requestNumber = requestMessage.randomNumber;
        this.port = ByteBuffer.allocate(2).putShort((short) tcpPort).array();
        ip = InetAddress.getLocalHost().getAddress();
    }

    public Offer(byte[] data){
        prefix = ArrayUtils.subarray(data , 0, 16);
        requestNumber = ArrayUtils.subarray(data , 16, 20);
        port = ArrayUtils.subarray(data , 20, 22);
        ip = ArrayUtils.subarray(data , 22, 26);
    }


    public byte[] offerBytes(){
        return ArrayUtils
                .addAll(ArrayUtils
                                .addAll(prefix, requestNumber),
                        ArrayUtils
                                .addAll(port, ip));
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
        return (int) (ip[0]) + "." + (int) (ip[1]) + "." + (int) (ip[2]) + "." + (int) (ip[3]);
    }
}
