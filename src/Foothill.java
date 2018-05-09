// CIS 1C Assignment #4
// Instructor Solution Client

import cs_1c.*;

import java.util.NoSuchElementException;

class PrintObject<E> implements Traverser<E>
{
   public void visit(E x)
   {
      System.out.print( x + " ");
   }
};

//------------------------------------------------------
public class Foothill
{
   // -------  main --------------
   public static void main(String[] args) throws Exception
   {
      int k;
      FHlazySearchTree<Integer> searchTree = new FHlazySearchTree<Integer>();
      PrintObject<Integer> intPrinter = new PrintObject<Integer>();

      System.out.println("\nInitial size: " );
      printTreeSizes(searchTree);
      searchTree = populateTree(searchTree);

      System.out.println("After populating -- traversal and sizes: ");
      printTreeSizes(searchTree);

      System.out.println("Collecting garbage on new tree - should be " +
              "no garbage.");
      searchTree.collectGarbage();
      printTreeSizes(searchTree);

      // test assignment operator
      FHlazySearchTree<Integer> searchTree2
              = (FHlazySearchTree<Integer>) searchTree.clone();
      System.out.println("\nCloning tree: tree 2:");
      printTreeSizes(searchTree2);

      System.out.println("\n\nAttempting 1 removal on 1st tree: ");
      if (searchTree.remove(20))
         System.out.println("removed " + 20);
      printTreeSizes(searchTree);

      System.out.println("Collecting Garbage - should clean 1 item. ");
      searchTree.collectGarbage();
      printTreeSizes(searchTree);

      System.out.println("Collecting Garbage again - no change expected. ");
      searchTree.collectGarbage();
      printTreeSizes(searchTree);

      // test soft insertion and deletion:

      System.out.println("Adding 'hard' 22 - should see new sizes. ");
      searchTree.insert(22);
      printTreeSizes(searchTree);

      System.out.println("\nAfter soft removal 22. ");
      searchTree.remove(22);
      printTreeSizes(searchTree);

      System.out.println("Repeating soft removal 22. Should see no change. ");
      searchTree.remove(22);
      printTreeSizes(searchTree);

      System.out.println("Soft insertion 22. Hard size should not change. ");
      searchTree.insert(22);
      printTreeSizes(searchTree);

      System.out.println("\nSoft removal 22. ");
      searchTree.remove(22);

      System.out.println("\n----------------------");
      System.out.println("\nTest Find() on filled tree");
      testFind(searchTree);
      System.out.println("\n----------------------");

      System.out.println("\nTest findMax() on filled tree");
      testFindMax(searchTree);
      System.out.println("\nSoft delete 70");
      searchTree.remove(70);
      testFindMax(searchTree);
      System.out.println("\n----------------------");

      System.out.println("\nTest findMin() on filled tree");
      testFindMin(searchTree);
      System.out.println("\nSoft delete 10");
      searchTree.remove(10);
      testFindMin(searchTree);
      System.out.println("\n----------------------");


      System.out.println("\nInserting extra 100 nodes " +
              "and attempting 100 removals: ");
      insertExtraHundredNodes(searchTree);
      System.out.print("removed:");
      for (k = 170; k > 0; k--)
      {
         if (searchTree.remove(k))
            System.out.print(" " + k);
      }

      System.out.println("\nsearch_tree now:");
      printTreeSizes(searchTree);

      searchTree.collectGarbage();

      System.out.println("\nsearch_tree now after garbage collector:");
      printTreeSizes(searchTree);

      System.out.println("\n----------------------");
      System.out.println("\nTest Find() on empty tree (should not be there)");
      testFind(searchTree);
      System.out.println("\n----------------------");

      System.out.println("\nTest findMax() on empty tree");
      testFindMax(searchTree);
      System.out.println("\n----------------------");

      System.out.println("\nTest findMin() on empty tree");
      testFindMin(searchTree);
      System.out.println("\n----------------------");

      insertExtraNodes(searchTree2);

      System.out.println("\nsearchTree2:");
      printTreeSizes(searchTree2);

      System.out.println("\n----------------------");
      System.out.println("\nTest findMin() on filled tree2");
      testFindMin(searchTree2);
      System.out.println("\n----------------------");

   }

   private static void testFindMin(FHlazySearchTree<Integer> searchTree)
   {
      printTreeSizes(searchTree);
      try
      {
         System.out.println("search tree for min: found "
                 + searchTree.findMin());
      } catch (NoSuchElementException e)
      {
         System.out.println("min not found");
      }
   }

   private static void testFindMax(FHlazySearchTree<Integer> searchTree)
   {
      printTreeSizes(searchTree);
      try
      {
         System.out.println("search tree for max: found "
                 + searchTree.findMax());
      } catch (NoSuchElementException e)
      {
         System.out.println("max not found");
      }
   }

   // helper methods
   public static FHlazySearchTree<Integer>
   populateTree(FHlazySearchTree<Integer> searchTree)
   {
      searchTree.insert(50);
      searchTree.insert(20);
      searchTree.insert(30);
      searchTree.insert(70);
      searchTree.insert(10);
      searchTree.insert(60);
      return searchTree;
   }

   public static void printTreeSizes(FHlazySearchTree<Integer> searchTree)
   {
      PrintObject<Integer> intPrinter = new PrintObject<Integer>();
      System.out.print("Nodes: ");
      searchTree.traverse(intPrinter);
      System.out.print("\nSoft deleted nodes: ");
      searchTree.traverseSoftDeleted(intPrinter);
      System.out.println("\nTree size: " + searchTree.size()
              + "  Hard size: " + searchTree.sizeHard());
   }

   public static void insertExtraNodes(FHlazySearchTree<Integer> searchTree)
   {
      searchTree.insert(500);
      searchTree.insert(200);
      searchTree.insert(300);
      searchTree.insert(700);
      searchTree.insert(100);
      searchTree.insert(600);
   }

   public static void
   insertExtraHundredNodes(FHlazySearchTree<Integer> searchTree)
   {
      for(int i = 71 ; i < 171; i++)
      searchTree.insert(i);
   }
   public static void testFind(FHlazySearchTree<Integer> searchTree)
   {
      System.out.println("\nSearch for existed node 30");
      try
      {
         System.out.println("search tree1 for 30: found "
                 + searchTree.find(30));
      } catch (NoSuchElementException e)
      {
         System.out.println("node 30 not found");
      }

      System.out.println("\nSearch for soft deleted node 22");
      try
      {
         System.out.println("search tree1 for 22: found "
                 + searchTree.find(22));
      } catch (NoSuchElementException e)
      {
         System.out.println("node 22 not found");
      }
      System.out.println("\nSearch for deleted node 20");
      try
      {
         System.out.println("search tree1 for 20: found "
                 + searchTree.find(20));
      } catch (NoSuchElementException e)
      {
         System.out.println("node 20 not found");
      }
   }
}


/* ---------------------- Run --------------------------


---------------------------------------------------------------------- */