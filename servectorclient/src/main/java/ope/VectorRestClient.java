package ope;

import java.util.Scanner;

public class VectorRestClient {
    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("App starting...");
        Operator op = new Operator();

        boolean exit = false;
        while (!exit) {
            System.out.println("Choose operation type:\n0 - Success\n1 - Incomplete\n2 - Error\n3 - exit");
            switch (input.nextLine()) {
                case "0" -> op.successOperation();
                case "1" -> op.incompleteOperation();
                case "2" -> op.errorOperation();
                default -> exit = true;
            }
        }

        op.closeClient();
    }
}
