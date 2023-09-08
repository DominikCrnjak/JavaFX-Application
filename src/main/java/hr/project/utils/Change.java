package hr.project.utils;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Change implements Serializable {

    private static final long serialVersionUID = 6529685098267757690L;
    private String type;
    private String object;
    private String userName;
    private String userRole;
    private LocalDateTime time;

    public Change(String type, String userName, String userRole, LocalDateTime time,String object) {
        this.type = type;
        this.userName = userName;
        this.userRole = userRole;
        this.time = time;
        this.object=object;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
}
