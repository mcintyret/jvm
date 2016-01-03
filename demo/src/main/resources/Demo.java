import java.util.Scanner;

public class Demo {

    public static void main(String[] args) {
        System.out.println("Running JVM Demo");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Ready for input. Enter 'q' to quit.");

        String line;
        while (!(line = scanner.nextLine()).equalsIgnoreCase("q")) {
            System.out.println(reverse(line));
        }

        System.out.println("Goodbye");
    }

    private static String reverse(String in) {
        char[] chars = new char[in.length()];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = in.charAt(in.length() - (i + 1));
        }
        return new String(chars);
    }

}
