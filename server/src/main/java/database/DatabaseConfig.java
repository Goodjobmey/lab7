package database;

public class DatabaseConfig {

    public static String getDbUrl() {return System.getenv("POSTGRES_DB_URL");}

    public static String getDbUsername() {return System.getenv("POSTGRES_USER");}

    public static String getDbPassword() {return System.getenv("POSTGRES_PASSWORD");}

}