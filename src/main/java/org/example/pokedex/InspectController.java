package org.example.pokedex;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InspectController
{
    @FXML
    AnchorPane anchorpane;
    @FXML
    private TextField searcher;
    @FXML
    private Button search_button;
    private static final String NAME_SQL = "select * from pokemon where name=?;";
    @FXML
    public void on_search_clicked() throws SQLException {
        try (Connection connection = MakeConnection.makeconnection()) {
            PreparedStatement pstmt = connection.prepareStatement(NAME_SQL);
            pstmt.setString(1,searcher.getText());
            ResultSet result = pstmt.executeQuery();
            if(result.next())
            {
                int count=1;
                String type2;
                String name = result.getString("name");
                String type1 = result.getString("type");
                type2 = result.getString("type2");
                byte[] imageData = result.getBytes("photo");

                // Create a button with the retrieved data
                Button pokemonButton = HelloController.createPokemonButton(name, type1, type2, imageData,count);
                anchorpane.getChildren().add(pokemonButton);

                // Center the button horizontally
                AnchorPane.setLeftAnchor(pokemonButton, 191.00);

                AnchorPane.setTopAnchor(pokemonButton, 80.00);


            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("DISSAPOINTED PIKACHU!");
                alert.setHeaderText("Pikachu is sad because it could not find your Pokémon.");
                alert.getButtonTypes().setAll(ButtonType.OK);
                InputStream inputStream = getClass().getResourceAsStream("/org/example/images/cry.gif");
                Image image = new Image(inputStream);

                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);

                // Create DialogPane
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.setGraphic(imageView);

                alert.showAndWait();
            }
        }
    }
}
