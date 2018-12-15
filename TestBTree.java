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
    }
    @Test    
    public void testInsertBF3() {
        try {
            BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(3);
            tree.insert(2, 2);
            tree.insert(33, 33);
            tree.insert(16, 16);
            tree.insert(26, 26);
            tree.insert(33, 33);
            tree.insert(13, 13);
            tree.insert(25, 25);
            tree.insert(20, 20);
            
            assertEquals("{[20]}"+"\n"
                    + "{[16], [26]}"+"\n"
                    + "{[2, 13], [16]}, {[20, 25], [26, 33]}"+"\n"
                    , tree.toString());
            assertEquals("[20, 25, 26, 33, 33]", tree.rangeSearch(20, ">=").toString());
            assertEquals("[2, 13, 16]", tree.rangeSearch(16, "<=").toString());
            assertEquals("[33, 33]", tree.rangeSearch(33, "==").toString());
        }
        catch(Exception e) {
            fail("Caught Unexpected Exception: " + e.getMessage());
        }
    }
    @Test    
    public void testInsertBF4() {
        try {
            BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(4);
            tree.insert(2, 2);
            tree.insert(33, 33);
            tree.insert(16, 16);
            tree.insert(26, 26);
            tree.insert(33, 33);
            tree.insert(33, 33);
            tree.insert(13, 13);
            tree.insert(4, 4);
            tree.insert(5, 5);
            tree.insert(6, 6);
            tree.insert(14, 14);
            tree.insert(15, 15);
            
            assertEquals("{[15]}"+"\n"
                    + "{[5, 13], [26]}"+"\n"
                    + "{[2, 4], [5, 6], [13, 14]}, {[15, 16], [26, 33]}"+"\n"
                    , tree.toString());
            assertEquals("[26, 33, 33, 33]", tree.rangeSearch(26, ">=").toString());
            assertEquals("[2, 4, 5, 6, 13]", tree.rangeSearch(13, "<=").toString());
            assertEquals("[33, 33, 33]", tree.rangeSearch(33, "==").toString());
        }
        catch(Exception e) {
            fail("Caught Unexpected Exception: " + e.getMessage());
        }
    }
    @Test    
    public void testInsertBF5() {
        try {
            BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(5);
            tree.insert(9, 2);
            tree.insert(10, 33);
            tree.insert(2, 16);
            tree.insert(20, 26);
            tree.insert(15, 33);
            tree.insert(17, 13);
            tree.insert(19, 4);
            tree.insert(18, 5);
            tree.insert(30, 6);
            tree.insert(22, 14);
            tree.insert(24, 15);
            tree.insert(23, 14);
            tree.insert(27, 15);
            
            assertEquals("{[19]}"+"\n"
                    + "{[10, 17], [22, 24]}"+"\n"
                    + "{[2, 9], [10, 15], [17, 18]}, {[19, 20], [22, 23], [24, 27, 30]}"+"\n"
                    , tree.toString());
            assertEquals("[14, 14, 15, 15, 6]", tree.rangeSearch(22, ">=").toString());
            assertEquals("[16, 2, 33]", tree.rangeSearch(10, "<=").toString());
            assertEquals("[5]", tree.rangeSearch(18, "==").toString());
        }
        catch(Exception e) {
            fail("Caught Unexpected Exception: " + e.getMessage());
        }
    }
    @Test    
    public void testInsertBF6() {
        try {
            BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(6);
            tree.insert(15, 2);
            tree.insert(18, 33);
            tree.insert(19, 16);
            tree.insert(25, 26);
            tree.insert(27, 33);
            tree.insert(29, 13);
            tree.insert(15, 4);
            tree.insert(16, 5);
            tree.insert(30, 6);
            tree.insert(22, 14);
            tree.insert(24, 15);
            tree.insert(20, 14);
            tree.insert(17, 15);
            tree.insert(23, 4);
            tree.insert(21, 5);
            tree.insert(11, 6);
            tree.insert(1, 14);
            tree.insert(5, 15);
            tree.insert(9, 14);
            tree.insert(3, 15);
            tree.insert(13, 4);
            tree.insert(14, 5);
            tree.insert(12, 6);
            
            assertEquals("{[19]}"+"\n"
                    + "{[9, 13, 16], [22, 25]}"+"\n"
                    + "{[1, 3, 5], [9, 11, 12], [13, 14, 15], [16, 17, 18]}, {[19, 20, 21], [22, 23, 24], [25, 27, 29, 30]}"+"\n"
                    , tree.toString());
            assertEquals("[14, 4, 15, 26, 33, 13, 6]", tree.rangeSearch(22, ">=").toString());
            assertEquals("[14, 15, 15, 14, 6, 6, 4, 5, 4, 2, 5, 15, 33, 16]", tree.rangeSearch(19, "<=").toString());
            assertEquals("[33]", tree.rangeSearch(18, "==").toString());
        }
        catch(Exception e) {
            fail("Caught Unexpected Exception: " + e.getMessage());
        }
    }
    @Test
    public void testRangeSearchGreaterThanOrEqualToBF3() {
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(3);
        tree.insert(5,5);
        tree.insert(7, 7);
        tree.insert(4, 4);
        tree.insert(3, 3);
        tree.insert(8, 8);
        tree.insert(6, 6);
        tree.insert(12, 12);
        tree.insert(15, 15);
        tree.insert(17, 17);
        tree.insert(35, 35); 
        assertEquals("[17, 35]", tree.rangeSearch(17, ">=").toString());
    }
    @Test
    public void testRangeSearchGreaterThanOrEqualToBF4() {
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(4);
        tree.insert(5,5);
        tree.insert(7, 7);
        tree.insert(4, 4);
        tree.insert(3, 3);
        tree.insert(8, 8);
        tree.insert(6, 6);
        tree.insert(12, 12);
        tree.insert(15, 15);
        tree.insert(17, 17);
        tree.insert(35, 35); 
        assertEquals("[17, 35]", tree.rangeSearch(17, ">=").toString());
    }
    @Test
    public void testRangeSearchGreaterThanOrEqualToBF5() {
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(5);
        tree.insert(5,5);
        tree.insert(7, 7);
        tree.insert(4, 4);
        tree.insert(3, 3);
        tree.insert(8, 8);
        tree.insert(6, 6);
        tree.insert(12, 12);
        tree.insert(15, 15);
        tree.insert(17, 17);
        tree.insert(35, 35); 
        assertEquals("[17, 35]", tree.rangeSearch(17, ">=").toString());
    }
    @Test
    public void testRangeSearchGreaterThanOrEqualToBF6() {
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(6);
        tree.insert(5,5);
        tree.insert(7, 7);
        tree.insert(4, 4);
        tree.insert(3, 3);
        tree.insert(8, 8);
        tree.insert(6, 6);
        tree.insert(12, 12);
        tree.insert(15, 15);
        tree.insert(17, 17);
        tree.insert(35, 35); 
        assertEquals("[17, 35]", tree.rangeSearch(17, ">=").toString());
    }
    @Test
    public void testRangeSearchGreaterThanOrEqualToBF7() {
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(7);
        tree.insert(5,5);
        tree.insert(7, 7);
        tree.insert(4, 4);
        tree.insert(3, 3);
        tree.insert(8, 8);
        tree.insert(6, 6);
        tree.insert(12, 12);
        tree.insert(15, 15);
        tree.insert(17, 17);
        tree.insert(35, 35); 
        assertEquals("[17, 35]", tree.rangeSearch(17, ">=").toString());
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
    public void testRangeSearchLessThanOrEqualTo0BF3() {
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
    public void testRangeSearchLessThanOrEqualTo0BF4() {
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(4);
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
    public void testRangeSearchLessThanOrEqualTo0BF5() {
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(5);
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
    public void testRangeSearchLessThanOrEqualTo0BF6() {
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(6);
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
    public void testRangeSearchLessThanOrEqualTo0BF7() {
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(7);
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
    public void testRangeSearchLessThanOrEqualToBF3() {
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
        assertEquals("[3, 4, 5, 6, 7, 8, 12, 15]", tree.rangeSearch(15, "<=").toString());
    }
    @Test
    public void testRangeSearchLessThanOrEqualToBF4() {
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(4);
        tree.insert(5,5);
        tree.insert(7, 7);
        tree.insert(4, 4);
        tree.insert(3, 3);
        tree.insert(8, 8);
        tree.insert(6, 6);
        tree.insert(12, 12);
        tree.insert(15, 15);
        tree.insert(35, 35);
        assertEquals("[3, 4, 5, 6, 7, 8, 12, 15]", tree.rangeSearch(15, "<=").toString());
    }
    @Test
    public void testRangeSearchLessThanOrEqualToBF5() {
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(5);
        tree.insert(5,5);
        tree.insert(7, 7);
        tree.insert(4, 4);
        tree.insert(3, 3);
        tree.insert(8, 8);
        tree.insert(6, 6);
        tree.insert(12, 12);
        tree.insert(15, 15);
        tree.insert(35, 35);
        assertEquals("[3, 4, 5, 6, 7, 8, 12, 15]", tree.rangeSearch(15, "<=").toString());
    }
    @Test
    public void testRangeSearchLessThanOrEqualToBF6() {
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(6);
        tree.insert(5,5);
        tree.insert(7, 7);
        tree.insert(4, 4);
        tree.insert(3, 3);
        tree.insert(8, 8);
        tree.insert(6, 6);
        tree.insert(12, 12);
        tree.insert(15, 15);
        tree.insert(35, 35);
        assertEquals("[3, 4, 5, 6, 7, 8, 12, 15]", tree.rangeSearch(15, "<=").toString());
    }
    @Test
    public void testRangeSearchLessThanOrEqualToBF7() {
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(7);
        tree.insert(5,5);
        tree.insert(7, 7);
        tree.insert(4, 4);
        tree.insert(3, 3);
        tree.insert(8, 8);
        tree.insert(6, 6);
        tree.insert(12, 12);
        tree.insert(15, 15);
        tree.insert(35, 35);
        assertEquals("[3, 4, 5, 6, 7, 8, 12, 15]", tree.rangeSearch(15, "<=").toString());
    }
    @Test
    public void testRangeSearchLessThanOrEqualToEmpty() {
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(3);
        assertEquals("[]", tree.rangeSearch(20, "<=").toString());
    }
    @Test
    public void testRangeSearchEqualToBF3() {
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(3);
        tree.insert(5,5);
        tree.insert(7, 7);
        tree.insert(4, 4);
        tree.insert(3, 3);
        tree.insert(35, 35);
        assertEquals("[4]", tree.rangeSearch(4, "==").toString());
    }
    @Test
    public void testRangeSearchEqualToBF4() {
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(4);
        tree.insert(5,5);
        tree.insert(7, 7);
        tree.insert(4, 4);
        tree.insert(3, 3);
        tree.insert(35, 35);
        assertEquals("[4]", tree.rangeSearch(4, "==").toString());
    }
    @Test
    public void testRangeSearchEqualToBF5() {
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(5);
        tree.insert(5,5);
        tree.insert(7, 7);
        tree.insert(4, 4);
        tree.insert(3, 3);
        tree.insert(35, 35);
        assertEquals("[4]", tree.rangeSearch(4, "==").toString());
    }
    @Test
    public void testRangeSearchEqualToBF6() {
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(6);
        tree.insert(5,5);
        tree.insert(7, 7);
        tree.insert(4, 4);
        tree.insert(3, 3);
        tree.insert(35, 35);
        assertEquals("[4]", tree.rangeSearch(4, "==").toString());
    }
    @Test
    public void testRangeSearchEqualToBF7() {
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(7);
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
    public void testRangeSearchSortByKeysLessThanEqualTo(){
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(3);
        tree.insert(5,1);
        tree.insert(7, 2);
        tree.insert(2, 3);
        tree.insert(3, 4);
        tree.insert(9, 5);
        tree.insert(17, 6);

        assertEquals("[3, 4, 1, 2, 5, 6]", tree.rangeSearch(20, "<=").toString());
    }
    @Test
    public void testRangeSearchSortByKeysGreaterThanEqualTo(){
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(3);
        tree.insert(5,1);
        tree.insert(7, 2);
        tree.insert(2, 3);
        tree.insert(3, 4);
        tree.insert(9, 5);
        tree.insert(17, 6);

        assertEquals("[4, 1, 2, 5, 6]", tree.rangeSearch(3, ">=").toString());
    }
    @Test
    public void testRangeSearchSortByKeysEqualTo(){
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(3);
        tree.insert(5,3);
        tree.insert(7, 3);
        tree.insert(2, 3);
        tree.insert(3, 3);
        tree.insert(9, 3);
        tree.insert(17, 6);

        assertEquals("[3]", tree.rangeSearch(3, "==").toString());
    }
    
    
}
