package map.db;

import map.htree.MHtree;
import map.htree.MHtreeNode;
import sun.security.util.BitArray;

import java.io.*;
import java.util.BitSet;

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
        BitSet bitArray = new BitSet(Pagesize.MAXPAGENUMBER);
        bitArray.set(1,true);
        bitArray.set(2,true);
        bitArray.set(3,true);
        bitArray.set(9,true);
        for (int i = 0; i < 100; i++) {
            bitArray.set(i + 1, true);
        }
        System.out.println(ObjectSeriaer.getbytes(bitArray).length);
    }

    private static void testtrenode() {
        MHtreeNode mHtreeNode = new MHtreeNode(0, null, null);
//        mHtreeNode.childs = new MHtreeNode[mHtreeNode.code];
        System.out.println(ObjectSeriaer.getbytes(mHtreeNode).length);
        mHtreeNode = ObjectSeriaer.geto(ObjectSeriaer.getbytes(mHtreeNode));
        System.out.println(mHtreeNode.code);
        MHtree mHtree = new MHtree();
        System.out.println(ObjectSeriaer.getbytes(mHtree).length);
        System.out.println(MStorage.PAGES_PER_FILE);
    }

    /*
     Kryo kryo = new Kryo();
    // ...
    Output output = new Output(new FileOutputStream("file.bin"));
    SomeClass someObject = ...
    kryo.writeObject(output, someObject);
    output.close();
    // ...
    Input input = new Input(new FileInputStream("file.bin"));
    SomeClass someObject = kryo.readObject(input, SomeClass.class);
    input.close();
    * */

}
