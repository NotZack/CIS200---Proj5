import java.io.*;
import java.text.DecimalFormat;
import java.util.Scanner;

/**
 * Proj5.java
 * Zackary Nichol / Friday 3:30PM lab session
 *
 * Grades a given assignment file by the given answers in scantron form for each given student. Displays each student grade
 * and assignment average to the screen, then saves all score data into a text file.
 */

public class Proj5 {
    
    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);
        DecimalFormat form = new DecimalFormat("00.0");

        String[] fileData = readFile(getFileInfo(scnr));

        System.out.print("Please enter the number of questions on the exam: ");
        int numOfQuestions = Integer.parseInt(scnr.nextLine());
        System.out.print("Please enter the number of points per question: ");
        int pointValue = Integer.parseInt(scnr.nextLine());

        char[] key = createKey(numOfQuestions);

        int highScore = 0;
        int lowScore = 0;
        int totalScores = 0;
        int totalStudents = 0;

        System.out.format("%-24s%-24s%-24s%-24s%-24s\n", "Student ID", "# Correct", "% Correct", "Score", "Grade");
        for (String submission : fileData) {
            if (submission != null && submission.length() > 2) {
                int grade = gradeQuiz(key, submission.substring(submission.indexOf(',') + 1));

                double percentGrade = Double.parseDouble(form.format(((double) grade / key.length) * 100));
                displayResults(
                    submission.substring(0, submission.indexOf(',')), grade, percentGrade, grade * pointValue,
                    getLetterGrade(percentGrade)
                );

                if (lowScore == 0 || grade < lowScore)
                    lowScore = grade;
                if (grade > highScore)
                    highScore = grade;
                totalScores += grade * pointValue;
                totalStudents++;
            }
        }
        getAvg(
            form.format((double) totalScores / (numOfQuestions * pointValue * totalStudents) * 100),
            highScore * pointValue,
            lowScore * pointValue
        );
        saveResults(fileData, key, pointValue);
    }

    /**
     * Writes student information and student score information to Results.txt
     * @param fileData All student information
     * @param key The grading key to check against student answers
     * @param pointValue The point value of each question
     */
    static void saveResults(String[] fileData, char[] key, int pointValue) {
        System.out.println("\nResults.txt File created...");

        File results = new File("Results.txt");
        try (FileWriter fr = new FileWriter(results)) {
            for (String submission : fileData) {
                if (submission != null && submission.length() > 2) {
                    int grade = gradeQuiz(key, submission.substring(submission.indexOf(',') + 1));
                    double percentGrade = ((double) grade / key.length) * 100;
                    fr.write(
                    submission.substring(0, submission.indexOf(',')) + ","
                        + grade + "," + percentGrade + "," + (grade * pointValue) + ","
                        + getLetterGrade(percentGrade) + "\n"
                    );
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Prints the average assignment score along with high and low scores to the screen
     * @param averageScore The average score of all the assignments given
     * @param highScore The highest student score in points
     * @param lowScore The lowest student score in points
     */
    static void getAvg(String averageScore, int highScore, int lowScore) {
        System.out.println("\nAverage: " + averageScore + "% (" + getLetterGrade(Double.parseDouble(averageScore)) + ")");
        System.out.println("High Score: " + highScore);
        System.out.println("Low Score: " + lowScore);
    }

    /**
     * Displays all of the gathered student information to the screen.
     * @param studentID The WID of the given student
     * @param numberGrade The number of questions the given student got correct
     * @param percentGrade The percent score of the given student's assignment
     * @param score The point score of the given student's assignment
     * @param letterGrade The letter grade of the given student's assignment
     */
    static void displayResults(String studentID, int numberGrade, double percentGrade, int score, char letterGrade) {
        System.out.format("%-24s%-24s%-24s%-24s%-24s\n", studentID, numberGrade, percentGrade + "%", score, letterGrade);
    }

    /**
     * Returns the letter grade corresponding to the given percentGrade
     * @param percentGrade The given percentGrade
     * @return A letter grade for the given percentGrade
     */
    static char getLetterGrade(double percentGrade) {
        if (percentGrade >= 90.0) return 'A';
        if (percentGrade >= 80.0) return 'B';
        if (percentGrade >= 70.0) return 'C';
        if (percentGrade > 60.0) return 'D';
        return 'F';
    }

    /**
     * Grades a given student's answers based off the given key
     * @param key The key to check answers against
     * @param studentAnswers The student answers to check against the key
     * @return The number of questions that the student got correct
     */
    private static int gradeQuiz(char[] key, String studentAnswers) {
        int numCorrect = 0;
        for (int i = 0; i < key.length; i++) {
            if (key[i] == studentAnswers.charAt(i))
                numCorrect++;
        }
        return numCorrect;
    }

    /**
     * Creates a grading key based off of user input
     * @param numOfQuestions The number of questions that the user input
     * @return The completed key in a char array
     */
    static char[] createKey(int numOfQuestions) {
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

    /**
     * Reads all file data from the given file
     * @param nameOfFile The name of the file to collect data from
     * @return An array of all the file data, line by line
     */
    static String[] readFile(String nameOfFile) {
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

    /**
     * Returns the name of the input file if the file exists.
     * @param scnr The scanner to collect input
     * @return The name of the file if found
     */
    static String getFileInfo(Scanner scnr) {
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
