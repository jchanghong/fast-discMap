/*
 *
 *
 *    Created on  16-12-21 下午9:49 by jiang
 *    very fast key value store 简单，快速的键值储存。
 *    特别为小文件储存设计，比如图片文件。
 *    把小文件存数据库中不是理想的选择。存在文件系统中又有太多小文件难管理
 *
 */

package map.htree;

import org.jetbrains.annotations.NotNull;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Created by jiang on 2016/12/19 0019.
 */
public class HtreeNode implements Comparable<HtreeNode>, Externalizable {

    /**
     * The High.
     */
    public int high;//root is 0
    /**
     * The Code.
     */
    public int hashtable_size;
    /**
     * The Childs.
     */
    public HtreeNode[] childs;
    /**
     * The Has v.
     */
    public boolean hasV;
    /**
     * The Key.
     */
    public String key;
    /**
     * The Values.
     */
    public Object values;

    /**
     * Instantiates a new M htree node.
     */
    public HtreeNode() {
        high=0;
        hashtable_size = HashCodes.codes[high];
        childs = new HtreeNode[hashtable_size];
    }

    /**
     * Instantiates a new M htree node.
     *
     * @param high   the high
     * @param key    the key
     * @param values the values
     */
    public HtreeNode(int high, String key, Object values) {
        this.high = high;
        hashtable_size = HashCodes.codes[high];
        this.key = key;
        this.values = values;
        hasV = key != null;
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
        this.hashtable_size = HashCodes.codes[high];
        childs = (HtreeNode[]) in.readObject();
        hasV = in.readBoolean();
        try {
            key = (String) in.readObject();
        } catch (ClassNotFoundException | IOException e) {
//            e.printStackTrace();
            key = null;
        }
        values = in.readObject();
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
        int mycode = hashcode % hashtable_size;
        if (childs == null) {
            childs = new HtreeNode[hashtable_size];
            childs[mycode] = new HtreeNode(high + 1, key, values);
            MHtree.nodes.add(childs[mycode]);
            return values;
        }
        if (childs[mycode] == null) {
            childs[mycode] = new HtreeNode(high + 1, key, values);
            MHtree.nodes.add(childs[mycode]);
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
        int mycode = hashcode % hashtable_size;
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
        int mycode = hashcode % hashtable_size;
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
    public int compareTo(@NotNull HtreeNode o) {
        if (key == null) {
            return o == null ? 0 : o.compareTo(this);
        }
        return key.compareTo(o.key);
    }
}
