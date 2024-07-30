// --== CS400 Fall 2023 File Header Information ==--
// Name: Diana Kotsonis
// Email: dakotsonis
// Group: A41
// TA: Lakshika Rathi
// Lecturer: Gary Dahl
// Notes to Grader: n/a

import java.util.LinkedList;
import java.util.Stack;


/**
 * Binary Search Tree implementation with a Node inner class for representing the nodes of the tree.
 * We will turn this Binary Search Tree into a self-balancing tree as part of project 1 by modifying
 * its insert functionality. In week 0 of project 1, we will start this process by implementing tree
 * rotations.
 */
public class BinarySearchTree<T extends Comparable<T>> implements SortedCollectionInterface<T> {

  /**
   * This class represents a node holding a single value within a binary tree.
   */
  protected static class Node<T> {
    public T data;

    // up stores a reference to the node's parent
    public Node<T> up;
    // The down array stores references to the node's children:
    // - down[0] is the left child reference of the node,
    // - down[1] is the right child reference of the node.
    // The @SupressWarning("unchecked") annotation is use to supress an unchecked
    // cast warning. Java only allows us to instantiate arrays without generic
    // type parameters, so we use this cast here to avoid future casts of the
    // node type's data field.
    @SuppressWarnings("unchecked")
    public Node<T>[] down = (Node<T>[]) new Node[2];

    public Node(T data) {
      this.data = data;
    }

    /**
     * @return true when this node has a parent and is the right child of that parent, otherwise
     *         return false
     */
    public boolean isRightChild() {
      return this.up != null && this.up.down[1] == this;
    }

  }

  protected Node<T> root; // reference to root node of tree, null when empty
  protected int size = 0; // the number of values in the tree

  /**
   * Inserts a new data value into the tree. This tree will not hold null references, nor duplicate
   * data values.
   * 
   * @param data to be added into this binary search tree
   * @return true if the value was inserted, false if is was in the tree already
   * @throws NullPointerException when the provided data argument is null
   */
  public boolean insert(T data) throws NullPointerException {
    if (data == null)
      throw new NullPointerException("Cannot insert data value null into the tree.");
    return this.insertHelper(new Node<>(data));
  }

  /**
   * Performs a naive insertion into a binary search tree: adding the new node in a leaf position
   * within the tree. After this insertion, no attempt is made to restructure or balance the tree.
   * 
   * @param node the new node to be inserted
   * @return true if the value was inserted, false if is was in the tree already
   * @throws NullPointerException when the provided node is null
   */
  protected boolean insertHelper(Node<T> newNode) throws NullPointerException {
    if (newNode == null)
      throw new NullPointerException("new node cannot be null");

    if (this.root == null) {
      // add first node to an empty tree
      root = newNode;
      size++;
      return true;
    } else {
      // insert into subtree
      Node<T> current = this.root;
      while (true) {
        int compare = newNode.data.compareTo(current.data);
        if (compare == 0) {
          return false;
        } else if (compare < 0) {
          // insert in left subtree
          if (current.down[0] == null) {
            // empty space to insert into
            current.down[0] = newNode;
            newNode.up = current;
            this.size++;
            return true;
          } else {
            // no empty space, keep moving down the tree
            current = current.down[0];
          }
        } else {
          // insert in right subtree
          if (current.down[1] == null) {
            // empty space to insert into
            current.down[1] = newNode;
            newNode.up = current;
            this.size++;
            return true;
          } else {
            // no empty space, keep moving down the tree
            current = current.down[1];
          }
        }
      }
    }
  }

