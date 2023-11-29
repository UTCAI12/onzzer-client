package fr.utc.onzzer.client.hmi.component;

import javafx.scene.control.Button;
import javafx.scene.layout.Region;

public class IconButton extends Button {
    public static final String ICON_DOWNLOAD = "M480-320 280-520l56-58 104 104v-326h80v326l104-104 56 58-200 200ZM240-160q-33 0-56.5-23.5T160-240v-120h80v120h480v-120h80v120q0 33-23.5 56.5T720-160H240Z";

    public IconButton(String shape) {
        this(shape, 20);
    }

    public IconButton(String shape, double size) {
        super();

        getStyleClass().add("icon-button");
        setPickOnBounds(true);

        Region icon = new Region();
        icon.setStyle("-fx-shape: \"" + shape + "\"; -icon-size: " + size + ";");
        icon.getStyleClass().add("icon");
        setGraphic(icon);
    }
}
