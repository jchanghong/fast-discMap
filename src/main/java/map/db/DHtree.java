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
public class DHtree implements Map<String,Object>,KryoSerializable{
    static {
        ObjectSeriaer.kryo.register(DHtree.class, 23);
    }
     public DHtreeNode root;
    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObjectOrNull(output, root, DHtreeNode.class);
    }
    @Override
    public void read(Kryo kryo, Input input) {
        root = kryo.readObjectOrNull(input, DHtreeNode.class);
    }
   transient public static List<DHtreeNode> nodes = new ArrayList<>(1000);

    public DHtree() {
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
        return size()==0;
    }

    @Override
    public boolean containsKey(Object key)
    {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
      return   nodes.stream().filter(a -> a.hasV && a.values.equals(value)).count()>0;
    }

    @Override
    public Object get(Object key) {
        if (key == null) {
            throw new NullPointerException("key not null");
        }
        int hashcode = Math.abs(key.hashCode());
        int code0 = hashcode %root.code;
        DHtreeNode node = root.childsm[code0];
        if (node == null) {
            return null;
        }
        else {
            if (node.hasV && node.key.equals(key)) {
                return node.values;
            }
            return node.getChild(key, hashcode);
        }
    }

    @Override
    public Object put(String key, Object value) {
        if (key == null||value==null) {
            throw new NullPointerException("key not null");
        }
        int hashcode = Math.abs(key.hashCode());
        int code0 = hashcode % root.code;
        DHtreeNode node = root.childsm[code0];
        if (node == null) {
            root.childsm[code0] = new DHtreeNode(1, key, value);
            DHtree.nodes.add(root.childsm[code0]);
            return null;
        }
        else {
            if (node.hasV&&node.key.equals(key)) {
                node.values = value;
                return node;
            }
            if (!node.hasV) {
                node.hasV = true;
                node.key = key;
                node.values = value;
                return null;
            }
            else {
                return node.putchild(key, value,hashcode);
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
        DHtreeNode node = root.childsm[code0];
        if (node == null) {
            return null;
        }
        else {
            if (node.hasV&&node.key.equals(key)) {
                node.hasV=false;
                return node.values;
            }

                return node.removeChild(key,hashcode);


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
        Set collect = nodes.stream().filter(a -> a.hasV).map(a ->{
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


}
