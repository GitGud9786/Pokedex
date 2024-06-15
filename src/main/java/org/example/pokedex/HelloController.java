package org.example.pokedex;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class HelloController {

    private static final String INSERT_SQL = "select * from pokemon;";
    ResultSet results;
    @FXML
    private GridPane gridpane;
    @FXML
    private Label val;

    @FXML
    public void initialize() throws SQLException {
        int count = 1;
        gridpane.setHgap(10);
        gridpane.setVgap(10);
        try (Connection connection = MakeConnection.makeconnection();
             PreparedStatement pstmt = connection.prepareStatement(INSERT_SQL)) {
            results = pstmt.executeQuery();

            /*if (results.next()) {
                // The result set contains at least one row
                System.out.println("Results contain some values.");
            } else {
                // The result set is empty
                System.out.println("Results are empty.");
            }*/
            int rowIndex = 0;
            int colIndex = 0;
            String type2;
            while (results.next()) {
                // Extract data from the result set
                String name = results.getString("name");
                String type1 = results.getString("type");
                type2 = results.getString("type2");
                byte[] imageData = results.getBytes("photo");

                // Create a button with the retrieved data
                Button pokemonButton = createPokemonButton(name, type1, type2, imageData,count);
                count++;
                // Add the button to the grid pane
                gridpane.add(pokemonButton, colIndex, rowIndex);

                // Increment the column index and move to the next row if necessary
                colIndex++;
                if (colIndex >= 5) {
                    colIndex = 0;
                    rowIndex++;
                }
            }
        }
    }

    static Button createPokemonButton(String name, String type1, String type2, byte[] imageData, int count) {
        Button pokemon_button = new Button();

        pokemon_button.setOnAction(new EventHandler<ActionEvent>() {
                                       @Override
                                       public void handle(ActionEvent event) {
                                           FXMLLoader loader = new FXMLLoader(getClass().getResource("pokeViewer.fxml"));

                                           // Load the FXML content into a Parent object
                                           Parent root = null;
                                           try {
                                               root = loader.load();
                                           } catch (IOException e) {
                                               throw new RuntimeException(e);
                                           }
                                            PokeViewerController controller = loader.getController();
                                           try {
                                               controller.initialization(name);
                                           } catch (SQLException e) {
                                               throw new RuntimeException(e);
                                           }

                                           Scene scene = new Scene(root);
                                           Stage newStage = new Stage();
                                           newStage.setScene(scene);
                                           newStage.setTitle("PokéView");
                                           newStage.setResizable(false);
                                           newStage.show();
                                       }

        });


        pokemon_button.setPrefWidth(256);
        pokemon_button.setPrefHeight(240);

        ImageView viewer = new ImageView();
        viewer.setFitHeight(150);
        viewer.setFitWidth(150);
        if (imageData != null && imageData.length > 0) {
            viewer.setImage(new Image(new ByteArrayInputStream(imageData)));

        }
        Label namelabel = new Label();
        /*Label type1Label = new Label();
        Label type2Label = new Label();*/
        Label countlabel = new Label();
        countlabel.setText("#" + count);
        /*namelabel.setText(name);
        type1Label.setText(type1);
        type2Label.setText(type2);*/

        Button type1Label = new Button();
        Button type2Label = new Button();
        //Button countlabel = new Button();
        countlabel.setText("#" + count);
        namelabel.setText(name);
        type1Label.setText(type1);
        type2Label.setText(type2);


        namelabel.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 22));
        type1Label.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 18));
        type2Label.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 18));
        countlabel.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 18));

        namelabel.setTextFill(Color.WHITE);
        type1Label.setTextFill(Color.WHITE);
        type2Label.setTextFill(Color.WHITE);
        countlabel.setTextFill(Color.BLACK);

        namelabel.setStyle("-fx-padding: 15px;");
        countlabel.setStyle("-fx-padding: 15px;");

        StackPane stackPane = new StackPane();
        if (Objects.equals(type2, "") || type2==null) {
            stackPane.getChildren().addAll(viewer, namelabel, type1Label, countlabel);
            StackPane.setAlignment(viewer, Pos.CENTER_RIGHT);
            StackPane.setAlignment(namelabel, Pos.TOP_LEFT);
            StackPane.setAlignment(type1Label, Pos.BOTTOM_LEFT);
            StackPane.setAlignment(countlabel, Pos.TOP_RIGHT);
            StackPane.setMargin(type1Label, new Insets(0, 0, 10, 0));
        }
        else
        {
            stackPane.getChildren().addAll(viewer, namelabel, type1Label,type2Label, countlabel);
            StackPane.setAlignment(viewer, Pos.CENTER_RIGHT);
            StackPane.setAlignment(namelabel, Pos.TOP_LEFT);
            StackPane.setAlignment(type1Label, Pos.BOTTOM_LEFT);
            StackPane.setAlignment(type2Label, Pos.BOTTOM_LEFT);
            StackPane.setAlignment(countlabel, Pos.TOP_RIGHT);
            StackPane.setMargin(type1Label, new Insets(0, 0, 50, 0));
            StackPane.setMargin(type2Label, new Insets(0, 0, 10, 0));
        }
        type1Label.setStyle("-fx-background-color: rgba(255, 255, 255, " + 0.5 + "); -fx-background-radius: 10;");
        type2Label.setStyle("-fx-background-color: rgba(255, 255, 255, " + 0.5 + "); -fx-background-radius: 10;");
        switch (type1) {
            case "Grass":
                pokemon_button.setStyle("-fx-background-color: #7AC74C; -fx-background-radius: 10;");
                break;
            case "Fire":
                pokemon_button.setStyle("-fx-background-color: #EE8130; -fx-background-radius: 10;");
                break;
            case "Water":
                pokemon_button.setStyle("-fx-background-color: #6390F0; -fx-background-radius: 10;");
                break;
            case "Normal":
                pokemon_button.setStyle("-fx-background-color: #A8A77A; -fx-background-radius: 10;");
                break;
            case "Ground":
                pokemon_button.setStyle("-fx-background-color: #E2BF65; -fx-background-radius: 10;");
                break;
            case "Dragon":
                pokemon_button.setStyle("-fx-background-color: #6F35FC; -fx-background-radius: 10;");
                break;
            case "Electric":
                pokemon_button.setStyle("-fx-background-color: #F7D02C; -fx-background-radius: 10;");
                break;
            // Add more cases for other types as needed
            default:
                pokemon_button.setStyle("-fx-background-color: #FFFFFF;");
                break;
        }
        pokemon_button.setGraphic(stackPane);


        pokemon_button.setOnMouseEntered(e -> {
            pokemon_button.setScaleX(1.1);
            pokemon_button.setScaleY(1.1);
        });

        pokemon_button.setOnMouseExited(e -> {
            pokemon_button.setScaleX(1.0);
            pokemon_button.setScaleY(1.0);
        });
        return pokemon_button;
    }

    @FXML
    public void on_button_clicked(ActionEvent event)
    {
        Button clickedButton = (Button) event.getSource();
        if(clickedButton.getText().equals("PokéSearch [NAME]")) {
            try {
                pokeSearchController.name_searcher();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("pokemonInspect.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.setTitle("SEARCH A NAME!");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            try {
                pokeSearchController.type_searcher();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("pokeSearcher.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.setTitle("SEARCH ANY TYPE!");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
