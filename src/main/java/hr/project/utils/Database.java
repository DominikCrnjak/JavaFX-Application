package hr.project.utils;

import hr.project.entities.film.Film;
import hr.project.entities.film.Genre;
import hr.project.entities.film.RatedFilm;
import hr.project.entities.person.Actor;
import hr.project.entities.person.Director;
import hr.project.entities.person.Gender;
import hr.project.exceptions.DatabaseException;
import hr.project.exceptions.EmptyRankedFilmsList;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Database {

    public static Boolean activeConnection = false;
    private static final String DATABASE_FILE = "dat/database.properties";

    public static Connection connectToDatabase() throws SQLException, IOException {
        Properties properties = new Properties();
        properties.load(new FileReader(DATABASE_FILE));

        String url = properties.getProperty("url");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");

        return DriverManager.getConnection(url, username, password);
    }

    public static List<Film> getFilmsFromDatabase() throws DatabaseException {
        List<Film> filmList = new ArrayList<>();
        List<Genre> genresList = new ArrayList<>();
        List<Actor> actorsList = new ArrayList<>();
        List<Director> directorsList = new ArrayList<>();

        try {
            genresList = getGenresFromDatabase();
            actorsList = getActorsFromDatabase();
            directorsList = getDirectorsFromDatabase();
        } catch (DatabaseException ex) {
            LoggerClass.logger.info("Fetching genres,actors and directors data from database unsuccessful!");
        }

        try (Connection veza = connectToDatabase()) {
            String query = "SELECT * FROM films";
            Statement statement = veza.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                Long id = rs.getLong("id");
                String code = rs.getString("code");
                String title = rs.getString("title");
                String releaseString = rs.getString("release");
                String genre = rs.getString("genre");
                String director = rs.getString("director");
                String actors = rs.getString("actors");
                String description = rs.getString("description");
                Long runtime = rs.getLong("runtime");
                String path = rs.getString("path");

                Genre genre1 = genresList.stream().filter(gen -> gen.getDescription().equals(genre)).findFirst().get();
                Long release = Long.valueOf(releaseString.substring(0, 4));

                Director directorObject = directorsList.stream().filter(director1 -> director1.getCode().equals(director)).findFirst().get();
                actors = actors.replace("[", "");
                actors = actors.replace("]", "");
                actors = actors.replace(" ", "");


                List<String> longList = Arrays.stream(actors.split(",")).toList();
                Set<Actor> actorsSet = new HashSet<>();
                List<Actor> finalActorsList = actorsList;

                longList.forEach(long1 -> {
                    actorsSet.add(finalActorsList.stream().filter(actor -> actor.getCode().equals(long1)).findFirst().get());
                });

                Film film = new Film.FilmBuilder()
                        .setId(id)
                        .setTitle(title)
                        .setRelease(release)
                        .setGenre(genre1)
                        .setCode(code)
                        .setDirector(directorObject)
                        .setActors(actorsSet)
                        .setDescription(description)
                        .setRuntime(runtime)
                        .setPath(path)
                        .createFilm();

                filmList.add(film);

            }
        } catch (SQLException | IOException ex) {
            LoggerClass.logger.info("Problem with database while fetching films!", ex);
        }

        if (filmList.size() < 1) {
            throw new DatabaseException("Database film resultset is less than 1!");
        } else {
            return filmList;
        }
    }

    public static List<Genre> getGenresFromDatabase() throws DatabaseException {
        List<Genre> genreList = new ArrayList<>();

        try (Connection veza = connectToDatabase()) {
            String query = "SELECT * FROM genres";
            Statement statement = veza.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                Long id = rs.getLong("id");
                String code = rs.getString("code");
                String name = rs.getString("name");
                String description = rs.getString("description");

                Optional<Genre> genreOpt = Arrays.stream(Genre.values()).filter(genre -> genre.getGenreName().toLowerCase().contains(name.toLowerCase())).findFirst();
                genreList.add(genreOpt.get());
            }

        } catch (SQLException | IOException ex) {
            LoggerClass.logger.info("Problem with database while fetching genres!", ex);
        }

        if (genreList.size() < 1) {
            throw new DatabaseException("Database genres resultset is less than 1!");
        } else {
            return genreList;
        }
    }

    public static List<Actor> getActorsFromDatabase() throws DatabaseException {
        List<Actor> actorsList = new ArrayList<>();

        try (Connection veza = connectToDatabase()) {
            String query = "SELECT * FROM actors";
            Statement statement = veza.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                Long id = rs.getLong("id");
                String code = rs.getString("code");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String date = rs.getString("date");
                String path = rs.getString("path");

                Actor actor = new Actor(id, firstName, lastName, date, Gender.NEUTRAL, code, path);
                actorsList.add(actor);
            }
        } catch (SQLException | IOException ex) {
            LoggerClass.logger.info("Problem with database while fetching actors!", ex);
        }

        if (actorsList.size() < 1) {
            throw new DatabaseException("Database actors resultset is less than 1!");
        } else {
            return actorsList;
        }
    }

    public static List<Director> getDirectorsFromDatabase() throws DatabaseException {
        List<Director> directorList = new ArrayList<>();

        try (Connection veza = connectToDatabase()) {
            String query = "SELECT * FROM directors";
            Statement statement = veza.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                Long id = rs.getLong("id");
                String code = rs.getString("code");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                Date date = rs.getDate("date");
                String path = rs.getString("path");

                LocalDate dateLocal = date.toLocalDate();
                Director director = new Director(id, firstName, lastName, "NOW", Gender.NEUTRAL, code, "path");
                directorList.add(director);
            }
        } catch (SQLException | IOException ex) {
            LoggerClass.logger.info("Problem with database while fetching directors!", ex);
        }

        if (directorList.size() < 1) {
            throw new DatabaseException("Database directors resultset is less than 1!");
        } else {
            return directorList;
        }
    }

    public static void saveRatedFilm(RatedFilm film) {

        try (Connection veza = connectToDatabase()) {

            PreparedStatement preparedStatement = veza.prepareStatement("INSERT INTO ratedfilms (code,username,rating,date,review) values (?,?,?,?,?)");

            preparedStatement.setString(1, film.getCode());
            preparedStatement.setString(2, film.getUsername());
            preparedStatement.setInt(3, film.getRating());
            preparedStatement.setString(4, LocalDate.now().toString());
            preparedStatement.setString(5, film.getReview());
            preparedStatement.executeUpdate();

        } catch (SQLException | IOException ex) {
            LoggerClass.logger.info("Review already exists, proceeding to update!");

            try (Connection veza = connectToDatabase()) {

                PreparedStatement preparedStatement = veza.prepareStatement("UPDATE ratedfilms SET rating=?,date=?,review=? WHERE code =?");

                preparedStatement.setInt(1, film.getRating());
                preparedStatement.setString(2, LocalDate.now().toString());
                preparedStatement.setString(3, film.getReview());
                preparedStatement.setString(4, film.getCode());
                preparedStatement.executeUpdate();

            } catch (SQLException | IOException ex1) {
                LoggerClass.logger.info("Problem with updating review!");
            }
        }
    }

    public static List<RatedFilm> getRatedFilms(String inputUsername) throws EmptyRankedFilmsList {
        List<Film> filmList = new ArrayList<>();
        List<RatedFilm> ratedFilms = new ArrayList<>();

        try {
            filmList = Database.getFilmsFromDatabase();
        } catch (DatabaseException e) {
            LoggerClass.logger.info("Problem while getting films from database");
        }

        try (Connection veza = connectToDatabase()) {
            PreparedStatement preparedStatement = veza.prepareStatement("SELECT * FROM ratedfilms WHERE username=?");
            preparedStatement.setString(1, inputUsername);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String code = rs.getString("code");
                String username = rs.getString("username");
                Integer rating = rs.getInt("rating");
                String date = rs.getString("date");
                String review = rs.getString("review");

                Film film = filmList.stream().filter(film1 -> film1.getCode().equals(code)).findFirst().get();
                RatedFilm newRatedFilm = new RatedFilm.RatedFilmBuilder().setFilm(film).setUsername(username).setRating(rating).setDate(date).setReview(review).createRatedFilm();
                ratedFilms.add(newRatedFilm);
            }
        } catch (SQLException | IOException ex) {
            LoggerClass.logger.info("Problem with database while fetching directors!", ex);
        }

        if (ratedFilms.size() < 1) {
            throw new EmptyRankedFilmsList("List of ranked films is empty!");
        } else {
            return ratedFilms;
        }

    }

    public static void deleteChosenFilm(String code) {

        try (Connection veza = connectToDatabase()) {
            PreparedStatement preparedStatement = veza.prepareStatement("DELETE FROM ratedfilms WHERE code=?");
            preparedStatement.setString(1, code);
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            LoggerClass.logger.info("Problem while deleting review from database");
        }
    }

    public static void deleteChosenFilmAdmin(String code) {

        try (Connection veza = connectToDatabase()) {
            PreparedStatement preparedStatement = veza.prepareStatement("DELETE FROM films WHERE code=?");
            preparedStatement.setString(1, code);
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            LoggerClass.logger.info("Problem while deleting film from database");
        }
    }

    public static void saveFilm(Film film) {

        try (Connection veza = connectToDatabase()) {

            String actors = film.getActors().stream().map(Actor::getCode).collect(Collectors.toList()).toString();

            PreparedStatement preparedStatement = veza.prepareStatement("INSERT INTO films (title,code,director,actors,genre,description,runtime,path,release) values (?,?,?,?,?,?,?,?,?)");

            preparedStatement.setString(1, film.getTitle());
            preparedStatement.setString(2, film.getCode());
            preparedStatement.setString(3, film.getDirector().getCode());
            preparedStatement.setString(4, actors);
            preparedStatement.setString(5, film.getGenre().getDescription());
            preparedStatement.setString(6, film.getDescription());
            preparedStatement.setLong(7, film.getRuntime());
            preparedStatement.setString(8, film.getPath());
            preparedStatement.setString(9, film.getRelease().toString());

            preparedStatement.executeUpdate();

        } catch (SQLException | IOException ex) {
            LoggerClass.logger.info("There is already such film in database, update will be executed!");

            try (Connection veza = connectToDatabase()) {

                PreparedStatement preparedStatement = veza.prepareStatement("UPDATE films SET title=?,runtime=?,release=?,genre=? WHERE code =?");


                preparedStatement.setString(1, film.getTitle());
                preparedStatement.setLong(2, film.getRuntime());
                preparedStatement.setString(3, film.getRelease().toString());
                preparedStatement.setString(4, film.getGenre().getDescription());
                preparedStatement.setString(5, film.getCode());
                preparedStatement.executeUpdate();

            } catch (SQLException | IOException ex1) {
                LoggerClass.logger.info("There is a problem with updating database films!");
            }

        }

    }

}