  /**
   * Performs the rotation operation on the provided nodes within this tree. When the provided child
   * is a left child of the provided parent, this method will perform a right rotation. When the
   * provided child is a right child of the provided parent, this method will perform a left
   * rotation. When the provided nodes are not related in one of these ways, this method will throw
   * an IllegalArgumentException.
   * 
   * @param child  is the node being rotated from child to parent position (between these two node
   *               arguments)
   * @param parent is the node being rotated from parent to child position (between these two node
   *               arguments)
   * @throws IllegalArgumentException when the provided child and parent node references are not
   *                                  initially (pre-rotation) related that way
   */
  protected void rotate(Node<T> child, Node<T> parent) throws IllegalArgumentException {
    // Check if the relationship between nodes is correct. If not, throw an IllegalArgumentException
    if ((parent.down[0] != child && parent.down[1] != child) || child.up != parent) {
      throw new IllegalArgumentException("the relationship between nodes is incorrect");
    }
    // Check if it is a right child. If so, perform a left rotation
    if (child.isRightChild()) {
      // If the child has either no children or only a right child:
      if (child.down[0] == null) {
        // Check if the parent is a root. If so, change all possible relationships between the 
        // parent and child to the new relationships after rotation
        if (root.equals(parent)) {
          parent.down[1] = null;
          child.up = null;

          child.down[0] = parent;
          parent.up = child;
          root = child;
        }
        // Otherwise, the parent is not a root and its parent is considered in the rotation:
        // The parentOfParent variable represents the parent node's parent
        else {
          Node<T> parentOfParent = parent.up;
          // Check if the parent is a left child of its parent. If so, change all possible 
          // relationships between the parent, the parent's parent and the child to the new 
          // relationships after rotation
          if (parentOfParent != null && parentOfParent.down[0] != null && parentOfParent.down[0].equals(parent)) {
            parentOfParent.down[0] = null;
            parent.down[1] = null;
            child.up = null;
            parent.up = null;

            parentOfParent.down[0] = child;
            child.down[0] = parent;
            parent.up = child;
            child.up = parentOfParent;
          }
          // Otherwise, the parent is a right child of its parent. Change all possible 
          // relationships between the parent, the parent's parent and the child to the new 
          // relationships after rotation
          else if (parentOfParent == null) {
            return;
          }
          else {
            parentOfParent.down[1] = null;
            parent.down[1] = null;
            child.up = null;
            parent.up = null;

            parentOfParent.down[1] = child;
            child.down[0] = parent;
            parent.up = child;
            child.up = parentOfParent;
          }
        }
      }
      // If the child has either a left child or two children:
      // The childOfChild variable represents the child's left child
      else if (child.down[0] != null) {
        // Check if the parent is a root. If so, change all possible relationships between the 
        // parent, child, and the child's child to the new relationships after rotation
        if (root.equals(parent)) {
          Node<T> childOfChild = child.down[0];
          parent.down[1] = null;
          child.down[0] = null;

          child.down[0] = parent;
          parent.down[1] = childOfChild;
          childOfChild.up = parent;
          parent.up = child;
          root = child;
        }
        // Otherwise, the parent is not a root and its parent is considered in the rotation:
        else {
          Node<T> parentOfParent = parent.up;
          // Check if the parent is the left child of its parent. If so, change all possible 
          // relationships between the parent, the parent's parent, the child, and the child's child
          // to the new relationships after rotation
          if (parentOfParent.down[0] != null && parentOfParent.down[0].equals(parent)) {
            Node<T> childsChild = child.down[0];
            parentOfParent.down[0] = null;
            parent.down[1] = null;
            child.down[0] = null;
            childsChild.up = null;
            child.up = null;
            parent.up = null;

            parentOfParent.down[0] = child;
            child.down[0] = parent;
            parent.down[1] = childsChild;
            childsChild.up = parent;
            parent.up = child;
            child.up = parentOfParent;
          }
          // Otherwise, the parent is the right child of its parent. Change all possible
          // relationships between the parent, the parent's parent, the child, and the child's child
          // to the new relationships after rotation
          else {
            Node<T> childsChild = child.down[0];
            parentOfParent.down[1] = null;
            parent.down[1] = null;
            child.down[0] = null;
            childsChild.up = null;
            child.up = null;
            parent.up = null;

            parentOfParent.down[1] = child;
            child.down[0] = parent;
            parent.down[1] = childsChild;
            childsChild.up = parent;
            parent.up = child;
            child.up = parentOfParent;
          }
        }
      }
    }
    // Otherwise, the child is a left child and a right rotation must occur:
    else {
      // If the child either has no children or has only a left child:
      if (child.down[1] == null) {
        // Check if the parent is a root. If so, change all possible relationships between the 
        // parent and child to the new relationships after rotation
        if (root.equals(parent)) {
          parent.down[0] = null;
          child.up = null;
          root = child;
          child.down[1] = parent;
          parent.up = child;
        }
        // Otherwise, the parent is not a root and its parent must be considered in the rotation:
        // The parentOfParent variable represents the parent variable's parent
        else {
          Node<T> parentOfParent = parent.up;
          // Check if the parent is the left child of its parent. If so, change all possible 
          // relationships between the parent, the parent's parent and the child to the new 
          // relationships after rotation
          if (parentOfParent.down[0] != null && parentOfParent.down[0].equals(parent)) {
            parentOfParent.down[0] = null;
            parent.down[0] = null;
            child.up = null;
            parent.up = null;

            parentOfParent.down[0] = child;
            child.down[1] = parent;
            parent.up = child;
            child.up = parentOfParent;
          }
          // Otherwise, the parent is the right child of its parent. Change all possible 
          // relationships between the parent, the parent's parent and the child to the new 
          // relationships after rotation
          else if (parentOfParent.down[1].equals(parent)) {
            parentOfParent.down[1] = null;
            parent.down[0] = null;
            child.up = null;
            parent.up = null;

            parentOfParent.down[1] = child;
            child.down[1] = parent;
            parent.up = child;
            child.up = parentOfParent;
          }
        }
      }
      // If child has either one right child or two children:
      // The childsChild varaible represents the child node's right child
      else if (child.down[1] != null) {
        // Check if the parent is a root. If so, change all possible relationships between the 
        // parent, child, and the child's child to the new relationships after rotation
        if (root.equals(parent)) {
          Node<T> childsChild = child.down[1];
          parent.down[0] = null;
          child.down[1] = null;
          child.up = null;
          childsChild.up = null;

          child.down[1] = parent;
          parent.down[0] = childsChild;
          child.down[1] = parent;
          childsChild.up = parent;
          parent.up = child;
          root = child;
        }
        // Otherwise, the parent is not a root and its parent must be considered when rotating
        else {
          Node<T> parentOfParent = parent.up;
          // Check if the parent is the left child of its parent. If so, change all possible 
          // relationships between the parent, the parent's parent, the child, and the child's child
          // to the new relationships after rotation
          if (parentOfParent.down[0] != null && parentOfParent.down[0].equals(parent)) {
            Node<T> childsChild = child.down[1];
            Node<T> parentsParent = parent.up;
            parentsParent.down[0] = null;
            parent.down[0] = null;
            child.down[1] = null;
            childsChild.up = null;
            child.up = null;
            parent.up = null;

            parentsParent.down[0] = child;
            child.down[1] = parent;
            parent.down[0] = childsChild;
            childsChild.up = parent;
            parent.up = child;
            child.up = parentsParent;
          }
          // Otherwise, the parent is the right child of its parent. Change all possible
          // relationships between the parent, the parent's parent, the child, and the child's child
          // to the new relationships after rotation
          else {
            Node<T> parentsParent = parent.up;
            Node<T> childsChild = child.down[1];
            parentsParent.down[1] = null;
            parent.down[0] = null;
            child.down[1] = null;
            childsChild.up = null;
            child.up = null;
            parent.up = null;
            
            parentsParent.down[1] = child;
            child.down[1] = parent;
            parent.down[0] = childsChild;
            childsChild.up = parent;
            parent.up = child;
            child.up = parentsParent;
          }
        }
      }
    }
  }

