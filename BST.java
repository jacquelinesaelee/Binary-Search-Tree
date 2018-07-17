/*
Jacqueline Saelee
COMP 282
Project #3
Binary Search Tree
BST.java
*/

package bst;

import java.util.Random;

class BSTNode<E>
{
   E item;   
   BSTNode<E> left;
   BSTNode<E> right;
   BSTNode<E> parent;
   
   // ADDED
   int height;
  
   public BSTNode ( E  x)
   {
      item = x; left = null; right = null; parent = null; height = 0;
      
   }
   
   public BSTNode (E x , BSTNode<E> left, BSTNode<E> right, BSTNode<E> parent)
   {
      item = x; 
      this.left = left; this.right = right; this.parent = parent;   
   }
   
   public String toString()
   {
       String s = "(i:" + this.item + ", h:" + this.height + ")\n"; 
       return s;
   }
}

/*----------------class BST ---------------------------*/
public class BST<E extends Comparable<E>>
{
   private BSTNode<E> root;
   private int size;
  
   
   
   public BST()
   {  root = null;  size = 0;  
   }
   
   /*---------------- public operations --------------------*/
   
      
        
   
   public int getSize()
   {  
      return size;
   }
   
        
   public boolean find( E x)
   {
      if( find(x,root) == null)
         return false;
      else
         return true;
   }
    
   
   public void preOrderTraversal()
   {
      preOrder (root);
      System.out.println();
   }
   
   public void inOrderTraversal()
   {
      inOrder (root);
      System.out.println();
   }
   
      
   public boolean insert( E x )
   {
   
      if( root == null)
      {
         root = new BSTNode(x, null, null, root);
         size++;
         return true;
      }    
       
      BSTNode<E> parent = null;
      BSTNode<E>  p = root;
      
      while (p != null)
      {
         if(x.compareTo(p.item) < 0)
         {
            parent = p; p = p.left; 
         }
         else if ( x.compareTo(p.item) > 0)
         {
            parent = p; p = p.right;
         }
         else  // duplicate value
            return false;  
      }
      
      //attach new node to parent
      BSTNode<E> insertedNode = new BSTNode<E>(x, null, null, parent);
      if( x.compareTo(parent.item) < 0){
         parent.left = insertedNode;
         insertedNode.parent = parent;
         walk(parent.left);
      }
      else{
         parent.right = insertedNode;
         insertedNode.parent = parent;
         walk(parent.right);
      }
      size++; 
      return true;   
        
   }  //insert
   
   
   public boolean remove(E x)
   {
      if(root == null)
         return false;  //x is not in tree
     
      //find x
      BSTNode<E> p = find(x, root);
      if( p == null)
         return false;  //x not in tree
                  
      //Case: p has a right child child and no left child
      if( p.left == null && p.right != null)
         deleteNodeWithOnlyRightChild(p);
            
       //Case: p has a left child and has no right child
      else if( p.left !=null && p.right == null)
         deleteNodeWithOnlyLeftChild(p);
         
            //case: p has no children
      else if (p.left ==null && p.right == null)
         deleteLeaf(p);
                
      else //case : p has two children. Delete successor node
      {
         BSTNode<E> succ =  getSuccessorNode(p);;
        
         p.item = succ.item;
           
          //delete succ node
         if(succ.right == null)
            deleteLeaf(succ);
         else
            deleteNodeWithOnlyRightChild(succ);
         
      }
      return true;         
   }   //remove
   
   
 
  /********************private methods ******************************/
  
        

   private BSTNode<E> find(E x, BSTNode<E> t)  
   {
      BSTNode<E> p = t;
      while ( p != null)
      {
         if( x.compareTo(p.item) <0)
            p = p.left;
         else if (x.compareTo(p.item) > 0)
            p = p.right;
         else  //found x
            return p;
      }
      return null;  //x is not found
   }
   
             
     /***************** private remove helper methods ***************************************/
   
   private void deleteLeaf( BSTNode<E> t)
   {
      if ( t == root)
         root = null;
      else
      {
         BSTNode<E>  parent = t.parent;
         if( t.item.compareTo(parent.item) < 0){
            parent.left = null;
            
            // RIGHT child is NOT null
            if(parent.right != null){
                parent.height = parent.right.height + 1;
            }
            // NO children
            else{
                parent.height = parent.height - 1;
            }
         }
         else{
            parent.right = null;
            
            // RIGHT child is NOT null
            if(parent.left != null){
                parent.height = parent.left.height + 1;
            }
            // NO children
            else{
                parent.height = parent.height - 1;
            }
         }
         
         // ADDED "walk"
         walk(parent);
      }
      size--;
   }
    
