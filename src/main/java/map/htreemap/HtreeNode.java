/*
 *
 *
 *    Created on  17-8-1 下午4:17 by jiang
 *    very fast key value store 简单，快速的键值储存。
 *    特别为小文件储存设计，比如图片文件。
 *    把小文件存数据库中不是理想的选择。存在文件系统中又有太多小文件难管理
 *
 */

/*
 *
 *
 *    Created on  16-12-21 下午9:49 by jiang
 *    very fast key value store 简单，快速的键值储存。
 *    特别为小文件储存设计，比如图片文件。
 *    把小文件存数据库中不是理想的选择。存在文件系统中又有太多小文件难管理
 *
 */

package map.htreemap;

import com.google.common.base.Objects;

/**
 * Created by jiang on 2016/12/19 0019.
 */
public class HtreeNode {

    /**
     * The High.
     */
    public int high;//root is 0

    public int hashtable_size;

    /**
     * The Childs.
     */
    public HtreeNode[] childs;
    /**
     * The Has v.
     */
    public boolean hasV;
    /**
     * The Key.
     */
    public String key;
    /**
     * The Values.
     */
    public Object values;

    /**
     * Instantiates a new M htree node.
     *
     * @param high   the high
     * @param key    the key
     * @param values the values
     */
    public HtreeNode(int high, String key, Object values) {
        this.high = high;
        hashtable_size = HashCodes.codes[high];
        this.key = key;
        this.values = values;
        childs = new HtreeNode[hashtable_size];
        hasV = key != null;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("key", key).
                add("values", values).
                add("hashsize", hashtable_size).toString();
    }
}
