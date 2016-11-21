package map.test;

import java.io.*;

/**
 * Created by 长宏 on 2016/11/21 0021.
 */
public class TestObject implements Externalizable{
    int id = 1;
    String name = "changhong";

    public TestObject() {
    }

    public TestObject(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return String.format("id is %s ,name is %s", id, name);
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(id);
        out.writeUTF(name);

    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

        id = in.readInt();
        name = in.readUTF();
    }
}
