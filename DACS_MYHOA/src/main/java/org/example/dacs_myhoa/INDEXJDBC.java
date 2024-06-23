package org.example.dacs_myhoa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class INDEXJDBC {
    private static final String  DB_URL = "jdbc:sqlserver://LAP-MYHOA\\MSSQLSERVER01;"
            + "databaseName=DACS_myhoa;encrypt=true;trustServerCertificate=true;";
    private static final String USER_NAME = "sa";
    private static final  String PASSWORD = "sa";
    public Connection connection;

    public INDEXJDBC() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
            System.out.println("Kết nối thành công!");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver không tìm thấy!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Kết nối thất bại!");
            e.printStackTrace();
        }



    }

    public Connection getConnection() {
        return connection;
    }
}