  /**
   * Get the size of the tree (its number of nodes).
   * 
   * @return the number of nodes in the tree
   */
  public int size() {
    return size;
  }

  /**
   * Method to check if the tree is empty (does not contain any node).
   * 
   * @return true of this.size() returns 0, false if this.size() != 0
   */
  public boolean isEmpty() {
    return this.size() == 0;
  }

  /**
   * Checks whether the tree contains the value *data*.
   * 
   * @param data a comparable for the data value to check for
   * @return true if *data* is in the tree, false if it is not in the tree
   */
  public boolean contains(Comparable<T> data) {
    // null references will not be stored within this tree
    if (data == null) {
      throw new NullPointerException("This tree cannot store null references.");
    } else {
      Node<T> nodeWithData = this.findNode(data);
      // return false if the node is null, true otherwise
      return (nodeWithData != null);
    }
  }

  /**
   * Removes all keys from the tree.
   */
  public void clear() {
    this.root = null;
    this.size = 0;
  }

  /**
   * Helper method that will return the node in the tree that contains a specific key. Returns null
   * if there is no node that contains the key.
   * 
   * @param data the data value for which we want to find the node that contains it
   * @return the node that contains the data value or null if there is no such node
   */
  protected Node<T> findNode(Comparable<T> data) {
    Node<T> current = this.root;
    while (current != null) {
      int compare = data.compareTo(current.data);
      if (compare == 0) {
        // we found our value
        return current;
      } else if (compare < 0) {
        if (current.down[0] == null) {
          // we have hit a null node and did not find our node
          return null;
        }
        // keep looking in the left subtree
        current = current.down[0];
      } else {
        if (current.down[1] == null) {
          // we have hit a null node and did not find our node
          return null;
        }
        // keep looking in the right subtree
        current = current.down[1];
      }
    }
    return null;
  }

