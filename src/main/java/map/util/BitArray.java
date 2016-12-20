package map.util;

import java.nio.ByteBuffer;
import java.util.BitSet;

/**
 * Created by jiang on 2016/12/20 0020.
 * 位置从0开始-size-1,直接操作文件头
 * true falues 代表1,0
 */
public class BitArray {
    ByteBuffer buffer;

    /**
     * Instantiates a new Bit array.
     *
     * @param buffer the buffer mapbuff
     */
    public BitArray(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    /**
     * Size int.
     *
     * @return the int
     */
    public int size() {
        return buffer.capacity() * 8;
    }

    /**
     * Get boolean.
     *
     * @param index the index
     * @return the boolean
     */
    public boolean get(int index) {
        buffer.position(0);
        byte b1 = buffer.get(index / 8);
        if (b1 < 0) {
            int bb = 255;
            int indexinbyte = index % 8;
            byte b = (byte) (bb >>> (7 - indexinbyte));
            return (b &1)== 1 ? true : false;
        }
        int indexinbyte = index % 8;
        byte b = (byte) (b1 >> (7 - indexinbyte));
        return b == 1 ? true : false;
    }

    /**
     * Set.
     *
     * @param index the index 从0开始
     * @param b     the b
     */
    public void set(int index, boolean b) {
        buffer.position(0);
        byte b1 = buffer.get(index / 8);
        int indexinbyte = index % 8;
        byte b2 = (byte) ((1 << 7) >>> indexinbyte);
        if (b) {
            b1 = (byte) (b1 | b2);
            buffer.position(0);
            buffer.put(index / 8, b1);
            return;
        }
        int b3 = (255 - (1 << 7 >>> indexinbyte));
        b1 = (byte) (b1 & b3);
        buffer.position(0);
        buffer.put(index / 8, b1);

    }

    public static void main(String[] sss) {

        BitSet bitSet = new BitSet(8);
        bitSet.set(6, true);
        System.out.println(bitSet.get(6));
        System.out.println(bitSet.hashCode());
        BitSet bitSet1 = new BitSet(8);
        bitSet.and(bitSet1);
        System.out.println(bitSet.get(6));

    }
}
