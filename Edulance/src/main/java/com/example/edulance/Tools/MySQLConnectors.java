package com.example.edulance.Tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnectors {
    private String url = "jdbc:mysql://localhost:3306/edulance3a3";

    private String login="root";
    private String pwd="";
    Connection cnx;
    public MySQLConnectors(){
        try {

            cnx = DriverManager.getConnection(url,login,pwd);
            System.out.println("Connection établie!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Connection getCnx() {
        return cnx;
    }


}
