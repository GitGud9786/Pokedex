package org.example.pokedex;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

public class DataInsert {
    @FXML
    private ImageView imageView;

    //private static final String DB_URL = "jdbc:sqlite:pokedex.db";
    private static final String INSERT_SQL = "INSERT INTO pokemon (name, image, description, type, height, weight,evolution_id) VALUES (?, ?, ?, ?, ?, ?,?)";

    public static void insertData() {
        try {
            byte[] imageData = readImageAsBytes("/org/example/images/blastoise.png");

            try (Connection connection = MakeConnection.makeconnection();
                 PreparedStatement pstmt = connection.prepareStatement(INSERT_SQL)) {

                pstmt.setString(1, "Blastoise");
                pstmt.setBytes(2, imageData);
                pstmt.setString(3, "Blastoise is a large, bipedal, reptilian Pokémon. It has a blue body with small purple eyes, a light brown belly, and a stubby tail");
                pstmt.setString(4, "Water");
                pstmt.setDouble(5, 1.6);
                pstmt.setDouble(6, 85.5);
                pstmt.setObject(7,null);
                pstmt.executeUpdate();

                System.out.println("Data inserted successfully.");

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public static Image loadImageFromDatabase(String pokemonName) {
        Image image=null;

        try (Connection connection = MakeConnection.makeconnection();
             PreparedStatement pstmt = connection.prepareStatement("SELECT image FROM pokemon WHERE name = ?")) {

            pstmt.setString(1, pokemonName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                byte[] imageData = rs.getBytes("image");
                try {
                    image = new Image(new ByteArrayInputStream(imageData));
                    System.out.println("Image created successfully");  // Debugging line
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Failed to create Image");  // Debugging line
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return image;
    }

    private static byte[] readImageAsBytes(String imagePath) throws IOException {
        return Files.readAllBytes(Paths.get("E:\\IUT porashona\\CSE time\\hello world\\Semester 04\\CSE 4402\\Pokedex\\src\\main\\resources" + imagePath));
    }
}
