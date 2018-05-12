import cs_1c.FHarrayList;
import cs_1c.FHlinkedList;

import java.util.Iterator;

public class SparseMat<E> implements Cloneable
{
    protected int numRows, numCols;
    protected E defaultVal;
    protected FHarrayList<FHlinkedList< MatNode >> rows;
    private static String lineSeparator = System.getProperty("line.separator");

    //row size and column size both >= 1
    public SparseMat(int numRows, int numCols, E defaultVal)
    {
        if(numRows < 1 || numCols < 1)
        {
            throw new IllegalArgumentException();
        }
        this.numRows = numRows;
        this.numCols = numCols;
        this.defaultVal = defaultVal;
        allocateEmptyMatrix();
    }

    //allocates the memory of an empty matrix
    private void allocateEmptyMatrix()
    {
        rows = new FHarrayList<FHlinkedList< MatNode >>(numRows);
        //filling array list with blank linked lists
        for(int i = 0; i< numRows; i ++)
        {
            rows.add(new FHlinkedList<MatNode>());
        }
    }

    // returns the object stored in row r and column c.
    // throws an IndexOutOfBoundsException if matrix bounds (row and/or column) are violated.
    public E get(int r, int c) throws IndexOutOfBoundsException
    {
        if (r < 0 || r >= numRows || c < 0 || c >= numCols)
        { throw new IndexOutOfBoundsException(); }

        FHlinkedList<MatNode> matNodes = rows.get(r);
        if (!matNodes.isEmpty())
        {
            Iterator<MatNode> iter = matNodes.iterator();
            MatNode<E> temp;
            while (iter.hasNext())
            {
                temp = iter.next();
                if (c == temp.getCol())
                {
                    return temp.getData();
                }
            }
        }
        return defaultVal;

    }
    // places x in row r and column c if x is not default
    // if x is the default value it will remove any existing node
    // returns false without an exception if bounds are violated
    public boolean set(int r, int c, E x)
    {
        // check the bounds
        if (r < 0 || r >= numRows || c < 0 || c >= numCols){ return false; }
        // check if row is empty
        FHlinkedList<MatNode> matNodes = rows.get(r);
        if (matNodes.isEmpty())
        {
            if ( !x.equals( defaultVal )) {
                matNodes.add(new MatNode(c, x));
            }
            return true;
        }

        Iterator<MatNode> iter = matNodes.iterator();
        boolean colunmExist = false;
        MatNode<E> temp = null;
        while (iter.hasNext())
        {
            temp = iter.next();
            if (c == temp.getCol())
            {
                colunmExist = true;
                break;
            }
        }
        if (colunmExist)
        {
            if ( x.equals( defaultVal) ) {
                iter.remove();
                return true;
            }
            //set new value
            try{
                matNodes.set(matNodes.indexOf(temp), new MatNode(c, x));
                return true;
            } catch (IndexOutOfBoundsException e){
                return false;
            }
        } else {
            if(!x.equals( defaultVal )){
                iter = matNodes.iterator();
                if (iter.hasNext())
                {
                    temp = iter.next();
                    if(temp.getCol() > c)
                    {
                        matNodes.add(matNodes.indexOf(temp), new MatNode(c, x));
                        return true;
                    }
                    else {
                        matNodes.add(new MatNode(c, x));
                        return true;
                    }
                }
            }
        }
        return true;
    }


    public void showSubSquare(int start, int size) {
        int msize = (start+size);
        StringBuilder rStr = new StringBuilder();
        if (start < 0 || start > numRows || start > numCols
                || msize > numCols || msize > numRows || size < 0)
        {
            System.out.print("");
            return;
        }
        if (size == 0)
        {
            System.out.println("");
            return;
        }
        for (int r = start; r < msize; r++)
        {
            if (rows.get(r).isEmpty())
            {
                for (int i = start; i < msize; i++)
                {
                    rStr.append(String.format("%5.1f", defaultVal));
                    rStr.append(" ");
                }
            } else {
                for (int c = start; c < msize; c++)
                {
                    try
                    {
                        rStr.append(String.format("%5.1f", this.get(r,c)));
                        rStr.append(" ");
                    }
                    catch (IndexOutOfBoundsException e)
                    {
                        rStr.append(String.format("%5.1f", defaultVal));
                        rStr.append(" ");
                    }
                }
            }
            rStr.append(lineSeparator);
        }

        System.out.println(rStr);
    }

    // clears all the rows, effectively setting all values to the defaultVal
    // (leaves the matrix size unchanged).
    public void clear()
    {
        for(int i = 0; i < rows.size(); i++)
        {
            if (!rows.get(i).isEmpty())
            {
                rows.get(i).clear();
            }
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        SparseMat<MatNode> newObject = (SparseMat<MatNode>)super.clone();
        newObject.rows = (FHarrayList<FHlinkedList< MatNode >>)rows.clone();
        for(int i = 0; i< numRows; i ++)
        {
            newObject.rows.set(i,(FHlinkedList<MatNode>)rows.get(i).clone());
        }
        return newObject;
    }

    public int getRowSize()
    {
        return numRows;
    }

    public int getColSize()
    {
        return numCols;
    }

    protected class MatNode<E> implements Cloneable
    {
        public int col;
        public E data;

        MatNode()
        {
            col = 0;
            data = null;
        }

        MatNode(int cl, E dt)
        {
            col = cl;
            data = dt;
        }

        E getData()
        {
            return data;
        }

        void setData(E data)
        {
            this.data = data;
        }

        int getCol()
        {
            return col;
        }

        public Object clone() throws CloneNotSupportedException
        {
            // shallow copy
            MatNode newObject = (MatNode)super.clone();
            return (Object) newObject;
        }
    }
}