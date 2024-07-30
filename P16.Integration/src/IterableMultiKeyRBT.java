// --== CS400 Fall 2023 File Header Information ==--
// Name: Diana Kotsonis
// Email: dakotsonis@wisc.edu
// Group: A41
// TA: Lakshika Rathi
// Lecturer: Gary Dahl
// Notes to Grader: NA

import java.util.Iterator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Stack;

/**
 * This class creates an Iterable Multi-Key Red-Black-Tree that can have multiple keys stored into
 * one node. It extends the RedBlackTree class, and uses KeyList objects as nodes in the tree. It
 * also implements an iterator for in-order traversals of the tree.
 * 
 * @author dianakotsonis
 * @implements IterableMultiKeySortedCollectionInterface
 * @param <T> The data type for the Key being inserted into the RBT
 */
public class IterableMultiKeyRBT<T extends Comparable<T>> extends RedBlackTree<KeyListInterface<T>>
    implements IterableMultiKeySortedCollectionInterface<T> {

  private Comparable<T> startPoint; // The point the iterator should start at
  private int numKeys; // The total number of keys in the tree (including duplicates)
  private KeyList<T> keyList; // the most recently inserted node's KeyList object. It is updated
                              // with each call to insertSingleKey() and is used in our JUnit test
                              // methods

  /**
   * Inserts value into tree that can store multiple objects per key by keeping lists of objects in
   * each node of the tree.
   * 
   * @param key object to insert
   * @return true if obj was inserted
   */
  @Override
  public boolean insertSingleKey(T key) {
    // 1. Create a KeyList object with the new Key
    KeyList<T> keyList = new KeyList<>(key);

    // 2. Check if the tree already contains a node with this key value
    if (this.findNode(keyList) == null) {
      // If this key is not already in the tree, insert its KeyList object, increment the number of
      // keys, and return true
      this.insert(keyList);
      this.keyList = keyList;
      numKeys++;
      return true;
    } else {
      // If the key value is already in the tree, find the node that contains its duplicate and
      // extract its data (the KeyList object within the node)
      Node<KeyListInterface<T>> duplicateNode = (Node<KeyListInterface<T>>) this.findNode(keyList);
      KeyList<T> duplicateList = (KeyList<T>) duplicateNode.data;
      // Add the key to this duplicate KeyList, increment the number of keys, and return false
      duplicateList.addKey(key);
      numKeys++;
      this.keyList = duplicateList;
      return false;
    }
  }

  /**
   * @return the number of values in the tree.
   */
  @Override
  public int numKeys() {
    return this.numKeys;
  }

  /**
   * Returns an iterator that does an in-order iteration over the tree.
   */
  @Override
  public Iterator<T> iterator() {
    // Call the getStartStack() method to obtain an initial stack that the iterator can use
    Stack<Node<KeyListInterface<T>>> initialStack = getStartStack();
    // Initialize a new iterator object, define it's hasNext() and next() methods
    Iterator<T> iterator = new Iterator<T>() {

      /**
       * This method returns true if the initialStack is not empty and is not null, false otherwise
       * 
       * @return true if the iterator has a next value, false if not
       */
      @Override
      public boolean hasNext() {
        if (initialStack == null || initialStack.isEmpty()) {
          return false;
        }
        return true;
      }

      /**
       * This method returns the next value at the top of the stack, and updates the stack
       * 
       * @return T the value at the top of the stack
       */
      @Override
      public T next() {
        while (hasNext()) {
          // If the next node in stack has a right child/subtree, pop and return the next value from
          // the stack and add any node that must be traversed through to get to the smallest node
          // in the right subtree.
          if (initialStack.peek().down[1] != null) {
            Node<KeyListInterface<T>> rightChild = initialStack.peek().down[1];
            Node<KeyListInterface<T>> currentNode = rightChild;
            Node<KeyListInterface<T>> nodeToReturn = initialStack.pop();

            // Add the right child, and any node that must be traversed through to get to the
            // smallest (leftmost) child in the right subtree
            initialStack.push(rightChild);
            while (currentNode.down[0] != null) {
              currentNode = currentNode.down[0];
              initialStack.push(currentNode);
            }

            // Once the stack is updated, use the iterator for the KeyList object to return the
            // value stored in the KeyList object
            KeyList<T> toReturnList = (KeyList<T>) nodeToReturn.data;
            Iterator<T> listIterator = toReturnList.iterator();
            T returnValue = listIterator.next();

            // If there are multiple (duplicate) values in the KeyList object, update the KeyList
            // object so it only contains the remaining values (everything except for the first
            // value), and add this new KeyList object to the top of the stack
            if (listIterator.hasNext()) {
              T nextKeyReturn = listIterator.next();
              KeyList<T> newKeyList = new KeyList<T>(nextKeyReturn);
              while (listIterator.hasNext()) {
                newKeyList.addKey(listIterator.next());

              }
              Node<KeyListInterface<T>> newNode = new Node<KeyListInterface<T>>(newKeyList);
              initialStack.push(newNode);
            }
            return returnValue;
          } else {
            // If the next node does not have a right child/subtree, pop it off the stack and save
            // its value
            Node<KeyListInterface<T>> nodeToReturn = initialStack.pop();

            // Once the stack is updated, use the iterator for the KeyList object to return the
            // value stored in the KeyList object
            KeyList<T> toReturnList = (KeyList<T>) nodeToReturn.data;
            Iterator<T> listIterator = toReturnList.iterator();
            T returnValue = listIterator.next();

            // If there are multiple (duplicate) values in the KeyList object, update the KeyList
            // object so it only contains the remaining values (everything except for the first
            // value), and add this new KeyList object to the top of the stack
            if (listIterator.hasNext()) {
              T nextKeyReturn = listIterator.next();
              KeyList<T> newKeyList = new KeyList<T>(nextKeyReturn);
              while (listIterator.hasNext()) {
                newKeyList.addKey(listIterator.next());
              }
              Node<KeyListInterface<T>> newNode = new Node<KeyListInterface<T>>(newKeyList);
              initialStack.push(newNode);
            }
            return returnValue;
          }
        }
        return null;
      }
    };
    // Return the iterator object when this method is called
    return iterator;
  }

  /**
   * This method creates the starting Stack object of Nodes for the iterator method to use when
   * implementing an in-order iteration. If the tree is null, null is returned.
   * <p>
   * If no iteration start point is set (the field that stores the start point is set to null), the
   * stack is initialized with the nodes on the path from the root node to (and including) the node
   * with the smallest key in the tree
   * <p>
   * If the iteration start point is set, then the stack is initialized with all the nodes with keys
   * equal to or larger than the start point along the path of the search for the start point.
   * 
   * @return a Stack object that contains the Nodes in the tree (nodes inserted depend on startPoint
   *         value)
   */
  protected Stack<Node<KeyListInterface<T>>> getStartStack() {
    // Create a Stack object
    Stack<Node<KeyListInterface<T>>> initialStack = new Stack<Node<KeyListInterface<T>>>();
    // If the root of the tree is null, the tree is also null, so null is returned.
    if (this.root == null) {
      return null;
    }
    if (startPoint == null) {
      // If the startPoint is not initialized, add all nodes on the path starting from the root to
      // the smallest element in the tree (the left-most node)
      Node<KeyListInterface<T>> currentNode = this.root;
      initialStack.push(currentNode);
      while (currentNode.down[0] != null) {
        currentNode = currentNode.down[0];
        initialStack.push(currentNode);
      }
      // return the stack object
      return initialStack;
    } else {
      // Otherwise, the startPoint is initialized and all nodes greater than or equal to the
      // startPoint on the path to finding the startPoint are added to the stack
      Node<KeyListInterface<T>> currentNode = this.root;
      while (currentNode != null) {
        // Save the KeyList data stored in the currentNode, use it to call the iterator() method in
        // the KeyList class
        KeyList<T> currentKeyList = (KeyList<T>) currentNode.data;
        Iterator<T> keyListIterator = currentKeyList.iterator();
        // Save the data stored within the KeyList object
        T currentKey = keyListIterator.next();

        if (this.startPoint.compareTo(currentKey) == 0) {
          // If the startPoint is equal to the current key, add the key to the stack and return the
          // stack
          initialStack.push(currentNode);
          return initialStack;
        } else if (this.startPoint.compareTo(currentKey) < 0) {
          // If the startPoint is less than the current key, add the key to the stack and update the
          // current key to become the current key's left child
          initialStack.push(currentNode);
          currentNode = currentNode.down[0];
        } else if (this.startPoint.compareTo(currentKey) > 0) {
          // If the start point is greater than the current key, update the current key to become
          // the current key's right child.
          currentNode = currentNode.down[1];
        }
      }
    }
    // Return the stack object
    return initialStack;
  }

  /**
   * Sets the starting point for iterations. Future iterations will start at the starting point or
   * the key closest to it in the tree. This setting is remembered until it is reset. Passing in
   * null disables the starting point.
   * 
   * @param startPoint the start point to set for iterations
   */
  @Override
  public void setIterationStartPoint(Comparable<T> startPoint) {
    this.startPoint = startPoint;
  }

  /**
   * This method clears the RedBlackTree and sets the numKeys value to 0
   * 
   * @Overrides the BinarySearchTree's clear() method
   */
  @Override
  public void clear() {
    super.clear();
    this.numKeys = 0;
  }

  /**
   * This method tests the insertSingleKey() and numKeys() methods by confirming that inserting
   * nodes that are duplicates and nodes that are not duplicates is done correctly.
   * <p>
   * This test passes for key that are not duplicates if the method insertingSingleKey() returns
   * true, if a new KeyList is created with the key in it, and if the key is a new node in the RBT
   * <p>
   * This test passes for keys that are duplicates if the method insertingSingleKey() returns false,
   * if the key is added to an already created KeyList object, and if the key is not inserted as a
   * new node in the RBT
   */
  @Test
  public void testInsertingSingleKey() {
    // 1. Inserting a key that is not a duplicate:
    {
      // Create a valid Red Black Tree with multiple keys
      IterableMultiKeyRBT<Integer> testTree = new IterableMultiKeyRBT<Integer>();
      testTree.insertSingleKey(120);
      testTree.insertSingleKey(130);
      testTree.insertSingleKey(110);
      testTree.insertSingleKey(105);
      testTree.insertSingleKey(125);

      // Confirm that the Red Black Tree was created Correctly:
      KeyList<Integer> key120 = new KeyList<Integer>(120);
      KeyList<Integer> key130 = new KeyList<Integer>(130);
      KeyList<Integer> key110 = new KeyList<Integer>(110);
      KeyList<Integer> key105 = new KeyList<Integer>(105);
      key105.addKey(105);
      KeyList<Integer> key125 = new KeyList<Integer>(125);

      assertTrue(testTree.contains(key120),
          "Red Black Tree was not created propely (does not contain expected node)");
      assertTrue(testTree.contains(key130),
          "Red Black Tree was not created propely (does not contain expected node)");
      assertTrue(testTree.contains(key110),
          "Red Black Tree was not created propely (does not contain expected node)");
      assertTrue(testTree.contains(key105),
          "Red Black Tree was not created propely (does not contain expected node)");
      assertTrue(testTree.contains(key125),
          "Red Black Tree was not created propely (does not contain expected node)");

      assertEquals(testTree.numKeys(), 5,
          "Number of keys expected is incorrect from numKeys() method");

      // Insert into the test Tree and confirm that it returns true
      int testKey = 115;
      boolean result = testTree.insertSingleKey(testKey);
      assertTrue(result, "insertSingleKey() did not return true when it was supposed to");

      // Confirm the KeyList created is correct:
      KeyList<Integer> testKeyList = new KeyList<Integer>(testKey);
      assertEquals(testKeyList.compareTo(testTree.keyList), 0,
          "Key list was not created correctly");

      // Confirm that the testTree now has the testKey in it
      assertTrue(testTree.contains(testKeyList), "Key List was not inserted into the tree");
    }
    // 2. Inserting a key that is a duplicate
    // 2a. Inserting into a tree where the duplicate is the only other node in the tree
    {
      // Create an IterableMultiKeyRBT with only one key
      IterableMultiKeyRBT<Integer> testTree = new IterableMultiKeyRBT<Integer>();
      testTree.insertSingleKey(120);

      // Insert into the RBT with a duplicate key, confirm it returns false
      int testKey = 120;
      boolean result = testTree.insertSingleKey(testKey);
      assertTrue(result == false, "insertSingleKey() did not return false when it was supposed to");

      // Confirm the keyList was not created, but the key was added to the duplicate node's KeyList
      KeyList<Integer> testKeyList = new KeyList<Integer>(testKey);
      testKeyList.addKey(testKey);
      assertEquals(testKeyList.compareTo(testTree.keyList), 0,
          "Key list was not created correctly");

      // Confirm that the testTree has 2 keys
      assertEquals(testTree.numKeys(), 2,
          "Number of keys expected is incorrect from numKeys() method");
    }

    // 2b. Inserting into a large tree with a duplicate
    {
      // Create a RBT
      IterableMultiKeyRBT<Integer> testTree = new IterableMultiKeyRBT<Integer>();
      testTree.insertSingleKey(120);
      testTree.insertSingleKey(130);
      testTree.insertSingleKey(110);
      testTree.insertSingleKey(105);
      testTree.insertSingleKey(125);

      // Insert into the tree with a duplicate key and confirm it returns false:
      int testKey = 110;
      boolean result = testTree.insertSingleKey(testKey);
      assertTrue(result == false, "insertSingleKey() did not return false when it was supposed to");

      // Confirm the keyList was not created, but the key was added to the duplicate node's KeyList
      KeyList<Integer> testKeyList = new KeyList<Integer>(testKey);
      testKeyList.addKey(testKey);
      assertEquals(testKeyList.compareTo(testTree.keyList), 0,
          "Key list was not created correctly");

      // Confirm the testTree has updated the number of keys
      assertEquals(testTree.numKeys(), 6,
          "Number of keys expected is incorrect from numKeys() method");

      // Confirm the size value has remained the same
      assertEquals(testTree.size, 5, "Size has been updated when it is not supposed to");
    }

    // 3. Insert into an empty list
    {
      // Create an empty RBT (and confirm there are 0 nodes)
      IterableMultiKeyRBT<Integer> testTree = new IterableMultiKeyRBT<Integer>();
      assertEquals(testTree.numKeys(), 0,
          "Number of keys expected is incorrect from numKeys() method");

      // Insert a key into the tree and confirm it returns true:
      int testKey = 110;
      boolean result = testTree.insertSingleKey(testKey);
      assertTrue(result == true, "insertSingleKey() did not return false when it was supposed to");

      // Confirm the testTree has the correct number of nodes
      assertEquals(testTree.numKeys(), 1,
          "Number of keys expected is incorrect from numKeys() method");

      // Confirm the keyList object is in the testTree
      KeyList<Integer> key110 = new KeyList<Integer>(110);
      assertTrue(testTree.contains(key110),
          "Red Black Tree was not created propely (does not contain expected node)");
    }
    // 4. Insert into a tree with multiple duplicates
    {
      // Create an RBT with duplicates already
      IterableMultiKeyRBT<Integer> testTree = new IterableMultiKeyRBT<Integer>();
      testTree.insertSingleKey(120);
      testTree.insertSingleKey(130);
      testTree.insertSingleKey(110);
      testTree.insertSingleKey(105);
      testTree.insertSingleKey(105);
      testTree.insertSingleKey(125);

      KeyList<Integer> key120 = new KeyList<Integer>(120);
      KeyList<Integer> key130 = new KeyList<Integer>(130);
      KeyList<Integer> key110 = new KeyList<Integer>(110);
      KeyList<Integer> key105 = new KeyList<Integer>(105);
      key105.addKey(105);
      KeyList<Integer> key125 = new KeyList<Integer>(125);

      // Confirm that the Red Black Tree was created correctly (it contains all keys added and
      // number of keys is correct):

      assertTrue(testTree.contains(key120),
          "Red Black Tree was not created propely (does not contain expected node)");
      assertTrue(testTree.contains(key130),
          "Red Black Tree was not created propely (does not contain expected node)");
      assertTrue(testTree.contains(key110),
          "Red Black Tree was not created propely (does not contain expected node)");
      assertTrue(testTree.contains(key105),
          "Red Black Tree was not created propely (does not contain expected node)");
      assertTrue(testTree.contains(key125),
          "Red Black Tree was not created propely (does not contain expected node)");


      assertEquals(testTree.numKeys(), 6,
          "Number of keys expected is incorrect from numKeys() method");


      // Insert another duplicate into the tree and confirm it returns false
      int testKey = 105;
      boolean result = testTree.insertSingleKey(testKey);
      KeyList<Integer> testList = new KeyList<Integer>(testKey);
      assertTrue(result == false, "insertSingleKey() did not return false when it was supposed to");
      assertTrue(testTree.contains(testList),
          "insertingSingleKey() did not insert the key into the list");

      // Confirm the testTree has the updated number of keys and the same placement of nodes
      assertEquals(testTree.numKeys(), 7,
          "Number of keys expected is incorrect from numKeys() method");

      // Confirm the size is still 5 (no new nodes have been created)
      assertEquals(testTree.size, 5, "Size has been updated when it is not supposed to");
    }
  }

  /**
   * This method tests that the iterator() method returns the nodes in the RBT in an in-order
   * traversal order (including duplicates). We will test different starting points in the next test
   * method (these all start at the root)
   */
  @Test
  public void testIterator() {
    // 1. Confirm that the iterator returns a correct in-order traversal if there are no duplicates
    {
      // Create a RBT with no duplicates:
      IterableMultiKeyRBT<Integer> testTree = new IterableMultiKeyRBT<Integer>();
      testTree.insertSingleKey(120);
      testTree.insertSingleKey(130);
      testTree.insertSingleKey(110);
      testTree.insertSingleKey(105);
      testTree.insertSingleKey(125);

      // Confirm that the testIterator returns the nodes in the correct order (using Iterator's
      // next() and hasNext() method)
      Iterator<Integer> testIterator = testTree.iterator();
      boolean expected11 = true;
      boolean actual11 = testIterator.hasNext();
      assertEquals(expected11, actual11, "hasNext returned incorrect boolean value");
      int expected1 = 105;
      int actual1 = testIterator.next();
      assertEquals(expected1, actual1,
          "Node was not returned in the correct order from the iterator() method");

      boolean expected12 = true;
      boolean actual12 = testIterator.hasNext();
      assertEquals(expected12, actual12, "hasNext returned incorrect boolean value");
      int expected2 = 110;
      int actual2 = testIterator.next();
      assertEquals(expected2, actual2,
          "Node was not returned in the correct order from the iterator() method");

      boolean expected13 = true;
      boolean actual13 = testIterator.hasNext();
      assertEquals(expected13, actual13, "hasNext returned incorrect boolean value");
      int expected3 = 120;
      int actual3 = testIterator.next();
      assertEquals(expected3, actual3,
          "Node was not returned in the correct order from the iterator() method");

      boolean expected14 = true;
      boolean actual14 = testIterator.hasNext();
      assertEquals(expected14, actual14, "hasNext returned incorrect boolean value");
      int expected4 = 125;
      int actual4 = testIterator.next();
      assertEquals(expected4, actual4,
          "Node was not returned in the correct order from the iterator() method");

      boolean expected15 = true;
      boolean actual15 = testIterator.hasNext();
      assertEquals(expected15, actual15, "hasNext returned incorrect boolean value");
      int expected5 = 130;
      int actual5 = testIterator.next();
      assertEquals(expected5, actual5,
          "Node was not returned in the correct order from the iterator() method");

      boolean expected16 = false;
      boolean actual16 = testIterator.hasNext();
      assertEquals(expected16, actual16, "hasNext returned incorrect boolean value");
    }
    // 2. Confirm that the iterator returns a correct in-order traversal if there are mulitple
    // duplicates
    {
      // Create a RBT with multiple duplicates:
      IterableMultiKeyRBT<Integer> testTree = new IterableMultiKeyRBT<Integer>();
      testTree.insertSingleKey(120);
      testTree.insertSingleKey(130);
      testTree.insertSingleKey(110);
      testTree.insertSingleKey(105);
      testTree.insertSingleKey(125);
      testTree.insertSingleKey(130);
      testTree.insertSingleKey(130);
      testTree.insertSingleKey(105);

      // Confirm that the testIterator returns the nodes (including duplicates) in the correct order
      Iterator<Integer> testIterator = testTree.iterator();

      boolean expected10 = true;
      boolean actual10 = testIterator.hasNext();
      assertEquals(expected10, actual10, "hasNext returned incorrect boolean value");
      int expected1 = 105;
      int actual1 = testIterator.next();
      assertEquals(expected1, actual1,
          "Node was not returned in the correct order from the iterator() method");

      boolean expected101 = true;
      boolean actual101 = testIterator.hasNext();
      assertEquals(expected101, actual101, "hasNext returned incorrect boolean value");
      int expected11 = 105;
      int actual11 = testIterator.next();
      assertEquals(expected11, actual11,
          "Node was not returned in the correct order from the iterator() method");

      int expected2 = 110;
      int actual2 = testIterator.next();
      assertEquals(expected2, actual2,
          "Node was not returned in the correct order from the iterator() method");

      int expected3 = 120;
      int actual3 = testIterator.next();
      assertEquals(expected3, actual3,
          "Node was not returned in the correct order from the iterator() method");

      int expected4 = 125;
      int actual4 = testIterator.next();
      assertEquals(expected4, actual4,
          "Node was not returned in the correct order from the iterator() method");

      int expected5 = 130;
      int actual5 = testIterator.next();
      assertEquals(expected5, actual5,
          "Node was not returned in the correct order from the iterator() method");

      boolean expected50 = true;
      boolean actual50 = testIterator.hasNext();
      assertEquals(expected50, actual50, "hasNext returned incorrect boolean value");
      int expected51 = 130;
      int actual51 = testIterator.next();
      assertEquals(expected51, actual51,
          "Node was not returned in the correct order from the iterator() method");
      int expected52 = 130;
      int actual52 = testIterator.next();
      assertEquals(expected52, actual52,
          "Node was not returned in the correct order from the iterator() method");

      boolean expected = false;
      boolean actual = testIterator.hasNext();
      assertEquals(expected, actual, "hasNext returned incorrect boolean value");
    }
    // 3. Confirm iterating on an empty tree returns false for hasNext()
    {
      IterableMultiKeyRBT<Integer> testTree = new IterableMultiKeyRBT<Integer>();
      Iterator<Integer> testIterator = testTree.iterator();
      boolean expected = false;
      boolean actual = testIterator.hasNext();
      assertEquals(expected, actual, "hasNext returned incorrect boolean value");
    }
  }

  /**
   * This method tests setting different starting points for the iterator() method, and confirms
   * that the iterator() method starts from the correct node based on the starting point. Note: If
   * the starting point provided is not any node's value, the iterator object uses the next
   * available node as the starting point.
   */
  @Test
  public void testSetIterationStartPoint() {

    // 1. Confirm the iterator works if the starting point is an inner node
    {
      // Create a RBT
      IterableMultiKeyRBT<Integer> testTree = new IterableMultiKeyRBT<Integer>();
      testTree.insertSingleKey(120);
      testTree.insertSingleKey(130);
      testTree.insertSingleKey(110);
      testTree.insertSingleKey(105);
      testTree.insertSingleKey(125);

      // Set the starting point to an inner node
      testTree.setIterationStartPoint(110);

      // Confirm that the iterator only iterates from the starting point onwards
      Iterator<Integer> testIterator = testTree.iterator();
      int expected1 = 110;
      int actual1 = testIterator.next();
      assertEquals(expected1, actual1,
          "Node was not returned in the correct order from the iterator() method");
      int expected2 = 120;
      int actual2 = testIterator.next();
      assertEquals(expected2, actual2,
          "Node was not returned in the correct order from the iterator() method");
      int expected3 = 125;
      int actual3 = testIterator.next();
      assertEquals(expected3, actual3,
          "Node was not returned in the correct order from the iterator() method");
      int expected4 = 130;
      int actual4 = testIterator.next();
      assertEquals(expected4, actual4,
          "Node was not returned in the correct order from the iterator() method");

      boolean result = testIterator.hasNext();
      assertTrue(result == false,
          "Iterator did not return false when there was nothing left to iterate");
    }
    // 2. Confirm that the iterator works if the starting point is a leaf node
    {
      // Create an RBT
      IterableMultiKeyRBT<Integer> testTree = new IterableMultiKeyRBT<Integer>();
      testTree.insertSingleKey(120);
      testTree.insertSingleKey(130);
      testTree.insertSingleKey(110);
      testTree.insertSingleKey(105);
      testTree.insertSingleKey(125);

      // Set the starting point to a leaf node
      testTree.setIterationStartPoint(125);

      // Confirm that the iterator only iterates from the starting point onwards
      Iterator<Integer> testIterator = testTree.iterator();
      int expected1 = 125;
      int actual1 = testIterator.next();
      assertEquals(expected1, actual1,
          "Node was not returned in the correct order from the iterator() method");
      int expected2 = 130;
      int actual2 = testIterator.next();
      assertEquals(expected2, actual2,
          "Node was not returned in the correct order from the iterator() method");
      boolean result = testIterator.hasNext();

      assertTrue(result == false,
          "Iterator did not return false when there was nothing left to iterate");
    }

    // 3. Confirm that the iterator works if the starting point is the last node in the tree
    {
      // Create an RBT
      IterableMultiKeyRBT<Integer> testTree = new IterableMultiKeyRBT<Integer>();
      testTree.insertSingleKey(120);
      testTree.insertSingleKey(130);
      testTree.insertSingleKey(110);
      testTree.insertSingleKey(105);
      testTree.insertSingleKey(125);

      // Set the starting point to the last node
      testTree.setIterationStartPoint(130);

      // Confirm that the iterator only iterates from the starting point onwards
      Iterator<Integer> testIterator = testTree.iterator();
      int expected1 = 130;
      int actual1 = testIterator.next();
      assertEquals(expected1, actual1,
          "Node was not returned in the correct order from the iterator() method");

      boolean result = testIterator.hasNext();
      assertTrue(result == false,
          "Iterator did not return false when there was nothing left to iterate");
    }

    // 4. Confirm the iterator works if the starting point provided isn't in the tree (by using the
    // next available)
    {
      // Create an RBT
      IterableMultiKeyRBT<Integer> testTree = new IterableMultiKeyRBT<Integer>();
      testTree.insertSingleKey(120);
      testTree.insertSingleKey(130);
      testTree.insertSingleKey(110);
      testTree.insertSingleKey(105);
      testTree.insertSingleKey(125);

      // Set starting point to a value not in the tree
      testTree.setIterationStartPoint(107);

      // Confirm that the iterator iterates from the closest/next available node:
      Iterator<Integer> testIterator = testTree.iterator();

      int expected2 = 110;
      int actual2 = testIterator.next();
      assertEquals(expected2, actual2,
          "Node was not returned in the correct order from the iterator() method");
      int expected3 = 120;
      int actual3 = testIterator.next();
      assertEquals(expected3, actual3,
          "Node was not returned in the correct order from the iterator() method");
      int expected4 = 125;
      int actual4 = testIterator.next();
      assertEquals(expected4, actual4,
          "Node was not returned in the correct order from the iterator() method");
      int expected5 = 130;
      int actual5 = testIterator.next();
      assertEquals(expected5, actual5,
          "Node was not returned in the correct order from the iterator() method");
    }
  }

  /**
   * This main method calls the JUnit test methods to confirm they work correctly
   * 
   * @param args unused
   */
  public static void main(String[] args) {
    IterableMultiKeyRBT<Integer> test = new IterableMultiKeyRBT<Integer>();
    test.testInsertingSingleKey();
    test.testIterator();
    test.testSetIterationStartPoint();
  }

}
