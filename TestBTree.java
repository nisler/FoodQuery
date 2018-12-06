import static org.junit.jupiter.api.Assertions.*;

import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

class TestBTree {
    @Test
    void testInsertMultiple() {
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
    void testRangeSearchGreaterThanOrEqualTo() {
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
    void testRangeSearchGreaterThanOrEqualTo0() {
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
        assertEquals("[]", tree.rangeSearch(0, ">=").toString());
    }
    @Test
    void testRangeSearchGreaterThanOrEqualToEmpty() {
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(3);
        assertEquals("[]", tree.rangeSearch(40, ">=").toString());
    }
    @Test
    void testRangeSearchLessThanOrEqualTo() {
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
    void testRangeSearchLessThanOrEqualTo0() {
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
        assertEquals("[5, 7, 4, 3, 8, 6, 12, 15]", tree.rangeSearch(15, "<=").toString());
    }
    @Test
    void testRangeSearchLessThanOrEqualToEmpty() {
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(3);
        assertEquals("[]", tree.rangeSearch(20, "<=").toString());
    }
    @Test
    void testRangeSearchEqualTo() {
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(3);
        tree.insert(5,5);
        tree.insert(7, 7);
        tree.insert(4, 4);
        tree.insert(3, 3);
        tree.insert(35, 35);
        assertEquals("[4]", tree.rangeSearch(4, "==").toString());
    }
    @Test
    void testRangeSearchEqualTo0() {
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(3);
        tree.insert(5,5);
        tree.insert(7, 7);
        tree.insert(4, 4);
        assertEquals("[]", tree.rangeSearch(0, "==").toString());
    }
    @Test
    void testRangeSearchGreaterThanOrEqualToMulti() {
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
    void testToString() {
        fail("Not yet implemented");
    }

}
