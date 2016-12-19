package map.db;

import java.io.*;

/**
 * Created by jiang on 2016/12/19 0019.
 */
@SuppressWarnings("TryWithIdenticalCatches")
public class ObjectSeriaer {
    private static byte[] buff = new byte[1024 * 4];


    public static byte[] getbytes(Object o) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
            objectOutputStream.writeObject(o);
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T geto(byte[] buff) {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(buff));
            return (T) inputStream.readObject();
        } catch (IOException e) {

            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static void main(String[] args) {
        byte[] bbb = getbytes("hello");
        getbytes("ddddddddddddddddddddddddd");
        System.out.println(geto(bbb) + "");
        System.out.println(getbytes("hello").length);
        System.out.println(getbytes("hello").length);
        byte[] bb = getbytes("hello");
        String b = geto(bb);
        b = geto(bb);
        System.out.println(b);

    }

}
