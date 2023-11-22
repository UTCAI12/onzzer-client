module fr.utc.onzzer.onzzerclient {
    requires javafx.controls;
    requires javafx.fxml;

    opens fr.utc.onzzer.onzzerclient to javafx.fxml;
    opens fr.utc.onzzer.onzzerclient.main to javafx.fxml;
    exports fr.utc.onzzer.onzzerclient;
    exports fr.utc.onzzer.onzzerclient.main;
}