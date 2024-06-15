package org.example.pokedex;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.InputStream;

public class ImageShower extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create an ImageView to display the image
        ImageView imageView = new ImageView();

        try {
            // Load the image from the resources directory
            InputStream inputStream = getClass().getResourceAsStream("/org/example/images/bulbasaur.jpg");
            Image image = new Image(inputStream);

            // Set the image to the ImageView
            imageView.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create a StackPane to hold the ImageView
        StackPane root = new StackPane();
        root.getChildren().add(imageView);

        // Create a Scene with StackPane as root node
        Scene scene = new Scene(root, 400, 400);

        // Set the title of the Stage and add the Scene to it
        primaryStage.setTitle("Image View Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
