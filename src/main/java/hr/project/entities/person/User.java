package hr.project.entities.person;

import hr.project.exceptions.InvalidUserException;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class User implements Serializable {

    private static final String FILE_NAME = "dat/users.txt";
    private static final Integer NUMBER_OF_LINES = 3;
    public static String sessionUsername;

    public static String sessionRole;
    private String username;
    private String password;
    private String role;

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public static boolean validateUser(String username, String password) {
        Set<User> users = User.usersFileRead();

        Boolean checkUsername = users.stream().anyMatch(user -> user.getUsername().equals(username));
        Boolean checkPassword = users.stream().anyMatch(user -> Integer.parseInt(user.getPassword()) == password.hashCode());

        if (checkUsername && checkPassword) {
            return true;
        } else {
            throw new InvalidUserException();
        }
    }

    public static void setUserRole() {
        Set<User> users = User.usersFileRead();

        String userRole = users
                .stream()
                .filter(user -> user.getUsername().equals(User.sessionUsername))
                .findFirst()
                .get()
                .getRole();

        User.sessionRole = userRole;
    }

    public static Set<User> usersFileRead() {
        Integer numberOfLines = NUMBER_OF_LINES;
        String fileName = FILE_NAME;

        Set<User> users = new HashSet<>();
        File usersFile = new File(fileName);

        try {
            BufferedReader categoriesFileReader = new BufferedReader(new FileReader(usersFile));

            String line;
            int lineCounter = 1;

            Optional<String> role = Optional.empty();
            Optional<String> username = Optional.empty();
            Optional<String> password = Optional.empty();


            while ((line = categoriesFileReader.readLine()) != null) {
                if (lineCounter % numberOfLines == 1) {
                    role = Optional.of(line);
                } else if (lineCounter % numberOfLines == 2) {
                    username = Optional.of(line);
                } else if (lineCounter % numberOfLines == 0) {
                    password = Optional.of(line);

                    User user = new User(username.get(), password.get(), role.get());
                    users.add(user);
                }
                lineCounter++;
            }
            categoriesFileReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }
}
