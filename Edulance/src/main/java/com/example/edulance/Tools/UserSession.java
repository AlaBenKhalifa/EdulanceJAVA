package com.example.edulance.Tools;

import com.example.edulance.Entities.GlobalUser;

public class UserSession {
    private static GlobalUser user = new GlobalUser();

    public static void setUser(GlobalUser userr) {
        user = userr;
    }

    public static GlobalUser getUser() {
        return user;
    }
}
