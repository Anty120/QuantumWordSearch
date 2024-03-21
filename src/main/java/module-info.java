module quantumwordsearch {
    requires transitive javafx.graphics;
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.media;
    requires java.desktop;

    opens quantumwordsearch.view to javafx.fxml;
    exports quantumwordsearch.view;
    exports quantumwordsearch.model;
}
