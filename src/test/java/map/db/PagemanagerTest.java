package map.db;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jiang on 2016/12/20 0020.
 */
public class PagemanagerTest {
    @Test
    public void testnumber() throws Exception {
        System.out.println(Pagesize.Max_file_size);
        System.out.println(Pagesize.max_page_number);
        double d = Pagesize.max_page_number /8;
        System.out.println(Pagesize.headsize);

    }

    @Test
    public void getfreepanages() throws Exception {

    }

}