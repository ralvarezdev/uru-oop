package util;

import java.util.Scanner;

public class Console {
    public static void clear() {
        String os = System.getProperty("os.name");
        ProcessBuilder pb = null;

        try {
            // For Windows
            if (os.startsWith("Windows"))
                pb = new ProcessBuilder("cmd", "/c", "cls");

                // For Linux
            else if (os.startsWith("Linux"))
                pb = new ProcessBuilder("clear");

            if (pb != null)
                pb.inheritIO().start().waitFor();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void pressEnterToCont() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Press ENTER to Continue: ");
        scanner.nextLine();
    }
}
