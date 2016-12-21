package map.db;

import map.htree.HtreeTest;

import java.nio.MappedByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiang on 2016/12/21 0021.
 */
public class DB {
    private void updatemap() {
        if (map_map==null||map_map.size() < 1) {
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

    public static void main(String[] args) {
        db = DB.getInstance("d");
        db.createmap("map4");
        db.createmap("map5");
//        Map<String,Object> htree = db.createmap("map1");
        DHtree dHtree = (DHtree) db.getmap("map4");
        System.out.println(dHtree.name);
         dHtree = (DHtree) db.getmap("map5");
        System.out.println(dHtree.name);

        db.createmap("mymap");
        dHtree = (DHtree) db.getmap("mymap");
        System.out.println(dHtree.root);

    }

    MappedByteBuffer headbuff;
    private static DB db;
    DiscIO discIO;
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

    public static DB getInstance(String filename) {
        if (db == null) {
            db = new DB(filename);
        }
        return db;
    }

    public  Map<String, Object> getmap(String mapname) {
        int index = map_map.get(mapname);
        return discIO.read(index);
    }
    public  Map<String, Object> createmap(String mapname) {
        if (map_map.containsKey(mapname)) {
            return getmap(mapname);
        }
        DHtree htree = new DHtree(mapname);
        int in = discIO.write(htree);
        map_map.put(mapname, in);
        updatemap();
        System.out.println(in);
        return htree;
    }


}