  /**
   * This method performs an inorder traversal of the tree. The string representations of each data
   * value within this tree are assembled into a comma separated string within brackets (similar to
   * many implementations of java.util.Collection, like java.util.ArrayList, LinkedList, etc).
   * 
   * @return string containing the ordered values of this tree (in-order traversal)
   */
  public String toInOrderString() {
    // generate a string of all values of the tree in (ordered) in-order
    // traversal sequence
    StringBuffer sb = new StringBuffer();
    sb.append("[ ");
    if (this.root != null) {
      Stack<Node<T>> nodeStack = new Stack<>();
      Node<T> current = this.root;
      while (!nodeStack.isEmpty() || current != null) {
        if (current == null) {
          Node<T> popped = nodeStack.pop();
          sb.append(popped.data.toString());
          if (!nodeStack.isEmpty() || popped.down[1] != null)
            sb.append(", ");
          current = popped.down[1];
        } else {
          nodeStack.add(current);
          current = current.down[0];
        }
      }
    }
    sb.append(" ]");
    return sb.toString();
  }

  /**
   * This method performs a level order traversal of the tree. The string representations of each
   * data value within this tree are assembled into a comma separated string within brackets
   * (similar to many implementations of java.util.Collection). This method will be helpful as a
   * helper for the debugging and testing of your rotation implementation.
   * 
   * @return string containing the values of this tree in level order
   */
  public String toLevelOrderString() {
    StringBuffer sb = new StringBuffer();
    sb.append("[ ");
    if (this.root != null) {
      LinkedList<Node<T>> q = new LinkedList<>();
      q.add(this.root);
      while (!q.isEmpty()) {
        Node<T> next = q.removeFirst();
        if (next.down[0] != null)
          q.add(next.down[0]);
        if (next.down[1] != null)
          q.add(next.down[1]);
        sb.append(next.data.toString());
        if (!q.isEmpty())
          sb.append(", ");
      }
    }
    sb.append(" ]");
    return sb.toString();
  }

  public String toString() {
    return "level order: " + this.toLevelOrderString() + "\nin order: " + this.toInOrderString();
  }

  // Implement at least 3 tests using the methods below. You can
  // use your notes from lecture for ideas of rotation examples to test with.
  // Make sure to include rotations at the root of a tree in your test cases.
  // Give each of the methods a meaningful header comment that describes what is being
  // tested and make sure your tests have inline comments that help with reading your test code.
  // If you'd like to add additional tests, then name those methods similar to the ones given below.
  // Eg: public static boolean test4() {}
  // Do not change the method name or return type of the existing tests.
  // You can run your tests through the static main method of this class.

