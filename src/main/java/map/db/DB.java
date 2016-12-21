package map.db;

import java.nio.MappedByteBuffer;
import java.util.Map;

/**
 * Created by jiang on 2016/12/21 0021.
 */
public class DB {
    private void updatemap() {
        if (map_map.size() < 1) {
            return;
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
//        Map<String,Object> htree = db.createmap("map1");
        DHtree dHtree = (DHtree) db.getmap("map1");
        System.out.println(dHtree.name);
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
        DHtree htree = new DHtree(mapname);
        int in = discIO.write(htree);
        map_map.put(mapname, in);
        updatemap();
        System.out.println(in);
        return htree;
    }


}
