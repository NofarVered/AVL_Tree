

/**
 *
 * AVLTree
 *
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 *
 */

public class AVLTree {
	protected IAVLNode root; // pointer for the root
	private IAVLNode mini; // pointer for the node with the smallest key
	private IAVLNode maxi; // pointer for the node with the bigest key

	


	public AVLTree() { // a constructor for an empty tree
		this.root = new AVLNode(-1, null); // the root is an external node, at srart 
		this.mini=null; 
		this.maxi=null;
	}

/**
   * public boolean empty()
   *
   * returns true if and only if the tree is empty
   *
   */
  public boolean empty() {
	  // O(1)
	  //return True if the tree is empty, otherwise it returns false
	  //our definition to an empty tree is a tree with a external node as a root
    return (this.getRoot().getKey() == -1);
  }

 /**
   * public String search(int k)
   *
   * returns the info of an item with key k if it exists in the tree
   * otherwise, returns null
   */
  public String search(int k) // O(log n)
  //return the info of the node with key = k, if there is no node with key = k returns false
  {
	  if (this.empty())
	  	 return null;
	  IAVLNode n = this.getNode(k);
	  if (n != null) return n.getValue();
	  return null;
  }
 
  private AVLNode getNode(int k) { 
	  //return a node with the key k 
	  //O(log n)
	  IAVLNode cur = this.root;
	  if (this.empty()) 
		  return null;
	  while (cur.isRealNode()) { 
		  if (cur.getKey() == k)
			  return (AVLNode) cur;
		  if (k>cur.getKey()) cur=cur.getRight();
		  else cur = cur.getLeft();
	  }
	  return null;  
  		}

  /**
   * public int insert(int k, String i)
   *
   * inserts an item with key k and info i to the AVL tree.
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
   * promotion/rotation - counted as one rebalnce operation, double-rotation is counted as 2.
   * returns -1 if an item with key k already exists in the tree.
   */

  
  public int insert(int k, String i) {		//O(log(n)) 
	// inserts new node with key=k and info=i to AVL
	// fixes the tree if there are AVL violations
	// returns amount of rotations needed or -1 if k is in the tree
 	  IAVLNode node=new AVLNode(k,i);	// new node to insert
	  return insert_node( node);
  }
  private int insert_node(IAVLNode node) {
	  if (this.empty()) {
		  this.root = node;
		  this.mini = node;
		  this.maxi = node;
		  return 0;
	  }
	  if (this.getNode(node.getKey())!= null) {			// O(log(n))
		  
		  return -1;}
	  if (this.mini != null)
	  if (node.getKey()<this.getMin().getKey()) { //update the nimi node
		  this.mini=node;
	  }
	  if (this.maxi != null)
	  if (node.getKey()>this.getMax().getKey()) { //update the maxi node
		  this.maxi=node;
	  }
	  this.BST_insert(node);
	  int efficiency= rebalance(node);
	  this.updateSizeUp(node);
	  return efficiency;
  }
     
  private void BST_insert(IAVLNode node) {	//O(log((n))
	   //insert to regular BST
	   IAVLNode parent = this.root;
	   IAVLNode last_parent=null;
	   if (this.empty()) { this.root=node; } //an empty tree
	   while (parent.getKey()!=-1) { // looking for a place to insert
		   last_parent=parent;
		   if (node.getKey()<parent.getKey()) {
			   parent=parent.getLeft();
			   }
		   else {
			   parent=parent.getRight();
			   }
	   }
	   node.setParent(last_parent); //update the pointer to the parent
	   if (node.getKey()<last_parent.getKey()) { //update the pointer to the node
		   last_parent.setLeft(node);
		   }
	   else { 
		   last_parent.setRight(node); 
		   }
	   }
  
