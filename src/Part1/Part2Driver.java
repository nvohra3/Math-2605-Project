package Part1;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Part2Driver {
    static Scanner in = new Scanner(System.in);
    static Matrix matrix;
    static int input;
    static String path;
    static Matrix[] list;

    public static void main(String[] args) throws FileNotFoundException {
        // Decide how to round decimals in matrix; to what extent must error be calculated?
        System.out.println("Welcome to part 2 of the project.");
        while (true) {
            System.out.println("Select an option.");
            System.out.println("1 - Solve Ax=b using Gauss-Seidel in [A|b] form read from file.");
            System.out.println("2 - Solve Ax=b using Jacobi in [A|b] form read from file.");
            System.out.println("3 - Produce output stream Y0 with input stream X read from file.");
            System.out.println("4 - Produce output stream Y0 with randomly generated input stream X.");
            System.out.println("5 - Produce output stream Y1 with input stream X read from file.");
            System.out.println("6 - Produce output stream Y1 with randomly generated input stream X.");
            System.out.println("7 - Produce full output stream Y with input stream X read from file.");
            System.out.println("8 - Produce full output stream Y with randomly generated input stream X.");
            System.out.println("9 - Produce input stream X with output stream Y0 read from file using Gauss-Seidel.");
            System.out.println("10 - Produce input stream X with output stream Y1 read from file using Gauss-Seidel.");
            System.out.println("11 - Produce input stream X with randomly generated output stream Y using Gauss-Seidel.");
            System.out.println("12 - Produce input stream X with output stream Y0 read from file using Jacobi.");
            System.out.println("13 - Produce input stream X with output stream Y1 read from file using Jacobi.");
            System.out.println("14 - Produce input stream X with randomly generated output stream Y using Jacobi.");
            input = in.nextInt();
            System.out.println("Enter file path. If a file path is not required, just enter any character to move on.");
            path = in.next();
            switch (input) {
                case 1:
                    Matrix[] augmented = FileParser.parseFileWithB(path);
                    System.out.println("A:\n" + augmented[0]);
                    caseOne(augmented);
                    break;
                case 2:
                    Matrix[] augmented2 = FileParser.parseFileWithB(path);
                    System.out.println("A:\n" + augmented2[0]);
                    caseTwo(augmented2);
                    break;
                case 3:
                    Matrix augmented3 = FileParser.parseVectorFile(path);
                    caseThree(augmented3);
                    break;
                case 4:
                    caseFour();
                    break;
                case 5:
                    Matrix augmented5 = FileParser.parseVectorFile(path);
                    caseFive(augmented5);
                    break;
                case 6:
                    caseSix();
                    break;
                case 7:
                    Matrix augmented7 = FileParser.parseVectorFile(path);
                    caseSeven(augmented7);
                    break;
                case 8:
                    caseEight();
                    break;
                case 9:
                    Matrix augmented9 = FileParser.parseVectorFile(path);
                    caseNine(augmented9);
                    break;
                case 10:
                    Matrix augmented10 = FileParser.parseVectorFile(path);
                    caseTen(augmented10);
                    break;
                case 11:
                    caseEleven();
                    break;
                case 12:
                    Matrix augmented12 = FileParser.parseVectorFile(path);
                    caseTwelve(augmented12);
                    break;
                case 13:
                    Matrix augmented13 = FileParser.parseVectorFile(path);
                    caseThirteen(augmented13);
                    break;
                case 14:
                    caseFourteen();
                    break;
            }
        }
    }

    public static void caseOne(Matrix[] list) {
        Matrix A = list[0];
        Matrix b = list[1];
        Matrix X = b.generateInitial();
        float tol = (float) .000000001;
        System.out.println("Tolerance is: "+tol);
        Matrix result = A.gauss_seidel(b, X, tol);
        System.out.println(result);
    }

    public static void caseTwo(Matrix[] list) {
        Matrix A = list[0];
        Matrix b = list[1];
        Matrix X = b.generateInitial();
        float tol = (float) .000000001;
        System.out.println("Tolerance is: "+tol);
        Matrix result = A.jacobi(b, X, tol);
        System.out.println(result);
    }

    public static void caseThree(Matrix matrix) {
        Matrix X = matrix;
        X = X.addZeros();
        System.out.println("X:\n" + X);
        Matrix A0 = X.getA0();
        Matrix Y0 = A0.multiplyMod(X);
        System.out.println("Y0:\n" + Y0);
    }

    public static void caseFour() {
        System.out.println("Please enter desired length of X");
        Matrix X = Matrix.getXStream(in.nextInt());
        System.out.println("X:\n" + X);
        Matrix A0 = X.getA0();
        Matrix Y0 = A0.multiplyMod(X);
        System.out.println("Y0:\n" + Y0);
    }

    public static void caseFive(Matrix matrix) {
        Matrix X = matrix;
        X = X.addZeros();
        System.out.println("X:\n" + X);
        Matrix A1 = X.getA1();
        Matrix Y1 = A1.multiplyMod(X);
        System.out.println("Y1:\n" + Y1);
    }

    public static void caseSix() {
        System.out.println("Please enter desired length of X");
        Matrix X = Matrix.getXStream(in.nextInt());
        System.out.println("X:\n" + X);
        Matrix A1 = X.getA1();
        Matrix Y1 = A1.multiplyMod(X);
        System.out.println("Y1:\n" + Y1);
    }

    public static void caseSeven(Matrix matrix) {
        Matrix X = matrix;
        X = X.addZeros();
        System.out.println("X:\n" + X);
        Matrix A0 = X.getA0();
        Matrix Y0 = A0.multiplyMod(X);
        Matrix A1 = X.getA1();
        Matrix Y1 = A1.multiplyMod(X);
        String[][] result = Matrix.combineY(Y0, Y1);
        System.out.println("Y:");
        for(int i = 0; i < result.length; i++){
            System.out.println(result[i][0]);
        }
    }

    public static void caseEight() {
        System.out.println("Please enter desired length of X");
        Matrix X = Matrix.getXStream(in.nextInt());
        System.out.println("X:\n" + X);
        Matrix A0 = X.getA0();
        Matrix Y0 = A0.multiplyMod(X);
        Matrix A1 = X.getA1();
        Matrix Y1 = A1.multiplyMod(X);
        String[][] result = Matrix.combineY(Y0, Y1);
        System.out.println("Y:");
        for(int i = 0; i < result.length; i++){
            System.out.println(result[i][0]);
        }

    }

    public static void caseNine(Matrix matrix) {
        Matrix Y = matrix;
        System.out.println("Y:\n" + Y);
        Matrix A = Y.getA0();
        Matrix guess = Y.generateInitial();
        float tol = (float) .000000001;
        System.out.println("Tolerance is: "+tol);
        Matrix X = A.gauss_seidel(Y,guess, tol);
        System.out.println("X:\n" + X);
    }

    public static void caseTen(Matrix matrix) {
        Matrix Y = matrix;
        System.out.println("Y:\n" + Y);
        Matrix A = Y.getA1();
        Matrix guess = Y.generateInitial();
        float tol = (float) .000000001;
        System.out.println("Tolerance is: "+tol);
        Matrix X = A.gauss_seidel(Y,guess, tol);
        System.out.println("X:\n" + X);
    }

    public static void caseEleven() {
        System.out.println("Please enter desired length of Y");
        Matrix Y = Matrix.getYStream(in.nextInt());
        System.out.println("Y:\n" + Y);
        System.out.println("Please choose which A you would like to use:");
        System.out.println("1 - A0");
        System.out.println("2 - A1");
        Matrix A =null;
        if(in.nextInt() == 1){
            System.out.println("Using A0:");
            A = Y.getA0();
            System.out.println(A);
        }else{
            System.out.println("Using A1:");
            A = Y.getA1();
            System.out.println(A);
        }
        Matrix guess = Y.generateInitial();
        float tol = (float) .000000001;
        System.out.println("Tolerance is: "+tol);
        Matrix X = A.gauss_seidel(Y, guess, tol);
        System.out.println("X:\n" + X);
    }

    public static void caseTwelve(Matrix matrix) {
        Matrix Y = matrix;
        System.out.println("Y:\n" + Y);
        Matrix A = Y.getA0();
        Matrix guess = Y.generateInitial();
        float tol = (float) .000000001;
        System.out.println("Tolerance is: "+tol);
        Matrix X = A.jacobi(Y, guess, tol);
        System.out.println("X:\n" + X);
    }

    public static void caseThirteen(Matrix matrix) {
        Matrix Y = matrix;
        System.out.println("Y:\n" + Y);
        Matrix A = Y.getA1();
        Matrix guess = Y.generateInitial();
        float tol = (float) .000000001;
        System.out.println("Tolerance is: "+tol);
        Matrix X = A.jacobi(Y, guess, tol);
        System.out.println("X:\n" + X);
    }

    public static void caseFourteen() {
        System.out.println("Please enter desired length of Y");
        Matrix Y = Matrix.getYStream(in.nextInt());
        System.out.println("Y:\n" + Y);
        System.out.println("Please choose which A you would like to use:");
        System.out.println("1 - A0");
        System.out.println("2 - A1");
        Matrix A =null;
        if(in.nextInt() == 1){
            System.out.println("Using A0:");
            A = Y.getA0();
            System.out.println(A);
        }else{
            System.out.println("Using A1:");
            A = Y.getA1();
            System.out.println(A);
        }
        Matrix guess = Y.generateInitial();
        float tol = (float) .000000001;
        System.out.println("Tolerance is: "+tol);
        Matrix X = A.jacobi(Y, guess, tol);
        System.out.println("X:\n" + X);
    }


}