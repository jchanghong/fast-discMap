package map.db;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import map.htree.MHashCodes;

/**
 * Created by jiang on 2016/12/19 0019.
 */
public class DHtreeNode implements Comparable<DHtreeNode>, KryoSerializable {
    static {
        ObjectSeriaer.kryo.register(DHtreeNode.class, 22);
    }
    public int high;//root si 0
    public int code;
    public int[] childs;//页面index 0kaishi
    public DHtreeNode[] childsm;
    public boolean hasV;
    public String key;
    public Object values;
    private MdiscIO io;
    IndexObjectMap map;

    @Override
    public void write(Kryo kryo, Output out) {
        out.write(high);
        kryo.writeObjectOrNull(out, childs, int[].class);
        out.writeBoolean(hasV);
        kryo.writeObjectOrNull(out, key, String.class);
        kryo.writeClassAndObject(out, values);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof DHtreeNode) {
            DHtreeNode node = (DHtreeNode) obj;
            if (node.code != code || node.high != high) {
                return false;
            }
            if (key != null) {
                return key.equals(node.key);
            }
        }
        return super.equals(obj);
    }

    @Override
    public void read(Kryo kryo, Input in) {
        this.high = in.read();
        childs = kryo.readObjectOrNull(in, int[].class);
        this.code = MHashCodes.codes[high];
        hasV = in.readBoolean();
        key = kryo.readObjectOrNull(in, String.class);
        values = kryo.readClassAndObject(in);
    }

    public DHtreeNode() {
        this(0, null, null);
    }

    public DHtreeNode(int high, String key, Object values) {
        this.high = high;
        code = MHashCodes.codes[high];
        this.key = key;
        this.values = values;
        hasV = key == null || high == 0 ? false : true;
        io = DiscIO.getInstance();
        map = IndexObjectMap.getInstance();
    }

    /**
     * Putchild object.
     * 在自己孩子节点插入。没有的话就建立
     *
     * @param key      the key
     * @param values   the values
     * @param hashcode the hashcode 避免重复计算
     * @return the object
     */
    public Object putchild(String key, Object values, int hashcode) {
        int mycode = hashcode % code;
        if (childs == null) {
            childs = new int[code];
            childsm = new DHtreeNode[code];
            childsm[mycode] = new DHtreeNode(high + 1, key, values);
            childs[mycode] = io.write(childsm[mycode]);
            map.updateobejct_to_dic(this);
            return null;
        }
        if (childs[mycode] == 0) {
            DHtreeNode o = new DHtreeNode(high + 1, key, values);
            childsm[mycode] = o;
            childs[mycode] = io.write(o);
            map.updateobejct_to_dic(this);
            return null;
        }
        DHtreeNode chindnode = (DHtreeNode) io.read(childs[mycode]);
        childsm[mycode] = chindnode;
        if (chindnode.hasV && chindnode.key.equals(key)) {
            Object values1 = chindnode.values;
            chindnode.values = values;
            map.updateobejct_to_dic(chindnode);
            return values1;
        }
        if (!chindnode.hasV) {
            chindnode.key = key;
            chindnode.hasV = true;
            chindnode.values = values;
            map.updateobejct_to_dic(chindnode);
            return null;
        }
        return childsm[mycode].putchild(key, values, hashcode);
    }

    /**
     * Gets child.
     * 在自己的孩子节点查找。
     *
     * @param key      the key
     * @param hashcode the hashcode
     * @return the child
     */
    public Object getChild(Object key, int hashcode) {
        int mycode = hashcode % code;
        if (childs == null) {
            return null;
        }
        if (childs[mycode] == 0) {
            return null;
        }
        DHtreeNode chindnode = (DHtreeNode) io.read(childs[mycode]);
        childsm[mycode] = chindnode;
        if (childsm[mycode].hasV && childsm[mycode].key.equals(key)) {
            return childsm[mycode].values;
        }
        return childsm[mycode].getChild(key, hashcode);
    }

    /**
     * Remove child object.
     * 从孩子节点删除
     *
     * @param key      the key
     * @param hashcode the hashcode
     * @return the object 之前的v
     */
    public Object removeChild(Object key, int hashcode) {
        int mycode = hashcode % code;
        if (childs == null) {
            return null;
        }
        if (childs[mycode] == 0) {
            return null;
        }
        DHtreeNode chindnode = (DHtreeNode) io.read(childs[mycode]);
        childsm[mycode] = chindnode;
        if (childsm[mycode].hasV && childsm[mycode].key.equals(key)) {
            childsm[mycode].hasV = false;
            map.updateobejct_to_dic(chindnode);
            return childsm[mycode].values;
        }
        return childsm[mycode].removeChild(key, hashcode);
    }

    @Override
    public int compareTo(DHtreeNode o) {
        if (key == null) {
            return o == null ? 0 : o.compareTo(this);
        }
        return key.compareTo(o.key);
    }


}
