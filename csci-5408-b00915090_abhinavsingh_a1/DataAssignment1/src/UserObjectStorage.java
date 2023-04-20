import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/***
 * Storing and retrieving user object from file storage
 */
public class UserObjectStorage {
    private static final String FILE_NAME = "user-data.txt";

    /***
     * Write user object to file
     * @param scanner
     */
    public  void userRegistration(Scanner scanner) {
        System.out.println("Registration");

        System.out.println("Please enter your user Id \n");
        String userId = scanner.nextLine();
        System.out.println("Please enter your password \n");
        String password = scanner.nextLine();
        System.out.println("Please enter your security question\n");
        String question = scanner.nextLine();
        System.out.println("Please enter your security answer\n");
        String answer = scanner.nextLine();

        User user = new User(userId, password, question, answer);
        try {
            boolean exists = new File(FILE_NAME).exists();
            FileOutputStream fos = new FileOutputStream(FILE_NAME, true);
            ObjectOutputStream oos = exists ?
                    new ObjectOutputStream(fos) {
                        protected void writeStreamHeader() throws IOException {
                            reset();
                        }
                    } : new ObjectOutputStream(fos);
            oos.writeObject(user);
            fos.close();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /***
     *
     * Fetch all the user from file storage
     * @return List<User>
     */
    public static List<User> readObjects() {
        List<User> objects = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            while (fis.available() > 0) {
                try {
                    User user = (User) ois.readObject();
                    objects.add(user);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (EOFException e) {
            // End of file reached
        } catch (IOException e) {
            e.printStackTrace();
        }
        return objects;
    }


}