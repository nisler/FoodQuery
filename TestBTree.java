import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class TestBTree {
    @Test
    public void testInsertMultiple() {
        try {
            BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(3);
            for(int i = 0; i < 20; i++) {tree.insert(18, 18);}
        }
        catch(Exception e) {
            fail("Caught Unexpected Exception: " + e.getMessage());
        }
        //TODO: check tree has 20 values?
    }
    @Test
    public void testRangeSearchGreaterThanOrEqualTo() {
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(3);
        tree.insert(5,5);
        tree.insert(7, 7);
        tree.insert(4, 4);
        tree.insert(3, 3);
        tree.insert(8, 8);
        tree.insert(6, 6);
        tree.insert(12, 12);
        tree.insert(15, 15);
        tree.insert(35, 35); 
        assertEquals("[35]", tree.rangeSearch(17, ">=").toString());
    }
    @Test
    public void testRangeSearchGreaterThanOrEqualTo0() {
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(3);
        tree.insert(5,5);
        tree.insert(7, 7);
        tree.insert(4, 4);
        tree.insert(3, 3);
        tree.insert(8, 8);
        tree.insert(6, 6);
        tree.insert(12, 12);
        tree.insert(15, 15);
        tree.insert(35, 35);
        assertEquals("[3, 4, 5, 6, 7, 8, 12, 15, 35]", tree.rangeSearch(0, ">=").toString());
    }
    @Test
    public void testRangeSearchGreaterThanOrEqualToEmpty() {
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(3);
        assertEquals("[]", tree.rangeSearch(40, ">=").toString());
    }
    @Test
    public void testRangeSearchLessThanOrEqualTo() {
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(3);
        tree.insert(5,5);
        tree.insert(7, 7);
        tree.insert(4, 4);
        tree.insert(3, 3);
        tree.insert(8, 8);
        tree.insert(6, 6);
        tree.insert(12, 12);
        tree.insert(15, 15);
        tree.insert(35, 35);
        assertEquals("[]", tree.rangeSearch(0, "<=").toString());
    }
    @Test
    public void testRangeSearchLessThanOrEqualTo0() {
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(3);
        tree.insert(5,5);
        tree.insert(7, 7);
        tree.insert(4, 4);
        tree.insert(3, 3);
        tree.insert(8, 8);
        tree.insert(6, 6);
        tree.insert(12, 12);
        tree.insert(15, 15);
        tree.insert(35, 35);
        assertEquals("[15, 12, 8, 7, 6, 5, 4, 3]", tree.rangeSearch(15, "<=").toString());
    }
    @Test
    public void testRangeSearchLessThanOrEqualToEmpty() {
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(3);
        assertEquals("[]", tree.rangeSearch(20, "<=").toString());
    }
    @Test
    public void testRangeSearchEqualTo() {
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(3);
        tree.insert(5,5);
        tree.insert(7, 7);
        tree.insert(4, 4);
        tree.insert(3, 3);
        tree.insert(35, 35);
        assertEquals("[4]", tree.rangeSearch(4, "==").toString());
    }
    @Test
    public void testRangeSearchEqualTo0() {
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(3);
        tree.insert(5,5);
        tree.insert(7, 7);
        tree.insert(4, 4);
        assertEquals("[]", tree.rangeSearch(0, "==").toString());
    }
    @Test
    public void testRangeSearchEqualToDuplicate() {
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(3);
        tree.insert(5,5);
        tree.insert(7, 7);
        tree.insert(4, 4);
        tree.insert(3, 3);
        tree.insert(8, 8);
        tree.insert(8, 8);
        tree.insert(8, 8);
        tree.insert(8, 8);
        tree.insert(6, 6);

        assertEquals("[8, 8, 8, 8]", tree.rangeSearch(8, "==").toString());
    }
    @Test
    public void testRangeSearchSortByKeys(){
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(3);
        tree.insert(5,1);
        tree.insert(7, 2);
        tree.insert(2, 3);
        tree.insert(3, 4);
        tree.insert(9, 5);
        tree.insert(17, 6);

        assertEquals("[6, 5, 2, 1, 4, 3]", tree.rangeSearch(20, "<=").toString());
    }
}
