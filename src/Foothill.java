// CIS 1C Assignment #5
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
        FHsplayTree<Integer> searchTree = new FHsplayTree<Integer>();
        PrintObject<Integer> intPrinter = new PrintObject<Integer>();

        searchTree.traverse(intPrinter);

        System.out.println( "Initial size: " + searchTree.size() );
        for (k = 1; k <= 32; k++)
            searchTree.insert(k);
        System.out.println( "New size: " + searchTree.size() );

        System.out.println( "\nTraversal");
        searchTree.traverse(intPrinter);
        System.out.println();

        for (k = -1; k < 10; k++)
        {
            // searchTree.contains(k);  // alternative to find() - different error return
            try
            {
                searchTree.find(k);
            }
            catch( Exception e )
            {
                System.out.println( " oops " );
            }
            System.out.println( "splay " + k + " --> root: "
                    + searchTree.showRoot()
                    + " height: " + searchTree.showHeight() );
        }
    }
}