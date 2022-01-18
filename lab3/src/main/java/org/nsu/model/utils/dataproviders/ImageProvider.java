package org.nsu.model.utils.dataproviders;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Objects;
import java.util.Properties;

public enum ImageProvider {
    APP_LOGO,
    SEARCH_ICON;

    private static ImageProvider imageProvider;
    private Properties images;
    private Image image;

    private void initImage() {
        if (images == null) {
            images = PropertiesProvider.getProperties("images.properties");
        }
        image = new Image(ImageProvider.class.getClassLoader().getResourceAsStream("img/" + images.get("IMG_" + this.name())));
    }

    public Image getImage() {
        if (image == null) {
            initImage();
        }
        return image;
    }
}
