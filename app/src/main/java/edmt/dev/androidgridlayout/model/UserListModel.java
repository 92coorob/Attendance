package edmt.dev.androidgridlayout.model;

import java.io.Serializable;

public class UserListModel  implements Serializable {

    private String name;
    private String date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setLocation(String location) {
        this.date = location;
    }

    public UserListModel(String name, String date) {
        this.name = name;
        this.date = date;
    }

}