package de.tum.in.ase.eist;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ExamSystem {

    private ExamSystem() {
    }

    // TODO 5: Change signature, make use of the bridge pattern
    public static String hashFile(String document, Hashing hashing) {
        return hashing.hashDocument(document);
    }

    public static void main(String[] args) {
        String file1 = readFile("exams/short_exam.txt");
        String file2 = readFile("exams/long_exam.txt");

        //This file is too big for Preview Hashing

        // TODO 6: Change SimpleHash to PreviewHashing
        Hashing hashing1 = new PreviewHashing();

        System.out.println(hashFile(file1, hashing1));
        try {
            System.out.println(hashFile(file2, hashing1));
            throw new IllegalStateException("Hashing this file with preview hashing should not work!");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        // TODO 6: Change CryptoSecureHashAlgorithm to EnterpriseHashing
        Hashing hashing2 = new EnterpriseHashing();

        System.out.println(hashFile(file1, hashing2));
        System.out.println(hashFile(file2, hashing2));
    }

    public static String readFile(String filepath) {
        Path path = Path.of(filepath);
        // TODO 4: Return the content of the passed file as a String.
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
