package com.drumond.librarianproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private TextField tf_id;
    @FXML
    private TextField tf_title;
    @FXML
    private TextField tf_author;
    @FXML
    private TextField tf_year;
    @FXML
    private TextField tf_pages;

    @FXML
    private TableView<Books> tv_books;

    @FXML
    private TableColumn<Books, Integer> tc_id;
    @FXML
    private TableColumn<Books, String> tc_title;
    @FXML
    private TableColumn<Books, String> tc_author;
    @FXML
    private TableColumn<Books, Integer> tc_year;
    @FXML
    private TableColumn<Books, Integer> tc_pages;

    @FXML
    private Button b_insert;
    @FXML
    private Button b_delete;
    @FXML
    private Button b_update;

    @FXML
    private void handleButtonAction(ActionEvent actionEvent) {
        if (actionEvent.getSource() == b_insert) {
            insertRecord();
            System.out.println("Insert button clicked!");
        } else if (actionEvent.getSource() == b_update) {
            updateRecord();
            System.out.println("Update button clicked!");
        } else if (actionEvent.getSource() == b_delete) {
            deleteRecord();
            System.out.println("Delete button clicked!");
        }
    }

    public Connection openConnection() {
        Connection connection;
        final String USERNAME = "root";
        final String PASSWORD = "020885";
        final String URL = "jdbc:mysql://localhost:3306/librarian_db";

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            return connection;
        } catch (Exception exception) {
            System.out.println("Error: " + exception.getMessage());
            return null;
        }
    }

    public ObservableList<Books> getBooks() {
        ObservableList<Books> books = FXCollections.observableArrayList();
        Connection connection = openConnection();
        String QUERY = "SELECT * FROM books";
        Statement statement;
        ResultSet resultSet;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(QUERY);
            Books book;
            while (resultSet.next()) {
                book = new Books(resultSet.getInt("id"),
                                 resultSet.getString("title"),
                                 resultSet.getString("author"),
                                 resultSet.getInt("year"),
                                 resultSet.getInt("pages"));
                books.add(book);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return books;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showBooks();
    }

    public void showBooks() {
        ObservableList<Books> booksList = getBooks();

        tc_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        tc_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        tc_author.setCellValueFactory(new PropertyValueFactory<>("author"));
        tc_year.setCellValueFactory(new PropertyValueFactory<>("year"));
        tc_pages.setCellValueFactory(new PropertyValueFactory<>("pages"));

        tv_books.setItems(booksList);
    }

    private void insertRecord() {

        String query = "INSERT INTO books VALUES (" + tf_id.getText() + ",'" + tf_title.getText() + "','" + tf_author.getText() + "',"
                + tf_year.getText() + "," + tf_pages.getText() + ")";
        performQuery(query);
        showBooks();
    }

    private void updateRecord() {
        String query = "UPDATE books SET title = '" + tf_title.getText() + "', author = '" + tf_author.getText() + "', year = " + tf_year.getText() + ", pages = " + tf_pages.getText() + " WHERE id = " + tf_id.getText() + ";";
        performQuery(query);
        showBooks();
    }

    private void deleteRecord() {
        String query = "DELETE FROM books WHERE id = " + tf_id.getText() + ";";
        performQuery(query);
        showBooks();
    }

    private void performQuery(String query) {
        Connection connection = openConnection();
        Statement statement;

        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}