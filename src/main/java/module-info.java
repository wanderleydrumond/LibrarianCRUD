/**
 * <p>Simple project to understand how JavaFX works.</p>
 * <p>Example of app able to perform CRUD operations through JDBC using MySQL as database</p>
 */
module com.drumond.librarianproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires static lombok;

    opens com.drumond.librarianproject to javafx.fxml;
    exports com.drumond.librarianproject;
}