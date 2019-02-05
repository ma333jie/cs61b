/** Array based list.
 *  @author Josh Hug
 */

//         0 1  2 3 4 5 6 7
// items: [6 9 -1 2 0 0 0 0 ...]
// size: 5

/* Invariants:
 addLast: The next item we want to add, will go into position size
 getLast: The item we want to return is in position size - 1
 size: The number of items in the list should be size.
*/

public class ArrayDeque<Item> {
    private Item[] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    /** Creates an empty list. */
    public ArrayDeque() {
        items = (Item[]) new Object[5];
        size = 0;
        nextFirst = 0;
        nextLast = 1;
    }

    /** Resizes the underlying array to the target capacity. */
    /*public static void arraycopy(Object source_arr, int sourcePos,
                            Object dest_arr, int destPos, int len)
    * */
    private void resize(int capacity) {
        Item[] a = (Item[]) new Object[capacity];
        int numToEnd = modHelper(items.length-nextFirst-1);
        int starPos = modHelper(nextFirst+1);
        int copyItem = modHelper(nextLast);
        nextFirst = capacity-1;
        nextLast = size;
        System.arraycopy(items,starPos, a, 0, numToEnd);
        System.arraycopy(items,0,a,numToEnd,copyItem);
        items = a;
    }

        /*public void printDeque()
    Prints the items in the deque from first to last, separated by a space.
    * */

    public void printDeque(){
        //print from nextFirst + 1, nextlast-1;
       for (int i = 0; i < size; i++){
           System.out.print(this.get(i));
       }
    }

    /** Inserts X into the back of the list. */
    public void addLast(Item x) {
        if (size == items.length) {
            resize(size *2);
        }

        items[nextLast] = x;
        size = size + 1;
        // since the size is enough, we can put last pointer in the front if there is no space
        nextLast = modHelper(nextLast+1);
    }

    /* public void addFirst(T item):
     Adds an item of type T to the front of the deque.
    * */
    public void addFirst(Item x){
        if (size == items.length){
            resize(size*2);
        }
        items[nextFirst]= x;
        size = size +1;
        nextFirst = modHelper(nextFirst-1);
    }

    /** Returns the item from the back of the list. */
    public Item getLast() {
        return items[modHelper(nextLast - 1)];
    }
    public Item getFirst(){
        return items[modHelper(nextFirst+1)];

    }
    /**
     * public boolean isEmpty():
     * Returns true if deque is empty, false otherwise.
     */
    public boolean isEmpty(){
        return (size== 0);
    }


    /** Gets the ith item in the list (0 is the front). */
    public Item get(int i) {
        int ith =modHelper(nextFirst+i+1);
        return items[ith];
    }

    /** Returns the number of items in the list. */
    public int size() {
        return size;
    }

    /** Deletes item from back of the list and
     * returns deleted item. */
    public Item removeLast() {

        if (size == 0){
            throw new ArrayIndexOutOfBoundsException();
        }


        Item x = getLast();
        int last = modHelper(nextLast-1);
        items[last] = null;
        size = size - 1;
        nextLast= last;
        if (size != 0) {
            if (items.length / size > 4) {
                //System.out.println("resize");
                resize(items.length / 2);
            }
        }
        return x;
    }

    public Item removeFirst(){
        Item x = getFirst();
        int first = modHelper(nextFirst+1);
        items[first] =null;
        size = size -1;
        nextFirst = first;
        if(items.length/size > 4){
            //System.out.println("resize");
            resize(items.length/2);
        }
        return x;
    }
    public int modHelper (int n){
        if (n %items.length < 0){
            return n %items.length+items.length;
        }
        return n%items.length;
    }
    public int getNextFirst(){
        return nextFirst;
    }
    public int getNextLast(){
        return nextLast;
    }
}


