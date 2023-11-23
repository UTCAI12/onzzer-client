module fr.utc.onzzer.onzzerclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires onzzer.common;

    opens fr.utc.onzzer.onzzerclient to javafx.fxml;
    opens fr.utc.onzzer.onzzerclient.main to javafx.fxml;
    exports fr.utc.onzzer.client.main;
    opens fr.utc.onzzer.client.main to javafx.fxml;
    exports fr.utc.onzzer.client;
    opens fr.utc.onzzer.client to javafx.fxml;
}