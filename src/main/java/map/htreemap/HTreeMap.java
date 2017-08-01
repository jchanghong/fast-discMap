/*
 *
 *
 *    Created on  17-8-1 下午4:17 by jiang
 *    very fast key value store 简单，快速的键值储存。
 *    特别为小文件储存设计，比如图片文件。
 *    把小文件存数据库中不是理想的选择。存在文件系统中又有太多小文件难管理
 *
 */

/*
 *
 *
 *    Created on  16-12-21 下午9:49 by jiang
 *    very fast key value store 简单，快速的键值储存。
 *    特别为小文件储存设计，比如图片文件。
 *    把小文件存数据库中不是理想的选择。存在文件系统中又有太多小文件难管理
 *
 */

package map.htreemap;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jiang on 2016/12/19 0019.
 */
@SuppressWarnings("Duplicates")
public class HTreeMap implements Map<String, Object> ,Serializable{
    /**
     * The constant allnodes.
     */
    private  List<HtreeNode> allnodes = new ArrayList<>(1000);
    private HtreeNode root;

    public int high() {
      return   allnodes.stream().mapToInt(a -> a.high).max().getAsInt();
    }

    public void printTree() {
        int high = high();
        for (int i=0;i<=high;i++) {
            int finalI = i;
            allnodes.stream().filter(a -> a.high== finalI&&a.hasV).forEach(a -> System.out.print(a.toString()));
            System.out.println("");
        }
    }
    /**
     * Instantiates a new M htree.
     */
    public HTreeMap() {
        root = new HtreeNode(0, null, null);
        allnodes.add(root);
    }

    @Override
    public int size() {
        return (int) allnodes.stream().filter(a -> a.hasV).count();
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
        return allnodes.stream().filter(a -> a.hasV && a.values.equals(value)).count() > 0;
    }

    @Override
    public Object get(Object key) {
        if (key == null) {
            throw new NullPointerException("key not null");
        }
        int hashcode = Math.abs(key.hashCode());
        HtreeNode htreeNode = root;
        while (htreeNode != null) {
            if (htreeNode.hasV && key.equals(htreeNode.key)) {
                return htreeNode.values;
            }
            int hashindex = hashcode % htreeNode.hashtable_size;
            htreeNode = htreeNode.childs[hashindex];
        }
        return null;
    }

    @Override
    public Object put(String key, Object value) {
        if (key == null || value == null) {
            throw new NullPointerException("key not null");
        }
        int hashcode = Math.abs(key.hashCode());
        HtreeNode htreeNode = root;
        HtreeNode pre = null;
        while (htreeNode != null) {
            if (htreeNode.hasV && key.equals(htreeNode.key)) {
                Object stemp = htreeNode.values;
                htreeNode.values = value;
                return stemp;
            } else if (!htreeNode.hasV) {
                htreeNode.hasV = true;
                htreeNode.values = value;
                htreeNode.key = key;
                return null;
            }
            pre = htreeNode;
            int hashindex = hashcode % htreeNode.hashtable_size;
            htreeNode = htreeNode.childs[hashindex];
        }
            int hashindex = hashcode % pre.hashtable_size;
            pre.childs[hashindex] = new HtreeNode(pre.high + 1, key, value);
        allnodes.add(pre.childs[hashindex]);
            return null;
    }

    @Override
    public Object remove(Object key) {
        if (key == null) {
            throw new NullPointerException("key not null");
        }
        int hashcode = Math.abs(key.hashCode());
        HtreeNode htreeNode = root;
        while (htreeNode != null) {
            if (htreeNode.hasV && key.equals(htreeNode.key)) {
                htreeNode.hasV = false;
                return htreeNode.values;
            }
            int hashindex = hashcode % htreeNode.hashtable_size;
            htreeNode = htreeNode.childs[hashindex];
        }
        return null;
    }

    @Override
    public void putAll(@NotNull Map<? extends String, ?> m) {
        m.entrySet().forEach(a -> put(a.getKey(), a.getValue()));
    }

    @Override
    public void clear() {
        for (HtreeNode node : allnodes) {
            node.hasV = false;
        }
    }

    @NotNull
    @Override
    public Set<String> keySet() {
        Set<String> collect = allnodes.stream().filter(a -> a.hasV).map(a -> a.key).collect(Collectors.toSet());
        return collect;
    }

    @NotNull
    @Override
    public Collection<Object> values() {
        Set<Object> collect = allnodes.stream().filter(a -> a.hasV).map(a -> a.values).collect(Collectors.toSet());
        return collect;
    }

    @NotNull
    @Override
    public Set<Entry<String, Object>> entrySet() {
        Set collect = allnodes.stream().filter(a -> a.hasV).map(a -> new Entry() {
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
        }).collect(Collectors.toSet());
        return collect;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
