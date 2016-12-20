package map.util;

import java.nio.ByteBuffer;

/**
 * Created by jiang on 2016/12/20 0020.
 * 位置从0开始-size-1
 * true falues 代表1,0
 */
public class BitArray {
    ByteBuffer buffer;

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
        byte b3 = (byte) ((1 << 8) - 1 - (1 << 7 >>> indexinbyte));
        b1 = (byte) (b1 & b3);
        buffer.position(0);
        buffer.put(index / 8, b1);

    }

    public static void main(String[] sss) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(3);
        BitArray array = new BitArray(byteBuffer);
        array.set(22, true);
        array.set(21, true);
        array.set(23, true);
        System.out.println(array.get(22));
        byteBuffer.position(2);
        System.out.println(byteBuffer.get());
//        System.out.println(array.size());

    }
}
