package org.example.pokedex;

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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

public class PokeViewerController
{
    @FXML
    StackPane anchor;
    private static final String INSERT_SQL = "select * from pokemon where name=?;";
    private static final String INSERT_SQL2 = "select name,photo from pokemon where id=(select evoID from pokemon where name=?);";
    @FXML
    public void initialization(String name) throws SQLException {
        try(Connection connection = MakeConnection.makeconnection();
            PreparedStatement pstmt = connection.prepareStatement(INSERT_SQL);
        PreparedStatement pstmt2 = connection.prepareStatement(INSERT_SQL2))
        {
            pstmt.setString(1,name);
            ResultSet result = pstmt.executeQuery();
            pstmt2.setString(1,name);
            ResultSet evoResult = pstmt2.executeQuery();
            if(result.next())
            {

                Button pokename = new Button();
                pokename.setText(result.getString("name"));

                Text desc = new Text();
                desc.setText(result.getString("description"));
                desc.setWrappingWidth(300);

                Button type1 = new Button();
                type1.setText(result.getString("type"));
                Button type2 = new Button();
                type2.setText(result.getString("type2"));

                Button height = new Button();
                String h = String.valueOf(result.getDouble("height"));
                h = h + " m";
                height.setText(h);

                Button weight = new Button();
                String w = String.valueOf(result.getDouble("weight"));
                w = w+" kg";
                weight.setText(w);

                byte[] imageData = result.getBytes("photo");
                ImageView viewer = new ImageView();
                viewer.setFitHeight(200);
                viewer.setFitWidth(200);
                if (imageData != null && imageData.length > 0) {
                    viewer.setImage(new Image(new ByteArrayInputStream(imageData)));

                }
                AtomicInteger current = new AtomicInteger(result.getInt("favourite"));
                String imagePath;
                if(current.get()==1)
                    imagePath = "/org/example/images/clicked.png";
                else
                    imagePath = "/org/example/images/unclicked.png";

                InputStream inputStream = getClass().getResourceAsStream(imagePath);
                ImageView viewerIcon = new ImageView();
                viewerIcon.setFitHeight(25);
                viewerIcon.setFitWidth(25);
                if (inputStream != null) {
                    Image image = new Image(inputStream);
                    viewerIcon.setImage(image);
                } else {
                    System.err.println("Failed to load image: " + imagePath);
                }
                viewerIcon.setOnMouseClicked(event -> {
                    try(Connection connection2 = MakeConnection.makeconnection();
                        PreparedStatement pstmt3 = connection2.prepareStatement("update pokemon set favourite=? where name=?;"))
                    {
                        System.out.println("CLICKED UPDATE");
                        pstmt3.setInt(1,current.get()^1);
                        pstmt3.setString(2, name);
                        pstmt3.executeUpdate();
                        String newImagePath;
                        if(((current.get())^1)==1)
                            newImagePath = "/org/example/images/clicked.png";
                        else
                            newImagePath = "/org/example/images/unclicked.png";
                        current.set(current.get()^1);
                        InputStream newInputStream = getClass().getResourceAsStream(newImagePath);
                        if (newInputStream != null) {
                            Image newImage = new Image(newInputStream);
                            viewerIcon.setImage(newImage);
                        } else {
                            System.err.println("Failed to load image: " + newImagePath);
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });





                pokename.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 25));
                desc.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 24));
                pokename.setTextFill(Color.WHITE);
                desc.setFill(Color.WHITE);
                height.setTextFill(Color.WHITE);
                weight.setTextFill(Color.WHITE);
                type1.setTextFill(Color.WHITE);
                type2.setTextFill(Color.WHITE);
                type1.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 20));
                type2.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 20));
                height.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 20));
                weight.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 20));


                pokename.setStyle("-fx-background-color: rgba(255, 255, 255, " + 0.5 + "); -fx-background-radius: 10;");
                type1.setStyle("-fx-background-color: rgba(255, 255, 255, " + 0.5 + "); -fx-background-radius: 10;");
                type2.setStyle("-fx-background-color: rgba(255, 255, 255, " + 0.5 + "); -fx-background-radius: 10;");
                weight.setStyle("-fx-background-color: rgba(255, 255, 255, " + 0.5 + "); -fx-background-radius: 10;");
                height.setStyle("-fx-background-color: rgba(255, 255, 255, " + 0.5 + "); -fx-background-radius: 10;");


                Button viewimage = new Button();
                StackPane stackPane = new StackPane();
                stackPane.getChildren().add(viewer);
                viewimage.setGraphic(stackPane);
                viewimage.setStyle("-fx-background-color: rgba(255, 255, 255, " + 0.5 + "); -fx-background-radius: 10;");
                stackPane.setMargin(viewimage, new Insets(20, 20, 0, 0));
                if(type2.getText()!=null && !type2.getText().isEmpty()) {
                    anchor.getChildren().addAll(pokename, desc, type1, type2, height, weight, viewimage,viewerIcon);
                    anchor.setAlignment(pokename, Pos.TOP_LEFT);
                    anchor.setAlignment(desc, Pos.TOP_LEFT);
                    anchor.setAlignment(type1, Pos.BOTTOM_LEFT);
                    anchor.setAlignment(type2, Pos.BOTTOM_LEFT);
                    anchor.setAlignment(height, Pos.CENTER_LEFT);
                    anchor.setAlignment(weight, Pos.CENTER_LEFT);
                    anchor.setAlignment(viewimage, Pos.TOP_RIGHT);
                    anchor.setAlignment(viewerIcon, Pos.TOP_CENTER);
                }
                else
                {
                    anchor.getChildren().addAll(pokename, desc, type1, height, weight, viewimage,viewerIcon);
                    anchor.setAlignment(pokename, Pos.TOP_LEFT);
                    anchor.setAlignment(desc, Pos.TOP_LEFT);
                    anchor.setAlignment(type1, Pos.BOTTOM_LEFT);
                    //anchor.setAlignment(type2, Pos.BOTTOM_LEFT);
                    anchor.setAlignment(height, Pos.CENTER_LEFT);
                    anchor.setAlignment(weight, Pos.CENTER_LEFT);
                    anchor.setAlignment(viewimage, Pos.TOP_RIGHT);
                    anchor.setAlignment(viewerIcon, Pos.TOP_CENTER);
                }
                anchor.setMargin(desc, new Insets(50, 0, 0, 15));
                anchor.setMargin(type1, new Insets(0, 0, 15, 10));
                anchor.setMargin(type2, new Insets(0, 0, 15, 110));
                anchor.setMargin(pokename, new Insets(4, 0, 0, 15));
                anchor.setMargin(height, new Insets(0, 0, -130, 15));
                anchor.setMargin(weight, new Insets(0, 0, -210, 15));
                anchor.setMargin(viewerIcon, new Insets(25, 0, 0, 90));
                switch (type1.getText()) {
                    case "Grass":
                        anchor.setStyle("-fx-background-color: #7AC74C; -fx-background-radius: 10;");
                        break;
                    case "Fire":
                        anchor.setStyle("-fx-background-color: #EE8130; -fx-background-radius: 10;");
                        break;
                    case "Water":
                        anchor.setStyle("-fx-background-color: #6390F0; -fx-background-radius: 10;");
                        break;
                    case "Normal":
                        anchor.setStyle("-fx-background-color: #A8A77A; -fx-background-radius: 10;");
                        break;
                    case "Ground":
                        anchor.setStyle("-fx-background-color: #E2BF65; -fx-background-radius: 10;");
                        break;
                    case "Dragon":
                        anchor.setStyle("-fx-background-color: #6F35FC; -fx-background-radius: 10;");
                        break;
                    case "Electric":
                        anchor.setStyle("-fx-background-color: #F7D02C; -fx-background-radius: 10;");
                        break;
                    // Add more cases for other types as needed
                    default:
                        anchor.setStyle("-fx-background-color: #FFFFFF;");
                        break;
                }
            }
            if(evoResult.next())
            {
                Label evolver = new Label();
                evolver.setText("Evolves to");
                evolver.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 18));
                evolver.setTextFill(Color.WHITE);
                String name2 = evoResult.getString("name");
                Button evolution = new Button();
                evolution.setOnAction(event -> {
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
                        controller.initialization(name2);
                        ((Stage) evolution.getScene().getWindow()).close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    Scene scene = new Scene(root);
                    Stage newStage = new Stage();
                    newStage.setScene(scene);
                    newStage.setTitle("PokéView");

                    newStage.show();
                });
                HBox evoBox = new HBox();
                byte[] imageData = evoResult.getBytes("photo");
                ImageView viewer = new ImageView();
                viewer.setFitHeight(100);
                viewer.setFitWidth(100);
                if (imageData != null && imageData.length > 0) {
                    viewer.setImage(new Image(new ByteArrayInputStream(imageData)));
                }
                evolution.setStyle("-fx-background-color: rgba(255, 255, 255, " + 0.5 + "); -fx-background-radius: 10;");
                evoBox.setMargin(evolver, new Insets(50, 0, 0, 0));
                evoBox.getChildren().addAll(evolver,viewer);
                evolution.setGraphic(evoBox);
                anchor.getChildren().addAll(evolution);
                anchor.setAlignment(Pos.BOTTOM_RIGHT);
                anchor.setMargin(evolution, new Insets(0, 20, 10, 0));
            }
        }
    }
}
