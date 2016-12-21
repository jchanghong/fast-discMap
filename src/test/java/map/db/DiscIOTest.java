package map.db;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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
        assertEquals(hh.equals(he),true);

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