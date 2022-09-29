module com.drumond.librarianproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.drumond.librarianproject to javafx.fxml;
    exports com.drumond.librarianproject;
}