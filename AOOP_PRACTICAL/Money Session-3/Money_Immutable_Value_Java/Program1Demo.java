public class Program1Demo {
    public static void main(String[] args) {
        double x = 0.1 + 0.2;
        System.out.println(x);       // 0.30000000000000004
        System.out.println(x == 0.3); // false

        double total = 0;
        for (int i = 0; i < 10; i++) {
            total += 0.10;
        }
        System.out.println(total);   // 0.9999999999999999

        long paise = 0;
        for (int i = 0; i < 10; i++) {
            paise += 10;
        }
        System.out.println(paise);   // 100
    }
}