  private int rebalance(IAVLNode node) {				//O(log(n))
	   // checks if there are AVL criminals 
	   // send calls rotation functions if needed
	   // returns amount of rotations needed to fix the AVL tree
	  IAVLNode p=node.getParent();
	  if (p==null) {return 0;}
	  int sum=0;
	  while(p!=null) {
		  if (Math.abs(this.diff(p))==1) { // in case1 of 01 or 10
			  this.update_height(p);
			  this.update_size(p);
			  sum+=1;
		  }
		  else if (this.diff(p)==-2) { // case 2 + 3, opposite
			  IAVLNode right=p.getRight();
			  //System.out.println(p.getKey());
			  if (this.diff(right)==-1) {	//"like" case 2 	
				  this.left_rotation(p);
				  	sum=+2; 
				  	break;
				  	}
			  if (this.diff(right)== 1) {	//"like" case 3	
				  this.right_left_rotation(p);		
				  sum=+5;
				  break;
			   }
			   if (this.diff(right)== 0) { //case special FOR join
				   this.left_rotation(p);		
				  	sum=+2;
				  	}

		   }
	   		else if (this.diff(p)==2) { // case 2 + 3
			   IAVLNode left=p.getLeft();	
			   if (this.diff(left)==-1) {	// case 3
				   this.left_right_rotation(p);	
				   sum=+5;
				   break;
			   }
			   if (this.diff(left)== 1) { //case 2 
				   this.right_rotation(p);		
				  	sum=+2;
				  	break;
				  	}
			   if (this.diff(left)== 0) {  //case special FOR join
				   this.right_rotation(p);		
				  	sum=+2;
				  	}

			   }
		  p=p.getParent();
		   }
	  return sum;
	   }
   

  
  private void left_rotation(IAVLNode node) {	//O(1)
	   // performs left rotation
	   IAVLNode R = node.getRight();
	   IAVLNode RL = R.getLeft();	//could be null
	   
	   R.setLeft(node);
	   replace_top(R,node);
	   if (RL.getKey()!=-1) {
		   RL.setParent(node);}
	   node.setRight(RL);
	   this.update_height(node);		//O(1)
	   this.update_height(R);			//O(1) 
	   this.update_size(node);    		//O(1)		//node.setSize(node.getSize()-1-R.getSize()+RL.getSize());
	   this.update_size(R);   			//O(1)		//L.setSize(R.getSize()-1-RL.getSize()+node.getSize());
  }
  	 
  private void right_left_rotation(IAVLNode node) {	//O(1)
	// performs right_left rotation
	   this.right_rotation(node.getRight());
	   this.left_rotation(node);
  }
  
  private void left_right_rotation(IAVLNode node) {	//O(1)
	// performs left_right rotation
	   this.left_rotation(node.getLeft());	
	   this.right_rotation(node);				
  }

  private void right_rotation(IAVLNode node) {	//O(1)
	// performs right rotation
	   IAVLNode L = node.getLeft();
	   IAVLNode LR = L.getRight();	//could be null
	   L.setRight(node);
	   replace_top(L,node);
	   if (LR.getKey()!=-1) {
		   LR.setParent(node);}
	   node.setLeft(LR);
	   this.update_height(node);		//O(1)
	   this.update_height(L);			//O(1)
	   this.update_size(node);    		//O(1)		//node.setSize(node.getSize()-1-L.getSize()+LR.getSize());
	   this.update_size(L);   			//O(1)		//L.setSize(L.getSize()-1-LR.getSize()+node.getSize());
  }
  
  private void replace_top(IAVLNode son, IAVLNode node) {	//O(1)
	   //switches relations between son and node
	   //if needed updates root 
	   if (this.root==node) {
		   this.root=son;			
		   son.setParent(null);}
	   else {
		   IAVLNode parent=node.getParent();
		   son.setParent(parent);
		   if (parent.getLeft()==node) {
			   parent.setLeft(son);
		   }
		   else {
			   parent.setRight(son);}
		   }
	   node.setParent(son);
  }
   
  private int calc_height(IAVLNode node) {	//O(1) V
	   //returns node's height by definition
	   //-1 for external node
	   if (node.getKey()==-1) {
		   return -1;}
	   return (1 + Math.max(node.getLeft().getHeight() ,node.getRight().getHeight()) );
  }
  
