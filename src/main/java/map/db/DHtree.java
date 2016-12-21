package map.db;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jiang on 2016/12/19 0019.
 */
@SuppressWarnings("Duplicates")
public class DHtree implements Map<String, Object>, KryoSerializable, Comparable<DHtree> {
    public DHtreeNode root;
    public String name;
    public int size;

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

    public DHtree() {

    }
    transient public List<DHtreeNode> nodes = new ArrayList<>(1000);

    public DHtree(String name) {
        this.name
                = name;
        root = new DHtreeNode(0, null, null);
        nodes.clear();
        nodes.add(root);
        root.childs = new int[root.code];
        root.childsm = new DHtreeNode[root.code];
    }

    @Override
    public int size() {
        return (int) nodes.stream().filter(a -> a.hasV == true).count();
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
        return nodes.stream().filter(a -> a.hasV && a.values.equals(value)).count() > 0;
    }

   static DiscIO io = DiscIO.getInstance("d");
    @Override
    public Object get(Object key) {
        if (key == null) {
            throw new NullPointerException("key not null");
        }
        int hashcode = Math.abs(key.hashCode());
        int code0 = hashcode % root.code;
        int node = root.childs[code0];
        if (node == 0) {
            return null;
        } else {
            DHtreeNode chid = io.read(node);
            if (chid.hasV && chid.key.equals(key)) {
                return chid.values;
            }
            return chid.getChild(key, hashcode);
        }
    }

    @Override
    public Object put(String key, Object value) {
        if (key == null || value == null) {
            throw new NullPointerException("key not null");
        }
        int hashcode = Math.abs(key.hashCode());
        int code0 = hashcode % root.code;
        int node = root.childs[code0];
        if (node == 0) {
            root.childsm[code0] = new DHtreeNode(1, key, value);
            root.childs[code0] = io.write(root.childsm[code0]);
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
    public void putAll(Map<? extends String, ?> m) {
        m.entrySet().forEach(a -> put(a.getKey(), a.getValue()));
    }

    @Override
    public void clear() {
        for (DHtreeNode node : nodes) {
            node.hasV = false;
        }
    }

    @Override
    public Set<String> keySet() {
        Set<String> collect = nodes.stream().filter(a -> a.hasV).map(a -> a.key).collect(Collectors.toSet());
        return collect;
    }

    @Override
    public Collection<Object> values() {
        Set<Object> collect = nodes.stream().filter(a -> a.hasV).map(a -> a.values).collect(Collectors.toSet());
        return collect;
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        Set collect = nodes.stream().filter(a -> a.hasV).map(a -> {
            return new Entry() {
                @Override
                public Object getKey() {
                    return a.key;
                }

                @Override
                public Object getValue() {
                    return a.values;
                }

                @Override
                public Object setValue(Object value) {
                    return null;
                }
            };
        }).collect(Collectors.toSet());
        return collect;
    }

    @Override
    public String toString() {
        return super.toString();
    }


    @Override
    public int compareTo(DHtree o) {
        if (name == null) {
            if (o.name == null) {
                return 0;
            } else {
                return -1;
            }
        } else return name.compareTo(o.name);
    }
}
