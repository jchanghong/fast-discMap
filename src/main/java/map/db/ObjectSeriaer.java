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

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayOutputStream;

/**
 * Created by jiang on 2016/12/19 0019.
 */
@SuppressWarnings("TryWithIdenticalCatches")
public class ObjectSeriaer {
    /**
     * The constant kryo.
     */
//KryoException
    public static Kryo kryo = new Kryo();
    /**
     * The Minput.
     */
    static Input minput = new Input(10);
    private static byte[] mbuff = new byte[1024 * 32];
    /**
     * The Moutput.
     */
    static Output moutput = new Output(mbuff);

    /**
     * Getbytes byte [ ].
     * 得到字节
     *
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