  private void update_height(IAVLNode node) {	//O(1) V
	   //updates node's height
	  node.setHeight( this.calc_height(node) );
  }
  private int calc_size(IAVLNode node) {	//O(1) V
	   //returns node's size by definition
	   //0 for external node
	   if (node.getKey()==-1) {
		   return 0;}
	   return (1 +node.getLeft().getSize()+node.getRight().getSize());
 }
 
  private void update_size(IAVLNode node) {	//O(1) V
	   //updates the size
	  node.setSize( this.calc_size(node));
 }
  
  public int diff(IAVLNode node) {	//O(1) V
	  //returns node's deviation 
	  // left's height - right's height
	   int LH = this.calc_height(node.getLeft());
	   int RH = this.calc_height(node.getRight());
	   return (LH-RH);
  }
  
  /**
   * public int delete(int k)
   *
   * deletes an item with key k from the binary tree, if it is there;
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
   * demotion/rotation - counted as one rebalnce operation, double-rotation is counted as 2.
   * returns -1 if an item with key k was not found in the tree.
   */
  	public int delete(int k) {
		// delete node with the key k in the tree. 
		// return how many times we done a rebalance moves for keeping the tree being AVL one. 
		// if there is no node with k key, return -1 
		if (this.empty()) { //no item with k key in an empty tree
			return -1;
		}
		IAVLNode node= this.getNode(k); // finding a node with k key
		   if (node == null) { // there is not such a key
			   return -1;
		   }
		if (node == this.root && node.getSize() == 1) { // the case we are deleting the only node in tree- after that the tree is empty
			this.root = new AVLNode(-1);
			this.mini = null;
			this.maxi = null;
			return 0;
		}
		
		IAVLNode cur = deleteNode(node); 
		 // cur is the node we need to perform rebalancing from
		if (cur != null) {
			this.updateSizeUp(cur);
			return rebalancedel(cur);
		}
		return 0;
		
	}
  
  	private IAVLNode deleteNode(IAVLNode node) { 
		   // delete the node without rebalancing
  			//O(1) or O(logn) when binary node
		IAVLNode parent = node.getParent();
		// node is a leaf
		if (node.getHeight() == 0) { 
			if (parent.getRight() == node) {
				parent.setRight(new AVLNode(-1));
			} else {
				parent.setLeft(new AVLNode(-1));
			}
			if(node.getKey()<this.mini.getKey()) this.mini = parent;
			if(node.getKey()<this.maxi.getKey()) this.mini = parent;
			return parent;
		}
		//node has no right son
		if (!node.getRight().isRealNode()) { 
			if(parent == null) {
				this.root = node.getLeft();
				this.root.setParent(null);
				return null;
			}
			if (parent.getRight() == node) {
				parent.setRight(node.getLeft());
			} else {
				parent.setLeft(node.getLeft());
			}
			node.getLeft().setParent(parent);
			return parent;
		}
		//node has no left son
		if (!node.getLeft().isRealNode()) { 
			if (parent == null) {
				this.root = node.getRight();
				this.root.setParent(null);
				return null;
			}
			if (parent.getRight() == node) {
				parent.setRight(node.getRight());
			} else {
				parent.setLeft(node.getRight());
			}
			node.getRight().setParent(parent);
			return parent;
		}
		//node is a binary node
		IAVLNode suc = node.successorForDeletion(); 
		IAVLNode sucP = suc.getParent();
		//not immediate successor
		if (node != sucP) { 
			node.getRight().setParent(suc);
			suc.getRight().setParent(sucP);
			sucP.setLeft(suc.getRight());
			suc.setRight(node.getRight());
		}
		node.getLeft().setParent(suc);
		suc.setLeft(node.getLeft());
		suc.setHeight(node.getHeight());
		suc.setSize(node.getSize()-1);
		suc.setParent(parent);

		if (parent == null) { 
			this.root = suc;
		} else {
			if (parent.getRight() == node) {
				parent.setRight(suc);
			} else {
				parent.setLeft(suc);
			}
		}
		if (node == sucP) { 
			return suc;
		}
		return sucP;

	}
   private int rebalancedel(IAVLNode node) {				//O(log(n))
	   // checks if there are AVL criminals 
	   // send calls rotation functions if needed
	   // returns amount of rotations needed to fix the AVL tree
	   IAVLNode p=node; 
	  if (p==null) {return 0;}
	  int sum=0;
	  while(p!=null) {
		  if (this.diff(p) == 0) { //  case 1 kk
			  this.update_height(p);
			  this.update_size(p);
			  sum+=1;
			  
		  }
		  else if (this.diff(p)==-2) { // case 2 + 3 + 4 
			  IAVLNode right=p.getRight();
			  if (this.diff(right)==0) {	//  case 2 	
				  this.left_rotation(p);
				  	sum=+3; 
				  	break;
				  	}
			  if (this.diff(right)== -1) {	// case 3	
				  this.left_rotation(p);
				  	sum=+3; 
			  }
			  if (this.diff(right)== 1) {	// case 4	
				  this.right_left_rotation(p);
				  	sum=+6; 
			  }
		   }
	   		else if (this.diff(p)==2) { // mirror cases 2+3+4
				  IAVLNode left=p.getLeft();
				  if (this.diff(left)==0) {	//  case 2 	
					  this.right_rotation(p);
					  	sum=+3; 
					  	break;
					  	}
				  if (this.diff(left)== 1) {	// case 3	
					  this.right_rotation(p);
					  	sum=+3; 
				  }
				  if (this.diff(left)== -1) {	// case 4	
					  this.left_right_rotation(p);
					  	sum=+6; 
				  }
			   }
		  	p=p.getParent();
		   }
	  this.updateSizeUp(p);
	  return sum;
	   }
 

