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
    /**
     * Text field id variable.
     */
    @FXML
    private TextField tf_id;
    /**
     * Text field title variable.
     */
    @FXML
    private TextField tf_title;
    /**
     * Text field author variable.
     */
    @FXML
    private TextField tf_author;
    /**
     * Text field year variable.
     */
    @FXML
    private TextField tf_year;
    /**
     * Text field pages variable.
     */
    @FXML
    private TextField tf_pages;

    /**
     * Table view variable for table books.
     */
    @FXML
    private TableView<Books> tv_books;

    /**
     * Table column id variable.
     */
    @FXML
    private TableColumn<Books, Integer> tc_id;
    /**
     * Table column title variable.
     */
    @FXML
    private TableColumn<Books, String> tc_title;
    /**
     * Table column author variable.
     */
    @FXML
    private TableColumn<Books, String> tc_author;
    /**
     * Table column year variable.
     */
    @FXML
    private TableColumn<Books, Integer> tc_year;
    /**
     * Table column pages variable.
     */
    @FXML
    private TableColumn<Books, Integer> tc_pages;

    /**
     * Button insert variable.
     */
    @FXML
    private Button b_insert;
    /**
     * Button delete variable.
     */
    @FXML
    private Button b_delete;
    /**
     * Button update variable.
     */
    @FXML
    private Button b_update;

    /**
     * Treats the buttons actions
     *
     * @param actionEvent gets the component source and distinguish each one of them
     */
    @FXML
    private void handleButtonAction(ActionEvent actionEvent) {
        if (actionEvent.getSource() == b_insert) {
            insertRecord();
        } else if (actionEvent.getSource() == b_update) {
            updateRecord();
        } else if (actionEvent.getSource() == b_delete) {
            deleteRecord();
        }
    }

    /**
     * Loads the text fields with the column content on mouse click.
     */
    @FXML
    public void handleMouseAction() {
        Books book = tv_books.getSelectionModel().getSelectedItem();

        tf_id.setText(String.valueOf(book.getId()));
        tf_title.setText(book.getTitle());
        tf_author.setText(book.getAuthor());
        tf_year.setText(String.valueOf(book.getYear()));
        tf_pages.setText(String.valueOf(book.getPages()));
    }

    /**
     * Establish connection to database.
     */
    private Connection connection;

    /**
     * Opens the database connection.
     *
     * @return the <code style="color: #50FA7B;">connection</code> object with the database data to be authenticated
     */
    public Connection openConnection() {
        final String USERNAME = "root";
        final String PASSWORD = "020885";
        final String URL = "jdbc:mysql://localhost:3306/librarian_db";

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            return connection;
        } catch (Exception exception) {
            System.out.println("Error opening connection: " + exception.getMessage());
            return null;
        }
    }

    /**
     * Closes the database connection.
     */
    public void closeConnection() {
        try {
            connection.close();
        } catch (Exception exception) {
            System.out.println("Error closing connection: " + exception.getMessage());
        }
    }

    /**
     * Contains the sql script that returns all table rows.
     */
    private final String SELECT_ALL = "SELECT * FROM books";

    /**
     * Gets the books list from the database and fills the <code style="color: #50FA7B;">tableView</code>
     *
     * @return the <code style="color: #50FA7B;">books</code> list
     */
    public ObservableList<Books> getBooks() {
        ObservableList<Books> books = FXCollections.observableArrayList();
        Connection connection = openConnection();

        Statement statement;
        ResultSet resultSet;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SELECT_ALL);
            Books book;
            while (resultSet.next()) {
                book = new Books(resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getInt("year"),
                        resultSet.getInt("pages"));
                books.add(book);
            }
            closeConnection();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return books;
    }

    /**
     * Fills the table and text fields with the database data.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showBooks();

        Connection connection = openConnection();

        String SELECT_FIRST = SELECT_ALL + " LIMIT 1;";

        Statement statement;
        ResultSet resultSet;
        try {
            Books book;

            statement = connection.createStatement();
            resultSet = statement.executeQuery(SELECT_FIRST);
            resultSet.next();

            book = new Books(resultSet.getInt("id"),
                    resultSet.getString("title"),
                    resultSet.getString("author"),
                    resultSet.getInt("year"),
                    resultSet.getInt("pages"));

            tf_id.setText(String.valueOf(book.getId()));
            tf_title.setText(book.getTitle());
            tf_author.setText(book.getAuthor());
            tf_year.setText(String.valueOf(book.getYear()));
            tf_pages.setText(String.valueOf(book.getPages()));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Fills the table view with the database data.
     */
    public void showBooks() {
        ObservableList<Books> booksList = getBooks();

        tc_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        tc_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        tc_author.setCellValueFactory(new PropertyValueFactory<>("author"));
        tc_year.setCellValueFactory(new PropertyValueFactory<>("year"));
        tc_pages.setCellValueFactory(new PropertyValueFactory<>("pages"));

        tv_books.setItems(booksList);
    }

    /**
     * Inserts a new record into system.
     */
    private void insertRecord() {
        String query = "INSERT INTO books VALUES (" + tf_id.getText() + ",'" + tf_title.getText() + "','" + tf_author.getText() + "'," + tf_year.getText() + "," + tf_pages.getText() + ")";
        performQuery(query);
        showBooks();
    }

    /**
     * Updates the current record of the system.
     */
    private void updateRecord() {
        String query = "UPDATE books SET title = '" + tf_title.getText() + "', author = '" + tf_author.getText() + "', year = " + tf_year.getText() + ", pages = " + tf_pages.getText() + " WHERE id = " + tf_id.getText() + ";";
        performQuery(query);
        showBooks();
    }

    /**
     * Deletes the current record from the system.
     */
    private void deleteRecord() {
        String query = "DELETE FROM books WHERE id = " + tf_id.getText() + ";";
        performQuery(query);
        showBooks();
    }

    /**
     * Executes the given query.
     *
     * @param query to be executed
     */
    private void performQuery(String query) {
        Connection connection = openConnection();
        Statement statement;

        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
            closeConnection();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}