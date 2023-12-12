package fr.utc.onzzer.client.hmi.component;

import javafx.scene.control.Button;
import javafx.scene.layout.Region;

public class IconButton extends Button {
    private static final double PADDING = 12;
    public static final String ICON_DOWNLOAD = "M480-320 280-520l56-58 104 104v-326h80v326l104-104 56 58-200 200ZM240-160q-33 0-56.5-23.5T160-240v-120h80v120h480v-120h80v120q0 33-23.5 56.5T720-160H240Z";

    public static final String ICON_EVALUATE = "M200-120q-33 0-56.5-23.5T120-200v-560q0-33 23.5-56.5T200-840h357l-80 80H200v560h560v-278l80-80v358q0 33-23.5 56.5T760-120H200Zm280-360ZM360-360v-170l367-367q12-12 27-18t30-6q16 0 30.5 6t26.5 18l56 57q11 12 17 26.5t6 29.5q0 15-5.5 29.5T897-728L530-360H360Zm481-424-56-56 56 56ZM440-440h56l232-232-28-28-29-28-231 231v57Zm260-260-29-28 29 28 28 28-28-28Z";

    public static final String ICON_DELETE = "M280-120q-33 0-56.5-23.5T200-200v-520h-40v-80h200v-40h240v40h200v80h-40v520q0 33-23.5 56.5T680-120H280Zm400-600H280v520h400v-520ZM360-280h80v-360h-80v360Zm160 0h80v-360h-80v360ZM280-720v520-520Z";

    public static final String ICON_LISTENING = "m380-300 280-180-280-180v360ZM480-80q-83 0-156-31.5T197-197q-54-54-85.5-127T80-480q0-83 31.5-156T197-763q54-54 127-85.5T480-880q83 0 156 31.5T763-763q54 54 85.5 127T880-480q0 83-31.5 156T763-197q-54 54-127 85.5T480-80Zm0-80q134 0 227-93t93-227q0-134-93-227t-227-93q-134 0-227 93t-93 227q0 134 93 227t227 93Zm0-320Z";
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
