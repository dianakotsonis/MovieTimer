// --== CS400 Fall 2023 File Header Information ==--
// Name: Diana Kotsonis
// Email: dakotsonis
// Group: A41
// TA: Lakshika Rathi
// Lecturer: Gary Dahl
// Notes to Grader: n/a

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class extends the Binary Search Tree class to create Red Black Tree Objects. It is capable
 * of inserting into Red Black Trees, and fixing insertions so they satisfy the conditions of a Red
 * Black Tree.
 * 
 * @author dianakotsonis
 *
 * @param <T> The Data type of the Red Black tree
 */
public class RedBlackTree<T extends Comparable<T>> extends BinarySearchTree<T> {

  /**
   * This class accesses and stores data for each node in the Red Black Tree.
   * 
   * @author CS400
   *
   * @param <T> The data type of the Red Black Tree Node
   */
  protected static class RBTNode<T> extends Node<T> {
    public int blackHeight = 0; // the color of the Red Black Tree node, either Red (0) or Black(1)

    /**
     * This constructor creates a RBT Node using the data given
     * 
     * @param data the data that is stored in the RBT Node
     */
    public RBTNode(T data) {
      super(data);
    }

    /**
     * This method accesses the RBT Node's parent
     * 
     * @return the parent node
     */
    public RBTNode<T> getUp() {
      return (RBTNode<T>) this.up;
    }

    /**
     * This method accesses the RBT Node's left child
     * 
     * @return the left child node
     */
    public RBTNode<T> getDownLeft() {
      return (RBTNode<T>) this.down[0];
    }

    /**
     * This method accesses the RBT Node's right child
     * 
     * @return the right child node
     */
    public RBTNode<T> getDownRight() {
      return (RBTNode<T>) this.down[1];
    }
  }

  /**
   * This method enforces RBT properties after insertions. The properties a RBT must satisfy are:
   * -All heights (black nodes) must be equal in any subtree 
   * -There can never be a red parent with a
   * red child.
   * 
   * If the red node that was inserted has a red parent, then this method modifies the RBT to
   * satisfy these conditions.
   * 
   * @param redNode The node that was just inserted
   */
  protected void enforceRBTreePropertiesAfterInsert(RBTNode<T> redNode) {

    // If the red node's parent is black, or the Red Node or parent node are null, then there is
    // no need to enforce tree properties. Return
    if (redNode == null || redNode.getUp() == null || redNode.getUp().blackHeight == 1) {
      return;
    }

    // Store the red node's surrounding connections in RBT Nodes
    RBTNode<T> parentNode = redNode.getUp(); // the parent of the red Node
    RBTNode<T> parentsParentNode = parentNode.getUp(); // The parent of the parent node
    RBTNode<T> siblingNode = null; // The sibling of the parent node

    // Check if the parent is a left child of its parent. If so, store the sibling node as the
    // right child of the parent's parent
    if (parentsParentNode.getDownLeft() != null
        && parentsParentNode.getDownLeft().equals(parentNode)) {
      siblingNode = parentsParentNode.getDownRight();
    }
    // Otherwise, store the sibling node as the left child of the parent's parent
    else {
      siblingNode = parentsParentNode.getDownLeft();
    }

    // Case 1: If the sibling is red, implement case 1 (change the parent's parent node to red and
    // the parent and sibling nodes to black)
    if (siblingNode != null && siblingNode.blackHeight == 0) {
      parentNode.blackHeight = 1;
      parentsParentNode.blackHeight = 0;
      siblingNode.blackHeight = 1;
      // Check if the parentsParent node has a parent that is red. If so, recursively call this
      // method to handle the new red node violation
      if (parentsParentNode.getUp() != null && parentsParentNode.getUp().blackHeight == 0) {
        enforceRBTreePropertiesAfterInsert(parentsParentNode);
      }
    }
    // Otherwise, the sibling is black and the case is either case 2 or 3
    else {
      if (parentsParentNode.getDownLeft() != null
          && parentsParentNode.getDownLeft().equals(parentNode)) {
        // If the parent and child are both left children of their parents, implement case 2: 
        // Rotate the parent and its parent, then swap their colors
        if (parentNode.getDownLeft() != null && parentNode.getDownLeft().equals(redNode)) {
          rotate(parentNode, parentsParentNode);
          parentNode.blackHeight = 1;
          parentsParentNode.blackHeight = 0;
        } else {
          // Otherwise, rotate the parent and child nodes, then recursively call this method to 
          // handle the violation as a case 2
          rotate(redNode, parentNode);
          enforceRBTreePropertiesAfterInsert(parentNode);
        }
      } else {
        if (parentNode.getDownRight() != null && parentNode.getDownRight().equals(redNode)) {
          // If the parent and child are both right children of their parents, implement case 2: 
          // Rotate the parent and its parent then swap their colors
          rotate(parentNode, parentsParentNode);
          parentNode.blackHeight = 1;
          parentsParentNode.blackHeight = 0;
        } else {
          // Otherwise, rotate the parent and child nodes, then recursively call this method to 
          // handle the violation as a case 2
          rotate(redNode, parentNode);
          enforceRBTreePropertiesAfterInsert(parentNode);
        }
      }
    }
  }

