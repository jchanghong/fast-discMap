/*
 *
 *
 *    Created on  16-12-21 下午9:49 by jiang
 *    very fast key value store 简单，快速的键值储存。
 *    特别为小文件储存设计，比如图片文件。
 *    把小文件存数据库中不是理想的选择。存在文件系统中又有太多小文件难管理
 *
 */

package map.db;

import map.util.BitArray;
import sun.misc.Cleaner;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.OverlappingFileLockException;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

/**
 * The type M storage.
 * 文件前面2m保留做头信息1024*1024*2/4096=512页面
 */
class MStorage {
    /**
     * The Transaction log file extension.
     */
    static final String transaction_log_file_extension = ".t";
    /**
     * The Clean data.
     */
    static final byte[] CLEAN_DATA = new byte[Pagesize.page_size];
    /**
     * use 'val & OFFSET_MASK' to quickly get offset within the page;
     */
    static final long OFFSET_MASK = 0xFFFFFFFFFFFFFFFFL >>> (64 - 12);
    /**
     * The Idr.
     */
    static final String IDR = ".i";
    /**
     * The Dbr.
     */
    static final String DBR = ".d";
    /**
     * The constant bitArray.
     */
    public static BitArray bitArray;
    /**
     * The constant init.
     */
    public static boolean init;
    //    static final byte[] all_zear_bytes = new byte[Pagesize.page_size];
//    static {
//        for (int i = 0; i < Pagesize.page_size; i++) {
//            all_zear_bytes[i] = 0;
//        }
//    }
    private static MStorage int1;
    /**
     * The Headbuff.
     */
/*head=128kb,32页面*/
    public MappedByteBuffer headbuff;
    private ArrayList<FileChannel> channels = new ArrayList<FileChannel>();
    private ArrayList<FileChannel> channelsTranslation = new ArrayList<FileChannel>();
    private IdentityHashMap<FileChannel, MappedByteBuffer> buffers = new IdentityHashMap<FileChannel, MappedByteBuffer>();
    private String fileName;
    private boolean transactionsDisabled;
    private boolean readonly;
    private boolean lockingDisabled;

    private MStorage(String fileName) {
        this.fileName = fileName;
        this.transactionsDisabled = true;
        this.readonly = false;
        this.lockingDisabled = false;
        //make sure first file can be opened
        //lock it
        try {
            getChannel(0).lock();
        } catch (IOException | OverlappingFileLockException e) {
            throw new NullPointerException("Could not lock DB file: " + fileName);
        }
    }

    /**
     * Instantiates a new M storage.
     *
     * @param fileName             the file name
     * @param readonly             the readonly
     * @param transactionsDisabled the transactions disabled
     * @param lockingDisabled      the locking disabled
     * @throws IOException the io exception
     */
    private MStorage(String fileName, boolean readonly, boolean transactionsDisabled, boolean lockingDisabled) throws IOException {
        this.fileName = fileName;
        this.transactionsDisabled = transactionsDisabled;
        this.readonly = readonly;
        this.lockingDisabled = lockingDisabled;
        //make sure first file can be opened
        //lock it
        try {
            if (!lockingDisabled)
                getChannel(0).lock();

        } catch (IOException | OverlappingFileLockException e) {
            throw new IOException("Could not lock DB file: " + fileName, e);
        }

    }

