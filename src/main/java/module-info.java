module org.example.pokedex {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;


    opens org.example.pokedex to javafx.fxml;
    exports org.example.pokedex;
}