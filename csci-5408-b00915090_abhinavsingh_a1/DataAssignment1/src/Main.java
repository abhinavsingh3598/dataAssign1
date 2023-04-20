import java.util.Scanner;

/***
 * Main class for the application
 */
public class Main {
    static Authentication authenticationImplementation = new AuthenticationImplementation();
    static Scanner scanner = new Scanner(System.in);

    static UserObjectStorage userObjectStorage = new UserObjectStorage();

    static Query queryImplementation = new QueryImplementation();

    /***
     * Bootstrap method for the application
     * @param args
     */
    public static void main(String[] args) {


        String choice;

        do {
            System.out.println("Enter a choice:");
            System.out.println("1. USER REGISTRATION");
            System.out.println("2. LOGIN");
            System.out.println("3. Exit");

            choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.println("Option 1: USER REGISTRATION");
                    userObjectStorage.userRegistration(scanner);
                    break;
                case "2":
                    System.out.println("Option 2: LOGIN");
                    String question = authenticationImplementation.userAuthentication(scanner);
                    if (question != null) {
                        System.out.println("Question: " + question);
                        System.out.println("Enter Security Answer");
                        String answer = scanner.nextLine();
                        if (authenticationImplementation.twoFactor(answer)) {
                            System.out.println("Enter the name of database");
                            String database = scanner.nextLine();
                            String choice2 = "10";
                            do {
                                System.out.println("Enter a operation you want to perform in " + database + " database");
                                System.out.println("1. Create a table");
                                System.out.println("2. Insert data into table");
                                System.out.println("3. Fetch data from table");
                                System.out.println("4. Update data in table");
                                System.out.println("5. Delete data from table");
                                System.out.println("6. Exit");

                                choice2 = scanner.nextLine();

                                switch (choice2) {
                                    case "1":
                                        System.out.println("Create table and its attributes ");
                                        System.out.println("FOR EX:  CREATE TABLE table_name (value1, value2, value3);");

                                        queryImplementation.createTable(scanner);
                                        break;
                                    case "2":
                                        System.out.println("Insert data into table;");
                                        System.out.println("FOR EX: INSERT INTO table_name VALUES (value1, value2, value3);");

                                        queryImplementation.insertIntoTable(scanner);
                                        break;
                                    case "3":
                                        System.out.println("Write Select Query");
                                        System.out.println("FOR EX: Select * from table_name;");

                                        queryImplementation.selectQuery(scanner);
                                        break;
                                    case "4":
                                        System.out.println("Write update Query");
                                        System.out.println("update table_name set col1=val1 where col2=val2;");
                                        queryImplementation.updateQuery(scanner);
                                        break;
                                    case "5":
                                        System.out.println("Delete from Table");
                                        System.out.println("delete from table_name where col=val;");
                                        queryImplementation.deleteQuery(scanner);
                                        break;
                                    case "6":
                                        System.out.println("Exiting...");
                                        break;
                                    default:
                                        System.out.println("Invalid choice");
                                        break;
                                }
                            } while (!choice2.equals("6"));
                        } else {
                            System.out.println("answer is wrong");
                        }
                    } else {
                        System.out.println("Please enter correct userid and password");
                    }
                    break;
                case "3":
                    System.out.println("Exiting....");
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        } while (!choice.equals("3"));

    }


}