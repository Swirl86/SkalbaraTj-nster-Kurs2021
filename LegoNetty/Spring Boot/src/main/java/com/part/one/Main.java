package com.part.one;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class Main {

    private static final List<Integer> nodesFromFile = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException {

        /* To start all ports from file, but can't see separate printouts/terminals */
        getNrOfNodes();

        for (Integer port : nodesFromFile) {
            SpringApplication app = new SpringApplication(Main.class);
            System.out.println("Listening to port: " + port);
            app.setDefaultProperties(Collections
                    .singletonMap("server.port", port));
            app.run(args);
        }

        /* Start 4 different instances/terminals of main on different ports (same as in port file) */
        //SpringApplication.run(Main.class, args);

        /* Add run/debug configurations <- start every config separately on its own port */
        /* Edit configurations
         *  Create 4 new configurations with environment variables server.port=PORT_FILE_NUMBER
         *  -> PORT_FILE_NUMBER match the once in ports.txt <-
         *  Make sure run option "Allow multiple instances" is checked
         */
    }

    public static void getNrOfNodes() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("ports.txt"));
        while (scanner.hasNextInt()) {
            nodesFromFile.add(scanner.nextInt());
        }
        scanner.close();
    }
}
