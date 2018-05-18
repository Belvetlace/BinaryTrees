import cs_1c.FHs_treeNode;

public class FHsplayTree<E extends Comparable<? super E>> extends FHlazySearchTree {
    @Override
    public boolean insert(E x){
        int compareResult;
        FHlazySTNode mRoot = super.mRoot;

        if (mRoot == null)
        {
            mRoot = new FHlazySTNode(x,null,null);
            mSize++;
            return true;
        }

        FHlazySTNode newNode, parent;
        parent = mRoot;

        while(true)
        {
            compareResult = parent.data.compareTo(x);
            if ( compareResult > 0 )
            {
                if( parent.lftThread != null )
                    parent = parent.lftChild;
                else
                {
                    // place as new left child
                    newNode = new FHlazySTNode(x, parent.lftChild, parent);
                    parent.lftChild = newNode;
                    parent.lftThread = false;
                    break;
                }
            }
            else if ( compareResult < 0 )
            {
                if( !(parent.rtThread) )
                    parent = parent.rtChild;
                else
                {
                    // place as new right child
                    newNode = new FHlazySTNode
                            (x, parent, parent.rtChild);
                    parent.rtChild = newNode;
                    parent.rtThread = false;
                    break;
                }
            }
            else
                return false;  // duplicate
        }

        mSize++;
        return true;
    }
    @Override
    public boolean remove(E x){
        FHlazySTNode mRoot = super.mRoot;
        if(mRoot == null){
            return false;
        }
        else{
            splay(super.mRoot, x);
        }
    }
    public E showRoot(){
    }
    protected FHs_treeNode<E> splay(FHs_treeNode<E> root, E x){
        FHs_treeNode<E> rightTree, leftTree, rightTreeMin, leftTreeMax = null;
        while(root != null){
            int compareResult = root.data.compareTo(x);
            if(compareResult < 0){
                if(root.lftChild == null){
                    break;
                }
                if(compareResult > 0){

                }
            }
        }
    }
}
