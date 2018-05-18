import cs_1c.*;

import java.util.*;

public class FHlazySearchTree<E extends Comparable<? super E>>
        implements Cloneable
{
   protected int mSize; //number of undeleted nodes
   protected int mSizeHard; //number of both deleted and undeleted
   protected FHlazySTNode<E> mRoot;

   public FHlazySearchTree()
   {
      clear();
   }
   public FHlazySearchTree(FHlazySearchTree obj){
      mSize = obj.mSize;
      mSizeHard = obj.mSizeHard;
      mRoot = obj.mRoot;
   }

   public boolean empty()
   {
      return (mSize == 0);
   }

   public int size()
   {
      return mSize;
   }

   public void clear()
   {
      mSize = 0;
      mSizeHard = 0;
      mRoot = null;
   }

   public int showHeight()
   {
      return findHeight(mRoot, -1);
   }

   public E findMin()
   {
      if (mRoot == null)
         throw new NoSuchElementException();
      return findMin(mRoot).data;
   }

   public E findMax()
   {
      if (mRoot == null)
         throw new NoSuchElementException();
      return findMax(mRoot).data;
   }

   public E find(E x)
   {
      FHlazySTNode<E> resultNode;
      resultNode = find(mRoot, x);
      if (resultNode == null)
         throw new NoSuchElementException();
      return resultNode.data;
   }

   public boolean contains(E x)
   {
      return find(mRoot, x) != null;
   }

   public boolean insert(E x)
   {
      int oldSize = mSize;
      mRoot = insert(mRoot, x);
      return (mSize != oldSize);
   }

   public boolean remove(E x)
   {
      int oldSize = mSize;
      remove(mRoot, x);
      return (mSize != oldSize);
   }

   public <F extends Traverser<? super E>>
   void traverse(F func)
   {
      traverse(func, mRoot);
   }

   public <F extends Traverser<? super E>>
   void traverseSoftDeleted(F func)
   {
      traverseSoftDeleted(func, mRoot);
   }

   public Object clone() throws CloneNotSupportedException
   {
      FHlazySearchTree<E> newObject = (FHlazySearchTree<E>) super.clone();
      newObject.clear();  // can't point to other's data

      newObject.mRoot = cloneSubtree(mRoot);
      newObject.mSize = mSize;
      newObject.mSizeHard = mSize;

      return newObject;
   }

   //accessor
   public int sizeHard()
   {
      return mSizeHard;
   }

   // private helper methods ----------------------------------------
   protected FHlazySTNode<E> findMin(FHlazySTNode<E> root)
   {
      if (root == null)
         return null;

      FHlazySTNode<E> current_root = findMin(root.lftChild);
      if (current_root != null)
         return current_root;
      if (!root.deleted )
         return root;
      return findMin(root.rtChild);
   }

   protected FHlazySTNode<E> findMax(FHlazySTNode<E> root)
   {
      if (root == null ) // || root.deleted
         return null;

      FHlazySTNode<E> current_root = findMax(root.rtChild);
      if (current_root != null)
         return current_root;
      if (!root.deleted )
         return root;
      return findMax(root.lftChild);
   }

   protected FHlazySTNode<E> insert(FHlazySTNode<E> root, E x)
   {
      int compareResult;  // avoid multiple calls to compareTo()

      if (root == null)
      {
         mSize++;
         mSizeHard++;
         return new FHlazySTNode<E>(x, null, null);
      }
      compareResult = x.compareTo(root.data);
      if (compareResult < 0)
         root.lftChild = insert(root.lftChild, x);
      else if (compareResult > 0)
         root.rtChild = insert(root.rtChild, x);
      else // root is soft deleted
      {
         root.restore();
         mSize++;
      }

      return root;
   }

   // done: Revise to implement lazy deletion.
   protected void remove(FHlazySTNode<E> root, E x)
   {
      int compareResult;  // avoid multiple calls to compareTo()

      if (root == null)
         return;

      compareResult = x.compareTo(root.data);
      if (compareResult < 0)
      {
         remove(root.lftChild, x);
      } else if (compareResult > 0)
      {
         remove(root.rtChild, x);
      } else if (!root.deleted) // found the node
      {
         // mark node deleted
         root.delete();
         mSize--;
      }
   }

   protected FHlazySTNode<E> findMinCollGarbage(FHlazySTNode<E> root)
   {
      if (root == null)
         return null;
      if (root.lftChild == null)
         return root;

      return findMinCollGarbage(root.lftChild);
   }

   protected FHlazySTNode<E> removeHard(FHlazySTNode<E> root)
   {
      if (root == null)
         return null;

      if (root.lftChild == null && root.rtChild == null)
      {
         root = null;
         mSizeHard--;
      }
      else
      if (root.lftChild != null && root.rtChild != null)
      {
         root.data = findMinCollGarbage(root.rtChild).data;
         root.deleted = false;
         root.rtChild = removeHard(root.rtChild);
      } else
      {
         root = (root.lftChild != null) ? root.lftChild : root.rtChild;
         mSizeHard--;
      }
      return root;
   }


   protected <F extends Traverser<? super E>>
   void traverse(F func, FHlazySTNode<E> treeNode)
   {
      if (treeNode == null)
         return;

      traverse(func, treeNode.lftChild);
      if (!treeNode.deleted)
      {
         func.visit(treeNode.data);
      }
      traverse(func, treeNode.rtChild);
   }

   protected <F extends Traverser<? super E>>
   void traverseSoftDeleted(F func, FHlazySTNode<E> treeNode)
   {
      if (treeNode == null)
         return;

      traverseSoftDeleted(func, treeNode.lftChild);
      if (treeNode.deleted)
      {
         func.visit(treeNode.data);
      }
      traverseSoftDeleted(func, treeNode.rtChild);
   }

   //done: add check for deleted nodes
   protected FHlazySTNode<E> find(FHlazySTNode<E> root, E x)
   {
      int compareResult;  // avoid multiple calls to compareTo()

      if (root == null || root.deleted)
         return null;

      compareResult = x.compareTo(root.data);
      if (compareResult < 0)
         return find(root.lftChild, x);
      if (compareResult > 0)
         return find(root.rtChild, x);
      return root;   // found
   }

   //todo: are we copying deleted nodes???
   protected FHlazySTNode<E> cloneSubtree(FHlazySTNode<E> root)
   {
      FHlazySTNode<E> newNode;

      if (root == null)
         return null;

      // does not set myRoot which must be done by caller
      newNode = new FHlazySTNode<E>
              (
                      root.data,
                      cloneSubtree(root.lftChild),
                      cloneSubtree(root.rtChild)
              );
      if (root.deleted)
         newNode.delete();

      return newNode;
   }

   protected int findHeight(FHlazySTNode<E> treeNode, int height)
   {
      int leftHeight, rightHeight;
      if (treeNode == null)
         return height;
      height++;
      leftHeight = findHeight(treeNode.lftChild, height);
      rightHeight = findHeight(treeNode.rtChild, height);
      return (leftHeight > rightHeight) ? leftHeight : rightHeight;
   }

   public boolean collectGarbage()
   {
      int oldSize = mSizeHard;
      mRoot = collectGarbage(mRoot);
      return (oldSize != mSizeHard);
   }


   protected FHlazySTNode<E> collectGarbage(FHlazySTNode<E> root)
   {

      if (root == null)
         return null;

      root.lftChild = collectGarbage(root.lftChild);
      root.rtChild = collectGarbage(root.rtChild);

      //deal with the node
      if (root.deleted)
      {
         root = removeHard(root);
      }

      return root;
   }

   class FHlazySTNode<E extends Comparable<? super E>>
   {
      FHlazySTNode<E> lftChild, rtChild;
      public E data;
      public FHlazySTNode<E> myRoot;  // needed to test for certain error
      public boolean deleted;


      FHlazySTNode(E d, FHlazySTNode<E> lft, FHlazySTNode<E> rt)
      {
         lftChild = lft;
         rtChild = rt;
         data = d;
         deleted = false;
      }

      public boolean isDeleted()
      {
         return deleted;
      }

      public boolean delete()
      {
         deleted = true;
         return true;
      }

      public boolean restore()
      {
         deleted = false;
         return true;
      }

      public FHlazySTNode()
      {
         this(null, null, null);
      }

      // function stubs -- for use only with AVL Trees when we extend
      public int getHeight()
      {
         return 0;
      }

      boolean setHeight(int height)
      {
         return true;
      }

   }

}
