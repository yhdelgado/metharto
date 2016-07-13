package cu.uci.gws.metharto.database;

import cu.uci.gws.metharto.config.MethartoConfigManager;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Yusniel Hidalgo Delgado
 */
public class SQLiteConnection {

    Connection connection = null;

    public SQLiteConnection() {

    }

    public Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            MethartoConfigManager cm = MethartoConfigManager.getInstance();
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:" + cm.loadConfigFile().getProperty("metharto.database"));
            } catch (IOException ex) {
                Logger.getLogger(SQLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
            return connection;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SQLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    }

    public void createDatabase() {
        try {
            Connection c = getConnection();
            Statement stmt = null;
            stmt = c.createStatement();
            String sql = "CREATE TABLE RECORD"
                    + "(ID INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL,"
                    + " TITLE           TEXT    NOT NULL, "
                    + " AUTHOR      TEXT     NOT NULL, "
                    + " ABSTRACT        TEXT, "
                    + " SOURCE        TEXT,"
                    + " DATE        TEXT,"
                    + " IDENTIFIER        TEXT,"
                    + " TYPE        TEXT,"
                    + " FORMAT        TEXT,"
                    + " LANGUAGE        TEXT,"
                    + " FILE       TEXT)";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void insertRecord(String title, String author, String description, String source, String date, String identifier,String type, String format, String language) {
        try {
            Statement stmt = null;
            Connection c = getConnection();
            c.setAutoCommit(false);
            stmt = c.createStatement();
            String sql = "INSERT INTO RECORD (TITLE,AUTHOR,ABSTRACT,SOURCE,DATE,IDENTIFIER,TYPE,FORMAT,LANGUAGE)"
                    + "VALUES ('" + scapeField(title) + "','" + scapeField(author) + "','" + scapeField(description) + "','" + scapeField(source) + "','" + scapeField(date) + "','" + scapeField(identifier) + "','" + scapeField(type) + "','" + scapeField(format) + "','" + scapeField(language) + "');";
            System.out.println(sql);

            stmt.executeUpdate(sql);
            stmt.close();
            c.commit();
            c.close();
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String scapeField(String field) {
        if (field.contains("'")) {
            field = field.replaceAll("'", "''");
        }
        return field;
    }
}
