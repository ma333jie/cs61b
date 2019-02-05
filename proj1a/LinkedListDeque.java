public class LinkedListDeque<T> {
	private class TNode{
		public T item;
		public TNode pre;
		public TNode next;

		public TNode(T i, TNode nPer, TNode nNext){
			item = i;
			pre = nPer;
			next = nNext;
		}
	}

	private TNode first;
	private TNode last;
	private int size;


	public LinkedListDeque(){
		first = new TNode(null,null,null);
		last = new TNode(null, first,null);
		first.next = last;
		last.pre = first;
		size = 0;
	}

	public LinkedListDeque(T x){
		first = new TNode(null,null,null);
		last = new TNode(null,null,null);
		TNode newNode = new TNode(x, first, last);
		first.next = newNode;
		last.pre = newNode;
		size  = 1;
	}



	public void addFirst (T item){
		TNode newNode = new TNode(item, first,first.next);
		first.next.pre = newNode; // link the third element first
		first.next = newNode;
		size = size +1;
	}

	public void addLast(T item){
		TNode newNode = new TNode(item,last.pre,last);
		last.pre.next = newNode;
		last.pre = newNode;
		size = size +1;
	}
    // Returns true if deque is empty, false otherwise.
	public boolean isEmpty(){
		if(size == 0){
			return true;
		}
		return false;
	}

	// Return size
	public int size(){
		return size;
	}

	public void printDeque(){
		int sizeCopy = size;
		TNode myFirst = first;

		while(sizeCopy != 0){
			System.out.println(myFirst.next.item);
			myFirst = myFirst.next;
			sizeCopy = sizeCopy-1;
		}
	}
	//Removes and returns the item at the front of the deque. If no such item exists, returns null.
	/*n
	need to add throw the illegal argument
	*/
	public T removeFirst(){
		if (size >0){
			T firstItem = first.next.item;
			first.next.next.pre = first;
			first.next =first.next.next;
			size = size -1;
			return firstItem;}
		else {
			return null;}
}

	public T removeLast() {
		if (size > 1) {
			T lastItem = last.pre.item;
			last.pre.pre.next = last;
			last.pre = last.pre.pre;
			size = size-1;
			return lastItem;
		} else
			{return null;}
	}

	/**?
	 *
	 * @param index
	 * @return
	 * Gets the item at the given index,
	 * where 0 is the front, 1 is the next item, and so forth.
	 * If no such item exists, returns null. Must not alter the deque!
	 */
	public T get(int index){
		int myIndex = index;
		TNode pointer = first;
		if(size < index){
			return null;
		}else{
			while(myIndex != 0){
				pointer = pointer.next;
				myIndex = myIndex-1;
			}
			return pointer.next.item;
		}
	}

	public T getRecursive(int index){

		if (size <index){
			return null;
		}else{
			TNode getNode = getRecursiveNode(index,first);
			return getNode.item;
		}

	}
	// a helper method might need for this
	public TNode getRecursiveNode(int index, TNode Node){
		TNode pointer = Node;
		if (size < index){
			return null;
		}else if (index ==0){
			return pointer.next;
		}else{
			return getRecursiveNode(index -1, pointer.next);
		}
	}


}



