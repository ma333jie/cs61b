public class ArrayDequeTest {
    public static void main(String[] args) {
        ArrayDeque<String> a1 = new ArrayDeque<String>();
        a1.addFirst("b");
       // System.out.println("The nextFirst is "+a1.getNextFirst());
        a1.addFirst("a");
        a1.addLast("c");
        a1.addLast("d");
        a1.addLast("e");
        a1.printDeque();
        System.out.println("The nextFirst is "+a1.getNextFirst());
        System.out.println("The nextLast is "+a1.getNextLast());
        a1.addLast("f");
        a1.printDeque();
        a1.removeLast();
        a1.removeLast();
        a1.removeLast();
        System.out.println();
        a1.printDeque();
        System.out.println();

        a1.removeLast();
        a1.removeLast();
        a1.removeLast();

        a1.removeLast();
        a1.removeLast();
        System.out.println(a1.getNextFirst());
        a1.printDeque();
    }
}