   private void deleteNodeWithOnlyLeftChild( BSTNode<E> t)
   {
      if( t == root)
      {
         root = t.left; root.parent = null; //WAS WRONG t.left.parent = root;
      }
      else
      {
         BSTNode<E> parent = t.parent;
         if( t.item.compareTo( parent.item)< 0)
         {
            parent.left = t.left;
            t.left.parent = parent;
             // ADDED "walk"
             walk(parent.left);
             //System.out.print("LEFT");
         }
         else
         {
            parent.right = t.left;
            t.left.parent = parent; 
            walk(parent.right);
            //System.out.print("p.RIGHT");
         }
    
      }
      size--;      
   }                  
     
   private void deleteNodeWithOnlyRightChild( BSTNode<E> t)
   {
      if( t == root)
      {
         root = t.right; root.parent = null; // WAS WRONG t.right.parent = root
         
      }
      else
      {
         BSTNode<E> parent = t.parent;
         if( t.item.compareTo(parent.item) < 0)
         {
            parent.left = t.right;
            t.right.parent = parent;
            // System.out.print("p.RIGHT");
             // ADDED "walk"
            walk(parent.left);  
         }
         else
         {
            parent.right = t.right;
            t.right.parent = parent;
            // System.out.print("p.RIGHT");
            // ADDED "walk"
            walk(parent.right);
         }
         
        
      }
      size--;         
   }                  

   private BSTNode<E>  getSuccessorNode(BSTNode<E> t)
   {
     //only called when t.right != null
      BSTNode<E> parent = t;
      BSTNode<E> p = t.right;
      while (p.left != null)
      {
         parent = p; p = p.left; 
      }
      return p;
   }
     
   
               
   //private traversal methods      
           
         
   private void preOrder ( BSTNode<E> t)
   {
      if ( t != null)
      {
         System.out.print(t + " ");
         preOrder(t.left);
         preOrder(t.right);
      }
   }
     
   private void inOrder ( BSTNode<E> t)
   {
      if ( t != null)
      {
             
         inOrder(t.left);
         System.out.print(t + " " );
         inOrder(t.right);
      }
   }
   // ADDED FUNCTIONS
   public BSTNode<E> findMin(){
       if(root == null)
          System.exit(0);
       
       BSTNode<E> curr = root;
       while(curr.left != null){
          curr = curr.left; 
       }       
        return curr;
   }
   
      public BSTNode<E> findMax(){
       if(root == null)
          System.exit(0);
       
       BSTNode<E> curr = root;
       while(curr.right != null){
          curr = curr.right; 
       }       
        return curr;
   }
   
   public BSTNode<E> removeMin(){
       BSTNode<E> curr = this.findMin();
       this.remove(curr.item);
       return curr;
   }
   
    public BSTNode<E> removeMax(){
       BSTNode<E> curr = this.findMax();
       this.remove(curr.item);
       return curr;
   }
   
   
   public int getHeight(){
       return root.height;
   }

   // ADDED: Ancestor Walk
   public void walk( BSTNode<E> t ){
       BSTNode<E> curr = t.parent;
       while(curr != null){
            BSTNode<E> rChild = curr.right;
            BSTNode<E> lChild = curr.left;
           
           // IF parent has two children
           if(rChild!= null && lChild != null){
               if(rChild.height > lChild.height){
                   curr.height = rChild.height + 1;
               }
               else
                   curr.height = lChild.height + 1;
           }
           // IF Parent only has RIGHT Child
           else if(rChild != null){
               curr.height = rChild.height + 1;
           }
           // IF Parent only has LEFT Child
           else if(lChild != null){
               curr.height = lChild.height + 1;
           }
           // Go to parent's parent
           curr = curr.parent;
       }   
   } //Walk  
   
  // Driver View FUNCS
    
    public void insertShowAll(BST<Integer> b,int n){
        Random rand = new Random();
        System.out.println("\tBST of random numbers: n = " + n + "\n");
        System.out.println("\tn\tlog2(n)\t   height\n\t- - - - - - - - - - - - - ");
        
        for(int i = 1; i <= 100; i ++){
            b.insert(rand.nextInt(1000000) + 1);
            System.out.println("\t" + i + "\t   " + (int)log2(i) + "\t     " + b.getHeight());
        }
    }
    
    public void insertRand(BST<Integer> b, int n){
        Random rand = new Random();
        System.out.println("\tn\tlog2(n)\t   height\n\t- - - - - - - - - - - - - ");
        
        for(int i = 1; i <= n; i ++){
            b.insert(rand.nextInt(1000000) + 1);
            if((i % 100 == 0) && i <= 400)
                System.out.println("\t" + i + "\t   " + (int)log2(i) + "\t     " + b.getHeight());
            else if((i % 500 == 0) && i >= 500 && i <= 5000 )
                 System.out.println("\t" + i + "\t   " + (int)log2(i) + "\t     " + b.getHeight());
        }
    }
    

    public static double log2(int n)
{
    return (Math.log(n) / Math.log(2));
}
}