   /**
    * public String min()
    *
    * Returns the info of the item with the smallest key in the tree,
    * or null if the tree is empty
    */
   public String min()
   {
	   // return the info of the mini node in the tree
	   // if the tree is empty it will return null
	   // O(1)
 		 if (this.getMin()==null) return null;
  		return this.getMin().getValue();
   }
   
   public IAVLNode getMin()
   {   //O(1) due to maintenance
	   if (this.empty())
		   return null;
	   return this.mini;
   }

   /**
    * public String max()
    *
    * Returns the info of the item with the largest key in the tree,
    * or null if the tree is empty
    */
   public String max()
   {
	   if (this.getMax()==null) return null;
	   return this.getMax().getValue();
   }
   public IAVLNode getMax()
   {   //O(1) due to maintenance
	   if (this.empty())
		   return null;
	   return this.maxi;
   }
   public static int index;
   public static IAVLNode [] nodes_array;
   
   private void in_order_nodes_rec(IAVLNode node) {
	   //O(n) time	
	   //O(log(n)) memory recursion stack
	   //returns sorted nodes by keys 
	   if (node.getKey()!=-1) {
		   in_order_nodes_rec(node.getLeft());
		   nodes_array[index]=node;
		   index++;
		   in_order_nodes_rec(node.getRight());
	   }
   }	
  /**
   * public int[] keysToArray()
   *
   * Returns a sorted array which contains all keys in the tree,
   * or an empty array if the tree is empty.
   */
   public int[] keysToArray()
   { 
 	  //O(n)
 	  //returns sorted (by key) array of keys inside the tree, if tree is empty returns null
 	  nodes_array = new IAVLNode[this.size()];
 	  index=0;
 	  in_order_nodes_rec(this.root);
 	  int [] keys_array = new int [this.size()];
 	  for (int i=0;i<this.size();i++) {	
 		  keys_array[i]=nodes_array[i].getKey();
 	  }
 	  return keys_array;
   }

  /**
   * public String[] infoToArray()
   *
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   */
   public String[] infoToArray()
   {
 	  //O(n)
 	  //returns sorted (by key) array of values inside the tree, if tree is empty returns null
 	  nodes_array = new IAVLNode[this.size()];
 	  index=0;
 	  in_order_nodes_rec(this.root);
 	  String [] vals_array = new String [this.size()];
 	  for (int i=0;i<this.size();i++) {	
 		  vals_array[i]=nodes_array[i].getValue();
 	  }
 	  return vals_array;
   }
   /**
    * public int size()
    *
    * Returns the number of nodes in the tree.
    *
    * precondition: none
    * postcondition: none
    */
   public int size()
   { // Returns the number of nodes in the tree.
   // O(1)
	   return this.root.getSize(); 
	   
   }
   
