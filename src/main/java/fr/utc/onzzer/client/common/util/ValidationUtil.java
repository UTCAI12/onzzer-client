package fr.utc.onzzer.client.common.util;

import javafx.scene.Node;

import java.util.Set;

public class ValidationUtil {

    public static void hideErrors(Node parent) {

        Set<Node> groups = parent.lookupAll(".input-group");
        groups.forEach(ValidationUtil::hideError);
    }

    public static void hideError(Node container) {

        Node node = container.lookup(".error");

        // No error message found.
        if(node == null) return;

        // Hiding error message.
        node.setVisible(false);
        node.setManaged(false);
    }

    public static void showError(Node container) {

        Node node = container.lookup(".error");

        // No error message found.
        if(node == null) return;

        // Displaying error message.
        node.setVisible(true);
        node.setManaged(true);
    }

    public static boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }
}
