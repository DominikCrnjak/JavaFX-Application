package hr.project.application.admin;

import hr.project.application.FilmApplication;
import hr.project.entities.film.Film;
import hr.project.entities.film.Genre;
import hr.project.entities.generics.SavedChange;
import hr.project.entities.person.User;
import hr.project.exceptions.DatabaseException;
import hr.project.utils.Change;
import hr.project.utils.Database;
import hr.project.utils.LoggerClass;
import hr.project.utils.Serialization;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdminEditFilmsController {
    private List<Film> filmList = new ArrayList<>();

    private List<Genre> genres = new ArrayList<>();

    private List<SavedChange<String, Change>> changes = new ArrayList<>();

    @FXML
    private TableView<Film> tableView;

    @FXML
    private TableColumn<Film, String> titleTableColumn;

    @FXML
    private TableColumn<Film, String> releaseTableColumn;

    @FXML
    private TableColumn<Film, String> genreTableColumn;

    @FXML
    private TableColumn<Film, String> runtimeTableColumn;

    @FXML
    private TextField titleField;

    @FXML
    private TextField releaseField;

    @FXML
    private ImageView imageView;

    @FXML
    private ComboBox<Genre> genreBox;

    @FXML
    private TextField titleFieldMain;

    @FXML
    private TextField runtimeFieldMain;

    @FXML
    private TextField releaseFieldMain;

    @FXML
    private ComboBox<Genre> genreBoxMain;

    public void initialize() {
        try {
            filmList = Database.getFilmsFromDatabase();
            genres = Database.getGenresFromDatabase();
        } catch (DatabaseException ex) {
            LoggerClass.logger.info("Problem with database while fetching genres and films!", ex);
        }

        try {
            changes = Serialization.Deserialize();
        } catch (RuntimeException ex) {
            LoggerClass.logger.info("Serialization file is empty!", ex);
        }

        genreBox.setItems(FXCollections.observableList(genres));
        genreBoxMain.setItems(FXCollections.observableList(genres));

        tableView.setItems(FXCollections.observableList(filmList));
        tableView.getSelectionModel().select(0);
        titleTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getTitle());
        });
        releaseTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getRelease().toString());
        });
        genreTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getGenre().getGenreName());
        });
        runtimeTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getRuntime().toString());
        });

        titleFieldMain.setText(filmList.get(0).getTitle());
        runtimeFieldMain.setText(filmList.get(0).getRuntime().toString());
        releaseFieldMain.setText(filmList.get(0).getRelease().toString());
        genreBoxMain.setValue(filmList.get(0).getGenre());

        Image image = new Image("https://image.tmdb.org/t/p/original" + filmList.get(0).getPath());
        imageView.setImage(image);

        tableView.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() >= 1) {
                onEdit();
            }
        });
    }

    public void onEdit() {

        if (tableView.getSelectionModel().getSelectedItem() != null) {

            Film selectedFilm = tableView.getSelectionModel().getSelectedItem();
            titleFieldMain.setText(selectedFilm.getTitle());
            releaseFieldMain.setText(selectedFilm.getRelease().toString());
            runtimeFieldMain.setText(selectedFilm.getRuntime().toString());
            genreBoxMain.setValue(selectedFilm.getGenre());

            Image image = new Image("https://image.tmdb.org/t/p/original" + selectedFilm.getPath());
            imageView.setImage(image);
        }
    }

    public void onButtonEdit() {
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            String selectedCode = tableView.getSelectionModel().getSelectedItem().getCode();

            List<Film> films = new ArrayList<>();
            try {
                films = Database.getFilmsFromDatabase();
            } catch (DatabaseException e) {
                throw new RuntimeException(e);
            }

            Film chosenFilm = films.stream().filter(film -> film.getCode().equals(selectedCode)).findFirst().get();

            String newTitle = titleFieldMain.getText();
            String newRelease = releaseFieldMain.getText();
            Genre newGenre = genreBoxMain.getValue();
            String newRuntime = runtimeFieldMain.getText();

            Film newFilm = new Film.FilmBuilder()
                    .setId(1L).setTitle(newTitle)
                    .setRelease(Long.valueOf(newRelease))
                    .setGenre(newGenre)
                    .setCode(chosenFilm.getCode())
                    .setDirector(chosenFilm.getDirector())
                    .setActors(chosenFilm.getActors())
                    .setDescription(chosenFilm.getDescription())
                    .setRuntime(Long.valueOf(newRuntime))
                    .setPath(chosenFilm.getPath())
                    .createFilm();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm editing!");
            alert.setHeaderText("Editing");
            alert.setContentText("Are you sure you want to edit your film?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get().equals(ButtonType.OK)) {
                Database.saveFilm(newFilm);
                Change change = new Change("FILM EDITED", User.sessionUsername, User.sessionRole, LocalDateTime.now(), "Film");
                SavedChange<String, Change> savedChange = new SavedChange<>(tableView.getSelectionModel().getSelectedItem().toString() + "->" + newFilm.toString(), change);

                changes.add(savedChange);
                Serialization.Serialize(changes);

                try {
                    filmList = Database.getFilmsFromDatabase();
                } catch (DatabaseException e) {
                    LoggerClass.logger.info("Films list is empty");
                    filmList = new ArrayList<>();
                }

                if (!filmList.isEmpty()) {
                    tableView.setItems(FXCollections.observableList(filmList));

                    titleFieldMain.setText(filmList.get(0).getTitle());
                    releaseFieldMain.setText(filmList.get(0).getRelease().toString());
                    genreBoxMain.setValue(filmList.get(0).getGenre());
                    runtimeFieldMain.setText(filmList.get(0).getRuntime().toString());

                    Image image = new Image("https://image.tmdb.org/t/p/original" + filmList.get(0).getPath());
                    imageView.setImage(image);
                }
            }
        }
    }

    @FXML
    protected void onSearchTyping() {

        List<Film> filteredList = new ArrayList<>(filmList);

        if (!titleField.getText().isEmpty()) {
            filteredList = filteredList.stream()
                    .filter(film -> film.getTitle().toLowerCase().contains(titleField.getText().toLowerCase())).toList();
        }
        if (!releaseField.getText().isEmpty()) {
            filteredList = filteredList.stream()
                    .filter(film -> film.getRelease().toString().contains(releaseField.getText())).toList();
        }
        if (genreBox.getValue() != null) {
            filteredList = filteredList.stream()
                    .filter(film -> film.getGenre().toString().contains(genreBox.getValue().toString())).toList();
        }

        tableView.setItems(FXCollections.observableList(filteredList));
    }

    public void onButtonDelete() {
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            String selectedCode = tableView.getSelectionModel().getSelectedItem().getCode();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm deleting!");
            alert.setHeaderText("Delete");
            alert.setContentText("Are you sure you want to delete your film?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get().equals(ButtonType.OK)) {
                Database.deleteChosenFilmAdmin(selectedCode);

                Change change = new Change("FILM DELETED", User.sessionUsername, User.sessionRole, LocalDateTime.now(), "Film");
                SavedChange<String, Change> savedChange = new SavedChange<>(tableView.getSelectionModel().getSelectedItem().toString(), change);

                changes.add(savedChange);
                Serialization.Serialize(changes);

                try {
                    filmList = Database.getFilmsFromDatabase();
                } catch (DatabaseException e) {
                    LoggerClass.logger.info("Films list is empty");
                    filmList = new ArrayList<>();

                    alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Ranked list is empty");
                    alert.setHeaderText("ADD REVIEWS!");
                    alert.setContentText("Your don't have any reviews saved!");
                    alert.showAndWait();

                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(FilmApplication.class.getResource("films-page.fxml"));
                        FilmApplication.setMainPage(fxmlLoader);
                    } catch (RuntimeException ex) {
                        LoggerClass.logger.info("Problem with loading FXML file", ex);
                    }
                }
                if (!filmList.isEmpty()) {
                    tableView.setItems(FXCollections.observableList(filmList));

                    titleFieldMain.setText(filmList.get(0).getTitle());
                    releaseFieldMain.setText(filmList.get(0).getRelease().toString());
                    genreBoxMain.setValue(filmList.get(0).getGenre());
                    runtimeFieldMain.setText(filmList.get(0).getRuntime().toString());

                    Image image = new Image("https://image.tmdb.org/t/p/original" + filmList.get(0).getPath());
                    imageView.setImage(image);
                }
            }
        }
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
