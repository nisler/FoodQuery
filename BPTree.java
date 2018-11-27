import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Implementation of a B+ tree to allow efficient access to
 * many different indexes of a large data set. 
 * BPTree objects are created for each type of index
 * needed by the program.  BPTrees provide an efficient
 * range search as compared to other types of data structures
 * due to the ability to perform log_m N lookups and
 * linear in-order traversals of the data items.
 * 
 * @author sapan (sapan@cs.wisc.edu)
 * @author Gabriella Cottiero (gcottiero@wisc.edu)
 *
 * @param <K> key - expect a string that is the type of id for each item
 * @param <V> value - expect a user-defined type that stores all data for a food item
 */
public class BPTree<K extends Comparable<K>, V> implements BPTreeADT<K, V> {

    // Root of the tree
    private Node root;
    
    // Branching factor is the number of children nodes 
    // for internal nodes of the tree
    private int branchingFactor;
    
    
    /**
     * Public constructor
     * 
     * creates tree with empty leaf node root and specified branching factor
     * 
     * @param branchingFactor - max number of children allowed for internal node
     * @throws IllegalArgumentException if branching factor is less than 3
     */
    public BPTree(int branchingFactor) {
        if (branchingFactor <= 2) {
            throw new IllegalArgumentException(
               "Illegal branching factor: " + branchingFactor);
        }
        
        this.branchingFactor = branchingFactor;
        this.root = new LeafNode();
    }
    
    
    /*
     * Inserts the specified key-value pair into the appropriate
     * leaf node in the BP tree
     * with rebalancing as needed
     * 
     * @param key - key used for searching and inserting into tree
     * @param value - the actual value associated with the key
     * 
     */
    @Override
    public void insert(K key, V value) {
    	
    	if (root instanceof BPTree.LeafNode) {
    		root.insert(key, value);
            //case where we overflow into the root and need to set a new one
            if (root.isOverflow()) {
    			Node sibling = root.split();
    			InternalNode newParent = new InternalNode();
    			newParent.children.add(sibling);
    			newParent.children.add(root);
    			newParent.keys.add(root.keys.get(0));
    			root = newParent;
            }
    	}
    	else {
            root = ((InternalNode) root).recursiveInsert(null,root,key,value);
    	}
    }
    
    
    /*
     * (non-Javadoc)
     * @see BPTreeADT#rangeSearch(java.lang.Object, java.lang.String)
     */
    @Override
    public List<V> rangeSearch(K key, String comparator) {
        if (!comparator.contentEquals(">=") && 
            !comparator.contentEquals("==") && 
            !comparator.contentEquals("<=") )
            return new ArrayList<V>();
        // TODO : Complete
        return null;
    }
    
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        Queue<List<Node>> queue = new LinkedList<List<Node>>();
        queue.add(Arrays.asList(root));
        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) {
            Queue<List<Node>> nextQueue = new LinkedList<List<Node>>();
            while (!queue.isEmpty()) {
                List<Node> nodes = queue.remove();
                sb.append('{');
                Iterator<Node> it = nodes.iterator();
                while (it.hasNext()) {
                    Node node = it.next();
                    sb.append(node.toString());
                    if (it.hasNext())
                        sb.append(", ");
                    if (node instanceof BPTree.InternalNode)
                        nextQueue.add(((InternalNode) node).children);
                }
                sb.append('}');
                if (!queue.isEmpty())
                    sb.append(", ");
                else {
                    sb.append('\n');
                }
            }
            queue = nextQueue;
        }
        return sb.toString();
    }
    
    
    /**
     * This abstract class represents any type of node in the tree
     * This class is a super class of the LeafNode and InternalNode types.
     * 
     * @author sapan
     */
    private abstract class Node {
        
        // List of keys
        List<K> keys;
        
        /**
         * Package constructor
         * Initializes empty list of keys
         */
        Node() {
            keys = new ArrayList<K>();
        }
        
        /**
         * Inserts key and value in the appropriate leaf node 
         * and balances the tree if required by splitting
         *  
         * @param key
         * @param value
         */
        abstract void insert(K key, V value);

        /**
         * Gets the first leaf key of the tree
         * 
         * @return key
         */
        abstract K getFirstLeafKey();
        
        /**
         * Gets the new sibling created after splitting the node
         * 
         * @return Node
         */
        abstract Node split();
        
        /*
         * (non-Javadoc)
         * @see BPTree#rangeSearch(java.lang.Object, java.lang.String)
         */
        abstract List<V> rangeSearch(K key, String comparator);

        /**
         * Returns true if node is overfull
         * If internal - true if # children > branching factor
         * if leaf - true if # children >= branching factor
         * @return boolean
         */
        abstract boolean isOverflow();
        
        abstract Node recursiveInsert(Node prev, Node root, K key, V value);
        
        /**
         * Prints out the list of keys in this node.
         * @return string representation of key list
         */
        public String toString() {
            return keys.toString();
        }
    
    } // End of abstract class Node
    
    /**
     * This class represents an internal node of the tree.
     * This class is a concrete sub class of the abstract Node class
     * and provides implementation of the operations
     * required for internal (non-leaf) nodes.
     * 
     * @author sapan
     */
    private class InternalNode extends Node {

        // List of children nodes
        List<Node> children;
        
        /**
         * Package constructor
         * Initializes with empty list of children and keys
         */
        InternalNode() {
            super();
            children = new ArrayList<Node>();
        }
        
        /**
         * Uses a recursive helper to find the first leaf key
         * in the tree
         * @return the first leaf key in the subtree
         * with this node as a root
         */
        K getFirstLeafKey() {
        	return recursiveGetFirstLeafKey(this);
        }
        
        K recursiveGetFirstLeafKey(Node n) {
        	
        	if (n == null) return null;
        	
        	if (n instanceof BPTree.LeafNode) {
        		return n.getFirstLeafKey();
        	}
        	
        	else {
        		return recursiveGetFirstLeafKey(children.get(0));
        	}
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#isOverflow()
         */
        boolean isOverflow() {
            if (children.size() > branchingFactor) return true;
            
            return false;
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#insert(java.lang.Comparable, java.lang.Object)
         */
        void insert(K key, V value) {
            this = recursiveInsert(null, this, key, value);
        }
        
        Node recursiveInsert(InternalNode prev, Node current, K key, V value) {
        	if (current == null) return null;
        	
        	if (current instanceof BPTree.LeafNode) {
        		current.insert(key, value);
        		if (current.isOverflow()) {
        			((LeafNode) current).rebalance(prev);
        		}
        	}
        	
        	else {
        		
        		//edge case 1: key is < everything in this node
        		if (key.compareTo(current.keys.get(0)) < 0) {

            		Node firstChild = ((InternalNode) current).children.get(0);
        			
        			recursiveInsert((InternalNode) current, firstChild,key,value);
        		}
        		
        		//edge case 2: key is > everything in this node
        		else if (key.compareTo(current.keys.get(current.keys.size() - 1)) > 0) {
        			
        			Node lastChild = ((InternalNode) current).children.get(((InternalNode) current).children.size() - 1);
        			
        			recursiveInsert((InternalNode) current,lastChild,key,value);
        		}
        		
        		//otherwise loop through all keys and find where the one we're trying to insert fits
        		else {
        			for (int i = 0; i < current.keys.size()-1; i++) {
        				if ((key.compareTo(current.keys.get(i)) > 0) && (key.compareTo(current.keys.get(i+1)) < 0)) {
        					recursiveInsert((InternalNode) current,((InternalNode) current).children.get(i+1),key,value);
        					break;
        				}
        			}
        		}
        		
        		//now for this combined case, check for overflow
        		if (current.isOverflow()) {
        			if (prev != null) {
        				System.out.println(prev);
        				System.out.println(current);
        				((InternalNode) current).rebalance(prev);
        			}
        			else {
        				Node sibling = current.split();
        				InternalNode newParent = new InternalNode();
        				newParent.children.add(sibling);
        				newParent.children.add(current);
        				newParent.keys.add(current.keys.remove(0));
        				current = newParent;
        			}
        		}
        	}
        	
        	return current;
        }
        
        void promote(Node child1, Node child2, K key) {
        	//case 1: key is less than everything in current key list
        	if (key.compareTo(keys.get(0)) < 0) {
        		keys.add(0,key);
        		children.add(0,child2);
        		children.add(0,child1);
        		return;
        	}
        	
        	//case 2: key is greater than everything in current key list
        	if (key.compareTo(keys.get(keys.size() - 1)) > 0) {
        		keys.add(key);
        		children.add(child1);
        		children.add(child2);
        		return;
        	}
        	
        	//case 3: somewhere in middle
        	
        	for (int i = 0; i < keys.size(); i++) {
        		if ((key.compareTo(keys.get(i)) > 0) && (key.compareTo(keys.get(i+1)) <= 0)) {
        			keys.add(i+1,key);
        			children.add(i+1,child2);
        			children.add(i+1,child1);
        		}
        	}
        }
        
        void rebalance(InternalNode prev) {
        	//split and get new sibling
        	InternalNode sibling = (InternalNode) split();
        	
        	//remove pointer to this (current) node from previous
        	prev.children.remove(this);
        	//merge new parent with previous (to promote up key)
        	prev.promote(sibling,this,this.keys.remove(0));
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#split()
         */
        Node split() {
            //create two new internal nodes, each getting half of keys and children
        	//and then create new parent node that will have those new nodes as children
        	
        	InternalNode sibling = new InternalNode();
        	
        	//give everything to left of middle key to sibling
        	for (int i = 0; i < (keys.size()/2); i++) {
        		sibling.keys.add(keys.remove(0));
        	}
        	
        	//now distribute children
        	//sibling gets everything up to middle
        	for (int k = 0; k <= children.size()/2; k++) {
        		sibling.children.add(children.remove(0));
        	}
        	
        	return sibling;
        	
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#rangeSearch(java.lang.Comparable, java.lang.String)
         */
        List<V> rangeSearch(K key, String comparator) {
            // TODO : Complete
            return null;
        }
    
    } // End of class InternalNode
    
    
    /**
     * This class represents a leaf node of the tree.
     * This class is a concrete sub class of the abstract Node class
     * and provides implementation of the operations that
     * required for leaf nodes.
     * 
     * @author sapan
     */
    private class LeafNode extends Node {
        
        // List of values
        List<V> values;
        
        // Reference to the next leaf node
        LeafNode next;
        
        // Reference to the previous leaf node
        LeafNode previous;
        
        /**
         * Package constructor
         */
        LeafNode() {
            super();
            values = new ArrayList<V>();
        }
        
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#getFirstLeafKey()
         */
        K getFirstLeafKey() {
            return keys.get(0);
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#isOverflow()
         */
        boolean isOverflow() {
            if (values.size() >= branchingFactor) return true;
            
            return false;
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#insert(Comparable, Object)
         */
        void insert(K key, V value) {
        	//edge case #0 - nothing in list
        	if (keys.size() == 0) {
        		keys.add(key);
        		values.add(value);
        		return;
        	}
        	
        	//edge case #1 - key smaller than everything in list
        	if (key.compareTo(keys.get(0)) <= 0) {
        		keys.add(0,key);
        		values.add(0,value);
        		return;
        	}
        	
        	//edge case #2 - key larger than everything in list
        	if (key.compareTo(keys.get(keys.size() - 1)) >= 0) {
        		keys.add(key);
        		values.add(value);
        		return;
        	}
        	
            for (int i = 0; i < keys.size(); i++) {
            	if ((key.compareTo(keys.get(i)) > 0) && (key.compareTo(keys.get(i+1)) <= 0)) {
            		keys.add(i+1,key);
            		values.add(i+1,value);
            		return;
            	}
            }
        }
        
        void rebalance(InternalNode prev) {
        	//split and get new sibling
        	LeafNode sibling = (LeafNode) split();
        	
        	//remove pointer to this (current) node from previous
        	prev.children.remove(this);
        	
        	//merge new parent with previous (to promote up key)
        	prev.promote(sibling,this,this.keys.get(0));
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#split()
         */
        Node split() {
            // split leaf down the middle into two new leaves
        	//and promote middle element into new internal node with leaves as children
        	
        	LeafNode sibling = new LeafNode();
        	
        	//maintain linked list pointers
        	sibling.previous = this.previous;
        	sibling.next = this;
        	this.previous = sibling;
        	
        	//give first half of values to sibling
        	for (int i = 0; i < (keys.size()/2); i++){
        		sibling.keys.add(keys.remove(0));
        		sibling.values.add(values.remove(0));
        	}
        	
        	return sibling;
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#rangeSearch(Comparable, String)
         */
        List<V> rangeSearch(K key, String comparator) {
            // TODO : Complete
            return null;
        }
        
    } // End of class LeafNode
    
    
    /**
     * Contains a basic test scenario for a BPTree instance.
     * It shows a simple example of the use of this class
     * and its related types.
     * 
     * @param args
     */
    public static void main(String[] args) {
        // create empty BPTree with branching factor of 3
        BPTree<Double, Double> bpTree = new BPTree<>(3);

        // create a pseudo random number generator
        Random rnd1 = new Random();

        // some value to add to the BPTree
        Double[] dd = {0.0d, 0.5d, 0.2d, 0.8d};

        // build an ArrayList of those value and add to BPTree also
        // allows for comparing the contents of the ArrayList 
        // against the contents and functionality of the BPTree
        // does not ensure BPTree is implemented correctly
        // just that it functions as a data structure with
        // insert, rangeSearch, and toString() working.
        List<Double> list = new ArrayList<>();
        for (int i = 0; i < 400; i++) {
            Double j = dd[rnd1.nextInt(4)];
            list.add(j);
            bpTree.insert(j, j);
            System.out.println("\n\nTree structure:\n" + bpTree.toString());
        }
        /**
        List<Double> filteredValues = bpTree.rangeSearch(0.2d, ">=");
        System.out.println("Filtered values: " + filteredValues.toString());
        **/
        
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(3);
        tree.insert(5, 5);
        System.out.println(tree.toString());
        tree.insert(7, 7);
        System.out.println(tree.toString());
        tree.insert(4, 4);
        System.out.println(tree.toString());
        tree.insert(3, 3);
        System.out.println(tree.toString());
        tree.insert(8, 8);
        System.out.println(tree.toString());
        tree.insert(6, 6);
        System.out.println(tree.toString());
        tree.insert(12, 12);
        System.out.println(tree.toString());
        tree.insert(15, 15);
        System.out.println(tree.toString());
        tree.insert(35, 35);
        
        System.out.println(tree.toString());
    }

} // End of class BPTree
