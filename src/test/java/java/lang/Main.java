package java.lang;

public class Main {

    public static void main(String[] args) {
//        int[] array = new int[10];
//        int a = 6;
//        array[5] = 17;
//        int res = array[a - 1] + a;

//        char[] chars = {'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};


        String str = "HELLO WORLD";

        String reversed = reverse(str);

        print(reversed);
    }

    private static String reverse(String in) {
        char[] chars = new char[in.length()];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = in.charAt(in.length() - (i + 1));
        }
        return new String(chars);
    }

    private static native void print(String in);
}
