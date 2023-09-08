package hr.project.application.admin;

import hr.project.application.FilmApplication;
import hr.project.entities.film.Film;
import hr.project.entities.film.Genre;
import hr.project.entities.generics.SavedChange;
import hr.project.entities.person.Actor;
import hr.project.entities.person.Director;
import hr.project.entities.person.User;
import hr.project.exceptions.DatabaseException;
import hr.project.exceptions.SerializationFileEmptyException;
import hr.project.utils.Change;
import hr.project.utils.Database;
import hr.project.utils.LoggerClass;
import hr.project.utils.Serialization;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;
import java.util.*;

public class AddingFilmController {

    @FXML
    private TextField titleField;

    @FXML
    private TextField releaseField;

    @FXML
    private TextField codeField;

    @FXML
    private TextField lengthField;

    @FXML
    private TextField pathField;

    @FXML
    private TextArea textArea;

    @FXML
    private ComboBox<Genre> genreComboBox;

    @FXML
    private ComboBox<Director> directorComboBox;

    @FXML
    private ComboBox<Actor> actorComboBox;

    private List<Genre> genres = new ArrayList<>();

    private List<Actor> actors = new ArrayList<>();

    private List<Director> directors = new ArrayList<>();
    private List<SavedChange<String, Change>> changes = new ArrayList<>();

    public void initialize() {
        try {
            genres = Database.getGenresFromDatabase();
            actors = Database.getActorsFromDatabase();
            directors = Database.getDirectorsFromDatabase();
        } catch (DatabaseException ex) {
            LoggerClass.logger.info("Problem with database while fetching genres, actors and directors!", ex);
        }

        try {
            changes = Serialization.Deserialize();
        } catch (SerializationFileEmptyException ex) {
            LoggerClass.logger.info("Serialization file is empty!", ex);
        }

        genreComboBox.setItems(FXCollections.observableList(genres));
        actorComboBox.setItems(FXCollections.observableList(actors));
        directorComboBox.setItems(FXCollections.observableList(directors));
    }

    public void onAddButtonClick() {
        StringBuilder errorMessages = new StringBuilder();

        if (titleField.getText().isEmpty()) {
            errorMessages.append("Title field empty!\n");
        }
        if (releaseField.getText().isEmpty()) {
            errorMessages.append("Release year empty!\n");
        }
        if (codeField.getText().isEmpty()) {
            errorMessages.append("Code field empty!\n");
        }
        if (lengthField.getText().isEmpty()) {
            errorMessages.append("Length field empty!\n");
        }
        if (pathField.getText().isEmpty()) {
            errorMessages.append("Path field empty!\n");
        }
        if (textArea.getText().isEmpty()) {
            errorMessages.append("Description empty!\n");
        }
        if (genreComboBox.getValue() == null) {
            errorMessages.append("Genre not chosen!\n");
        }
        if (directorComboBox.getValue() == null) {
            errorMessages.append("Director not chosen!\n");
        }
        if (actorComboBox.getValue() == null) {
            errorMessages.append("Actor not chosen!\n");
        }
        if (errorMessages.isEmpty()) {
            Set<Actor> actorSet = new HashSet<>();
            actorSet.add(actorComboBox.getValue());

            Film film = new Film.FilmBuilder()
                    .setId(Long.getLong("1"))
                    .setTitle(titleField.getText())
                    .setRelease(Long.valueOf(releaseField.getText()))
                    .setGenre(genreComboBox.getValue())
                    .setCode(codeField.getText())
                    .setDirector(directorComboBox.getValue())
                    .setActors(actorSet)
                    .setDescription(textArea.getText())
                    .setRuntime(Long.valueOf(lengthField.getText()))
                    .setPath(pathField.getText())
                    .createFilm();
            Database.saveFilm(film);

            Change change = new Change("FILM ADDED", User.sessionUsername, User.sessionRole, LocalDateTime.now(), "Film");
            SavedChange<String, Change> savedChange = new SavedChange<>(film.toString(), change);

            changes.add(savedChange);
            Serialization.Serialize(changes);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save action successful!");
            alert.setHeaderText("Student data saved");
            alert.setContentText("Student saved!");
            alert.showAndWait();

            LoggerClass.logger.info("Film sucessfuly saved in database");

        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save action not successful!");
            alert.setHeaderText("Student data not saved");
            alert.setContentText(errorMessages.toString());
            alert.showAndWait();

            LoggerClass.logger.warn("Saving film in database unsuccesfull!");
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
