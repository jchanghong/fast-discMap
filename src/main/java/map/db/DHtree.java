/*
 *
 *
 *    Created on  16-12-21 下午9:49 by jiang
 *    very fast key value store 简单，快速的键值储存。
 *    特别为小文件储存设计，比如图片文件。
 *    把小文件存数据库中不是理想的选择。存在文件系统中又有太多小文件难管理
 *
 */

/*
 *
 *   * ${NAME}.java
 *   * Created on  20${DATE} ${TIME}
 *  版本       修改时间          作者      修改内容
 *   V1.0.1   20${DATE}      ${USER}    初始版本
 *   very fast key value store 简单，快速的键值储存。
 *   特别为小文件储存设计，比如图片文件。
 *   把小文件存数据库中不是理想的选择。存在文件系统中又有太多小文件难管理
 * /
 */

package map.db;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by jiang on 2016/12/19 0019.
 */
@SuppressWarnings({"Duplicates", "ConstantConditions"})
public class DHtree implements Map<String, Object>, KryoSerializable, Comparable<DHtree> {
    /**
     * The Io.
     */
    static DiscIO io = DiscIO.getInstance("d");
    /**
     * The Root.
     */
    public DHtreeNode root;
    /**
     * The Name.
     */
    public String name;
    /**
     * The Size.
     */
    public int size;
    Map<String, Object> cachemap = new HashMap<>();

    /**
     * Instantiates a new D htree.
     */
    public DHtree() {

    }

    /**
     * Instantiates a new D htree.
     *
     * @param name the name
     */
    public DHtree(String name) {
        this.name
                = name;
        root = new DHtreeNode(0, null, null);
        root.childs = new int[root.code];
        root.childsm = new DHtreeNode[root.code];
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, root);
        output.writeString(name);
        output.writeInt(size);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        root = kryo.readObject(input, DHtreeNode.class);
        name = input.readString();
        size = input.readInt();
        if (this.root.childsm == null) {
            this.root.childsm = new DHtreeNode[this.root.code];
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof DHtree) {
            DHtree o = (DHtree) obj;
            return name.equals(o.name) && root.equals(o.root);
        }
        return super.equals(obj);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
//        return allnodes.stream().filter(a -> a.hasV && a.values.equals(value)).count() > 0;
        return false;
    }

    @Override
    public Object get(Object key) {
        if (key == null) {
            throw new NullPointerException("key not null");
        }
        Object o = cachemap.get(key);
        if (o != null) {
            return o;
        }
        int hashcode = Math.abs(key.hashCode());
        int code0 = hashcode % root.code;
        int node = root.childs[code0];
        if (node == 0) {
            return null;
        } else {
            DHtreeNode chid = io.read(node);
            if (chid.hasV && chid.key.equals(key)) {
                cachemap.put(key.toString(), chid.values);
                return chid.values;
            }
            Object v = chid.getChild(key, hashcode);
            cachemap.put(key.toString(), v);
            return v;
        }
    }

    @Override
    public Object put(String key, Object value) {
        if (key == null || value == null) {
            throw new NullPointerException("key not null");
        }
        cachemap.put(key, value);
        int hashcode = Math.abs(key.hashCode());
        int code0 = hashcode % root.code;
        int node = root.childs[code0];
        if (node == 0) {
            root.childsm[code0] = new DHtreeNode(1, key, value);
            root.childs[code0] = io.write(root.childsm[code0]);
            size++;
            io.update(this, ObjectMap.getindex(this));
            return null;
        } else {
            DHtreeNode chid = io.read(node);
            root.childsm[code0] = chid;
            if (chid.hasV && chid.key.equals(key)) {
                Object o = chid.values;
                chid.values = value;
                io.update(chid, node);
                return o;
            }
            if (!chid.hasV) {
                chid.hasV = true;
                chid.key = key;
                chid.values = value;
                io.update(chid, node);
                return null;
            } else {
                return chid.putchild(key, value, hashcode);
            }
        }
    }

    @Override
    public Object remove(Object key) {
        if (key == null) {
            throw new NullPointerException("key not null");
        }
        cachemap.remove(key);
        int hashcode = Math.abs(key.hashCode());
        int code0 = hashcode % root.code;
        int node = root.childs[code0];
        if (node == 0) {
            return null;
        } else {
            DHtreeNode chid = io.read(node);
            root.childsm[code0] = chid;
            if (chid.hasV && chid.key.equals(key)) {
                chid.hasV = false;
                io.update(chid, node);
                return chid.values;
            }
            return chid.removeChild(key, hashcode);

        }
    }

    @Override
    public void putAll(@NotNull Map<? extends String, ?> m) {
        m.entrySet().forEach(a -> put(a.getKey(), a.getValue()));
    }

    @Override
    public void clear() {
//
    }

    @NotNull
    @Override
    public Set<String> keySet() {
//        Set<String> collect = allnodes.stream().filter(a -> a.hasV).map(a -> a.key).collect(Collectors.toSet());
        return null;
//        return collect;
    }

    @NotNull
    @Override
    public Collection<Object> values() {
//        Set<Object> collect = allnodes.stream().filter(a -> a.hasV).map(a -> a.values).collect(Collectors.toSet());
        return null;
//        return collect;
    }

    @NotNull
    @Override
    public Set<Entry<String, Object>> entrySet() {
//        Set collect = allnodes.stream().filter(a -> a.hasV).map(a -> {
//            return new Entry() {
//                @Override
//                public Object getKey() {
//                    return a.key;
//                }
//
//                @Override
//                public Object getValue() {
//                    return a.values;
//                }
//
//                @Override
//                public Object setValue(Object value) {
//                    return null;
//                }
//            };
//        }).collect(Collectors.toSet());
//        return collect;
        return null;
    }

    @Override
    public String toString() {
        return super.toString();
    }


    @Override
    public int compareTo(@NotNull DHtree o) {
        if (name == null) {
            if (o.name == null) {
                return 0;
            } else {
                return -1;
            }
        } else return name.compareTo(o.name);
    }
}
