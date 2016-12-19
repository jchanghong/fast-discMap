package map.htree;

/**
 * Created by 长宏 on 2016/11/23 0023.
 */
public interface Log {
    default void log(String s) {
        System.out.println(s);
    }

    default void log(int in
    ) {
        System.out.println(in);

    }
}
