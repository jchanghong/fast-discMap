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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by jiang on 2016/12/21 0021.
 */
public class DiscIOTest {
    DiscIO io = DiscIO.getInstance("d");

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void write() throws Exception {
        String he = "ddd";
        int index = io.write(he);
        String hh = io.read(index);
        assertEquals(hh.equals(he), true);

    }

    @Test
    public void update() throws Exception {
        String he = "ddd";
        int index = io.write(he);
        io.update("ddda", index);
        String hh = io.read(index);
        assertEquals("ddda", hh);
    }

    @Test
    public void read() throws Exception {
        String hh = io.read(537);
        assertEquals("ddda", hh);
    }

}