// CIS 1C Assignment #4
// Instructor Solution Client

import cs_1c.*;

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

      searchTree.traverse(intPrinter);

      System.out.println("\ninitial size: " + searchTree.size());

      searchTree.insert(50);
      searchTree.insert(20);
      searchTree.insert(30);
      searchTree.insert(70);
      searchTree.insert(10);
      searchTree.insert(60);

      System.out.println("After populating -- traversal and sizes: ");
      searchTree.traverse(intPrinter);
      System.out.println("\ntree 1 size: " + searchTree.size()
              + "  Hard size: " + searchTree.sizeHard());

      System.out.println("Collecting garbage on new tree - should be " +
              "no garbage.");
      searchTree.collectGarbage();
      System.out.println("tree 1 size: " + searchTree.size()
              + "  Hard size: " + searchTree.sizeHard());

      // test assignment operator
      FHlazySearchTree<Integer> searchTree2
              = (FHlazySearchTree<Integer>) searchTree.clone();
      System.out.println("\nCloning tree: ");
      System.out.println("tree 2 size: " + searchTree2.size()
              + "  Hard size: " + searchTree2.sizeHard());
      searchTree2.traverse(intPrinter);


      System.out.println("\n\nAttempting 1 removal: ");
      if (searchTree.remove(20))
         System.out.println("removed " + 20);
      System.out.println("tree 1 size: " + searchTree.size()
              + "  Hard size: " + searchTree.sizeHard());

      System.out.println("Collecting Garbage - should clean 1 item. ");
      searchTree.collectGarbage();
      //searchTree.removeHard(searchTree.mRoot,20); // testing hard remove in absence of garbage collector
      System.out.println("tree 1 size: " + searchTree.size()
              + "  Hard size: " + searchTree.sizeHard());

      System.out.println("Collecting Garbage again - no change expected. ");
      searchTree.collectGarbage();
      System.out.println("tree 1 size: " + searchTree.size()
              + "  Hard size: " + searchTree.sizeHard());

      // test soft insertion and deletion:

      System.out.println("Adding 'hard' 22 - should see new sizes. ");
      searchTree.insert(22);
      searchTree.traverse(intPrinter);
      System.out.println("\ntree 1 size: " + searchTree.size()
              + "  Hard size: " + searchTree.sizeHard());

      System.out.println("\nAfter soft removal 22. ");
      searchTree.remove(22);
      searchTree.traverse(intPrinter);
      System.out.println("\ntree 1 size: " + searchTree.size()
              + "  Hard size: " + searchTree.sizeHard());

      System.out.println("Repeating soft removal 22. Should see no change. ");
      searchTree.remove(22);
      searchTree.traverse(intPrinter);
      System.out.println("\ntree 1 size: " + searchTree.size()
              + "  Hard size: " + searchTree.sizeHard());

      System.out.println("Soft insertion 22. Hard size should not change. ");
      searchTree.insert(22);
      searchTree.traverse(intPrinter);
      System.out.println("\ntree 1 size: " + searchTree.size()
              + "  Hard size: " + searchTree.sizeHard());

      System.out.println("\n\nAttempting 100 removals: ");
      for (k = 70; k > 0; k--)
      {
         if (searchTree.remove(k))
            System.out.println("removed " + k);
      }

      System.out.println("\nsearch_tree now:");
      searchTree.traverse(intPrinter);
      System.out.println("\ntree 1 size: " + searchTree.size()
              + "  Hard size: " + searchTree.sizeHard());

      searchTree.collectGarbage();

      System.out.println("\nsearch_tree now:");
      searchTree.traverse(intPrinter);
      System.out.println("\ntree 1 size: " + searchTree.size()
              + "  Hard size: " + searchTree.sizeHard());

      searchTree2.insert(500);
      searchTree2.insert(200);
      searchTree2.insert(300);
      searchTree2.insert(700);
      searchTree2.insert(100);
      searchTree2.insert(600);
      System.out.println("\nsearchTree2:");
      searchTree2.traverse(intPrinter);
      System.out.println("\ntree 2 size: " + searchTree2.size()
              + "  Hard size: " + searchTree2.sizeHard());

      //System.out.println("\nsearch_tree removing last node:");
      //searchTree.remove(70);
      //searchTree.collectGarbage();
   }
}


/* ---------------------- Run --------------------------

initial size: 0
After populating -- traversal and sizes: 
10 20 30 50 60 70 
tree 1 size: 6  Hard size: 6
Collecting garbage on new tree - should be no garbage.
tree 1 size: 6  Hard size: 6


Attempting 1 removal: 
removed 20
tree 1 size: 5  Hard size: 6
Collecting Garbage - should clean 1 item. 
tree 1 size: 5  Hard size: 5
Collecting Garbage again - no change expected. 
tree 1 size: 5  Hard size: 5
Adding 'hard' 22 - should see new sizes. 
10 22 30 50 60 70 
tree 1 size: 6  Hard size: 6

After soft removal. 
10 30 50 60 70 
tree 1 size: 5  Hard size: 6
Repeating soft removal. Should see no change. 
10 30 50 60 70 
tree 1 size: 5  Hard size: 6
Soft insertion. Hard size should not change. 
10 22 30 50 60 70 
tree 1 size: 6  Hard size: 6


Attempting 100 removals: 
removed 70
removed 60
removed 50
removed 30
removed 22
removed 10

search_tree now:

tree 1 size: 0  Hard size: 0

searchTree2:

10 20 30 50 60 70 100 200 300 500 600 700 
tree 2 size: 12  Hard size: 12
---------------------------------------------------------------------- */