package prueba.com.example.demo.services;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class PasswordGeneratorService {

    private static final String LOWER  = "abcdefghijkmnopqrstuvwxyz";
    private static final String UPPER  = "ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final String DIGITS = "23456789";
    private static final String SYMBOLS = "!@#$%&*";
    private static final String ALL = LOWER + UPPER + DIGITS + SYMBOLS;

    private final SecureRandom random = new SecureRandom();

    public String generateTemporaryPassword() {
        return generate(12);
    }

    public String generate(int length) {
        if (length < 8) length = 8;
        StringBuilder sb = new StringBuilder(length);
        // garantizar al menos uno de cada categoria
        sb.append(LOWER.charAt(random.nextInt(LOWER.length())));
        sb.append(UPPER.charAt(random.nextInt(UPPER.length())));
        sb.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        sb.append(SYMBOLS.charAt(random.nextInt(SYMBOLS.length())));
        for (int i = sb.length(); i < length; i++) {
            sb.append(ALL.charAt(random.nextInt(ALL.length())));
        }
        // shuffle
        char[] chars = sb.toString().toCharArray();
        for (int i = chars.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char tmp = chars[i]; chars[i] = chars[j]; chars[j] = tmp;
        }
        return new String(chars);
    }
}
