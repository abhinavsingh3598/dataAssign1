import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Scanner;

public class AuthenticationImplementation implements Authentication {

    User user;


    public  String userAuthentication(Scanner scanner) {
        System.out.println("Please enter your user Id \n");
        String userId = scanner.nextLine();
        System.out.println("Please enter your password \n");
        String password = scanner.nextLine();

        return validateOneFactor(userId, password);
    }
    public String validateOneFactor(String userId, String password) {
        if (userId != null && password != null) {
            List<User> allObjects = UserObjectStorage.readObjects();
            String question=null;
            for (User user1:allObjects)
            {
               if( passwordMatch(user1.getPassword(), password))
               {
                   this.user=user1;
                   question=user1.getQuestion();
                   break;
               }
            }
            return question;
        }

        return null;
    }

    private boolean passwordMatch(String storagePassword, String inputPassword) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] messageDigest = md.digest(inputPassword.getBytes());
        BigInteger bigInteger = new BigInteger(1, messageDigest);
        return storagePassword.equals(bigInteger.toString(16));
    }

    public boolean twoFactor(String answer) {
        return user.getAnswer().matches(answer);
    }
}
