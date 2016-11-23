package map;

import java.io.*;

/**
 * Created by 长宏 on 2016/11/21 0021.
 */
public class TestObject implements Serializable{
  public   int id = 1;
  public   String name = "changhong";
  public   String name1 = "changhong";


    public TestObject() {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < 25; i++) {
            b.append("1111111111111111111111111111111111111111");
        }
        name1 = b.toString();
    }

    public TestObject(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return String.format("id is %s ,name is %s", id, name);
    }
//
//    public void writeExternal(ObjectOutput out) throws IOException {
//        out.writeInt(id);
//        out.writeUTF(name);
//
//    }
//
//    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
//
//        id = in.readInt();
//        name = in.readUTF();
//    }
}
