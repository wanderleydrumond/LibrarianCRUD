module com.drumond.librarianproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires static lombok;

    opens com.drumond.librarianproject to javafx.fxml;
    exports com.drumond.librarianproject;
}