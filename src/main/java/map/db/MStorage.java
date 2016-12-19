package map.db;

import sun.misc.Cleaner;

import javax.xml.ws.spi.http.HttpHandler;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.OverlappingFileLockException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.IdentityHashMap;
import java.util.List;

/**
 * The type M storage.
 */
class MStorage {
    public static BitSet bitSet;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws IOException the io exception
     */
    public static void main(String[] args) throws IOException {
        MStorage storage = new MStorage("d", false, false, false);
        storage.transactionsDisabled = true;
//132/4
        for (int i = 0; i < 33; i++) {
            bitSet.set(i + 1, true);
        }
        storage.updatehead();

//        ByteBuffer byteBuffer = storage.read(1);
//        System.out.println(byteBuffer.limit());
//        storage.write(1, byteBuffer);

    }

    public void updatehead() {
        headbuff.position(0);
        headbuff.put(ObjectSeriaer.getbytes(bitSet));
    }

    /**
     * The Transaction log file extension.
     */
    static final String transaction_log_file_extension = ".t";
    /**
     * The Page size shift.
     */
    static final int PAGE_SIZE_SHIFT = 12;
    /**
     * The Page size.
     */
    static final int PAGE_SIZE = 1 << PAGE_SIZE_SHIFT;
    /**
     * The Clean data.
     */
    static final byte[] CLEAN_DATA = new byte[MStorage.PAGE_SIZE];
    /**
     * use 'val & OFFSET_MASK' to quickly get offset within the page;
     */
    static final long OFFSET_MASK = 0xFFFFFFFFFFFFFFFFL >>> (64 - MStorage.PAGE_SIZE_SHIFT);
    /**
     * The Idr.
     */
    static final String IDR = ".i";

    /**
     * The Dbr.
     */
    static final String DBR = ".d";

    /**
     * Maximal number of pages in single file.
     * Calculated so that each file will have 1 GB
     */
    final static long PAGES_PER_FILE = (1024 * 1024 * 1024 * 4l) >>> 12;

    private ArrayList<FileChannel> channels = new ArrayList<FileChannel>();
    private ArrayList<FileChannel> channelsTranslation = new ArrayList<FileChannel>();
    private IdentityHashMap<FileChannel, MappedByteBuffer> buffers = new IdentityHashMap<FileChannel, MappedByteBuffer>();
    /*head=128kb,32页面*/
//    public IdentityHashMap<FileChannel, MappedByteBuffer> headbuffers = new IdentityHashMap<FileChannel, MappedByteBuffer>();

    public MappedByteBuffer headbuff;
    private String fileName;
    private boolean transactionsDisabled;
    private boolean readonly;
    private boolean lockingDisabled;

    /**
     * Instantiates a new M storage.
     *
     * @param fileName             the file name
     * @param readonly             the readonly
     * @param transactionsDisabled the transactions disabled
     * @param lockingDisabled      the locking disabled
     * @throws IOException the io exception
     */
    public MStorage(String fileName, boolean readonly, boolean transactionsDisabled, boolean lockingDisabled) throws IOException {
        this.fileName = fileName;
        this.transactionsDisabled = transactionsDisabled;
        this.readonly = readonly;
        this.lockingDisabled = lockingDisabled;
        //make sure first file can be opened
        //lock it
        try {
            if (!lockingDisabled)
                getChannel(0).lock();
        } catch (IOException e) {
            throw new IOException("Could not lock DB file: " + fileName, e);
        } catch (OverlappingFileLockException e) {
            throw new IOException("Could not lock DB file: " + fileName, e);
        }

    }

    private FileChannel getChannel(long pageNumber) throws IOException {
        int fileNumber = (int) (Math.abs(pageNumber) / PAGES_PER_FILE);


        List<FileChannel> c = pageNumber >= 0 ? channels : channelsTranslation;

        //increase capacity of array lists if needed
        for (int i = c.size(); i <= fileNumber; i++) {
            c.add(null);
        }

        FileChannel ret = c.get(fileNumber);
        if (ret == null) {
            boolean init = false;
            String name = makeFileName(fileName, pageNumber, fileNumber);
            ret = new RandomAccessFile(name, "rw").getChannel();
            if (ret.size() < 128 * 1024) {
                init = true;
            }
            c.set(fileNumber, ret);
            MappedByteBuffer map = ret.map(FileChannel.MapMode.READ_WRITE, 0, ret.size());
            buffers.put(ret, map);
            headbuff = ret.map(FileChannel.MapMode.READ_WRITE, 0, 132 * 1024);
            if (init) {
                unmapBuffer(map);
                map = ret.map(FileChannel.MapMode.READ_WRITE, 0, 64 * 1024 * 1024);
                byte[] data = new byte[1024];
                for (int i = 0; i < 1024 * 64; i++) {
                    map.put(data);
                }
                map.force();
                buffers.put(ret, map);
                bitSet = new BitSet(Pagesize.MAXPAGENUMBER);
                byte[] bytes = ObjectSeriaer.getbytes(bitSet);
                headbuff.put(bytes);
                headbuff.force();
            } else {
                byte[] bytes = new byte[130 * 1024];
                headbuff.get(bytes);
                bitSet = ObjectSeriaer.geto(bytes);
                System.out.println(bitSet.size());
                System.out.println(bitSet.get(1));

            }
        }
        return ret;
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
        int offsetInFile = (int) ((Math.abs(pageNumber) % PAGES_PER_FILE) * PAGE_SIZE);
        MappedByteBuffer b = buffers.get(f);
        if (b.limit() <= offsetInFile) {

            //remapping buffer for each newly added page would be slow,
            //so allocate new size in chunks
            int increment = Math.min(PAGE_SIZE * 1024, offsetInFile / 10);
            increment -= increment % PAGE_SIZE;

            long newFileSize = offsetInFile + PAGE_SIZE + increment;
            newFileSize = Math.min(PAGES_PER_FILE * PAGE_SIZE, newFileSize);

            //expand file size
            f.position(newFileSize - 1);
            f.write(ByteBuffer.allocate(1));
            //unmap old buffer
            unmapBuffer(b);
            //remap buffer
            b = f.map(FileChannel.MapMode.READ_WRITE, 0, newFileSize);
            buffers.put(f, b);
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
        int offsetInFile = (int) ((Math.abs(pageNumber) % PAGES_PER_FILE) * PAGE_SIZE);
        MappedByteBuffer b = buffers.get(f);

        if (b == null) { //not mapped yet
            b = f.map(FileChannel.MapMode.READ_WRITE, 0, f.size());
        }

        //check buffers size
        if (b.limit() <= offsetInFile) {
            //file is smaller, return empty data
            return ByteBuffer.wrap(CLEAN_DATA).asReadOnlyBuffer();
        }

        b.position(offsetInFile);
        ByteBuffer ret = b.slice();
        ret.limit(PAGE_SIZE);
        if (!transactionsDisabled || readonly) {
            // changes written into buffer will be directly written into file
            // so we need to protect buffer from modifications
            ret = ret.asReadOnlyBuffer();
        }
        return ret;
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
