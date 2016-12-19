package map;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;

/**
 * Created by 长宏 on 2016/11/23 0023.
 * 磁盘map实现。方法2
 */
public class MyDiscMap2 implements Map<String, Object> {
    private static final int NUM_DATA = 997;
    private static final int NUM_HEAD = 3;
    private final int NUM = NUM_DATA + NUM_HEAD;
    private static final int SIZE = 1024 * 10;
    private int filesize;//filesize*NUM*SIZE
    private RandomAccessFile file;
    private FileChannel fileChannel;
    private MappedByteBuffer headbyteBuffer;//性能原因。保存

    /**
     * 一个文件一个map。
     * 如何改进？
     */
    public MyDiscMap2(String filename) {
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
            headbyteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, NUM_HEAD * SIZE);
            filesize = headbyteBuffer.getInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        try {
            MappedByteBuffer buffer = file.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, NUM * SIZE);
            byte[] bytes = new byte[SIZE * NUM];
            Arrays.fill(bytes, (byte) 0);
            buffer.put(bytes);
            buffer.position(0);
            buffer.putInt(1);
            keyset = new HashSet<>();
            byte[] bytes1 = getEsirabytes(keyset);
            buffer.position(4);
            buffer.putInt(bytes1.length);
            buffer.put(bytes1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addfilesize() {
        try {
            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, filesize * SIZE * NUM, SIZE * NUM);
            byte[] bytes = new byte[SIZE * NUM];
            Arrays.fill(bytes, (byte) 0);
            buffer.put(bytes);
            headbyteBuffer.position(0);
            headbyteBuffer.putInt(++filesize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the number of key-value mappings in this map.  If the
     * map contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of key-value mappings in this map
     */
    @Override
    public int size() {
        if (keyset != null) {
            return keyset.size();
        }
        headbyteBuffer.position(4);
        int size = headbyteBuffer.getInt();
        if (size == 0) {
            return 0;
        }
//        System.out.println(NUM_HEAD * size - 8);
        ByteBuffer buffer = null;
        if (size <=NUM_HEAD * SIZE - 8) {//没有超过空间
            byte[] bytes = new byte[size];
            headbyteBuffer.position(8);
//            System.out.println(headbyteBuffer.remaining());
//            System.out.println("size"+size);
            headbyteBuffer.get(bytes);
            keyset = (Set<String>) getseriObject(bytes);
        } else {
            int shengxia = size;
            buffer = ByteBuffer.allocate(size);
            int i = 0;
            while (shengxia > 0 && i < filesize) {
                if (i == 0) {
                    shengxia -= NUM_HEAD * SIZE - 8;
                    buffer.put(getbytes_head(i++, 0, NUM_HEAD * SIZE - 8));
                }
                if (shengxia > NUM_HEAD * SIZE) {
                    shengxia -= NUM_HEAD * SIZE;
                    buffer.put(getbytes_head(i, 0, NUM_HEAD * SIZE));
                } else {
                    buffer.put(getbytes_head(i, 0, shengxia));
                    shengxia = 0;

                }
            }
            byte[] bytes = buffer.array();
            keyset = (Set<String>) getseriObject(bytes);
        }
        return keyset.size();
    }

    private Set<String> keyset;

    /**
     * Returns <tt>true</tt> if this map contains no key-value mappings.
     *
     * @return <tt>true</tt> if this map contains no key-value mappings
     */
    @Override
    public boolean isEmpty() {
        if (keyset == null) {
            size();
        }
        return keyset.isEmpty();
    }

    /**
     * Returns <tt>true</tt> if this map contains a mapping for the specified
     * key.  More formally, returns <tt>true</tt> if and only if
     * this map contains a mapping for a key <tt>k</tt> such that
     * <tt>(key==null ? k==null : key.equals(k))</tt>.  (There can be
     * at most one such mapping.)
     *
     * @param key key whose presence in this map is to be tested
     * @return <tt>true</tt> if this map contains a mapping for the specified
     * key
     * @throws ClassCastException   if the key is of an inappropriate type for
     *                              this map
     *                              (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified key is null and this map
     *                              does not permit null keys
     *                              (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public boolean containsKey(Object key) {
        if (keyset == null) {
            size();
        }
        return keyset.contains(key);
    }

    /**
     * Returns <tt>true</tt> if this map maps one or more keys to the
     * specified value.  More formally, returns <tt>true</tt> if and only if
     * this map contains at least one mapping to a value <tt>v</tt> such that
     * <tt>(value==null ? v==null : value.equals(v))</tt>.  This operation
     * will probably require time linear in the map size for most
     * implementations of the <tt>Map</tt> interface.
     *
     * @param value value whose presence in this map is to be tested
     * @return <tt>true</tt> if this map maps one or more keys to the
     * specified value
     * @throws ClassCastException   if the value is of an inappropriate type for
     *                              this map
     *                              (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified value is null and this
     *                              map does not permit null values
     *                              (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public boolean containsValue(Object value) {
        if (keyset == null) {
            size();
        }
        for (String s : keyset) {
            if (get(s).equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     * <p>
     * <p>More formally, if this map contains a mapping from a key
     * {@code k} to a value {@code v} such that {@code (key==null ? k==null :
     * key.equals(k))}, then this method returns {@code v}; otherwise
     * it returns {@code null}.  (There can be at most one such mapping.)
     * <p>
     * <p>If this map permits null values, then a return value of
     * {@code null} does not <i>necessarily</i> indicate that the map
     * contains no mapping for the key; it's also possible that the map
     * explicitly maps the key to {@code null}.  The {@link #containsKey
     * containsKey} operation may be used to distinguish these two cases.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or
     * {@code null} if this map contains no mapping for the key
     * @throws ClassCastException   if the key is of an inappropriate type for
     *                              this map
     *                              (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified key is null and this map
     *                              does not permit null keys
     *                              (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public Object get(Object key) {
        List<Entry> values = null;
        int hashindex = (Math.abs(key.hashCode())) % NUM_DATA + NUM_HEAD;
//        System.out.println("key is" + key);
        try {
            MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, hashindex * SIZE, SIZE);
            int size = mappedByteBuffer.getInt();
            if (size == 0) {
                return null;
            }
            ByteBuffer buffer = null;
            if (size <= SIZE - 4) {//没有超过空间
                byte[] bytes = new byte[size];
                mappedByteBuffer.get(bytes);
                values = (List<Entry>) getseriObject(bytes);
            } else {
                int shengxia = size;
                int fileindex = 0;
                buffer = ByteBuffer.allocate(size);
                while (shengxia > 0 && fileindex < filesize) {
                    if (fileindex == 0) {
                        shengxia -=SIZE - 4;
                        buffer.put(getbytes(fileindex++, hashindex, SIZE - 4));
                    }
                    if (shengxia >= SIZE) {
                        shengxia -= SIZE;
                        buffer.put(getbytes(fileindex++, hashindex, SIZE));
                    } else {
                        buffer.put(getbytes(fileindex++, hashindex, shengxia));
                        shengxia = 0;
                    }
                }
                byte[] bytes = buffer.array();
                values = (List<Entry>) getseriObject(bytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Entry value : values) {
            if (value.getKey().equals(key.toString())) {
                return value.getValue();
            }
        }
        return null;
    }

    /**
     * Associates the specified value with the specified key in this map
     * (optional operation).  If the map previously contained a mapping for
     * the key, the old value is replaced by the specified value.  (A map
     * <tt>m</tt> is said to contain a mapping for a key <tt>k</tt> if and only
     * if {@link #containsKey(Object) m.containsKey(k)} would return
     * <tt>true</tt>.)
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with <tt>key</tt>, or
     * <tt>null</tt> if there was no mapping for <tt>key</tt>.
     * (A <tt>null</tt> return can also indicate that the map
     * previously associated <tt>null</tt> with <tt>key</tt>,
     * if the implementation supports <tt>null</tt> values.)
     * @throws UnsupportedOperationException if the <tt>put</tt> operation
     *                                       is not supported by this map
     * @throws ClassCastException            if the class of the specified key or value
     *                                       prevents it from being stored in this map
     * @throws NullPointerException          if the specified key or value is null
     *                                       and this map does not permit null keys or values
     * @throws IllegalArgumentException      if some property of the specified key
     *                                       or value prevents it from being stored in this map
     */
    @Override
    public Object put(String key, Object value) {
        if (keyset == null) {
            size();
        }
        keyset.add(key);
        updatekeySet();
        List<Entry> values = null;
        MappedByteBuffer mappedByteBuffer = null;
        int hashindex = (Math.abs(key.hashCode())) % NUM_DATA + NUM_HEAD;
//        System.out.println("key is" + hashindex);
        try {
            mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, hashindex * SIZE, SIZE);
            int size = mappedByteBuffer.getInt();
            ByteBuffer buffer = null;
            if (size == 0) {
                values = new ArrayList<>();
            }
            if (size > 0 && size <= SIZE - 4) {//没有超过空间
                byte[] bytes = new byte[size];
                mappedByteBuffer.get(bytes);
                values = (List<Entry>) getseriObject(bytes);
            } else if (size > SIZE - 4) {
                int shengxia = size;
                int fileindex = 0;
                buffer = ByteBuffer.allocate(size);
                while (shengxia > 0 && fileindex < filesize) {
                    if (fileindex == 0) {
                        shengxia -= NUM_HEAD * SIZE - 4;
                        buffer.put(getbytes(fileindex++, hashindex, SIZE - 4));
                    }
                    if (shengxia >= SIZE) {
                        shengxia -= SIZE;
                        buffer.put(getbytes(fileindex++, hashindex, SIZE));
                    } else {
                        if (shengxia < 1) {
                            break;
                        }
                        buffer.put(getbytes(fileindex++, hashindex, shengxia));
                        shengxia = 0;

                    }
                }
                byte[] bytes = buffer.array();
                values = (List<Entry>) getseriObject(bytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Entry pre = null;
        for (Entry value1 : values) {
            if (value1.getKey().equals(key.toString())) {
                pre = value1;
                value1.setValue(value);
                break;
            }
        }
        if (pre == null) {
            values.add(new Entry(key, value));
            byte[] bytes = getEsirabytes(values);
            mappedByteBuffer.position(0);
            mappedByteBuffer.putInt(bytes.length);
            putback(bytes, hashindex);
            return null;
        }
        byte[] bytes = getEsirabytes(values);
        mappedByteBuffer.position(0);
        mappedByteBuffer.putInt(bytes.length);
        putback(bytes, hashindex);
        return pre.getValue();
    }

    private void updatekeySet() {
        byte[] bytes = getEsirabytes(keyset);
        int size = bytes.length;
        if (size > filesize * SIZE*NUM_HEAD - 8) {
            addfilesize();
        }
        headbyteBuffer.position(4);
        headbyteBuffer.putInt(size);
//        System.out.println("updatekey size is:"+size);
        if (size <= SIZE*NUM_HEAD - 8) {//没有超过空间
            headbyteBuffer.position(8);
            headbyteBuffer.put(bytes);
            return;
        }
        int shengxia = size;
        int fileindex = 0;
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        while (shengxia > 0 && fileindex < filesize) {
            if (fileindex == 0) {
                shengxia -= SIZE*NUM_HEAD - 8;
                byte[] h = new byte[SIZE*NUM_HEAD - 8];
                byteBuffer.get(h);
                headbyteBuffer.position(8);
                headbyteBuffer.put(h);
                fileindex++;
            }
            if (shengxia >= SIZE*NUM_HEAD) {
                shengxia -= SIZE*NUM_HEAD;
                byte[] h = new byte[SIZE*NUM_HEAD];
                byteBuffer.get(h);
                putbytes_head(fileindex++, 0, SIZE*NUM_HEAD, h);
            } else {
                byte[] h = new byte[shengxia];
                byteBuffer.get(h);
                putbytes_head(fileindex++, 0, shengxia, h);
                shengxia = 0;
            }
        }
    }

    private void putback(byte[] bytes, int hashindex) {
        int size = bytes.length;
        if (size > filesize * SIZE - 4) {
            addfilesize();
        }
        if (size <= SIZE - 4) {//没有超过空间
            putbytes(0, hashindex, size, bytes);
            return;
        }
        int shengxia = size;
        int fileindex = 0;
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        while (shengxia > 0 && fileindex < filesize) {
            if (fileindex == 0) {
                shengxia -= SIZE - 4;
                byte[] h = new byte[SIZE - 4];
                byteBuffer.get(h);
                putbytes(fileindex++, hashindex, SIZE - 4, h);
            }
            if (shengxia >= SIZE) {
                shengxia -= SIZE;
                byte[] h = new byte[SIZE];
                byteBuffer.get(h);
                putbytes(fileindex++, hashindex, SIZE, h);
            } else {
                byte[] h = new byte[shengxia];
                byteBuffer.get(h);
                putbytes(fileindex++, hashindex, shengxia, h);
                shengxia = 0;
            }
        }
    }

    /**
     * Removes the mapping for a key from this map if it is present
     * (optional operation).   More formally, if this map contains a mapping
     * from key <tt>k</tt> to value <tt>v</tt> such that
     * <code>(key==null ?  k==null : key.equals(k))</code>, that mapping
     * is removed.  (The map can contain at most one such mapping.)
     * <p>
     * <p>Returns the value to which this map previously associated the key,
     * or <tt>null</tt> if the map contained no mapping for the key.
     * <p>
     * <p>If this map permits null values, then a return value of
     * <tt>null</tt> does not <i>necessarily</i> indicate that the map
     * contained no mapping for the key; it's also possible that the map
     * explicitly mapped the key to <tt>null</tt>.
     * <p>
     * <p>The map will not contain a mapping for the specified key once the
     * call returns.
     *
     * @param key key whose mapping is to be removed from the map
     * @return the previous value associated with <tt>key</tt>, or
     * <tt>null</tt> if there was no mapping for <tt>key</tt>.
     * @throws UnsupportedOperationException if the <tt>remove</tt> operation
     *                                       is not supported by this map
     * @throws ClassCastException            if the key is of an inappropriate type for
     *                                       this map
     *                                       (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException          if the specified key is null and this
     *                                       map does not permit null keys
     *                                       (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public Object remove(Object key) {
        if (keyset == null) {
            size();
        }
        if (!keyset.remove(key)) {
            updatekeySet();
            return null;
        }
        updatekeySet();
        MappedByteBuffer mappedByteBuffer = null;
        List<Entry> values = null;
        int hashindex = (Math.abs(key.hashCode())) % NUM_DATA + NUM_HEAD;
//        System.out.println("key is" + key);
        try {
            mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, hashindex * SIZE, SIZE);
            int size = mappedByteBuffer.getInt();
            if (size == 0) {
                return null;
            }
            ByteBuffer buffer = null;
            if (size <= SIZE - 4) {//没有超过空间
                byte[] bytes = new byte[size];
                mappedByteBuffer.get(bytes);
                values = (List<Entry>) getseriObject(bytes);
            } else {
                int shengxia = size;
                int fileindex = 0;
                buffer = ByteBuffer.allocate(size);
                while (shengxia > 0 && fileindex < filesize) {
                    if (fileindex == 0) {
                        shengxia -= NUM_HEAD * SIZE - 4;
                        buffer.put(getbytes(fileindex++, hashindex, SIZE - 4));
                    }
                    if (shengxia >= SIZE) {
                        shengxia -= SIZE;
                        buffer.put(getbytes(fileindex++, hashindex, SIZE));
                    } else {
                        buffer.put(getbytes(fileindex++, hashindex, shengxia));
                        shengxia = 0;

                    }
                }
                byte[] bytes = buffer.array();
                values = (List<Entry>) getseriObject(bytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Entry pre = null;
        for (Entry value1 : values) {
            if (value1.getKey().equals(key.toString())) {
                pre = value1;
                break;
            }
        }
        if (pre != null) {
            values.remove(pre);
        }
        if (values.size() == 0) {
            mappedByteBuffer.position(0);
            mappedByteBuffer.putInt(0);
            return pre.getValue();
        }
        byte[] bytes = getEsirabytes(values);
        mappedByteBuffer.position(0);
        mappedByteBuffer.putInt(bytes.length);
        putback(bytes, hashindex);
        return pre.getValue();
    }

    /**
     * Copies all of the mappings from the specified map to this map
     * (optional operation).  The effect of this call is equivalent to that
     * of calling {@link #put(Object, Object) put(k, v)} on this map once
     * for each mapping from key <tt>k</tt> to value <tt>v</tt> in the
     * specified map.  The behavior of this operation is undefined if the
     * specified map is modified while the operation is in progress.
     *
     * @param m mappings to be stored in this map
     * @throws UnsupportedOperationException if the <tt>putAll</tt> operation
     *                                       is not supported by this map
     * @throws ClassCastException            if the class of a key or value in the
     *                                       specified map prevents it from being stored in this map
     * @throws NullPointerException          if the specified map is null, or if
     *                                       this map does not permit null keys or values, and the
     *                                       specified map contains null keys or values
     * @throws IllegalArgumentException      if some property of a key or value in
     *                                       the specified map prevents it from being stored in this map
     */
    @Override
    public void putAll( Map<? extends String, ?> m) {
        for (Map.Entry s : m.entrySet()) {
            put((String) s.getKey(), s.getValue());
        }
    }

    /**
     * Removes all of the mappings from this map (optional operation).
     * The map will be empty after this call returns.
     *
     * @throws UnsupportedOperationException if the <tt>clear</tt> operation
     *                                       is not supported by this map
     */
    @Override
    public void clear() {
        keyset.clear();
        updatekeySet();
    }

    /**
     * Returns a {@link Set} view of the keys contained in this map.
     * The set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa.  If the map is modified
     * while an iteration over the set is in progress (except through
     * the iterator's own <tt>remove</tt> operation), the results of
     * the iteration are undefined.  The set supports element removal,
     * which removes the corresponding mapping from the map, via the
     * <tt>Iterator.remove</tt>, <tt>Set.remove</tt>,
     * <tt>removeAll</tt>, <tt>retainAll</tt>, and <tt>clear</tt>
     * operations.  It does not support the <tt>add</tt> or <tt>addAll</tt>
     * operations.
     *
     * @return a set view of the keys contained in this map
     */
    @Override
    public Set<String> keySet() {
        if (keyset == null) {
            size();
        }
        return keyset;

    }

    /**
     * Returns a {@link Collection} view of the values contained in this map.
     * The collection is backed by the map, so changes to the map are
     * reflected in the collection, and vice-versa.  If the map is
     * modified while an iteration over the collection is in progress
     * (except through the iterator's own <tt>remove</tt> operation),
     * the results of the iteration are undefined.  The collection
     * supports element removal, which removes the corresponding
     * mapping from the map, via the <tt>Iterator.remove</tt>,
     * <tt>Collection.remove</tt>, <tt>removeAll</tt>,
     * <tt>retainAll</tt> and <tt>clear</tt> operations.  It does not
     * support the <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * @return a collection view of the values contained in this map
     */
    @Override
    public Collection<Object> values() {
        List l = new ArrayList();
        for (String s : keyset) {
            l.add(get(s));
        }
        return l;
    }

    /**
     * Returns a {@link Set} view of the mappings contained in this map.
     * The set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa.  If the map is modified
     * while an iteration over the set is in progress (except through
     * the iterator's own <tt>remove</tt> operation, or through the
     * <tt>setValue</tt> operation on a map entry returned by the
     * iterator) the results of the iteration are undefined.  The set
     * supports element removal, which removes the corresponding
     * mapping from the map, via the <tt>Iterator.remove</tt>,
     * <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt> and
     * <tt>clear</tt> operations.  It does not support the
     * <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * @return a set view of the mappings contained in this map
     */
    @Override
    public Set<Map.Entry<String, Object>> entrySet() {
        Set<Map.Entry<String, Object>> set = new HashSet<>();
        for (String s : keyset) {
            set.add(new Entry(s, get(s)));
        }
        return set;
    }


    private static Object getseriObject(byte[] bytes) {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return inputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }

    private static byte[] getEsirabytes(Object object) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(object);
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * fileindex从0开始
     * hashindex<NUM
     */
    private byte[] getbytes(int fileindex, int hashindex, int bytesize) {
        try {
            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, fileindex * NUM * SIZE + hashindex * SIZE, SIZE);
            if (fileindex == 0 && hashindex > 2) {
                buffer.position(4);
            }
            if (fileindex == 0 && hashindex < 3) {
                buffer.position(8);
            }
            byte[] bytes = new byte[bytesize];
            buffer.get(bytes);
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * fileindex从0开始
     * hashindex<NUM
     */
    private byte[] getbytes_head(int fileindex, int hashindex, int bytesize) {
        try {
            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, fileindex * NUM * SIZE + hashindex * SIZE, SIZE * NUM_HEAD);
            if (fileindex == 0) {
                buffer.position(8);
            }
            byte[] bytes = new byte[bytesize];
            buffer.get(bytes);
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * fileindex从0开始
     * hashindex<NUM
     */
    private void putbytes(int fileindex, int hashindex, int bytesize, byte[] bytes1) {
        try {
            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, fileindex * NUM * SIZE + hashindex * SIZE, SIZE);
            if (fileindex == 0) {
                buffer.position(4);
            }
            buffer.put(bytes1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * fileindex从0开始
     * hashindex<NUM
     */
    private void putbytes_head(int fileindex, int hashindex, int bytesize, byte[] bytes1) {
        try {
            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, fileindex * NUM * SIZE + hashindex * SIZE, SIZE*NUM_HEAD);
            if (fileindex == 0) {
                buffer.position(8);
            }
            buffer.put(bytes1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class Entry implements Map.Entry<String, Object>, Externalizable {

        private String key;
        private Object v;

        public Entry() {
        }

        public Entry(String key, Object v) {
            this.key = key;
            this.v = v;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public Object getValue() {
            return v;
        }

        @Override
        public Object setValue(Object value) {
            return v = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Entry)) {
                return false;
            }
            Entry entry = (Entry) obj;
            return entry.getKey().equals(key);
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeUTF(key);
            out.writeObject(v);

        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

            key = in.readUTF();
            v = in.readObject();
        }
    }

}