  /**
   * This test method uses a balanced Binary Search Tree of Integers and tests both left and right
   * rotations on all possible types of parents (if a parent is a root, if it is a right child, if
   * it is a left child). It also confirms that the exceptions that are meant to be thrown work
   * correctly.
   * 
   * @return true if and only if all test cases pass, false otherwise
   */
  public static boolean test1() {
    // Create a balanced Binary Search Tree of Integers
    BinarySearchTree<Integer> testBST = new BinarySearchTree<Integer>();
    testBST.insert(40);
    testBST.insert(25);
    testBST.insert(15);
    testBST.insert(27);
    testBST.insert(70);
    testBST.insert(55);
    testBST.insert(80);
    // Confirm that the Binary Search Tree is made correctly using in-order and in-line traversals
    String expected = "[ 15, 25, 27, 40, 55, 70, 80 ]";
    String actual = testBST.toInOrderString();
    if (!expected.equals(actual))
      return false;
    String expected2 = "[ 40, 25, 70, 15, 27, 55, 80 ]";
    String actual2 = testBST.toLevelOrderString();
    if (!expected2.equals(actual2))
      return false;

    // 1a. Check that a right rotation works when the parent is a root:
    {
      Node<Integer> parent = null;
      Node<Integer> child = null;
      parent = testBST.findNode(40);
      child = testBST.findNode(25);
      testBST.rotate(child, parent);
      String actual1 = testBST.toInOrderString();
      String expected1 = "[ 15, 25, 27, 40, 55, 70, 80 ]";
      if (!expected1.equals(actual1))
        return false;
      String actual0 = testBST.toLevelOrderString();
      String expected0 = "[ 25, 15, 40, 27, 70, 55, 80 ]";
      if (!expected0.equals(actual0))
        return false;
    }
    // 1b. Check that a left rotation works when the parent is a root:
    {
      Node<Integer> parent = null;
      Node<Integer> child = null;
      parent = testBST.findNode(25);
      child = testBST.findNode(40);
      testBST.rotate(child, parent);
      String actual1 = testBST.toInOrderString();
      String expected1 = "[ 15, 25, 27, 40, 55, 70, 80 ]";
      if (!expected1.equals(actual1))
        return false;
      String actual0 = testBST.toLevelOrderString();
      String expected0 = "[ 40, 25, 70, 15, 27, 55, 80 ]";
      if (!expected0.equals(actual0))
        return false;
    }
    // 2a. Check that a right rotation works on a parent that is a right child
    {
      Node<Integer> parent = null;
      Node<Integer> child = null;
      parent = testBST.findNode(70);
      child = testBST.findNode(55);
      testBST.rotate(child, parent);
      String actual1 = testBST.toInOrderString();
      String expected1 = "[ 15, 25, 27, 40, 55, 70, 80 ]";
      if (!expected1.equals(actual1))
        return false;
      String actual0 = testBST.toLevelOrderString();
      String expected0 = "[ 40, 25, 55, 15, 27, 70, 80 ]";
      if (!expected0.equals(actual0))
        return false;
    }
    // 2b. Check that a left rotation works on a parent that is a right child
    {
      Node<Integer> parent = null;
      Node<Integer> child = null;
      parent = testBST.findNode(70);
      child = testBST.findNode(80);
      testBST.rotate(child, parent);
      String actual1 = testBST.toInOrderString();
      String expected1 = "[ 15, 25, 27, 40, 55, 70, 80 ]";
      if (!expected1.equals(actual1))
        return false;
      String actual0 = testBST.toLevelOrderString();
      String expected0 = "[ 40, 25, 55, 15, 27, 80, 70 ]";
      if (!expected0.equals(actual0))
        return false;
    }
    // 3a. Check that a left rotation works on a parent that is a left child
    {
      Node<Integer> parent = null;
      Node<Integer> child = null;
      parent = testBST.findNode(25);
      child = testBST.findNode(27);
      testBST.rotate(child, parent);
      String actual1 = testBST.toInOrderString();
      String expected1 = "[ 15, 25, 27, 40, 55, 70, 80 ]";
      if (!expected1.equals(actual1))
        return false;
      String actual0 = testBST.toLevelOrderString();
      String expected0 = "[ 40, 27, 55, 25, 80, 15, 70 ]";
      if (!expected0.equals(actual0))
        return false;
    }
    // 3b. Check that a right rotation works on a prent that is a left child
    {
      Node<Integer> parent = null;
      Node<Integer> child = null;
      parent = testBST.findNode(27);
      child = testBST.findNode(25);
      testBST.rotate(child, parent);
      String actual1 = testBST.toInOrderString();
      String expected1 = "[ 15, 25, 27, 40, 55, 70, 80 ]";
      if (!expected1.equals(actual1))
        return false;
      String actual0 = testBST.toLevelOrderString();
      String expected0 = "[ 40, 25, 55, 15, 27, 80, 70 ]";
      if (!expected0.equals(actual0))
        return false;
    }
    // 4. Confirm that an exception is thrown when the parent child relationship is incorrect
    {
      try {
        Node<Integer> parent = null;
        Node<Integer> child = null;
        parent = testBST.findNode(15);
        child = testBST.findNode(25);
        testBST.rotate(child, parent);
        return false;
      } catch (IllegalArgumentException e) {
      } catch (Exception e) {
        return false;
      }
    }
    // If all the test cases pass, return true
    return true;
  }

