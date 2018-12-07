/**
 * Filename:   BPTree.java
 * Project:    p5 - JavaFX Team Project
 * Course:     CS400
 * Authors:    Benjamin Nisler, Gabriella Cottiero, Olivia Gonzalez, 
 *             Timothy James, Tollan Renner
 * Due Date:   Saturday, December 15, 11:59pm
 *
 * Additional credits:
 *
 * Bugs or other notes: none
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
 * @author Olivia Gonzalez (odgonzalez2@wisc.edu)
 * @author Benjamin Nisler
 * @author Timothy James
 * @author Tollan Renner
 *
 * @param <K> key - expect a string that is the type of id for each item
 * @param <V> value - expect a user-defined type that stores all data for a food item
 */
public class BPTree<K extends Comparable<K>, V> implements BPTreeADT<K, V> {

    // Root of the tree
	// may be a leaf node (1-node tree) or internal
    private Node root;
    
    // Branching factor is the number of children nodes 
    // for internal nodes of the tree
    private int branchingFactor;
    
    // Maps each key in the tree with a list of its corresponding values
    // Each key will only be present in the tree structure once, but it may be associated with multiple values
    private HashMap<K,ArrayList<V>> valueMap = new HashMap<K,ArrayList<V>>();
    
    /**
     * Public constructor
     * 
     * creates tree with empty leaf node root and specified branching factor
     * 
     * @param branchingFactor - max number of children allowed for internal node
     * @throws IllegalArgumentException if branching factor is less than 3
     */
    public BPTree(int branchingFactor) {
    	
    	//do not allow branching factor less than 3
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
    	
    	//handle case where root is a leaf node (root is only node) separately
    	if (root instanceof BPTree.LeafNode) {
    		((BPTree.LeafNode) root).insert(key, value, valueMap);
    		
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
    	
    	//otherwise use recursion to insert into root as internal node
    	else {
            root = ((InternalNode) root).recursiveInsert(null, root, key, value, valueMap);
    	}
    }
    
    
    /*
     * Gets the values that satisfy the given range 
     * search arguments.
     * 
     * Value of comparator can be one of these: 
     * "<=", "==", ">="
     * 
     * Example:
     *     If given key = 2.5 and comparator = ">=":
     *         return all the values with the corresponding 
     *      keys >= 2.5
     *      
     * If key is null or not found, return empty list.
     * If comparator is null, empty, or not according
     * to required form, return empty list.
     * 
     * @param key to be searched
     * @param comparator is a string
     * @return list of values that are the result of the 
     * range search; if nothing found, return empty list
     */
    @Override
    public List<V> rangeSearch(K key, String comparator) {
        //return an empty list if the comparator is invalid
        if (!comparator.contentEquals(">=") && 
            !comparator.contentEquals("==") && 
            !comparator.contentEquals("<=") )
            return new ArrayList<V>();
        
        List<V> list = new ArrayList<V>();
        //handle case where root is a leaf node (root is only node) separately
        if (root instanceof BPTree.LeafNode) {
            ((LeafNode)root).rangeSearch(key, comparator, list, valueMap);
        }
        
        //otherwise use recursion to find the correct leaf to start searching in
        else {
            ((InternalNode)root).recursiveSearch(root, key, comparator, list, valueMap);
        }
        
        return list;
    }
    
    
    /*
     * Returns a string representation for the tree
     * This method is provided to students in the implementation.
     * @return a string representation
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
     * Prints out the linked list of leaf nodes in the bottom of the tree,
     * In the forwards direction (left -> right)
     * Meant for use in debugging
     */
    private void printLeafListForwards() {
    	Node current = root;
    	//keep going down the left side of the tree until we hit a leaf
    	while (!(current instanceof BPTree.LeafNode)) {
    		current = ((InternalNode) current).children.get(0);
    	}
    	
    	if (current instanceof BPTree.InternalNode) return;
    	
    	//now go through all leaves in linked list
    	LeafNode curLeaf = ((LeafNode) current);
    	while (curLeaf != null) {
    		System.out.print("{" + curLeaf + "} ");
    		curLeaf = curLeaf.next;
    	}
    	System.out.println();
    	System.out.println();
    }
    
    /**
     * Prints out the linked list of leaf nodes in the bottom of the tree,
     * In the backwards direction (right -> left)
     * Meant for use in debugging
     */
    private void printLeafListBackwards() {
    	Node current = root;
    	List<Node> children;
    	//keep going down right side of tree until we hit a leaf
    	while (!(current instanceof BPTree.LeafNode)) {
    		children = ((InternalNode) current).children;
    		current = children.get(children.size() - 1);
    	}
    	
    	if (current instanceof BPTree.InternalNode) return;
    	
    	//now go through all leaves in linked list (backwards)
    	LeafNode curLeaf = ((LeafNode) current);
    	while (curLeaf != null) {
    		System.out.print("{" + curLeaf + "} ");
    		curLeaf = curLeaf.previous;
    	}
    	System.out.println();
    	System.out.println();
    }
    
    private void printValueList() {
    	Node current = root;
    	//keep going down the left side of the tree until we hit a leaf
    	while (!(current instanceof BPTree.LeafNode)) {
    		current = ((InternalNode) current).children.get(0);
    	}
    	
    	if (current instanceof BPTree.InternalNode) return;
    	
    	//now go through all leaves in linked list
    	LeafNode curLeaf = ((LeafNode) current);
    	K key;
    	ArrayList<V> valueList;
    	while (curLeaf != null) {
    		for (int i = 0; i < curLeaf.keys.size(); i++) {
    			System.out.print("{");
    			key = curLeaf.keys.get(i);
    			System.out.print(key + ": ");
    			valueList = valueMap.get(key);
    			for (int j = 0; j < valueList.size(); j++) {
    				System.out.print(valueList.get(j));
    				if (j < (valueList.size() - 1)) {
    					System.out.print(",");
    				}
    			}
    			System.out.print("}");
    		}
    		curLeaf = curLeaf.next;
    	}
    	System.out.println();
    	System.out.println();
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
         * Gets the new sibling created after splitting the node
         * 
         * @return Node
         */
        abstract Node split();
        
        /*
         * (non-Javadoc)
         * @see BPTree#rangeSearch(java.lang.Object, java.lang.String)
         */
        abstract void rangeSearch(K key, String comparator, List<V> list, HashMap<K,ArrayList<V>> valueMap);

        /**
         * Returns true if node is overfull
         * If internal - true if # children > branching factor
         * if leaf - true if # children >= branching factor
         * @return boolean
         */
        abstract boolean isOverflow();
        
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
         * Returns true if node is overfull
         * If internal - true if # children > branching factor
         * if leaf - true if # children >= branching factor
         * @return boolean
         */
        boolean isOverflow() {
            if (children.size() > branchingFactor) return true;
            
            return false;
        }
        
        
        /**
         * Recursive helper for inserting into this subtree where current is the root
         * Finds the appropriate leaf node in this subtree, inserts into it,
         * and then rebalances the tree to maintain B tree properties
         * @param prev - the parent of the current node (previous node that was examined)
         * @param current - the root of the current subtree
         * @param key - the key being inserted
         * @param value - the value associated with the key
         */
        Node recursiveInsert(InternalNode prev, Node current, K key, V value, HashMap<K,ArrayList<V>> valueMap) {
        	//if the current node is null, don't do anything
        	if (current == null) return null;
        	
        	//if the current node is a leaf, we should insert into it
        	if (current instanceof BPTree.LeafNode) {
        		((BPTree.LeafNode) current).insert(key, value, valueMap);
        		
        		//if there is now overflow, rebalance at this node
        		if (current.isOverflow()) {
        			((LeafNode) current).rebalance(prev);
        		}
        	}
        	
        	//otherwise this node is internal and we need to find the appropriate leaf
        	else {
        		
        		//edge case 1: key is <= everything in this node
        		//so insert into the subtree with this node's first child as root
        		if (key.compareTo(current.keys.get(0)) <= 0) {
            		Node firstChild = ((InternalNode) current).children.get(0);
        			recursiveInsert((InternalNode) current, firstChild,key,value,valueMap);
        		}
        		
        		//edge case 2: key is > everything in this node
        		//so insert into the subtree with this node's last child as root
        		else if (key.compareTo(current.keys.get(current.keys.size() - 1)) > 0) {
        			Node lastChild = ((InternalNode) current).children.get(((InternalNode) current).children.size() - 1);
        			
        			recursiveInsert((InternalNode) current,lastChild,key,value,valueMap);
        		}
        		
        		//otherwise loop through all keys and find where the one we're trying to insert fits
        		else {
        			for (int i = 0; i < current.keys.size()-1; i++) {
        				//check if this key is between the current key and its neighbor
        				if ((key.compareTo(current.keys.get(i)) >= 0) && (key.compareTo(current.keys.get(i+1)) <= 0)) {
        					//if yes, insert into the child between these two keys
        					recursiveInsert((InternalNode) current,((InternalNode) current).children.get(i+1),key,value,valueMap);
        					break;
        				}
        			}
        		}
        		
        		//check for overflow in this internal node
        		if (current.isOverflow()) {
        			//if we have a previous node (ie, this node is not the root of the whole BPTree)
        			//then rebalance by promoting into that
        			if (prev != null) {
        				((InternalNode) current).rebalance(prev);
        			}
        			
        			//otherwise if there is no previous node (we're acting on the root of the whole BPTree)
        			//generate a new node, promote a key into it, and make it the new root
        			else {
        				Node sibling = current.split();
        				InternalNode newParent = new InternalNode();
        				//add pointers to children
        				newParent.children.add(sibling);
        				newParent.children.add(current);
        				//remove and promote middle element
        				//based on split, this will be the first element in the current node
        				newParent.keys.add(current.keys.remove(0));
        				//now make the new node the new root
        				current = newParent;
        			}
        		}
        	}
        	
        	//return whatever the current node is
        	return current;
        }
        
        /**
         * rebalance the BST tree where the current node (this)
         * is the root
         * @param prev - the parent of the current node (the previously visited node)
         */
        void rebalance(InternalNode prev) {
        	//split and get new sibling
        	InternalNode sibling = (InternalNode) split();
        	
        	//remove pointer to this (current) node from previous
        	prev.children.remove(this);
        	
        	//merge new parent with previous (to promote up key)
        	prev.promote(sibling,this,this.keys.remove(0));
        }
        
        /**
         * Promotes the given key and children nodes
         * into the node that we're currently looking at
         * @param child1 - smaller (based on sizes of keys) child to add pointers to
         * @param child2 - larger child to add pointers to
         * @param key - key to be promoted up
         */
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
        	
        	//case 3: somewhere in middle, so search through all keys and find where this fits
        	for (int i = 0; i < keys.size(); i++) {
        		//if this key is between element i and element i+1, put it between them
        		//and add its associated node children in between as well
        		if ((key.compareTo(keys.get(i)) > 0) && (key.compareTo(keys.get(i+1)) <= 0)) {
        			keys.add(i+1,key);
        			children.add(i+1,child2);
        			children.add(i+1,child1);
        		}
        	}
        }
        
        /**
         * Creates a new sibling node holding the smaller half of the keys
         * from the current node
         * The current node retains all keys from the middle onward
         * @return new sibling node
         */
        Node split() {
        	
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
        void rangeSearch(K key, String comparator, List<V> list, HashMap<K,ArrayList<V>> valueMap) {
            recursiveSearch(this, key, comparator, list, valueMap);
        }
        
        void recursiveSearch(Node current, K key, String comparator, List<V> list, HashMap<K,ArrayList<V>> valueMap) {
            //if the current node is null, don't do anything
            if (current == null) return;
            
            //if the current node is a leaf, we should search it
            if (current instanceof BPTree.LeafNode) {
                current.rangeSearch(key, comparator, list, valueMap);
            }
            else {
                    if(comparator.equals("<=")){
                        for (int i = current.keys.size()-1; i >= 0; i++) {
                            //check if this key is greater than or equal to current key
                            if ((key.compareTo(current.keys.get(i)) >= 0)) {
                                //if yes, search in child to right of current key
                                recursiveSearch(((InternalNode) current).children.get(i+1), key, comparator, list, valueMap);
                                break;
                            }
                            else {
                                //else search in child to left of current key
                                recursiveSearch(((InternalNode) current).children.get(i), key, comparator, list, valueMap);
                                break;
                            }
                        }
                    }
                    else if(comparator.equals(">=")){
                        for (int i = 0; i < current.keys.size(); i++) {
                            //check if this key is less than or equal to current key
                            if ((key.compareTo(current.keys.get(i)) < 0)) {
                                //search in child to left of current key if i is not 0
                                recursiveSearch(((InternalNode) current).children.get(i), key, comparator, list, valueMap);
                                break;
                            }
                            else {
                                //i is 0, search in leftmost child
                                recursiveSearch(((InternalNode) current).children.get(i + 1), key, comparator, list, valueMap);
                                break;
                            }
                        }
                    }
                    else if(comparator.equals("==")){
                        for (int i = 0; i < current.keys.size(); i++) {
                            //check if this key is equal to current key
                            if ((key.compareTo(current.keys.get(i)) < 0)){
                                //if yes, search in right child
                                recursiveSearch(((InternalNode) current).children.get(i),key,comparator, list, valueMap);
                                break;
                            }
                            else if (i == current.keys.size()){
                                recursiveSearch(((InternalNode) current).children.get(i),key,comparator, list, valueMap);
                                break;
                            }
                            else {
                                //if no, search in left child
                                recursiveSearch(((InternalNode) current).children.get(i+1),key,comparator, list, valueMap);
                                break;
                            }
                        }
                    }
                    else{
                        return;
                    }
                //}
                
            }
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

        
        // Reference to the next leaf node
        LeafNode next;
        
        // Reference to the previous leaf node
        LeafNode previous;
        
        /**
         * Package constructor
         * initializes empty list of values, keys
         */
        LeafNode() {
            super();
        }
        
        /**
         * Checks whether this leaf has too many children
         * @return true if this node has >= branchingFactor elements, false otherwise
         */
        boolean isOverflow() {
            if (keys.size() >= branchingFactor) return true;
            
            return false;
        }
        
        /**
         * Inserts the key and value into this leaf's key and value lists
         * While maintaining sorted order
         * keeps hte key and its associated value at the same index in the list
         * @param key - key to be inserted
         * @param value - value associated with the key
         */
        protected void insert(K key, V value, HashMap<K,ArrayList<V>> valueMap) {
        	
        	ArrayList<V> valuesList;
        	
        	//edge case #0 - nothing in list
        	if (keys.size() == 0) {
        		if (!valueMap.containsKey(key)) {
        			keys.add(0,key);
            		valuesList = new ArrayList<V>();
            		valueMap.put(key, valuesList);
        		}
        		else {
        			valuesList = valueMap.get(key);
        		}
        		valuesList.add(value);
        		return;
        	}
        	
        	//edge case #1 - key smaller than everything in list
        	if (key.compareTo(keys.get(0)) < 0) {
        		if (!valueMap.containsKey(key)) {
        			keys.add(0,key);
            		valuesList = new ArrayList<V>();
            		valueMap.put(key, valuesList);
        		}
        		else {
        			valuesList = valueMap.get(key);
        		}
        		valuesList.add(value);
        		return;
        	}
        	
        	//edge case #2 - key larger than everything in list
        	if (key.compareTo(keys.get(keys.size() - 1)) > 0) {
        		if (!valueMap.containsKey(key)) {
        			keys.add(key);
            		valuesList = new ArrayList<V>();
            		valueMap.put(key, valuesList);
        		}
        		else {
        			valuesList = valueMap.get(key);
        		}
        		valuesList.add(value);
        		return;
        	}
        	
        	//edge case #3 - key equal to last element in list
        	if (key.compareTo(keys.get(keys.size() - 1)) == 0) {
        		valuesList = valueMap.get(key);
        		valuesList.add(value);
        	}
        	
        	//otherwise, need to insert somewhere in the middle
            for (int i = 0; i < keys.size() - 1; i++) {
            	//if key is duplicate, 
            	if (key.compareTo(keys.get(i)) == 0) {
            		valuesList = valueMap.get(key);
            		valuesList.add(value);
            		return;
            	}
            	if ((key.compareTo(keys.get(i)) > 0) && (key.compareTo(keys.get(i+1)) < 0)) {
            		if (!valueMap.containsKey(key)) {
            			keys.add(i+1,key);
                		valuesList = new ArrayList<V>();
                		valueMap.put(key, valuesList);
            		}
            		else {
            			valuesList = valueMap.get(key);
            		}
            		valuesList.add(value);
            		return;
            	}
            }
        }
        
        /**
         * Rebalances the tree
         * Splits current node into two, and promotes up middle key
         * @param prev - the parent of the current node
         */
        void rebalance(InternalNode prev) {
        	
        	//split and get new sibling
        	LeafNode sibling = (LeafNode) split();

        	//remove pointer to this (current) node from previous
        	prev.children.remove(this);
        	
        	//merge new parent with previous (to promote up key)
        	//NOTE: unlike with internal node, the nmiddle key is not removed
        	//since all keys must be contained in leaves
        	prev.promote(sibling,this,this.keys.get(0));
        }
        
        /**
         * Splits the current node into two
         * With the new sibling getting all keys and values smaller than the middle
         * And the current node keeping everything from the middle on
         * @return the new sibling node
         */
        Node split() {
        	
            // split leaf down the middle into two new leaves
        	LeafNode sibling = new LeafNode();
        	
        	//maintain linked list pointers
        	sibling.previous = this.previous;
        	if(sibling.previous != null){sibling.previous.next = sibling;}
        	sibling.next = this;
        	this.previous = sibling;
        	
        	//give first half of values to sibling
        	for (int i = 0; i < (keys.size()/2); i++){
        		sibling.keys.add(keys.remove(0));
        	}
        	
        	return sibling;
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#rangeSearch(Comparable, String)
         */
       void rangeSearch(K key, String comparator, List<V> list, HashMap<K,ArrayList<V>> valueMap){
    	   ArrayList<V> valuesArr;
            // Case 1:  ">=
            if(comparator.equals(">=")){
                for(int i=0; i < keys.size(); i++){
                    if(key.compareTo(keys.get(i)) <= 0){
                        valuesArr = valueMap.get(keys.get(i));
                        for (int j = 0; j < valuesArr.size(); j++) {
                        	list.add(valuesArr.get(j));
                        }
                    }
                }
                
                if(this.next !=null) {
                    next.rangeSearch(key, comparator, list, valueMap);
                }
                
            }
            //Case 2: "<="
            else if(comparator.equals("<=")){
                for(int i = keys.size()-1; i >= 0; i--){
                    if(key.compareTo(keys.get(i)) >= 0){
                        valuesArr = valueMap.get(keys.get(i));
                        for (int j = 0; j < valuesArr.size(); j++) {
                        	list.add(valuesArr.get(j));
                        }
                    }
                }  
                if(this.previous !=null) {
                    previous.rangeSearch(key, comparator, list, valueMap);
                }
                
            }
            //Case 3: "=="
            else if(comparator.equals("==")){
                for(int i = keys.size()-1; i > 0; i--){
                    if((key.compareTo(keys.get(i)) == 0)){
                        valuesArr = valueMap.get(keys.get(i));
                        for (int j = 0; j < valuesArr.size(); j++) {
                        	list.add(valuesArr.get(j));
                        }
                    }
                    if((key.compareTo(keys.get(i))) > 0) {
                        return;
                    }
                }
                
                if(this.next != null) {
                    next.rangeSearch(key, comparator, list, valueMap);
                }
            }
            //Case 4: incorrect comparator
            else{
                return;
            }
        }
    } // End of class LeafNode
    
    
    /**
     * Contains a basic test scenario for a BPTree instance.
     * It shows a simple example of the use of this class
     * and its related types.
     * 
     * @param args
     */
    /**
     * @param args
     */
    public static void main(String[] args) {
        // create empty BPTree with branching factor of 3
        BPTree<Double, Double> bpTree = new BPTree<>(3);

        // create a pseudo random number generator
        Random rnd1 = new Random();

        // some value to add to the BPTree
        Double[] dd = {0.0d, 0.5d, 0.2d, 0.8d};

        //build an ArrayList of those value and add to BPTree also
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
        
        List<Double> filteredValues = bpTree.rangeSearch(0.2d, ">=");
        System.out.println("Filtered values: " + filteredValues.toString());
        System.out.println(filteredValues.size());
        
        
        BPTree<Integer, Integer> tree = new BPTree<Integer,Integer>(3);
        tree.insert(5, 5);
        tree.insert(5, 5);
        System.out.println(tree.toString());
        tree.insert(5, 5);
        System.out.println(tree.toString());
        tree.insert(5, 5);
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
        tree.insert(35, 37);
        tree.insert(35, 65);
        tree.insert(35, 12);
        
        tree.printValueList();
        System.out.println(tree.rangeSearch(12, "==").toString());
    }

} // End of class BPTree
