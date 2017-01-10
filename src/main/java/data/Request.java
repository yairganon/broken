package data;

import org.apache.commons.lang3.ArrayUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Random;

public class Request {
    private byte[] prefix;
    byte[] randomNumber;

    public Request(){
        prefix = "##Networking17##".getBytes();
        randomNumber =  ByteBuffer.allocate(4).putInt((new Random().nextInt(9000)+ 1000)).array();
    }

    public Request(byte[] data) {
        prefix = ArrayUtils.subarray(data , 0, 16);
        randomNumber = ArrayUtils.subarray(data , 16, 20);
    }

    public byte[] getBytes(){
        return ArrayUtils.addAll(prefix, randomNumber);
    }

    public boolean isValidRequest(InetAddress address) throws UnknownHostException {
        return new String(prefix).contains("Networking17")
                && !address.getHostAddress().equals(InetAddress.getLoopbackAddress());
    }

    @Override
    public String toString() {
        return new String(prefix)+ ByteBuffer.wrap(randomNumber).getInt();
    }
}