     /**
    * public int getRoot()
    *
    * Returns the root AVL node, or null if the tree is empty
    *
    * precondition: none
    * postcondition: none
    */
   public IAVLNode getRoot()
   { // Returns the root AVL node
   // O(1)
	   return this.root;
   }
     /**
    * public string split(int x)
    *
    * splits the tree into 2 trees according to the key x.
    * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
 * precondition: search(x) != null (i.e. you can also assume that the tree is not empty)
    * postcondition: none
    */  
   
   public AVLTree[] split(int x) {
	   // split at x, and join sub trees with efficient joins
	   // O(logn) 
		AVLTree bigger = new AVLTree();
		AVLTree smaller = new AVLTree();
		IAVLNode curr = getNode(x);
		if (curr.getLeft().getHeight() != -1) {
			smaller.newTree(curr.getLeft());
		}
		if (curr.getRight().getHeight() != -1){
			bigger.newTree(curr.getRight());
		}
		IAVLNode parent = curr.getParent();
		AVLTree toJoin = new AVLTree();
		while(parent != null) {
			if (parent.getRight() == curr) {
				curr = parent;
				parent = parent.getParent();
				if (curr.getLeft().getHeight() != -1) {
					toJoin.newTree(curr.getLeft());
				} else {
					toJoin = new AVLTree();
				}
				curr.setParent(null);
				curr.setLeft(new AVLNode(-1));
				curr.setRight(new AVLNode(-1));
				curr.setHeight(0);
				curr.setSize(1);
				smaller.join(curr, toJoin);
			} else {
				curr = parent;
				parent = parent.getParent();
				if (curr.getRight().getHeight() != -1) {
					toJoin.newTree(curr.getRight());
				} else {
					toJoin = new AVLTree();
				}
				curr.setParent(null);
				curr.setLeft(new AVLNode(-1));
				curr.setRight(new AVLNode(-1));
				curr.setHeight(0);
				curr.setSize(1);
				bigger.join(curr, toJoin);
			}
		}
		return new AVLTree[] {smaller,bigger};
	}
   
   
   
   protected void find_set_min() {
	 //O(logn)
	 this.mini = this.find_min();
	   
   }
   protected IAVLNode find_min() {
	   //finds the min by go always right
	   //O(logn) (height of the entire tree)
	   IAVLNode curp = this.getRoot();
	   IAVLNode cur = curp.getLeft();
	   
	   while (cur.isRealNode()) {
		   curp = cur;
		   cur = cur.getLeft();
	   } 
	   return curp;
   }
   
   protected void find_set_max() {
	   //O(logn)
	   this.mini = this.find_max();
	   
   }
   protected IAVLNode find_max() {
	   //finds the max by go always left
	   //O(logn) (height of the entire tree)
	   IAVLNode curp = this.getRoot();
	   IAVLNode cur = curp.getRight();
	   
	   while (cur.isRealNode()) {
		   curp = cur;
		   cur = cur.getRight();
	   } 
	   return curp;
   }
   /**
    * public join(IAVLNode x, AVLTree t)
    *
    * joins t and x with the tree.
    * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
 * precondition: keys(x,t) < keys() or keys(x,t) > keys(). t/tree might be empty (rank = -1).
    * postcondition: none
    */
   
