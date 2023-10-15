import org.flywaydb.core.Flyway;
import props.PropertyReader;

public class DatabaseMigration {
    public static void main(String[] args) {
        String url = PropertyReader.getConnectionUrlForPostgres();
        String user = PropertyReader.getUserForPostgres();
        String password = PropertyReader.getPasswordForPostgres();
        Flyway flyway = Flyway.configure().dataSource(url, user, password).load();
        flyway.migrate();
    }
}