  /**
   * This method inserts into the Red Black Tree
   * @overrides the insert method in the BinarySearchTree class
   * @param data the data to be inserted into the Red Black Tree
   */
  @Override
  public boolean insert(T data) throws NullPointerException {
    // Create a RBT Node for the data
    RBTNode<T> newNode = new RBTNode<>(data);
    // Insert the new node using the BinarySearchTree insert method
    if (insertHelper(newNode)) {
      // If the insert occurs, call the enforceRBTreeProperties method to fix any violations within
      // the tree.
      enforceRBTreePropertiesAfterInsert(newNode);
      // Change the root to be black
      RBTNode<T> rootNode = (RBTNode<T>) this.root;
      rootNode.blackHeight = 1;
      return true;
    }
    return false;
  }

  /**
   * This tests the first case of inserting into a Red Black Tree, where the parent violating node
   * and its sibling are both red.
   * <p>
   * A correct implementation should make the parent and its sibling black, and their parent red.
   * After this, check if there are any other red node violations higher up, and continue to resolve
   * them if so.
   */
  @Test
  public void testInsertingCase1() {
    // 1. Insert into this RBT so a case 1 appears (and so there is only one red node violation to
    // solve), and the root must be changed to black
    {
      // Create a valid red black tree
      RedBlackTree<String> testTree = new RedBlackTree<String>();
      testTree.insert("C");
      testTree.insert("B");
      testTree.insert("D");
      // Confirm that the tree is correct before inserting for case 1 (by confirming the nodes were
      // inserted in the correct order and are the correct color
      String expected = "[ C, B, D ]";
      String actual = testTree.toLevelOrderString();
      String expected0 = "[ B, C, D ]";
      String actual0 = testTree.toInOrderString();
      assertEquals(expected, actual, "Tree was not created correctly");
      assertEquals(expected0, actual0, "Tree was not created correctly");

      RBTNode<String> rootNode = (RBTNode<String>) testTree.findNode("C");
      assertEquals(rootNode.blackHeight, 1,
          "Root node (before inserting case 1 violation) is not black");
      RBTNode<String> bNode = (RBTNode<String>) testTree.findNode("B");
      assertEquals(bNode.blackHeight, 0, "B's node (before inserting case 1 violation) is not red");
      RBTNode<String> dNode = (RBTNode<String>) testTree.findNode("D");
      assertEquals(dNode.blackHeight, 0, "D's node (before inserting case 1 violation) is not red");

      // Insert into the RBT so a case 1 appears
      testTree.insert("A");
      // Confirm that the insertion was done correctly (all heights are updated properly and all
      // nodes are in the correct order)
      String expected1 = "[ C, B, D, A ]";
      String actual1 = testTree.toLevelOrderString();
      String expected2 = "[ A, B, C, D ]";
      String actual2 = testTree.toInOrderString();
      assertEquals(expected1, actual1, "Case 1 insertion level order of nodes is incorrect");
      assertEquals(expected2, actual2, "Case 1 insertion in-order of nodes is incorrect");

      RBTNode<String> aNode = (RBTNode<String>) testTree.findNode("A");
      assertEquals(aNode.blackHeight, 0, "A's node (after inserting case 1 violation) is not red");
      assertEquals(rootNode.blackHeight, 1,
          "C's node (after inserting case 1 violation) is not black");
      assertEquals(bNode.blackHeight, 1,
          "B's node (after inserting case 1 violation) is not black");
      assertEquals(dNode.blackHeight, 1,
          "D's node (after inserting case 1 violation) is not black");
    }
    // 2. Insert into a RBT that has multiple red node violations to solve (the first one being a
    // case one)
    {
      // Create a valid red black tree
      RedBlackTree<String> testTree = new RedBlackTree<String>();
      testTree.insert("E");
      testTree.insert("C");
      testTree.insert("G");
      testTree.insert("F");
      testTree.insert("I");
      testTree.insert("H");
      testTree.insert("J");

      // Confirm that it was created correctly
      String expected = "[ E, C, G, F, I, H, J ]";
      String actual = testTree.toLevelOrderString();
      String expected0 = "[ C, E, F, G, H, I, J ]";
      String actual0 = testTree.toInOrderString();
      assertEquals(expected, actual, "Tree was not created correctly");
      assertEquals(expected0, actual0, "Tree was not created correctly");

      RBTNode<String> eNode = (RBTNode<String>) testTree.findNode("E");
      assertEquals(eNode.blackHeight, 1,
          "Root node (before inserting case 1 violation) is not black");
      RBTNode<String> cNode = (RBTNode<String>) testTree.findNode("C");
      assertEquals(cNode.blackHeight, 1,
          "C's node (before inserting case 1 violation) is not black");
      RBTNode<String> gNode = (RBTNode<String>) testTree.findNode("G");
      assertEquals(gNode.blackHeight, 0, "G's node (before inserting case 1 violation) is not red");

      RBTNode<String> fNode = (RBTNode<String>) testTree.findNode("F");
      assertEquals(fNode.blackHeight, 1,
          "F's node (before inserting case 1 violation) is not black");
      RBTNode<String> iNode = (RBTNode<String>) testTree.findNode("I");
      assertEquals(iNode.blackHeight, 1,
          "I's node (before inserting case 1 violation) is not black");
      RBTNode<String> hNode = (RBTNode<String>) testTree.findNode("H");
      assertEquals(hNode.blackHeight, 0, "H's node (before inserting case 1 violation) is not red");

      RBTNode<String> jNode = (RBTNode<String>) testTree.findNode("J");
      assertEquals(jNode.blackHeight, 0, "J's node (before inserting case 1 violation) is not red");

      // Insert into the RBT so a case 1 appears
      testTree.insert("K");

      // Confirm that the insertion was done correctly (all heights are updated properly and all
      // nodes are in the correct order)
      String expected1 = "[ G, E, I, C, F, H, J, K ]";
      String actual1 = testTree.toLevelOrderString();
      String expected2 = "[ C, E, F, G, H, I, J, K ]";
      String actual2 = testTree.toInOrderString();
      assertEquals(expected1, actual1, "Case 1 insertion level order of nodes is incorrect");
      assertEquals(expected2, actual2, "Case 1 insertion in-order of nodes is incorrect");

      RBTNode<String> kNode = (RBTNode<String>) testTree.findNode("K");
      assertEquals(gNode.blackHeight, 1,
          "root node (after inserting case 1 violation) is not black");
      assertEquals(eNode.blackHeight, 0, "E's node (after inserting case 1 violation) is not red");

      assertEquals(iNode.blackHeight, 0, "I's node (after inserting case 1 violation) is not red");

      assertEquals(cNode.blackHeight, 1,
          "C's node (after inserting case 1 violation) is not black");
      assertEquals(fNode.blackHeight, 1,
          "F's node (after inserting case 1 violation) is not black");
      assertEquals(hNode.blackHeight, 1,
          "H's node (after inserting case 1 violation) is not black");
      assertEquals(jNode.blackHeight, 1,
          "J's node (after inserting case 1 violation) is not black");
      assertEquals(kNode.blackHeight, 0, "K's node (after inserting case 1 violation) is not red");
    }
  }

