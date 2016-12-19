package map.db;

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

        byte[] bytes = new byte[100000];
        byte[] bb = ObjectSeriaer.getbytes(bytes);
        System.out.println(bb.length);
        byte[] a = ObjectSeriaer.geto(bb);
        System.out.println(a.length);
        BitSet bitArray = new BitSet(Pagesize.MAXPAGENUMBER);
        bitArray.set(1,true);
        bitArray.set(2,true);
        bitArray.set(9,true);
        System.out.println(bitArray.toString());
        System.out.println(ObjectSeriaer.getbytes(bitArray).length/1024l);
        System.out.println(Pagesize.MAXPAGENUMBER*1024*4l/1024/1024/1024);
        testtrenode();
    }

    private static void testtrenode() {
        MHtreeNode mHtreeNode = new MHtreeNode(0, null, null);
//        mHtreeNode.childs = new MHtreeNode[mHtreeNode.code];
        System.out.println(ObjectSeriaer.getbytes(mHtreeNode).length);
        mHtreeNode = ObjectSeriaer.geto(ObjectSeriaer.getbytes(mHtreeNode));
        System.out.println(mHtreeNode.code);
    }

}
