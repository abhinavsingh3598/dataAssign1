import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User implements Serializable {
    private String userId;
    private String password;

    private String question;

    private String answer;

    public User(String userId, String password, String question, String answer) {
        this.userId = userId;
        if(password!=null)
        {
            try {
                MessageDigest md=MessageDigest.getInstance("MD5");
                byte[] messageDigest= md.digest(password.getBytes());
                BigInteger bigInteger=new BigInteger(1,messageDigest);
                this.password=bigInteger.toString(16);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }

        }

        this.question = question;
        this.answer = answer;
    }

    public User() {
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