  /**
   * This tests the second case of inserting into a Red Black Tree, where the parent violating node
   * is red and its sibling is black, and the two red violating nodes are either both left children
   * or both right children of their parents.
   * <p>
   * A correct implementation should rotate the red violating parent with its parent, then swap
   * their colors.
   */
  @Test
  public void testInsertingCase2() {
    // 1. Insert into this RBT so a case 2 appears
    {
      // Create a RBT
      RedBlackTree<Integer> testTree = new RedBlackTree<Integer>();
      testTree.insert(6);
      testTree.insert(3);
      testTree.insert(9);
      testTree.insert(2);
      testTree.insert(10);
      // Confirm the tree was created correctly
      String expected = "[ 6, 3, 9, 2, 10 ]";
      String actual = testTree.toLevelOrderString();
      String expected0 = "[ 2, 3, 6, 9, 10 ]";
      String actual0 = testTree.toInOrderString();
      assertEquals(expected, actual, "Tree was not created correctly");
      assertEquals(expected0, actual0, "Tree was not created correctly");

      RBTNode<Integer> node6 = (RBTNode<Integer>) testTree.findNode(6);
      assertEquals(node6.blackHeight, 1,
          "Root node (before inserting case 2 violation) is not black");
      RBTNode<Integer> node3 = (RBTNode<Integer>) testTree.findNode(3);
      assertEquals(node3.blackHeight, 1, "3 node (before inserting case 2 violation) is not black");
      RBTNode<Integer> node9 = (RBTNode<Integer>) testTree.findNode(9);
      assertEquals(node9.blackHeight, 1, "9 node (before inserting case 2 violation) is not black");
      RBTNode<Integer> node2 = (RBTNode<Integer>) testTree.findNode(2);
      assertEquals(node2.blackHeight, 0, "2 node (before inserting case 2 violation) is not red");
      RBTNode<Integer> node10 = (RBTNode<Integer>) testTree.findNode(10);
      assertEquals(node10.blackHeight, 0, "10 node (before inserting case 2 violation) is not red");

      // Insert into the RBT so a case 2 appears
      testTree.insert(11);
      // Confirm that it was inserted correctly (all nodes are in the correct order and have the
      // correct color)
      String expected1 = "[ 6, 3, 10, 2, 9, 11 ]";
      String actual1 = testTree.toLevelOrderString();
      String expected2 = "[ 2, 3, 6, 9, 10, 11 ]";
      String actual2 = testTree.toInOrderString();
      assertEquals(expected1, actual1,
          "Insertion for case 2 is not the correct level order of nodes");
      assertEquals(expected2, actual2, "Insertion for case 2 is not the correct in-order of nodes");

      RBTNode<Integer> node11 = (RBTNode<Integer>) testTree.findNode(11);
      assertEquals(node6.blackHeight, 1,
          "Root node (after inserting case 2 violation) is not black");
      assertEquals(node3.blackHeight, 1, "3 node (after inserting case 2 violation) is not black");
      assertEquals(node10.blackHeight, 1,
          "10 node (after inserting case 2 violation) is not black");
      assertEquals(node2.blackHeight, 0, "2 node (after inserting case 2 violation) is not red");
      assertEquals(node9.blackHeight, 0, "9 node (after inserting case 2 violation) is not red");
      assertEquals(node11.blackHeight, 0, "11 node (after inserting case 2 violation) is not red");
    }
  }