   public int join(IAVLNode x, AVLTree t) {
		// find a node in the higher tree i the same height of the lower tree and join them in O(1)
	   	//O(big.hight - small.hight + 1)
		if (t.empty() && this.empty()) {
			this.root = x;
			this.mini = x;
			this.maxi = x;
			update_size(this.root);
			x.setParent(null);
			x.setLeft( new AVLNode(-1));
			x.setRight(new AVLNode(-1));
			x.setSize(1);
			x.setHeight(0);
		}
		if (t.empty()) {
			int height = this.root.getHeight() + 1;
			insert(x.getKey(), x.getValue());
			return height;
		}
		if (this.empty()) { 
			this.root = t.getRoot();
			this.mini = t.mini;
			this.maxi = t.maxi;
			update_size(this.root);
			int height = t.root.getHeight() + 1;
			insert(x.getKey(), x.getValue());
			return height;
		} 
		AVLTree bigger = t;  // if the keys in "t" are bigger then "x"
		AVLTree smaller = this; // if the keys in "t" are bigger then "x"
		if (this.root.getKey() > x.getKey()) { //if the keys in "this" are bigger than "x"
			bigger = this;
			smaller = t;
		}
		int diff;
		boolean biggerIsHeigher = true;
		IAVLNode axis;
		if (bigger.root.getHeight() > smaller.root.getHeight()) {
			diff = bigger.root.getHeight() - smaller.root.getHeight();
			if (diff == 0) { //the trees have the same height
				simpleJoin(smaller, x, bigger);
				return diff + 1;
			}
			axis = axisL(bigger, smaller.root.getHeight()); //searches the joining node
			if (axis == bigger.root) {
				simpleJoin(smaller, x, bigger);
				return diff + 1;
			}
			if(!axis.isRealNode()) {
				smaller.insert_node(x);
				return diff +1;
			}
			joinNodes(x, axis, smaller.root);
		} else {
			diff = smaller.root.getHeight() - bigger.root.getHeight();
			if (diff == 0) { //the trees have the same height
				simpleJoin(smaller, x, bigger);
				return diff + 1;
			}
			biggerIsHeigher = false;
			axis = axisR(smaller, bigger.root.getHeight());  //searches the joining node
			if (axis == smaller.root) {
				simpleJoin(smaller, x, bigger);
				return diff + 1;
			}
			if(!axis.isRealNode()) {
				bigger.insert_node(x);
				return diff +1;
			}
			joinNodes(x, axis, bigger.root);
		}
		
		if (biggerIsHeigher) {
			this.root = bigger.root;
		} else {
			this.root = smaller.root;
		}
		update_size(x.getParent());
		this.updateSizeUp(x.getParent());
		this.mini = smaller.mini;
		this.maxi = bigger.maxi;
		rebalance(x);
		return diff + 1;
	}
	private void joinNodes(IAVLNode x, IAVLNode b, IAVLNode a) {
		// simply join the nodes after found
		//O(1)
			IAVLNode c = b.getParent();
			if (c.getRight() == b) {
				x.setLeft(b);
				x.setRight(a);
				c.setRight(x);
			} else {
				x.setRight(b);
				x.setLeft(a);
				c.setLeft(x);
			}
			b.setParent(x);
			a.setParent(x);
			x.setParent(c);
			update_height(x);
			update_size(x);
			update_size(c);
		}
	
	protected void updateSizeUp(IAVLNode n) {
		//updating the size from node n up to the root 
		// O(log n)
		while (n != null) {
			this.update_size(n);
			n = n.getParent();
			} 
	   }

	private void newTree(IAVLNode newRoot) {
		// create a tree from a node
		//O(1)
		this.root = newRoot;
		this.root.setParent(null);
		this.find_set_max();
		this.find_set_max();
	}
	private IAVLNode axisL(AVLTree t, int k) {
		// find joining axis on the left
		// O(t.height - k)
		IAVLNode curr = t.root;
		while (curr.getHeight() > k) {
			curr = curr.getLeft();
		}
		return curr;
	}
	
