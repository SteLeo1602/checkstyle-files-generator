package org.example;

import java.io.File;

import java.nio.file.Files;

public class Main {

    public static String ABSOLUTE_PATH_TO_CHECKSTYLE;

    public static void main(String[] args) throws Exception {

        if (args.length == 0) {
            System.out.println("Please specify path to checkstyle repository");
            System.exit(0);
        }
        ABSOLUTE_PATH_TO_CHECKSTYLE = args[0];

        System.out.println("Absolute path: " + args[0]);

        File temporaryFolder = Files.createTempDirectory(null).toFile();

        XdocGenerator.generateXdocContent(temporaryFolder);
    }


}