  /**
   * This tests the third case of inserting into a Red Black Tree, where the parent violating node
   * is red and its sibling is black, and the two red violating nodes are different children of
   * their parents (For example, if the parent violating node is a right child of its parent, and
   * the child violating node is a left child of its parent, this is a case 3).
   * <p>
   * A correct implementation should rotate the violating child and parent nodes, which makes the
   * violation a case 2. The case 2 algorithm is then implemented.
   */
  @Test
  public void testInsertingCase3() {
    // 1. Insert into a RBT so a case 3 appears
    {
      // Create a RBT
      RedBlackTree<String> testTree = new RedBlackTree<String>();
      testTree.insert("E");
      testTree.insert("C");
      testTree.insert("G");
      testTree.insert("B");
      testTree.insert("I");
      // Confirm the RBT was created correctly:
      String expected = "[ E, C, G, B, I ]";
      String actual = testTree.toLevelOrderString();
      String expected0 = "[ B, C, E, G, I ]";
      String actual0 = testTree.toInOrderString();
      assertEquals(expected, actual, "Tree was not created correctly");
      assertEquals(expected0, actual0, "Tree was not created correctly");

      RBTNode<String> eNode = (RBTNode<String>) testTree.findNode("E");
      assertEquals(eNode.blackHeight, 1,
          "Root(E) node (before inserting case 3 violation) is not black");
      RBTNode<String> cNode = (RBTNode<String>) testTree.findNode("C");
      assertEquals(cNode.blackHeight, 1,
          "C's node (before inserting case 3 violation) is not black");
      RBTNode<String> gNode = (RBTNode<String>) testTree.findNode("G");
      assertEquals(gNode.blackHeight, 1,
          "G's (before inserting case 3 violation) node is not black");
      RBTNode<String> bNode = (RBTNode<String>) testTree.findNode("B");
      assertEquals(bNode.blackHeight, 0, "B's node (before inserting case 3 violation) is not red");
      RBTNode<String> iNode = (RBTNode<String>) testTree.findNode("I");
      assertEquals(iNode.blackHeight, 0, "I's node (before inserting case 3 violation) is not red");

      // Insert into the RBT so a case 3 appears
      testTree.insert("H");
      // Confirm that it was inserted correctly (all nodes are in the correct order and have the
      // correct color)
      String expected1 = "[ E, C, H, B, G, I ]";
      String actual1 = testTree.toLevelOrderString();
      String expected2 = "[ B, C, E, G, H, I ]";
      String actual2 = testTree.toInOrderString();
      assertEquals(expected1, actual1,
          "Insertion for case 3 is not the correct level order of nodes");
      assertEquals(expected2, actual2, "Insertion for case 3 is not the correct in-level of nodes");

      RBTNode<String> hNode = (RBTNode<String>) testTree.findNode("H");
      assertEquals(eNode.blackHeight, 1,
          "Root(E) (after inserting case 3 violation) node is not black");
      assertEquals(cNode.blackHeight, 1,
          "C's node (after inserting case 3 violation) is not black");
      assertEquals(hNode.blackHeight, 1,
          "H's node (after inserting case 3 violation) is not black");
      assertEquals(bNode.blackHeight, 0, "B's node (after inserting case 3 violation) is not red");
      assertEquals(gNode.blackHeight, 0, "G's node (after inserting case 3 violation) is not red");
      assertEquals(iNode.blackHeight, 0, "I's node (after inserting case 3 violation) is not red");
    }
  }

  public static void main(String[] args) {
    RedBlackTree<String> testTree = new RedBlackTree<String>();
    testTree.testInsertingCase1();
    testTree.testInsertingCase2();
    testTree.testInsertingCase3();
  }
}
