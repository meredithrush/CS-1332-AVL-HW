import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * Your implementation of an AVL.
 *
 * @author Meredith Rush
 * @version 1.0
 * @userid mrush30
 * @GTID 903574798
 *
 * Collaborators: LIST ALL COLLABORATORS YOU WORKED WITH HERE
 *
 * Resources: LIST ALL NON-COURSE RESOURCES YOU CONSULTED HERE
 */
public class AVL<T extends Comparable<? super T>> {

    // Do not add new instance variables or modify existing ones.
    private AVLNode<T> root;
    private int size;

    /**
     * Constructs a new AVL.
     *
     * This constructor should initialize an empty AVL.
     *
     * Since instance variables are initialized to their default values, there
     * is no need to do anything for this constructor.
     */
    public AVL() {
        // DO NOT IMPLEMENT THIS CONSTRUCTOR!
    }

    /**
     * Constructs a new AVL.
     *
     * This constructor should initialize the AVL with the data in the
     * Collection. The data should be added in the same order it is in the
     * Collection.
     *
     * @param data the data to add to the tree
     * @throws java.lang.IllegalArgumentException if data or any element in data
     *                                            is null
     */
    public AVL(Collection<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot create an AVL out of null data.");
        }
        size = 0;
        for (T i : data) {
            add(i);
        }
    }

    /**
     * Adds the element to the tree.
     *
     * Start by adding it as a leaf like in a regular BST and then rotate the
     * tree as necessary.
     *
     * If the data is already in the tree, then nothing should be done (the
     * duplicate shouldn't get added, and size should not be incremented).
     *
     * Remember to recalculate heights and balance factors while going back
     * up the tree after adding the element, making sure to rebalance if
     * necessary.
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot add null data to the AVL.");
        }
        root = addHelper(root, data);
    }

    /**
     * Private helper method for add(T data)
     *
     * @param current Node currently being looked at
     * @param data the data to add
     * @return Next node being looked at
     */
    private AVLNode<T> addHelper(AVLNode<T> current, T data) {
        if (current == null) {
            size++;
            return new AVLNode<T>(data);
        } else if (current.getData().compareTo(data) > 0) {
            current.setLeft(addHelper(current.getLeft(), data));
        } else if (current.getData().compareTo(data) < 0) {
            current.setRight(addHelper(current.getRight(), data));
        } else {
            return current;
        }
        updateHeight(current);
        updateBalanceFactor(current);
        return balance(current);
    }

    /**
     * Private helper method to update the height of nodes in the AVL
     *
     * @param current Node currently being looked at
     */
    private void updateHeight(AVLNode<T> current) {
        if (current.getRight() == null && current.getLeft() == null) {
            current.setHeight(0);
        } else if (current.getRight() != null && current.getLeft() != null) {
            current.setHeight(Math.max(current.getLeft().getHeight(), current.getRight().getHeight()) + 1);
        } else if (current.getLeft() != null) {
            current.setHeight(current.getLeft().getHeight() + 1);
        } else {
            current.setHeight(current.getRight().getHeight() + 1);
        }
    }

    /**
     * Private helper method to update the balance factor of nodes in the AVL
     *
     * @param current Node currently being looked at
     */
    private void updateBalanceFactor(AVLNode<T> current) {
        if (current.getRight() == null && current.getLeft() == null) {
            current.setBalanceFactor(0);
        } else if (current.getRight() != null && current.getLeft() != null) {
            current.setBalanceFactor(current.getLeft().getHeight() - current.getRight().getHeight());
        } else if (current.getLeft() != null) {
            current.setBalanceFactor(current.getLeft().getHeight() + 1);
        } else {
            current.setBalanceFactor(- 1 - current.getRight().getHeight());
        }
    }

    /**
     * Private helper method that balances the AVL
     *
     * @param current Node currently being looked at
     */
    private AVLNode<T> balance(AVLNode<T> current) {
        if (current.getBalanceFactor() < -1) {
            if (current.getRight().getBalanceFactor() > 0) {
                return rightLeftRotation(current);
            } else {
                return leftRotation(current);
            }
        } else if (current.getBalanceFactor() > 1) {
            if (current.getLeft().getBalanceFactor() < 0) {
                return leftRightRotation(current);
            } else {
                return rightRotation(current);
            }
        }
        return current;
    }

    /**
     * Private helper method to balance(AVLNode<\T> current) that performs a right rotation
     *
     * @param current Node currently being looked at
     * @return New node with the right rotation
     */
    private AVLNode<T> rightRotation(AVLNode<T> current) {
        AVLNode<T> temporary = current.getLeft();
        current.setLeft(current.getLeft().getRight());
        temporary.setRight(current);
        updateHeight(current);
        updateBalanceFactor(current);
        updateHeight(temporary);
        updateBalanceFactor(temporary);
        return temporary;
    }

    /**
     * Private helper method to balance(AVLNode<\T> current) that performs a left rotation
     *
     * @param current Node currently being looked at
     * @return New node with the left rotation
     */
    private AVLNode<T> leftRotation(AVLNode<T> current) {
        AVLNode<T> temporary = current.getRight();
        current.setRight(current.getRight().getLeft());
        temporary.setLeft(current);
        updateHeight(current);
        updateBalanceFactor(current);
        updateHeight(temporary);
        updateBalanceFactor(temporary);
        return temporary;
    }

    /**
     * Private helper method to balance(AVLNode<\T> current) that performs a right-left rotation
     *
     * @param current Node currently being looked at
     * @return New node with the right-left rotation
     */
    private AVLNode<T> rightLeftRotation(AVLNode<T> current) {
        current.setRight(rightRotation(current.getRight()));
        return leftRotation(current);
    }

    /**
     * Private helper method to balance(AVLNode<\T> current) that performs a left-right rotation
     *
     * @param current Node currently being looked at
     * @return New node with the left-right rotation
     */
    private AVLNode<T> leftRightRotation(AVLNode<T> current) {
        current.setLeft(leftRotation(current.getLeft()));
        return rightRotation(current);
    }

    /**
     * Removes and returns the element from the tree matching the given
     * parameter.
     *
     * There are 3 cases to consider:
     * 1: The node containing the data is a leaf (no children). In this case,
     * simply remove it.
     * 2: The node containing the data has one child. In this case, simply
     * replace it with its child.
     * 3: The node containing the data has 2 children. Use the successor to
     * replace the data, NOT predecessor. As a reminder, rotations can occur
     * after removing the successor node.
     *
     * Remember to recalculate heights and balance factors while going back
     * up the tree after removing the element, making sure to rebalance if
     * necessary.
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to remove
     * @return the data that was removed
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not found
     */
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("There is not null data in the AVL to remove.");
        }
        AVLNode<T> removed = new AVLNode<T>(null);
        root = removeHelper(root, data, removed);
        return removed.getData();
    }

    /**
     * Private helper method for remove(T data)
     *
     * @param current Node currently being looked at
     * @param data The data being removed in the AVL
     * @param removed Node that will store the removed data
     * @return The node with the data that was removed
     */
    private AVLNode<T> removeHelper(AVLNode<T> current, T data, AVLNode<T> removed) {
        if (current == null) {
            throw new NoSuchElementException("The data is not in the tree.");
        } else if (current.getData().compareTo(data) > 0) {
            current.setLeft(removeHelper(current.getLeft(), data, removed));
        } else if (current.getData().compareTo(data) < 0) {
            current.setRight(removeHelper(current.getRight(), data, removed));
        } else {
            removed.setData(current.getData());
            size--;
            if (current.getLeft() == null) {
                return current.getRight();
            } else if (current.getRight() == null) {
                return current.getLeft();
            } else {
                AVLNode<T> child = new AVLNode<T>(null);
                current.setRight(successorHelper(current.getRight(), child));
                current.setData(child.getData());
            }
        }
        updateHeight(current);
        updateBalanceFactor(current);
        return balance(current);
    }

    /**
     * Private helper for remove that finds the successor of the given node
     *
     * @param current Node currently being looked at
     * @param child The child of the node being removed
     * @return The node being removed
     */
    private AVLNode<T> successorHelper(AVLNode<T> current, AVLNode<T> child) {
        if (current.getLeft() == null) {
            child.setData(current.getData());
            return current.getRight();
        }
        current.setLeft(successorHelper(current.getLeft(), child));
        updateHeight(current);
        updateBalanceFactor(current);
        return balance(current);
    }

    /**
     * Returns the element from the tree matching the given parameter.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * @param data the data to search for in the tree
     * @return the data in the tree equal to the parameter
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot get null data from the AVL.");
        }
        return getHelper(root, data);
    }

    /**
     * Private helper method for get
     *
     * @param current Node currently being looked at
     * @param data The data being searched for
     * @return Data from the AVL
     */
    private T getHelper(AVLNode<T> current, T data) {
        if (current == null) {
            throw new NoSuchElementException("The data cannot be found in the AVL.");
        }
        if (current.getData().compareTo(data) == 0) {
            return current.getData();
        } else if (current.getData().compareTo(data) > 0) {
            return getHelper(current.getLeft(), data);
        } else if (current.getData().compareTo(data) < 0) {
            return getHelper(current.getRight(), data);
        }
        return null;
    }

    /**
     * Returns whether or not data matching the given parameter is contained
     * within the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to search for in the tree.
     * @return true if the parameter is contained within the tree, false
     * otherwise
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public boolean contains(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot search for null data in the AVL.");
        }
        try {
            get(data);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Returns the height of the root of the tree.
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        if (root == null) {
            return -1;
        }
        return root.getHeight();
    }

    /**
     * Clears the tree.
     *
     * Clears all data and resets the size.
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Returns the data in the deepest node. If there is more than one node
     * with the same deepest depth, return the rightmost (i.e. largest) node with 
     * the deepest depth. 
     *
     * Must run in O(log n) for all cases.
     *
     * Example
     * Tree:
     *           2
     *        /    \
     *       0      3
     *        \
     *         1
     * Max Deepest Node:
     * 1 because it is the deepest node
     *
     * Example
     * Tree:
     *           2
     *        /    \
     *       0      4
     *        \    /
     *         1  3
     * Max Deepest Node:
     * 3 because it is the maximum deepest node (1 has the same depth but 3 > 1)
     *
     * @return the data in the maximum deepest node or null if the tree is empty
     */
    public T maxDeepestNode() {
        if (root == null) {
            return null;
        }
        return maxDeepestNodeHelper(root).getData();
    }

    /**
     * Private helper method for maxDeepestNode()
     *
     * @param current Node currently being looked at
     * @return The deepest node in the tree
     */
    private AVLNode<T> maxDeepestNodeHelper(AVLNode<T> current) {
        if (current.getBalanceFactor() < 0) {
            return maxDeepestNodeHelper(current.getRight());
        } else if (current.getBalanceFactor() > 0) {
            return maxDeepestNodeHelper(current.getLeft());
        } else if (current.getBalanceFactor() == 0) {
            if (current.getLeft() == null && current.getRight() == null) {
                return current;
            } else {
                AVLNode<T> leftSubtree = maxDeepestNodeHelper(current.getLeft());
                AVLNode<T> rightSubtree = maxDeepestNodeHelper(current.getRight());
                if (leftSubtree.getData().compareTo(rightSubtree.getData()) > 0) {
                    return leftSubtree;
                } else {
                    return rightSubtree;
                }
            }
        }
        return current;
    }

    /**
     * In BSTs, you learned about the concept of the successor: the
     * smallest data that is larger than the current data. However, you only
     * saw it in the context of the 2-child remove case.
     *
     * This method should retrieve (but not remove) the successor of the data
     * passed in. There are 2 cases to consider:
     * 1: The right subtree is non-empty. In this case, the successor is the
     * leftmost node of the right subtree.
     * 2: The right subtree is empty. In this case, the successor is the lowest
     * ancestor of the node containing data whose left child is also
     * an ancestor of data.
     * 
     * The second case means the successor node will be one of the node(s) we 
     * traversed left from to find data. Since the successor is the SMALLEST element 
     * greater than data, the successor node is the lowest/last node 
     * we traversed left from on the path to the data node.
     *
     * This should NOT be used in the remove method.
     *
     * Ex:
     * Given the following AVL composed of Integers
     *                    76
     *                  /    \
     *                34      90
     *                  \    /
     *                  40  81
     * successor(76) should return 81
     * successor(81) should return 90
     * successor(40) should return 76
     *
     * @param data the data to find the successor of
     * @return the successor of data. If there is no larger data than the
     * one given, return null.
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T successor(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot search for null data in the AVL.");
        }
        AVLNode<T> successor = successorHelp(root, null, data);
        if (successor == null) {
            return null;
        } else {
            return successor.getData();
        }
    }

    /**
     * Private helper method for successor(T data)
     *
     * @param current Node currently being looked at
     * @param lastLeft Node with last left turn in the subtree
     * @param data The data being searched for
     * @return The successor node
     */
    private AVLNode<T> successorHelp(AVLNode<T> current, AVLNode<T> lastLeft, T data) {
        if (current == null) {
            throw new NoSuchElementException("The data is not in the tree.");
        } else if (current.getData().compareTo(data) == 0) {
            if (current.getRight() == null) {
                return lastLeft;
            } else {
                return getLeftMost(current.getRight());
            }
        } else if (current.getData().compareTo(data) > 0) {
            return successorHelp(current.getLeft(), current, data);
        } else if (current.getData().compareTo(data) < 0) {
            return successorHelp(current.getRight(), lastLeft, data);
        }
        return current;
    }

    /**
     * Private helper method that finds the left-most node from the node's subtree
     *
     * @param current Node currently being looked at
     * @return left-most node in the subtree
     */
    private AVLNode<T> getLeftMost(AVLNode<T> current) {
        if (current.getLeft() != null) {
            return getLeftMost(current.getLeft());
        }
        return current;
    }

    /**
     * Returns the root of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the root of the tree
     */
    public AVLNode<T> getRoot() {
        // DO NOT MODIFY THIS METHOD!
        return root;
    }

    /**
     * Returns the size of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the tree
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }
}