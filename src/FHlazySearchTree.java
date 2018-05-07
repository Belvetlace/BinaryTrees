import cs_1c.*;
import java.util.*;


//todo: Add a public/private pair, collectGarbage() (
// the private method is the recursive counterpart of the public one,
// and takes/returns a root node).
// This allows the client to truly remove all deleted (stale) nodes.
// Don't do this by creating a new tree and inserting data into it,
// but by traversing the tree and doing a hard remove on each deleted node.
// This will require that you have a private removeHard() utility
// that works very much like our old remove() method.

public class FHlazySearchTree<E extends Comparable< ? super E > >
   implements Cloneable
{
   protected int mSize; //number of undeleted nodes
   protected int mSizeHard; //number of both deleted and undeleted
   protected FHlazySTNode<E> mRoot;
   
   public FHlazySearchTree() { clear(); }
   public boolean empty() { return (mSize == 0); }
   public int size() { return mSize; }
   public void clear() { mSize = 0; mSizeHard = 0; mRoot = null; }
   public int showHeight() { return findHeight(mRoot, -1); }

   //todo: add check for deleted nodes
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

   //todo: add check for deleted nodes
   public E find( E x )
   {
      FHlazySTNode<E> resultNode;
      resultNode = find(mRoot, x);
      if (resultNode == null)
         throw new NoSuchElementException();
      return resultNode.data;
   }
   public boolean contains(E x)  { return find(mRoot, x) != null; }
   
   public boolean insert( E x )
   {
      int oldSize = mSize;
      mRoot = insert(mRoot, x);
      return (mSize != oldSize);
   }
   // todo: Revise to implement lazy deletion.
   public boolean remove( E x )
   {
      int oldSize = mSize;
      remove(mRoot, x);
      return (mSize != oldSize);
   }
   
   public < F extends Traverser<? super E > > 
   void traverse(F func)
   {
      traverse(func, mRoot);
   }

   //todo: add check for deleted nodes
   public Object clone() throws CloneNotSupportedException
   {
      FHlazySearchTree<E> newObject = (FHlazySearchTree<E>)super.clone();
      newObject.clear();  // can't point to other's data

      newObject.mRoot = cloneSubtree(mRoot);
      newObject.mSize = mSize;
      
      return newObject;
   }

   //accessor
   public int sizeHard()
   {
      return mSizeHard;
   }
   // private helper methods ----------------------------------------
   protected FHlazySTNode<E> findMin( FHlazySTNode<E> root ) 
   {
      if (root == null)
         return null;
      if (root.lftChild == null)
         return root;
      return findMin(root.lftChild);
   }
   
   protected FHlazySTNode<E> findMax( FHlazySTNode<E> root ) 
   {
      if (root == null)
         return null;
      if (root.rtChild == null)
         return root;
      return findMax(root.rtChild);
   }
   
   protected FHlazySTNode<E> insert( FHlazySTNode<E> root, E x )
   {
      int compareResult;  // avoid multiple calls to compareTo()
      
      if (root == null)
      {
         mSize++;
         mSizeHard++;
         return new FHlazySTNode<E>(x, null, null);
      }
      
      compareResult = x.compareTo(root.data); 
      if ( compareResult < 0 )
         root.lftChild = insert(root.lftChild, x);
      else if ( compareResult > 0 )
         root.rtChild = insert(root.rtChild, x);

      return root;
   }

// todo: Revise to implement lazy deletion.
// The private version can now be void return type.
   protected void remove( FHlazySTNode<E> root, E x  )
   {
      int compareResult;  // avoid multiple calls to compareTo()
     
      if (root == null)
         return;

      compareResult = x.compareTo(root.data); 
      if ( compareResult < 0 )
         remove(root.lftChild, x);
      else if ( compareResult > 0 )
         remove(root.rtChild, x);

      // found the node
      else if (root.lftChild != null && root.rtChild != null)
      {
         root.data = findMin(root.rtChild).data;
         remove(root.rtChild, root.data);
      }
      else
      {
         root =
            (root.lftChild != null)? root.lftChild : root.rtChild;
         // mark node deleted
         mSize--;
      }
   }

   protected FHlazySTNode<E> removeHard( FHlazySTNode<E> root, E x  )
   {
      int compareResult;  // avoid multiple calls to compareTo()

      if (root == null)
         return null;

      compareResult = x.compareTo(root.data);
      if (compareResult < 0)
      {
         root.lftChild = removeHard(root.lftChild, x);
      } else if (compareResult > 0)
      {
         root.rtChild = removeHard(root.rtChild, x);
      }
         // found the node
      else if (root.lftChild != null && root.rtChild != null)
      {
         root.data = findMin(root.rtChild).data;
         root.rtChild = removeHard(root.rtChild, root.data);
      }
      else
      {
         root =
                 (root.lftChild != null)? root.lftChild : root.rtChild;
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
      func.visit(treeNode.data);
      traverse(func, treeNode.rtChild);
   }
   
   protected FHlazySTNode<E> find( FHlazySTNode<E> root, E x )
   {
      int compareResult;  // avoid multiple calls to compareTo()

      if (root == null)
         return null;

      compareResult = x.compareTo(root.data); 
      if (compareResult < 0)
         return find(root.lftChild, x);
      if (compareResult > 0)
         return find(root.rtChild, x);
      return root;   // found
   }
   
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
      return newNode;
   }
   
   protected int findHeight( FHlazySTNode<E> treeNode, int height ) 
   {
      int leftHeight, rightHeight;
      if (treeNode == null)
         return height;
      height++;
      leftHeight = findHeight(treeNode.lftChild, height);
      rightHeight = findHeight(treeNode.rtChild, height);
      return (leftHeight > rightHeight)? leftHeight : rightHeight;
   }

   public boolean collectGarbage()
   {
      int oldSize = mSizeHard;
      collectGarbage(mRoot);
      return (oldSize != mSizeHard);
   }

   protected void collectGarbage(FHlazySTNode<E> root)
   {
      // traverse the tree search for boolean deleted
      // hard remove these nodes
   }

   class FHlazySTNode<E extends Comparable< ? super E > >
   {
      FHlazySTNode<E> lftChild, rtChild;
      public E data;
      public FHlazySTNode<E> myRoot;  // needed to test for certain error
      boolean deleted;


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

      public FHlazySTNode()
      {
         this(null, null, null);
      }
      
      // function stubs -- for use only with AVL Trees when we extend
      public int getHeight() { return 0; }
      boolean setHeight(int height) { return true; }
   }

}
