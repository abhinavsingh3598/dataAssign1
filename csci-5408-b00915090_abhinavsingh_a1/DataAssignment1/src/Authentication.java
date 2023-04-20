import java.util.Scanner;

public interface Authentication {
    public  String userAuthentication(Scanner scanner);
    public String validateOneFactor(String userId, String password);

    public boolean twoFactor(String answer);
}
