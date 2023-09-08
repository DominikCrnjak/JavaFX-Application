package hr.project.application.admin;

import hr.project.application.FilmApplication;
import hr.project.entities.generics.SavedChange;
import hr.project.exceptions.SerializationFileEmptyException;
import hr.project.utils.Change;
import hr.project.utils.LoggerClass;
import hr.project.utils.Serialization;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class ChangesPageController {

    @FXML
    private TableView<SavedChange<String, Change>> tableView;

    @FXML
    private TableColumn<SavedChange<String, Change>, String> typeTableColumn;

    @FXML
    private TableColumn<SavedChange<String, Change>, String> usernameTableColumn;

    @FXML
    private TableColumn<SavedChange<String, Change>, String> codeTableColumn;

    @FXML
    private TableColumn<SavedChange<String, Change>, String> roleTableColumn;
    @FXML
    private TableColumn<SavedChange<String, Change>, String> timeTableColumn;

    @FXML
    private TextField usernameField;

    @FXML
    private ComboBox<String> roleComboBox;

    private List<SavedChange<String, Change>> changes = new ArrayList<>();

    public void initialize() {
        try {
            changes = Serialization.Deserialize();
        } catch (SerializationFileEmptyException ex) {
            LoggerClass.logger.info("Serialization file is empty!", ex);
        }
        tableView.setItems(FXCollections.observableList(changes));

        typeTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getChangeInfo().getType());
        });
        usernameTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getChangeInfo().getUserName());
        });
        roleTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getChangeInfo().getUserRole());
        });
        timeTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getChangeInfo().getTime().toString());
        });
        codeTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getChangedFilm());
        });

        List<String> strings = new ArrayList<>();
        strings.add("Admin");
        strings.add("Guest");
        roleComboBox.setItems(FXCollections.observableList(strings));

    }

    @FXML
    protected void onSearchTyping() {

        List<SavedChange<String, Change>> filteredList = new ArrayList<>(changes);

        if (!usernameField.getText().isEmpty()) {
            filteredList = filteredList.stream()
                    .filter(stringChangeSavedChange -> stringChangeSavedChange.getChangeInfo().getUserName().toLowerCase().contains(usernameField.getText().toLowerCase()))
                    .toList();
        }
        if (roleComboBox.getValue() != null) {
            filteredList = filteredList.stream()
                    .filter(stringChangeSavedChange -> stringChangeSavedChange.getChangeInfo().getUserRole().toLowerCase().equals(roleComboBox.getValue().toLowerCase()))
                    .toList();
        }
        tableView.setItems(FXCollections.observableList(filteredList));
    }

    public void goBack() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FilmApplication.class.getResource("admin-page.fxml"));
            FilmApplication.setMainPage(fxmlLoader);
        } catch (RuntimeException ex) {
            LoggerClass.logger.info("Unable to load FXML file!", ex);
        }
    }

}
