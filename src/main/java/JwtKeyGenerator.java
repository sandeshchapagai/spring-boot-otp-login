import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

public class JwtKeyGenerator {
    public static void main(String[] args) {
        SecretKey secretKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
        String base64Key = java.util.Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("Generated Secret Key (Base64): " + base64Key);
    }
}
