package map;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

/**
 * Created by 长宏 on 2016/11/21 0021.
 * 磁盘map的实现，重在简单，速度。
 */
public class MyMapImpl implements MMap {
    int NUM = 997;//选择好的素数
    int size = 1024 * 10;//一个hashcode的值大小，如果有冲突。
    // 这个大小用链表类型的方式储存。这样是为了简单速度
    private RandomAccessFile file;
    private FileChannel fileChannel;
    public MyMapImpl(String filename) {
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
    }

    private void init() {

        try {
            MappedByteBuffer buffer = file.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, NUM * size);
            byte[] bytes = new byte[size * NUM];
            Arrays.fill(bytes, (byte) 0);
            buffer.put(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void put(String ket, Object v) {
        int key=(Math.abs(ket.hashCode()))%NUM;
//        System.out.println("key is" + key);
              try {
            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, key * size, size);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(size);
            ObjectOutputStream outputStream1 = new ObjectOutputStream(outputStream);
            outputStream1.writeObject(v);
            byte[] bytes = outputStream.toByteArray();
            buffer.putInt(bytes.length);
            buffer.put(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public Object get(String key1) {
        int key=(Math.abs(key1.hashCode()))%NUM;
//        System.out.println("key is" + key);
        try {
            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, key * size, size);
            int size = buffer.getInt();
            if (size<1) {
                return null;
            }
            byte[] bytes = new byte[size];
            buffer.get(bytes);
            ByteArrayInputStream outputStream = new ByteArrayInputStream(bytes);
            ObjectInput outputStream1 = new ObjectInputStream(outputStream);
            Object r=null;
            try {
                r = outputStream1.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return r;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
