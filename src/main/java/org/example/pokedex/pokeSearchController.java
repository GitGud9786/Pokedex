package org.example.pokedex;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class pokeSearchController
{
    static boolean type;
    int count;
    private static final String TYPE_SQL = "select * from pokemon where type=? or type2=?;";
    private static final String NAME_SQL = "select * from pokemon where name=?;";
    @FXML
    Button confirm_search;
    @FXML
    TextField search_name;
    @FXML
    GridPane gridSearch;

    @FXML
    public static void type_searcher(){
        type=true;
    }

    @FXML
    public static void name_searcher(){
        type=false;
    }

    @FXML
    public void on_confirm_search_clicked() throws SQLException {
        if(search_name.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input is EMPTY!");
            alert.setContentText("Please type something.");
            alert.showAndWait();
        }
        else
        {
            gridSearch.getChildren().clear();
            try (Connection connection = MakeConnection.makeconnection())
            {
                gridSearch.setHgap(10);
                gridSearch.setVgap(10);
                count=1;
                if(type)//looking for types
                {
                    PreparedStatement pstmt = connection.prepareStatement(TYPE_SQL);
                    pstmt.setString(1,search_name.getText());
                    pstmt.setString(2,search_name.getText());
                    ResultSet result = pstmt.executeQuery();
                        int rowIndex = 0;
                        int colIndex = 0;
                        String type2;
                        while (result.next()) {
                            // Extract data from the result set
                            String name = result.getString("name");
                            String type1 = result.getString("type");
                            type2 = result.getString("type2");
                            byte[] imageData = result.getBytes("photo");

                            // Create a button with the retrieved data
                            Button pokemonButton = HelloController.createPokemonButton(name, type1, type2, imageData,count);
                            count++;
                            // Add the button to the grid pane
                            gridSearch.add(pokemonButton, colIndex, rowIndex);

                            // Increment the column index and move to the next row if necessary
                            colIndex++;
                            if (colIndex >= 5) {
                                colIndex = 0;
                                rowIndex++;
                            }
                    }
                }
                else
                {
                    PreparedStatement pstmt = connection.prepareStatement(NAME_SQL);
                    pstmt.setString(1,search_name.getText());
                    ResultSet result = pstmt.executeQuery();
                        int rowIndex = 0;
                        int colIndex = 0;
                        String type2;
                        while (result.next()) {
                            // Extract data from the result set
                            String name = result.getString("name");
                            String type1 = result.getString("type");
                            type2 = result.getString("type2");
                            byte[] imageData = result.getBytes("photo");

                            // Create a button with the retrieved data
                            Button pokemonButton = HelloController.createPokemonButton(name, type1, type2, imageData,count);
                            count++;
                            // Add the button to the grid pane
                            gridSearch.add(pokemonButton, colIndex, rowIndex);

                            // Increment the column index and move to the next row if necessary
                            colIndex++;
                            if (colIndex >= 5) {
                                colIndex = 0;
                                rowIndex++;
                            }
                    }
                }
            }
        }
    }
}