	private IAVLNode axisR(AVLTree t, int k) {
		// find joining axis on the right
		// O(t.height - k)
		IAVLNode curr = t.root;
		while (curr.getHeight() > k) {
			curr = curr.getRight();
		}
		return curr;
	}
	private void simpleJoin(AVLTree smaller, IAVLNode x, AVLTree bigger) {
		// join two trees in the same height
		// O(logn)
			x.setLeft(smaller.root);
			x.setRight(bigger.root);
			smaller.root.setParent(x);
			bigger.root.setParent(x);
			update_height(x);
			update_size(x);
			this.root = x;
			update_size(this.root);
			this.mini = smaller.mini;
			this.maxi = bigger.maxi;
		}


/**
  * public interface IAVLNode
  * ! Do not delete or modify this - otherwise all tests will fail !
  */
   public interface IAVLNode{
	   public int getKey(); //returns node's key (for virtuval node return -1)
	   public String getValue(); //returns node's value [info] (for virtuval node return null)
	   public void setLeft(IAVLNode node); //sets left child
	   public IAVLNode getLeft(); //returns left child (if there is no left child return null)
	   public void setRight(IAVLNode node); //sets right child
	   public IAVLNode getRight(); //returns right child (if there is no right child return null)
	   public void setParent(IAVLNode node); //sets parent
	   public IAVLNode getParent(); //returns the parent (if there is no parent return null)
	   public boolean isRealNode(); // Returns True if this is a non-virtual AVL node
	   public void setHeight(int height); // sets the height of the node
	   public int getHeight(); // Returns the height of the node (-1 for virtual nodes)
	   public void setSize(int size); // sets the height of the node
	   public int getSize(); // Returns the height of the node (-1 for virtual nodes)
	   public IAVLNode successorForDeletion(); // get the successor for a binary node
    
}

   /**
   * public class AVLNode
   *
   * If you wish to implement classes other than AVLTree
   * (for example AVLNode), do it in this file, not in
   * another file.
   * This class can and must be modified.
   * (It must implement IAVLNode)
   */
  public class AVLNode implements IAVLNode{
	  protected IAVLNode left; //pointer for left child
	  protected IAVLNode right; //pointer for right child
	  protected IAVLNode parent; //pointer for the parent
	  protected int key; //  key
	  protected String info; //  value, info
	  protected int height; // the heigh of the node by defenition in class
	  protected int size; // the size of the node by defenition in class

	  public AVLNode (int key, String info){
		 // constructor for an new node
	  	//O(1)
			this.key = key;
			this.info = info;
			this.left = new AVLNode(-1);
			this.right = new AVLNode(-1);
			this.parent = null;
			this.height = 0; 
			this.size=1; 
		}
		public AVLNode(int key) {
		 //constructor for an external node
		//O(1)
			this.key = key;
			this.info = null;
			this.left = null;
			this.right = null;
			this.parent = null;
			this.height = -1; 
			this.size=0; 
		}
		public int getKey()
		{	//returns node's key
			//O(1)
			return this.key; 

		}
		public String getValue()
		{	//returns node's value
			//O(1)
			return this.info;

		}
		public void setLeft(IAVLNode node)
		{	//sets node's left son 
			//O(1)
			this.left=node;

		}
		public IAVLNode getLeft()
		{	//return node's left son 
			//O(1)
			
			return this.left; 
		}
		public void setRight(IAVLNode node)
		{
			//sets node's right son 
			//O(1)
			this.right=node;

		}
		public IAVLNode getRight()
		{	//return node's right son 
			//O(1)
			
			return this.right; 

		}
		public void setParent(IAVLNode node)
		{	
			//sets node's parent
			//O(1)
			
			this.parent=node;

		}
		public IAVLNode getParent()
		{	//returns node's parent
			//O(1)
			return this.parent;
		}

		public void setHeight(int height)
		{	//sets node's height
			//O(1)
			this.height=height; 
		}
		public int getHeight()
		{	//returns node's height
			//O(1)
			return this.height;
		}
		public void setSize(int size)
		{	//sets node's size
			//O(1)
			this.size=size; 
		}
		public int getSize()
		{	//returns node's size
			//O(1)
			return this.size;
		}
		@Override
		public boolean isRealNode() {
			//returns false if the node is an external leaf
			// returns true otherwise
			// we decide it by the the key's value
			//O(1)

			return (this.getKey() != -1);
		}
		
		public IAVLNode successorForDeletion() { 
			// find a successor, supports only find successor for a binary node
			// O(logn)
			IAVLNode node = this;
			if (node.getRight().isRealNode()) {
				node = node.getRight();
				while (node.getLeft().isRealNode()) {
					node = node.getLeft();
				}
				return node;
			} else {
				IAVLNode parent = node.getParent();
				while (parent != null && parent.getRight() == this) {
					node = parent;
					parent = node.getParent();
				}
				return parent;
			}
		}
		
		
  }

}

 

