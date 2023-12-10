package fr.utc.onzzer.client.hmi.component;

import javafx.scene.control.Button;
import javafx.scene.layout.Region;

public class IconButton extends Button {
    private static final double PADDING = 12;
    public static final String ICON_DOWNLOAD = "M480-320 280-520l56-58 104 104v-326h80v326l104-104 56 58-200 200ZM240-160q-33 0-56.5-23.5T160-240v-120h80v120h480v-120h80v120q0 33-23.5 56.5T720-160H240Z";

    public static final String ICON_EVALUATE = "M18 2H6a3 3 0 0 0-3 3v14a1 1 0 0 0 1 1h1a1 1 0 0 0 1-1v-2h10v2a1 1 0 0 0 1 1h1a1 1 0 0 0 1-1V5a3 3 0 0 0-3-3zm1 3a1 1 0 0 1 1 1v1.39l-4.7 4.7a1 1 0 0 0-.3 0L7.41 15L16 6.41V5a1 1 0 0 1 1-1zM6.71 16L12 10.71L17.29 16z";

    public static final String ICON_DELETE = "M6 4a1 1 0 0 1 1-1h10a1 1 0 0 1 1 1v1h2a1 1 0 0 1 1 1v1a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V6a1 1 0 0 1 1-1h2V4zm14 2H4v10a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1V6zM8 14a1 1 0 0 1 2 0v2a1 1 0 0 1-2 0v-2zm6 0a1 1 0 0 1 2 0v2a1 1 0 0 1-2 0v-2z";
    public IconButton(String shape) {
        this(shape, 20);
    }

    public IconButton(String shape, double size) {
        super();

        setHeight(size + PADDING);
        setWidth(size + PADDING);
        setMinHeight(size + PADDING);
        setMinWidth(size + PADDING);
        setMaxHeight(size + PADDING);
        setMaxWidth(size + PADDING);

        getStyleClass().add("icon-button");
        setPickOnBounds(true);

        Region icon = new Region();
        icon.setStyle("-fx-shape: \"" + shape + "\"; -icon-size: " + size + ";");
        icon.getStyleClass().add("icon");
        setGraphic(icon);
    }
}
