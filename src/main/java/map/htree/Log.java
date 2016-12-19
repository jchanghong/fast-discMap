package map.htree;

/**
 * Created by 长宏 on 2016/11/23 0023.
 */
public interface Log {
    default void log(Object s) {
        System.out.println(s.toString());
    }
}
