package hr.project.application;

import hr.project.entities.film.Film;
import hr.project.entities.film.Genre;
import hr.project.entities.film.RatedFilm;
import hr.project.entities.generics.SavedChange;
import hr.project.entities.person.User;
import hr.project.exceptions.DatabaseException;
import hr.project.exceptions.SerializationFileEmptyException;
import hr.project.thread.InsertNewReviewThread;
import hr.project.utils.Change;
import hr.project.utils.Database;
import hr.project.utils.LoggerClass;
import hr.project.utils.Serialization;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class FilmPageController {

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
    private TableColumn<Film, String> directorTableColumn;

    @FXML
    private ImageView imageView;
    @FXML
    private Label titleLabel;
    @FXML
    private Label directorLabel;
    @FXML
    private Label actorLabel;
    @FXML
    private Label genreLabel;

    @FXML
    private RadioButton button1 = new RadioButton("1");

    @FXML
    private RadioButton button2 = new RadioButton("2");

    @FXML
    private RadioButton button3 = new RadioButton("3");

    @FXML
    private RadioButton button4 = new RadioButton("4");

    @FXML
    private RadioButton button5 = new RadioButton("5");

    private ToggleGroup toggleGroup = new ToggleGroup();

    @FXML
    private TextArea reviewArea;

    @FXML
    private TextField titleField;

    @FXML
    private TextField releaseField;

    @FXML
    private ComboBox<Genre> genreBox;

    private ExecutorService service = Executors.newCachedThreadPool();

    @FXML
    public void initialize() {

        try {
            genres = Database.getGenresFromDatabase();
            filmList = Database.getFilmsFromDatabase();
        } catch (DatabaseException ex) {
            LoggerClass.logger.info("Problem with database while fetching genres and films!", ex);
        }

        try {
            changes = Serialization.Deserialize();
        } catch (SerializationFileEmptyException ex) {
            LoggerClass.logger.info("Serialization file is empty!", ex);
        }

        button1.setToggleGroup(toggleGroup);
        button2.setToggleGroup(toggleGroup);
        button3.setToggleGroup(toggleGroup);
        button4.setToggleGroup(toggleGroup);
        button5.setToggleGroup(toggleGroup);

        button1.setText("1");
        button2.setText("2");
        button3.setText("3");
        button4.setText("4");
        button5.setText("5");

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
        directorTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getDirector().toString());
        });

        genreBox.setItems(FXCollections.observableList(genres));

        Image image = new Image("https://image.tmdb.org/t/p/original" + filmList.get(0).getPath());
        imageView.setImage(image);

        titleLabel.setText(filmList.get(0).getTitle());
        directorLabel.setText(filmList.get(0).getDirector().toString());
        actorLabel.setText(filmList.get(0).getActors().toString());
        genreLabel.setText(filmList.get(0).getGenre().getGenreName());

        tableView.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() >= 1) {
                onEdit();
            }
        });

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

    @FXML
    protected void onSaveButtonClick() {
        if (toggleGroup.getSelectedToggle() != null) {
            RadioButton selectedButton = (RadioButton) toggleGroup.getSelectedToggle();
            Integer rating = Integer.valueOf(selectedButton.getText());
            Film chosenFilm = filmList.get(0);

            if (tableView.getSelectionModel().getSelectedItem() != null) {
                chosenFilm = tableView.getSelectionModel().getSelectedItem();
            }
            RatedFilm ratedFilm = new RatedFilm.RatedFilmBuilder()
                    .setFilm(chosenFilm)
                    .setUsername(User.sessionUsername)
                    .setRating(rating)
                    .setDate(LocalDate.now().toString())
                    .setReview(reviewArea.getText())
                    .createRatedFilm();
            try {
                Platform.runLater(new InsertNewReviewThread(ratedFilm));

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Saving information");
                alert.setHeaderText("Saving your film");
                alert.setContentText("Your film was successfully saved!");
                alert.showAndWait();
                LoggerClass.logger.info("Film review saved to database!");

                Change change = new Change("REVIEW SAVED", User.sessionUsername, User.sessionRole, LocalDateTime.now(), "Review");
                SavedChange<String, Change> savedChange = new SavedChange<>(ratedFilm.toString(), change);

                changes.add(savedChange);
                Serialization.Serialize(changes);
            } catch (RuntimeException ex) {
                LoggerClass.logger.info("Problem with database saving rated films!", ex);
            }

        } else {
            LoggerClass.logger.info("Problem with saving film to database!");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Saving failed");
            alert.setHeaderText("Please rate film");
            alert.setContentText("Film rating radiobuttons are empty!");
            alert.showAndWait();
        }
    }
    public void onEdit() {

        if (tableView.getSelectionModel().getSelectedItem() != null) {
            RadioButton rb = (RadioButton) toggleGroup.getSelectedToggle();
            if (rb != null) {
                rb.setSelected(false);
            }

            Film selectedFilm = tableView.getSelectionModel().getSelectedItem();
            titleLabel.setText(selectedFilm.getTitle());
            directorLabel.setText(selectedFilm.getDirector().toString());
            actorLabel.setText(selectedFilm.getActors().toString());
            genreLabel.setText(selectedFilm.getGenre().getGenreName().toString());

            Image image = new Image("https://image.tmdb.org/t/p/original" + selectedFilm.getPath());
            imageView.setImage(image);
        }
    }
}
