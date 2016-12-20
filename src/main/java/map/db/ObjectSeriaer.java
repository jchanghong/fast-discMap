package map.db;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.*;
import java.util.BitSet;

/**
 * Created by jiang on 2016/12/19 0019.
 */
@SuppressWarnings("TryWithIdenticalCatches")
public class ObjectSeriaer {
    //KryoException
    static Kryo kryo = new Kryo();
    private static byte[] buff = new byte[1024 * 4];
    static Output output = new Output(buff);

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

    public static byte[] getbytes2(Object o) {
        output.clear();
        try {
            kryo.writeObject(output, o);
            output.flush();
        } catch (KryoException e) {
            do {
                buff = new byte[buff.length * 2];
                output = new Output(buff);
                e.printStackTrace();
                try {
                    kryo.writeObject(output, o);
                    break;
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } while (true);
        }
        return output.toBytes();
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

    public static <T> T geto2(byte[] buff) {
        Input input = new Input(buff);
        return (T) kryo.readObject(input,Object.class);
    }


    public static void main(String[] args) {
        testtrenode();
    }

    private static void testtrenode() {
        long s = System.nanoTime();
        for (int i = 0; i < 20; i++) {

            BitSet bitSet = new BitSet(Pagesize.max_page_number);
            byte[] getbytes = ObjectSeriaer.getbytes(bitSet);
//            System.out.println(getbytes.length);
            bitSet = ObjectSeriaer.geto(getbytes);
        }
        System.out.println("javatime" + (System.nanoTime() - s) / 1000l);
        s = System.nanoTime();
        for (int i = 0; i < 20; i++) {
            Kryo kryo = new Kryo();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(2048);
            Output output = new Output(outputStream);
            BitSet bitSet = new BitSet(Pagesize.max_page_number);
            bitSet.set(3, true);
            kryo.writeObject(output, bitSet);
            output.close();
            byte[] buf = outputStream.toByteArray();
//            System.out.println(buf.length);
            Input input = new Input(new ByteArrayInputStream(buf));
            bitSet = kryo.readObject(input, BitSet.class);
        }
        System.out.println("yotime" + (System.nanoTime() - s) / 1000l);
    }

}