  /**
   * This test method uses an "unbalanced" Binary Search Tree of Strings to test both left and right
   * rotations on all possible types of parents (if a parent is a root, if it is a right child, if
   * it is a left child). It also confirms that the exceptions that are meant to be thrown work
   * correctly.
   * 
   * @return true if and only if all test cases pass, false otherwise
   */
  public static boolean test2() {
    // Create an "unbalanced" Binary Search Tree
    BinarySearchTree<String> test2BST = new BinarySearchTree<String>();
    test2BST.insert("e");
    test2BST.insert("a");
    test2BST.insert("g");
    test2BST.insert("f");
    test2BST.insert("i");
    test2BST.insert("k");

    // Confirm that the Binary Search Tree is made correctly using in-order and in-line traversal
    String expected = "[ a, e, f, g, i, k ]";
    String actual = test2BST.toInOrderString();
    if (!expected.equals(actual))
      return false;
    String expected2 = "[ e, a, g, f, i, k ]";
    String actual2 = test2BST.toLevelOrderString();
    if (!expected2.equals(actual2))
      return false;

    // 1a. Check that a right rotation works when the parent is a root:
    {
      Node<String> parent = null;
      Node<String> child = null;
      parent = test2BST.findNode("e");
      child = test2BST.findNode("a");
      test2BST.rotate(child, parent);
      String actual1 = test2BST.toInOrderString();
      String expected1 = "[ a, e, f, g, i, k ]";
      if (!expected1.equals(actual1))
        return false;
      String actual0 = test2BST.toLevelOrderString();
      String expected0 = "[ a, e, g, f, i, k ]";
      if (!expected0.equals(actual0))
        return false;
    }
    // 1b. Check that a left rotation works when the parent is a root:
    {
      Node<String> parent = null;
      Node<String> child = null;
      parent = test2BST.findNode("a");
      child = test2BST.findNode("e");
      test2BST.rotate(child, parent);
      String actual1 = test2BST.toInOrderString();
      String expected1 = "[ a, e, f, g, i, k ]";
      if (!expected1.equals(actual1))
        return false;
      String actual0 = test2BST.toLevelOrderString();
      String expected0 = "[ e, a, g, f, i, k ]";
      if (!expected0.equals(actual0))
        return false;
    }
    // 2a. Check that a right rotation works on a parent that is a right child
    {
      Node<String> parent = null;
      Node<String> child = null;
      parent = test2BST.findNode("g");
      child = test2BST.findNode("f");
      test2BST.rotate(child, parent);
      String actual1 = test2BST.toInOrderString();
      String expected1 = "[ a, e, f, g, i, k ]";
      if (!expected1.equals(actual1))
        return false;
      String actual0 = test2BST.toLevelOrderString();
      String expected0 = "[ e, a, f, g, i, k ]";
      if (!expected0.equals(actual0))
        return false;
    }
    // 2b. Check that a left rotation works on a parent that is a right child
    {
      Node<String> parent = null;
      Node<String> child = null;
      parent = test2BST.findNode("f");
      child = test2BST.findNode("g");
      test2BST.rotate(child, parent);
      String actual1 = test2BST.toInOrderString();
      String expected1 = "[ a, e, f, g, i, k ]";
      if (!expected1.equals(actual1))
        return false;
      String actual0 = test2BST.toLevelOrderString();
      String expected0 = "[ e, a, g, f, i, k ]";
      if (!expected0.equals(actual0))
        return false;
    }
    // 3a. Check that a left rotation works on a parent that is a left child
    {
      test2BST.insert("b");
      Node<String> parent = null;
      Node<String> child = null;
      parent = test2BST.findNode("a");
      child = test2BST.findNode("b");
      test2BST.rotate(child, parent);
      String actual1 = test2BST.toInOrderString();
      String expected1 = "[ a, b, e, f, g, i, k ]";
      if (!expected1.equals(actual1))
        return false;
      String actual0 = test2BST.toLevelOrderString();
      String expected0 = "[ e, b, g, a, f, i, k ]";
      if (!expected0.equals(actual0))
        return false;
    }
    // 3b. Check that a right rotation works on a parent that is a left child
    {
      Node<String> parent = null;
      Node<String> child = null;
      parent = test2BST.findNode("b");
      child = test2BST.findNode("a");
      test2BST.rotate(child, parent);
      String actual1 = test2BST.toInOrderString();
      String expected1 = "[ a, b, e, f, g, i, k ]";
      if (!expected1.equals(actual1))
        return false;
      String actual0 = test2BST.toLevelOrderString();
      String expected0 = "[ e, a, g, b, f, i, k ]";
      if (!expected0.equals(actual0))
        return false;
    }
    // 4. Check that an exception is thrown correctly when the relationship is wrong
    {
      try {
        Node<String> parent = null;
        Node<String> child = null;
        parent = test2BST.findNode("b");
        child = test2BST.findNode("i");
        test2BST.rotate(child, parent);
        System.out.print("Hey");
        return false;
      } catch (IllegalArgumentException e) {
      } catch (Exception e) {
        System.out.print("hey");
        return false;
      }
    }
    // If and only if all test cases pass, return true
    return true;
  }

