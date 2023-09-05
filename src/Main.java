import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    /*
        Main class: acts as the front end aspect of the program

     */

    public static void main(String[] args) throws IOException {

        System.out.println("Welcome to Assignment 1 -> In this assignment we will look at 3 different methods of machine Learning");
        System.out.println("Press: [1] KNN [2] DecisionTree [3] Prerceptron");
        // Enter data using BufferReader
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // Reading data using readLine
        String section = reader.readLine();

        // Printing the read line
        System.out.println(section);

        switch (section) {
            case "1" -> {
                System.out.println("Welcome to part 1 - It is all about KNN algorithm.");
                System.out.println("Please ENTER the training file -> then the test file. Use a space to separate the files");
                System.out.println("[wine-training wine-test] are file names used for this:");
                BufferedReader part1Reader = new BufferedReader(new InputStreamReader(System.in));
                // Reading data using readLine
                String part1Section = reader.readLine();
                String[] arrOfStr = part1Section.split(" ", 2);
                part1 p = new part1(arrOfStr[0], arrOfStr[1]);
                p.run();
            }
            case "2" -> {
                System.out.println("Welcome to part 2 - It is all about Decision Tree Learning.");
                System.out.println("Please ENTER the training file -> then the test file. Use a space to separate the files");
                System.out.println("[hepatitis-training hepatitis-test] are file names used for this:");
                String part2Section = reader.readLine();
                String[] arrOfStr = part2Section.split(" ", 2);
                part2 p2 = new part2(arrOfStr[0], arrOfStr[1]);
            }
            case "3" -> {
                System.out.println("Welcome to part 3 - It is all about Perceptrons.");
                System.out.println("Please ENTER the file");
                System.out.println("[ionosphere.data]->is the file name needed for this:");
                String part3Section = reader.readLine();
                part3 p3 = new part3(part3Section);
            }
            default -> System.out.println("That is not a part in this Assignment");
        }

    }


}
