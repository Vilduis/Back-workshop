package prueba.com.example.demo.services;

public interface EmailService {
    void sendTemporaryPassword(String toEmail, String technicianName, String tempPassword, String workshopName);

    void sendPasswordResetEmail(String toEmail, String userName, String resetUrl);
}
