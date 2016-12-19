package map.map2;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by jiang on 2016/12/19 0019.
 */
@SuppressWarnings("Duplicates")
public class MHtree implements Map<String,Object>{
    private MHtreeNode root;

    public MHtree() {
        root = new MHtreeNode(0, null, null);
        root.childs = new MHtreeNode[root.code];
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public Object get(Object key) {
        if (key == null) {
            throw new NullPointerException("key not null");
        }
        int hashcode = Math.abs(key.hashCode());
        int code0 = hashcode %root.code;
        MHtreeNode node = root.childs[code0];
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
        MHtreeNode node = root.childs[code0];
        if (node == null) {
            root.childs[code0] = new MHtreeNode(1, key, value);
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
        MHtreeNode node = root.childs[code0];
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

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<String> keySet() {
        return null;
    }

    @Override
    public Collection<Object> values() {
        return null;
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return null;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
