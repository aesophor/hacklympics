import java.util.Scanner;

public class Test {
    
    public static void main(String[] args) {
        Scanner cin = new Scanner(System.in);
        String s;
        
        while (cin.hasNext()) {
            s = cin.nextLine();
            System.out.println("hello, " + s);
        }
    }
}
