package map.db;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.*;
import java.util.BitSet;

/**
 * Created by jiang on 2016/12/19 0019.
 */
@SuppressWarnings("TryWithIdenticalCatches")
public class ObjectSeriaer {
    //KryoException
    public static Kryo kryo = new Kryo();
    private static byte[] mbuff = new byte[1024 * 32];
    static Output moutput = new Output(mbuff);
    static Input minput = new Input(10);

    /**
     * Getbytes byte [ ].
     *得到字节
     * @param o the o
     * @return the byte [ ]
     */
    public static byte[] getbytes(Object o) {
        moutput.clear();
        try {
            kryo.writeClassAndObject(moutput, o);
        } catch (KryoException e) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(mbuff.length * 2);
            Output out2 = new Output(outputStream);
            kryo.writeClassAndObject(out2, o);
            out2.flush();
            return outputStream.toByteArray();
        }
        return moutput.toBytes();
    }

    /**
     * Gets object.
     *
     * @param <T>  the type parameter
     * @param buff the buff
     * @return the object
     */
    public static <T> T getObject(byte[] buff) {
        minput.setBuffer(buff);
        return (T) kryo.readClassAndObject(minput);
    }
}
