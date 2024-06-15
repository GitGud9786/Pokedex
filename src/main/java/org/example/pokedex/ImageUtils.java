package org.example.pokedex;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImageUtils {

    public static byte[] readImageAsBytes(String imagePath) throws IOException, IOException {
        return Files.readAllBytes(Paths.get(imagePath));
    }
}