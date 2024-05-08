package com.example.edulancejava.Connectors;


import com.example.edulancejava.Connectors.Entities.GlobalUser;

public class UserSession {
    private static GlobalUser user = new GlobalUser(-1);

    public static void setUser(GlobalUser userr) {
        user = userr;
    }

    public static GlobalUser getUser() {
        return user;
    }


}
