package com.example.joinlines;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.geom.Line2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HelloApplication extends Application {
    private static final String INPUT_FILE = "src/main/resources/input56dad8.txt";

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        List<Line2D> lines = new ArrayList<>();

        // Read input file contents and create a list of lines
        File file = new File(INPUT_FILE);

        Scanner scanner;
        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String[] lineParameters = scanner.nextLine().split(" ");
                lines.add(new Line2D.Double(
                                Double.parseDouble(lineParameters[0]),
                                Double.parseDouble(lineParameters[1]),
                                Double.parseDouble(lineParameters[2]),
                                Double.parseDouble(lineParameters[3])
                        )
                );
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LineJoiner lineJoiner = new LineJoiner(lines);
        lineJoiner.createListOfPolylines();


        launch();
    }
}