package map.db;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;

import static org.junit.Assert.*;

/**
 * Created by jiang on 2016/12/20 0020.
 */
public class ObjectSeriaerTest {
    static       Kryo kryo = new Kryo();


    @Test
    public void TESTJAVA() throws Exception {
        for (int i = 0; i < 20; i++) {

            BitSet bitSet = new BitSet(Pagesize.max_page_number);
            byte[] getbytes = ObjectSeriaer.getbytes(bitSet);
            System.out.println(getbytes.length);
//            bitSet = ObjectSeriaer.geto(getbytes);
            assertEquals(Pagesize.max_page_number, bitSet.size());
        }
    }

    static byte[] bytes = new byte[1024 * 17];
    Output output = new Output(bytes);
    @Test
    public void testkryo() throws Exception {
        for (int i = 0; i < 20; i++) {
//            Kryo kryo = new Kryo();

            output.clear();
            BitSet bitSet = new BitSet(Pagesize.max_page_number);
            kryo.writeClassAndObject(output, bitSet);
            byte[] buf = output.toBytes();
            System.out.println(buf.length);
            Input input = new Input(buf);
            bitSet = (BitSet) kryo.readClassAndObject(input);
            assertEquals(Pagesize.max_page_number, bitSet.size());
        }

    }

    @Test
    public void testyotring() throws Exception {
        for (int i = 0; i < 200; i++) {
            output.clear();
            String string = "hhhhhhhhhh";
            kryo.writeObject(output, string);
//            output.close();
            byte[] buf = output.toBytes();
            System.out.println(buf.length);
            Input input = new Input(buf);
            string = kryo.readObject(input, String.class);
            assertEquals("hhhhhhhhhh", string);
        }
    }

    @Test
    public void javastring() throws Exception {
        for (int i = 0; i < 200; i++) {

            String string = "hhhhhhhhhh";
            byte[] getbytes = ObjectSeriaer.getbytes(string);
            System.out.println(getbytes.length);
//            string = ObjectSeriaer.geto(getbytes);
            assertEquals("hhhhhhhhhh", string);
        }
    }

//    public byte[] bytes = new byte[1024 * 1024];
    public DHtreeNode dHtreeNode = new DHtreeNode(1, "dfffff", "ggg");
    @Before
    public void setUp() throws Exception {
//        dHtreeNode.childs = new DHtreeNode[dHtreeNode.code];
//        kryo.setReferences(false);
//        kryo.register(BitSet.class, 9);
       System.out.println(kryo.getSerializer(int[].class));


    }

    @Test
    public void testko() throws Exception {
        for (int i = 0; i < 1; i++) {
//            Kryo kryo = new Kryo();
            output.clear();
            DHtreeNode dHtreeNode = new DHtreeNode(1, "dd", "dd");
            kryo.writeClassAndObject(output, dHtreeNode);
//            output.close();
            byte[] buf = output.toBytes();
            System.out.println(buf.length);
            Input input = new Input(buf);
            dHtreeNode = (DHtreeNode) kryo.readClassAndObject(input);
            assertEquals("dd", dHtreeNode.values);
        }
    }

    @Test
    public void test111() throws Exception {

        for (int i = 0; i < 2; i++) {
            BitSet bitSet = new BitSet(Pagesize.max_page_number*10);
            int size = bitSet.size();
            byte[] getbytes = ObjectSeriaer.getbytes(bitSet);
            System.out.println(getbytes.length);
            bitSet = ObjectSeriaer.getObject(getbytes);
            assertEquals(size, bitSet.size());
        }
    }
}