    /**
     * Gets instance.
     *
     * @param filename the filename
     * @return the instance
     */
    public static MStorage getInstance(String filename) {
        if (int1 == null) {
            int1 = new MStorage(filename);
        }
        return int1;
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws IOException the io exception
     */
    public static void main(String[] args) throws IOException {
        MStorage storage = MStorage.getInstance("d");
//        for (int i = 0; i < 512; i++) {
//            bitArray.set(i, true);
//        }
//        bitArray.set(510, false);
        System.out.println(bitArray.get(520));
        System.out.println(bitArray.get(511));
        System.out.println(bitArray.get(510));
    }

    /**
     * Make file name string.
     *
     * @param fileName   the file name
     * @param pageNumber the page number
     * @param fileNumber the file number
     * @return the string
     */
    static String makeFileName(String fileName, long pageNumber, int fileNumber) {
        return fileName + (pageNumber >= 0 ? DBR : IDR) + "." + fileNumber;
//        return "E:\\迅雷下载\\ideaIU-2016.3.exe";
    }

    /**
     * Delete files.
     *
     * @param fileName the file name
     */
    static void deleteFiles(String fileName) {
        for (int i = 0; true; i++) {
            String name = makeFileName(fileName, +1, i);
            File f = new File(name);
            boolean exists = f.exists();
            if (exists && !f.delete()) f.deleteOnExit();
            if (!exists) break;
        }
        for (int i = 0; true; i++) {
            String name = makeFileName(fileName, -1, i);
            File f = new File(name);
            boolean exists = f.exists();
            if (exists && !f.delete()) f.deleteOnExit();
            if (!exists) break;
        }
    }

    private FileChannel getChannel(long pageNumber) throws IOException {
        int fileNumber = (int) (Math.abs(pageNumber) / Pagesize.max_page_number);

        List<FileChannel> c = pageNumber >= 0 ? channels : channelsTranslation;

        //increase capacity of array lists if needed
        for (int i = c.size(); i <= fileNumber; i++) {
            c.add(null);
        }

        FileChannel ret = c.get(fileNumber);
        if (ret == null) {
            String name = makeFileName(fileName, pageNumber, fileNumber);
            initfileif(name);
            ret = new RandomAccessFile(name, "rw").getChannel();
            MappedByteBuffer map;
            if (ret.size() < 128 * 1024) {
                map = ret.map(FileChannel.MapMode.READ_WRITE, 0, 64 * 1024 * 1024);
                for (int i = 0; i < 1024 * 16; i++) {
                    map.put(CLEAN_DATA);
                }
                map.force();
                if (headbuff == null) {
                    headbuff = ret.map(FileChannel.MapMode.READ_WRITE, 0, 512 * 1024);
                    bitArray = new BitArray(headbuff);
                    for (int i = 0; i < 512; i++) {
                        bitArray.set(i, true);
                    }
                    init = true;
                }
            } else {
                map = ret.map(FileChannel.MapMode.READ_WRITE, 0, ret.size());
                init = false;
            }
            c.set(fileNumber, ret);
            buffers.put(ret, map);
            if (headbuff == null) {
                headbuff = ret.map(FileChannel.MapMode.READ_WRITE, 0, 512 * 1024);
                bitArray = new BitArray(headbuff);
            }
        }
        return ret;
    }

    private void initfileif(String name) {
        File file = new File("name");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Write.
     *
     * @param pageNumber the page number
     * @param data       the data
     * @throws IOException the io exception
     */
    public void write(long pageNumber, ByteBuffer data) throws IOException {
        if (transactionsDisabled && data.isDirect()) {
            //if transactions are disabled and this buffer is direct,
            //changes written into buffer are directly reflected in file.
            //so there is no need to write buffer second time
            return;
        }

        FileChannel f = getChannel(pageNumber);
        int offsetInFile = (int) ((Math.abs(pageNumber) % Pagesize.max_page_number) * Pagesize.page_size);
        MappedByteBuffer b = buffers.get(f);
        if (b.limit() <= offsetInFile) {
            b = addfilesize(f, offsetInFile, b);
        }

        //write into buffer
        b.position(offsetInFile);
        data.rewind();
        b.put(data);
    }

    private void unmapBuffer(MappedByteBuffer b) {
        if (b != null) {
            Cleaner cleaner = ((sun.nio.ch.DirectBuffer) b).cleaner();
            if (cleaner != null)
                cleaner.clean();
        }
    }

    /**
     * Read byte buffer.
     *
     * @param pageNumber the page number
     * @return the byte buffer
     * @throws IOException the io exception
     */
    public ByteBuffer read(long pageNumber) throws IOException {
        FileChannel f = getChannel(pageNumber);
        int offsetInFile = (int) ((Math.abs(pageNumber) % Pagesize.max_page_number) * Pagesize.page_size);
        MappedByteBuffer b = buffers.get(f);

        if (b == null) { //not mapped yet
            b = f.map(FileChannel.MapMode.READ_WRITE, 0, f.size());
        }
        //增加文件大小，64m为单位
        if (b.limit() <= offsetInFile) {
            b = addfilesize(f, offsetInFile, b);
        }
//        //check buffers size
//        if (b.limit() <= offsetInFile) {
//            //file is smaller, return empty data
////            return ByteBuffer.wrap(CLEAN_DATA).asReadOnlyBuffer();
//            return ByteBuffer.wrap(CLEAN_DATA);
//        }
        b.position(offsetInFile);
        ByteBuffer ret = b.slice();
        ret.limit(Pagesize.page_size);
        if (!transactionsDisabled || readonly) {
            // changes written into buffer will be directly written into file
            // so we need to protect buffer from modifications
            ret = ret.asReadOnlyBuffer();
        }
        return ret;
    }

    /**
     * Addfilesize mapped byte buffer.
     *
     * @param f            the f
     * @param offsetInFile the offset in file
     * @param b            the b
     * @return the mapped byte buffer 新的buff
     * @throws IOException the io exception
     */
    public MappedByteBuffer addfilesize(FileChannel f, int offsetInFile, MappedByteBuffer b) throws IOException {
        long newFileSize = b.limit() + Pagesize.page_size * 1024 * 16L;
        newFileSize = Math.min(newFileSize, Pagesize.Max_file_size);
        int oledsize = b.limit();
        //unmap old buffer
        unmapBuffer(b);
        //remap buffer
        b = f.map(FileChannel.MapMode.READ_WRITE, 0, newFileSize);
        //expand file size
        b.position(offsetInFile);
        for (int i = 0; i < 1024 * 16; i++) {
            b.put(CLEAN_DATA);
        }
        b.force();
        buffers.put(f, b);
        return b;
    }

    /**
     * Force close.
     *
     * @throws IOException the io exception
     */
    public void forceClose() throws IOException {
        for (FileChannel f : channels) {
            if (f == null) continue;
            f.close();
            unmapBuffer(buffers.get(f));
        }
        for (FileChannel f : channelsTranslation) {
            if (f == null) continue;
            f.close();
            unmapBuffer(buffers.get(f));
        }

        channels = null;
        channelsTranslation = null;
        buffers = null;
    }

    /**
     * Sync.
     *
     * @throws IOException the io exception
     */
    public void sync() throws IOException {
        for (MappedByteBuffer b : buffers.values()) {
            b.force();
        }
    }

    /**
     * Open transaction log data output stream.
     *
     * @return the data output stream
     * @throws IOException the io exception
     */
    public DataOutputStream openTransactionLog() throws IOException {
        String logName = fileName + transaction_log_file_extension;
        final FileOutputStream fileOut = new FileOutputStream(logName);
        return new DataOutputStream(new BufferedOutputStream(fileOut)) {

            //default implementation of flush on FileOutputStream does nothing,
            //so we use little workaround to make sure that data were really flushed
            public void flush() throws IOException {
                super.flush();
                fileOut.flush();
                fileOut.getFD().sync();
            }
        };
    }

    /**
     * Delete all files.
     *
     * @throws IOException the io exception
     */
    public void deleteAllFiles() throws IOException {
        deleteTransactionLog();
        deleteFiles(fileName);
    }

    /**
     * Read transaction log data input stream.
     *
     * @return the data input stream
     */
    public DataInputStream readTransactionLog() {

        File logFile = new File(fileName + transaction_log_file_extension);
        if (!logFile.exists())
            return null;
        if (logFile.length() == 0) {
            logFile.delete();
            return null;
        }

        DataInputStream ois = null;
        try {
            ois = new DataInputStream(new BufferedInputStream(new FileInputStream(logFile)));
        } catch (FileNotFoundException e) {
            //file should exists, we check for its presents just a miliseconds yearlier, anyway move on
            return null;
        }

//        try {
//            if (ois.readShort() != Magic.LOGFILE_HEADER)
//                throw new Error("Bad magic on log file");
//        } catch (IOException e) {
//             corrupted/empty logfile
//            logFile.delete();
//            return null;
//        }
        return ois;
    }

    /**
     * Delete transaction log.
     */
    public void deleteTransactionLog() {
        File logFile = new File(fileName + transaction_log_file_extension);
        if (logFile.exists())
            logFile.delete();
    }

    /**
     * Is readonly boolean.
     *
     * @return the boolean
     */
    public boolean isReadonly() {
        return readonly;
    }


}
