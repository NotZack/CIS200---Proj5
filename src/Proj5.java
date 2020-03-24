import java.io.*;
import java.util.Scanner;

/**
 * Proj5.java
 * Zackary Nichol / Friday 3:30PM lab session
 *
 *
 */

public class Proj5 {
    
    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);

        String[] fileData = readFile(getFileInfo(scnr));

        System.out.print("Please enter the number of questions on the exam: ");
        int numOfQuestions = Integer.parseInt(scnr.nextLine());
        System.out.print("Please enter the number of points per question: ");
        int pointValue = Integer.parseInt(scnr.nextLine());

        char[] key = createKey(numOfQuestions, fileData);

        int highScore = 0;
        int lowScore = 0;
        int totalScores = 0;

        System.out.format("%-12s%-12s%-12s%-12s%-12s\n", "Student ID", "# Correct", "% Correct", "Score", "Grade");
        for (String submition : fileData) {
            if (submition != null && submition.length() > 2) {
                int grade = gradeQuiz(key, submition.substring(submition.indexOf(',') + 1));
                double percentGrade = ((double) grade / key.length);
                displayResults(
                        submition.substring(0, submition.indexOf(',')),
                        grade,
                        percentGrade,
                        grade * pointValue,
                        getLetterGrade(percentGrade)
                );
                if (lowScore == 0 || grade < lowScore)
                    lowScore = grade;
                if (grade > highScore)
                    highScore = grade;
                totalScores += grade;
            }
        }
        //getAvg();
        //saveResults();
    }

    private static void displayResults(String studentID, int numberGrade, double percentGrade, int score, char letterGrade) {
        System.out.format("%-12s%-12s%-12s%-12s%-12s\n", studentID, numberGrade, percentGrade, score, letterGrade);
    }

    private static char getLetterGrade(double percentGrade) {
        if (percentGrade >= 90.0) return 'A';
        if (percentGrade >= 80.0) return 'B';
        if (percentGrade >= 70.0) return 'C';
        if (percentGrade >= 60.0) return 'D';
        return 'F';
    }

    private static int gradeQuiz(char[] key, String studentAnswers) {
        int numCorrect = 0;
        System.out.println(studentAnswers);
        for (int i = 0; i < key.length; i++) {
            if (key[i] == studentAnswers.charAt(i))
                numCorrect++;
        }
        return numCorrect;
    }

    private static char[] createKey(int numOfQuestions, String[] fileData) {
        Scanner scnr = new Scanner(System.in);

        System.out.print("\n");

        char[] key = new char[numOfQuestions];

        System.out.println("Please enter the answers for the following questions \n" +
                "where 'T' = true, 'F' = false, or A, B, C, D, E for multiple choice");

        for (int i = 0; i < key.length; i++) {
            System.out.print((i + 1) + ") ");
            char answer = scnr.nextLine().toLowerCase().charAt(0);
            key[i] = answer;
            switch (answer) {
                case 't':
                case 'a':
                    key[i] = '1'; break;
                case 'f':
                case 'b':
                    key[i] = '2'; break;
                case 'c': key[i] = '3'; break;
                case 'd': key[i] = '4'; break;
                case 'e': key[i] = '5'; break;
            }
        }
        System.out.print("\n");
        return key;
    }

    private static String[] readFile(String nameOfFile) {
        String[] fileData = new String[50];

        try (BufferedReader br = new BufferedReader(new FileReader(nameOfFile))){
            String line = br.readLine();

            int counter = 0;
            while (line != null) {
                fileData[counter] = line;

                counter++;
                line = br.readLine();
            }
            return fileData;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getFileInfo(Scanner scnr) {
        while (true) {
            System.out.print("Enter name of quiz file (i.e. QuizScores.txt): ");
            String nameOfFile = scnr.nextLine();

            if (new File(nameOfFile).exists())
                return nameOfFile;
            else
                System.out.println("Error! File does not exist");
        }


    }
}
