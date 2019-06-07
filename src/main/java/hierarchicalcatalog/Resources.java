package hierarchicalcatalog;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

public class Resources {

    private static final String[] imageNamesList = {
            "logo",
            "add",
            "remove",
            "dir_open",
            "dir"
    };

    private HashMap<String, Image> imageMap = new HashMap<>();

    public Resources() {
        ClassLoader classLoader = getClass().getClassLoader();
        Image image;
        for (String name : imageNamesList) {
            try {
                image = ImageIO.read(classLoader.getResourceAsStream("images/" + name + ".png"));
            } catch (IOException e) {
                image = null;
            }
            imageMap.put(name, image);
        }
    }

    public Image getImage(String name) {
        return imageMap.get(name);
    }

    public ImageIcon getImageIcon(String name) {
        Image image;
        image = imageMap.get(name);
        if (image == null) return null;
        return new ImageIcon(image);
    }

}
