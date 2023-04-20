import java.util.Scanner;

public interface Query {

    public void createTable(Scanner scanner);

    public void insertIntoTable(Scanner scanner);

    public void selectQuery(Scanner scanner);

    public void updateQuery(Scanner scanner);

    public void deleteQuery(Scanner scanner);
}
