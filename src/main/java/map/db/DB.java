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

import java.nio.MappedByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiang on 2016/12/21 0021.
 */
@SuppressWarnings("ALL")
public class DB {
    /**
     * The Debug.
     */
    static public boolean debug = false;
    private static DB db;
    /**
     * The Headbuff.
     */
    MappedByteBuffer headbuff;
    /**
     * The Disc io.
     */
    DiscIO discIO;
    /**
     * The Map map.
     */
    Map<String, Integer> map_map;

    private DB(String filename) {
        discIO = DiscIO.getInstance(filename);
        headbuff = discIO.getStorage().headbuff;
        ObjectSeriaer.kryo.register(DHtree.class, 23);
        ObjectSeriaer.kryo.register(DHtreeNode.class, 22);
        if (MStorage.init) {
            updatemap();
        }
        setMap_map();
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        db = DB.getInstance("d");
        db.createorGetmap("map4");
        db.createorGetmap("map5");
//        Map<String,Object> htree = db.createorGetmap("map1");
        DHtree dHtree = (DHtree) db.getmap("map4");
        System.out.println(dHtree.name);
        dHtree = (DHtree) db.getmap("map5");
        System.out.println(dHtree.name);

        db.createorGetmap("mymap");
        dHtree = (DHtree) db.getmap("mymap");
        System.out.println(dHtree.root);

    }

    /**
     * Gets instance.
     *
     * @param filename the filename
     * @return the instance
     */
    public static DB getInstance(String filename) {
        if (db == null) {
            db = new DB(filename);
        }
        return db;
    }

    private void updatemap() {
        if (map_map == null) {
            map_map = new HashMap<>();
        }
        headbuff.position(128 * 1024);
        byte[] bytes = ObjectSeriaer.getbytes(map_map);
        headbuff.putInt(bytes.length);
        headbuff.put(bytes);
    }

    private void setMap_map() {
        headbuff.position(128 * 1024);
        int size = headbuff.getInt();
        byte[] bytes = new byte[size];
        headbuff.get(bytes, 0, bytes.length);
        map_map = ObjectSeriaer.getObject(bytes);
    }

    /**
     * Gets .name必须存在
     *
     * @param mapname the mapname
     * @return the
     */
    public Map<String, Object> getmap(String mapname) {
        int index = map_map.get(mapname);
        return discIO.read(index);
    }

    /**
     * Createor getmap map.一般用这个方法，如果不存在，就新建立map
     *
     * @param mapname the mapname
     * @return the map
     */
    public Map<String, Object> createorGetmap(String mapname) {
        if (map_map.containsKey(mapname)) {
            return getmap(mapname);
        }
        DHtree htree = new DHtree(mapname);
        int in = discIO.write(htree);
        map_map.put(mapname, in);
        updatemap();
//        System.out.println(in);
        return htree;
    }


}
