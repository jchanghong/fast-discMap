package map.test;

import java.io.Serializable;

/**
 * Created by 长宏 on 2016/11/21 0021.
 */
public class TestObject implements Serializable{
    int id = 1;
    String name = "changhong";

    public TestObject(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return String.format("id is %s ,name is %s", id, name);
    }
}
