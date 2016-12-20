package map.db;


import map.htree.MHashCodes;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Created by jiang on 2016/12/19 0019.
 */
public class DHtreeNode implements Comparable<DHtreeNode> ,Externalizable {

     public int high;//root si 0
     public int code;
    public DHtreeNode[] childs;
    public boolean hasV;
    public String key;
    public Object values;

    public DHtreeNode() {
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.write(high);
        out.writeObject(childs);
        out.writeBoolean(hasV);
        out.writeObject(key);
        out.writeObject(values);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

      this.high = in.read();
        this.code = MHashCodes.codes[high];
        childs = (DHtreeNode[]) in.readObject();
        hasV = in.readBoolean();
        try {
            key = (String) in.readObject();
        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
            key = null;
        } catch (IOException e) {
//            e.printStackTrace();
            key = null;
        }
        values = in.readObject();
    }

    public DHtreeNode(int high, String key, Object values) {
        this.high = high;
        code = MHashCodes.codes[high];
        this.key = key;
        this.values = values;
        hasV = key == null || high == 0 ? false : true;
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
            childs = new DHtreeNode[code];
            childs[mycode] = new DHtreeNode(high + 1, key, values);
//            MHtree.nodes.add(childs[mycode]);
            return values;
        }
        if (childs[mycode] == null) {
            childs[mycode] = new DHtreeNode(high + 1, key, values);
//            MHtree.nodes.add(childs[mycode]);
            return values;
        }
        if (childs[mycode].hasV && childs[mycode].key.equals(key)) {
            childs[mycode].values = values;
            return childs[mycode].values;
        }
        if (!childs[mycode].hasV) {
            childs[mycode].key = key;
            childs[mycode].hasV = true;
            childs[mycode].values = values;
            return null;
        }
        return childs[mycode].putchild(key, values, hashcode);

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
        if (childs[mycode] == null) {
            return null;
        }
        if (childs[mycode].hasV && childs[mycode].key.equals(key)) {
            return childs[mycode].values;
        }
        return childs[mycode].getChild(key, hashcode);
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
        if (childs[mycode] == null) {
            return null;
        }
        if (childs[mycode].hasV && childs[mycode].key.equals(key)) {
            childs[mycode].hasV = false;
            return childs[mycode].values;
        }
        return childs[mycode].removeChild(key, hashcode);
    }

    @Override
    public int compareTo(DHtreeNode o) {
        if (key == null) {
            return o == null ? 0 : o.compareTo(this);
        }
        return key.compareTo(o.key);
    }
}
