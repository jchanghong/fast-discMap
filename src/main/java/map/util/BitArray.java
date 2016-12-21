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
    static int mask = 0xff;
    static int true0 = 128;
    static int treue1 = 64;
    static int treue2 = 32;
    static int treue3 = 16;
    static int treue4 = 8;
    static int treue5 = 4;
    static int treue6 = 2;
    static int true7 = 1;
    static int f0 = 127;
    static int f1 = 0xbf;
    static int f2 = 0xdf;
    static int f3 = 0xef;
    static int f4 = 0xf7;
    static int f5 = 0xfb;
    static int f6 = 0xfd;
    static int f7 = 0xfe;

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
        int b1 = buffer.get(index / 8) & mask;
        switch (index%8) {
            case 0:
              return   (b1 & true0)==true0;
            case 1:
             return   (b1 & treue1)==treue1;
            case 2:
              return   (b1 & treue2) == treue2;
            case 3:
                return   (b1 & treue3) == treue3;
            case 4:
                return   (b1 & treue4) == treue4;
            case 5:
                return   (b1 & treue5) == treue5;
            case 6:
                return   (b1 & treue6) == treue6;
            case 7:
                return   (b1 & true7) == true7;
        }
        return false;
    }

    /**
     * Set.
     *
     * @param index the index 从0开始
     * @param b     the b
     */
    public void set(int index, boolean b) {
        if (b) {
            set(index);
        }
        else {
            clear(index);
        }
    }

    private void clear(int index) {
        buffer.position(0);
        int b1 = buffer.get(index / 8) & mask;
        switch (index%8) {
            case 0:
                b1 = b1 & f0;
                break;
            case 1:
                b1 = b1 & f1;
                break;
            case 2:
                b1 = b1 & f2;
                break;
            case 3:
                b1 = b1 & f3;
                break;
            case 4:
                b1 = b1 & f4;
                break;
            case 5:
                b1 = b1 & f5;
                break;
            case 6:
                b1 = b1 & f6;
                break;
            case 7:
                b1 = b1 & f7;
                break;
        }
        buffer.put(index / 8, (byte) b1);
    }
    private void set(int index) {
        buffer.position(0);
        int b1 = buffer.get(index / 8) & mask;
        switch (index%8) {
            case 0:
                b1 = b1 | true0;
                break;
            case 1:
                b1 = b1 |treue1;
                break;
            case 2:
                b1 = b1 |treue2;
                break;
            case 3:
                b1 = b1 |treue3;
                break;
            case 4:
                b1 = b1 |treue4;
                break;
            case 5:
                b1 = b1 |treue5;
                break;
            case 6:
                b1 = b1 |treue6;
                break;
            case 7:
                b1 = b1 |true7;
                break;
        }
        buffer.put(index / 8, (byte) b1);
    }

    public static void main(String[] sss) {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        BitArray bitArray = new BitArray(buffer);
        bitArray.set(15, true);
        System.out.println(bitArray.get(15));
        buffer.position(0);
        System.out.println(buffer.getShort());

    }
}
