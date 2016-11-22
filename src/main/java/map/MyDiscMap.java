package map;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Set;

/**
 * Created by 长宏 on 2016/11/21 0021.
 * 磁盘map的实现，重在简单，速度。
 */
public class MyDiscMap implements MMap {
    private static final int NUM = 997;//选择好的素数
    private static final int SIZE_ELEMENT = 1024;//单个map值最大的大小
    private static final int NUM_ELEMENT = 10;//冲突hash后不扩容的情况下能存的map个数。
    //文件大小是NUM*size+size。其中一个size存原数据。
    //超过以后NUM+1循环
    private static final int size = SIZE_ELEMENT * NUM_ELEMENT;//一个hashcode的值大小，如果有冲突。
    // 这个大小用链表类型的方式储存。一个1024byte，这样是为了简单速度
    //格式是（key大小，key，v大小，v）
    //元数据条带的数据格式是(key，0)。一个条件可以存size/()个数。
    //满了就到num+2上去读取
    private RandomAccessFile file;
    private FileChannel fileChannel;
    private MappedByteBuffer headbyteBuffer;//性能原因。保存
    /**
     * 一个文件一个map。
     * 如何改进？
     */
    public MyDiscMap(String filename) {
        boolean neeinit = true;
        File file1 = new File(filename);
        if (file1.exists()) {
            neeinit = false;
        }
        try {
            file = new RandomAccessFile(filename, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (neeinit) {
            init();
        }
        fileChannel = file.getChannel();
        try {
            headbyteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, size);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        try {
            MappedByteBuffer buffer = file.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, NUM * size + size);
            byte[] bytes = new byte[size * NUM + size];
            Arrays.fill(bytes, (byte) 0);
            buffer.put(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * v的大小不能超过1024b
     */
    public boolean put(String ket, Object v) {
        int key = (Math.abs(ket.hashCode())) % NUM + 1;
//        System.out.println("key is" + key);
        try {
            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, key * size, size);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(size);
            ObjectOutputStream outputStream1 = new ObjectOutputStream(outputStream);
            int number = 0;
            int size;
            size = buffer.getInt();
            String keytemp = "___";
            while (size > 0) {
                number++;
                byte[] btemp = new byte[size];
                buffer.get(btemp);
                keytemp = new String(btemp);
                if (keytemp.equals(ket)) {
                    outputStream1.writeObject(v);
                    byte[] bytes = outputStream.toByteArray();
                    buffer.putInt(bytes.length);
                    buffer.put(bytes);
                    return false;
                }
                if (number == 10) {
                    return put(ket, v, key + NUM + 1);
                }
                buffer.position(number * SIZE_ELEMENT);
                size = buffer.getInt();
            }
            if (number == 10) {
                return put(ket, v, key + NUM + 1);
            }
            buffer.position(number * SIZE_ELEMENT);
            outputStream1.writeObject(v);
            byte[] bytes = outputStream.toByteArray();
            buffer.putInt(ket.getBytes().length);
            buffer.put(ket.getBytes());
            buffer.putInt(bytes.length);
            buffer.put(bytes);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * v的大小不能超过1024b
     */
    public boolean put(String ket, Object v, int hashcode) {
        int key = hashcode;
//        System.out.println("key is" + key);
        try {
            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, key * size, size);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(size);
            ObjectOutputStream outputStream1 = new ObjectOutputStream(outputStream);
            int number = 0;
            int size;
            size = buffer.getInt();
            String keytemp = "___";
            while (size > 0) {
                number++;
                byte[] btemp = new byte[size];
                buffer.get(btemp);
                keytemp = new String(btemp);
                if (keytemp.equals(ket)) {
                    outputStream1.writeObject(v);
                    byte[] bytes = outputStream.toByteArray();
                    buffer.putInt(bytes.length);
                    buffer.put(bytes);
                    return false;
                }
                buffer.position(number * SIZE_ELEMENT);
                size = buffer.getInt();
            }
            buffer.position(number * SIZE_ELEMENT);
            outputStream1.writeObject(v);
            byte[] bytes = outputStream.toByteArray();
            buffer.putInt(ket.getBytes().length);
            buffer.put(ket.getBytes());
            buffer.putInt(bytes.length);
            buffer.put(bytes);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public Object get(String key1) {
        int key = (Math.abs(key1.hashCode())) % NUM + 1;
//        System.out.println("key is" + key);
        try {
            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, key * size, size);
            int number = 0;
            int size = buffer.getInt();
            String keytemp = "__";
            while (size > 0) {
                number++;
                byte[] btemp = new byte[size];
                buffer.get(btemp);
                keytemp = new String(btemp);
                Object r = null;
                if (keytemp.equals(key1)) {
                    size = buffer.getInt();
                    byte[] bytes = new byte[size];
                    buffer.get(bytes);
                    ByteArrayInputStream outputStream = new ByteArrayInputStream(bytes);
                    ObjectInput outputStream1 = new ObjectInputStream(outputStream);
                    try {
                        r = outputStream1.readObject();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        return number;
                    }
                    return r;
                }
                if (number == 10) {
                    return get(key + NUM + 1,key1);
                }
                buffer.position(number * SIZE_ELEMENT);
                size = buffer.getInt();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object get(int hashcode,String key1) {
        int key = hashcode;
//        System.out.println("key is" + key);
        try {
            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, key * size, size);
            int number = 0;
            int size = buffer.getInt();
            String keytemp = "__";
            while (size > 0) {
                number++;
                byte[] btemp = new byte[size];
                buffer.get(btemp);
                keytemp = new String(btemp);
                Object r = null;
                if (keytemp.equals(key1)) {
                    size = buffer.getInt();
                    byte[] bytes = new byte[size];
                    buffer.get(bytes);
                    ByteArrayInputStream outputStream = new ByteArrayInputStream(bytes);
                    ObjectInput outputStream1 = new ObjectInputStream(outputStream);
                    try {
                        r = outputStream1.readObject();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        return number;
                    }
                    return r;
                }
                if (number == 10) {
                }
                buffer.position(number * SIZE_ELEMENT);
                size = buffer.getInt();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void putkey(String key) {

    }
    private void deletekey(String key) {

    }
    @Override
    public Set<String> keySet() {
        return null;
    }

    @Override
    public int size() {
        headbyteBuffer.position(0);
        return headbyteBuffer.getInt();
    }

    //-1
    private void sizesub1() {
        headbyteBuffer.position(0);
        int size = headbyteBuffer.getInt();
        if (size < 1) {
            return;
        }
        headbyteBuffer.position(0);
        headbyteBuffer.putInt(--size);
    }
    //+1
    private void subadd1() {
        headbyteBuffer.position(0);
        int size = headbyteBuffer.getInt();
        headbyteBuffer.position(0);
        headbyteBuffer.putInt(++size);
    }
    public void keyis(String key1) {
        int key = (Math.abs(key1.hashCode())) % NUM + 1;
        System.out.println("key is" + key);
    }

}