  /**
   * This test method uses a "balanced" Binary Search Tree to test right and left rotations for all
   * possible children types (a child with no children, a child with a right child, a child with a
   * left child, a child with two children). It also confirms that the exceptions that are meant to
   * be thrown work correctly.
   * 
   * @return true if and only if all test cases pass, false otherwise
   */
  public static boolean test3() {
    // Create a balanced Binary Search Tree
    BinarySearchTree<Integer> test3BST = new BinarySearchTree<Integer>();
    test3BST.insert(20);
    test3BST.insert(10);
    test3BST.insert(30);

    // Confirm the Binary Search Tree is created as expected using in-order and in-level traversals
    String expected = "[ 10, 20, 30 ]";
    String actual = test3BST.toInOrderString();
    if (!expected.equals(actual))
      return false;
    String expected2 = "[ 20, 10, 30 ]";
    String actual2 = test3BST.toLevelOrderString();
    if (!expected2.equals(actual2))
      return false;

    // 1a. Test a left rotation on a child that has no children:
    {
      Node<Integer> parent = null;
      Node<Integer> child = null;
      parent = test3BST.findNode(20);
      child = test3BST.findNode(30);
      test3BST.rotate(child, parent);
      String actual1 = test3BST.toInOrderString();
      String expected1 = "[ 10, 20, 30 ]";
      if (!actual1.equals(expected1))
        return false;
      String actual0 = test3BST.toLevelOrderString();
      String expected0 = "[ 30, 20, 10 ]";
      if (!actual0.equals(expected0))
        return false;
    }
    // 1b. Test a right rotation on a child that has no children:
    {
      Node<Integer> parent = null;
      Node<Integer> child = null;
      parent = test3BST.findNode(30);
      child = test3BST.findNode(20);
      test3BST.rotate(child, parent);
      String actual1 = test3BST.toInOrderString();
      String expected1 = "[ 10, 20, 30 ]";
      if (!actual1.equals(expected1))
        return false;
      String actual0 = test3BST.toLevelOrderString();
      String expected0 = "[ 20, 10, 30 ]";
      if (!actual0.equals(expected0))
        return false;
    }
    // 2a. Test a left rotation on a child that has one right child
    {
      test3BST.insert(35);
      Node<Integer> parent = null;
      Node<Integer> child = null;
      parent = test3BST.findNode(20);
      child = test3BST.findNode(30);
      test3BST.rotate(child, parent);
      String actual1 = test3BST.toInOrderString();
      String expected1 = "[ 10, 20, 30, 35 ]";
      if (!actual1.equals(expected1))
        return false;
      String actual0 = test3BST.toLevelOrderString();
      String expected0 = "[ 30, 20, 35, 10 ]";
      if (!actual0.equals(expected0))
        return false;
    }
    // 2b. Test a right rotation on a child that has one right child
    {
      test3BST.insert(15);
      Node<Integer> parent = null;
      Node<Integer> child = null;
      parent = test3BST.findNode(20);
      child = test3BST.findNode(10);
      test3BST.rotate(child, parent);
      String actual1 = test3BST.toInOrderString();
      String expected1 = "[ 10, 15, 20, 30, 35 ]";
      if (!actual1.equals(expected1))
        return false;
      String actual0 = test3BST.toLevelOrderString();
      String expected0 = "[ 30, 10, 35, 20, 15 ]";
      if (!actual0.equals(expected0))
        return false;
    }
    // 3a. Test a left rotation on a child that has one left child
    {
      Node<Integer> parent = null;
      Node<Integer> child = null;
      parent = test3BST.findNode(10);
      child = test3BST.findNode(20);
      test3BST.rotate(child, parent);
      String actual1 = test3BST.toInOrderString();
      String expected1 = "[ 10, 15, 20, 30, 35 ]";
      if (!actual1.equals(expected1))
        return false;
      String actual0 = test3BST.toLevelOrderString();
      String expected0 = "[ 30, 20, 35, 10, 15 ]";
      if (!actual0.equals(expected0))
        return false;
    }
    // 3b. Test a right rotation on a child that has one left child
    {
      Node<Integer> parent = null;
      Node<Integer> child = null;
      parent = test3BST.findNode(30);
      child = test3BST.findNode(20);
      test3BST.rotate(child, parent);
      String actual1 = test3BST.toInOrderString();
      String expected1 = "[ 10, 15, 20, 30, 35 ]";
      if (!actual1.equals(expected1))
        return false;
      String actual0 = test3BST.toLevelOrderString();
      String expected0 = "[ 20, 10, 30, 15, 35 ]";
      if (!actual0.equals(expected0))
        return false;
    }
    // 4a. Test a left rotation on a child with two children
    {
      test3BST.insert(25);
      Node<Integer> parent = null;
      Node<Integer> child = null;
      parent = test3BST.findNode(20);
      child = test3BST.findNode(30);
      test3BST.rotate(child, parent);
      String actual1 = test3BST.toInOrderString();
      String expected1 = "[ 10, 15, 20, 25, 30, 35 ]";
      if (!actual1.equals(expected1))
        return false;
      String actual0 = test3BST.toLevelOrderString();
      String expected0 = "[ 30, 20, 35, 10, 25, 15 ]";
      if (!actual0.equals(expected0))
        return false;
    }
    // 4b. Test a right rotation on a child with two children
    {
      test3BST.insert(25);
      Node<Integer> parent = null;
      Node<Integer> child = null;
      parent = test3BST.findNode(30);
      child = test3BST.findNode(20);
      test3BST.rotate(child, parent);
      String actual1 = test3BST.toInOrderString();
      String expected1 = "[ 10, 15, 20, 25, 30, 35 ]";
      if (!actual1.equals(expected1))
        return false;
      String actual0 = test3BST.toLevelOrderString();
      String expected0 = "[ 20, 10, 30, 15, 25, 35 ]";
      if (!actual0.equals(expected0))
        return false;
    }
    // 5. Confirm an exception is thrown when the parent child relationship is wrong
    {
      try {
        Node<Integer> parent = null;
        Node<Integer> child = null;
        parent = test3BST.findNode(30);
        child = test3BST.findNode(20);
        test3BST.rotate(child, parent);
        return false;
      } catch (IllegalArgumentException e) {
      } catch (Exception e) {
        return false;
      }
    }
    // Return true if all test cases pass
    return true;
  }

  /**
   * Main method to run tests. If you'd like to add additional test methods, add a line for each of
   * them.
   * 
   * @param args
   */
  public static void main(String[] args) {
    System.out.println("Test 1 passed: " + test1());
    System.out.println("Test 2 passed: " + test2());
    System.out.println("Test 3 passed: " + test3());
  }

}
