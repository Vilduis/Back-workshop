package prueba.com.example.demo.services;

public interface PasswordResetService {

    /**
     * Genera un token de reset y envía el email. No revela si el email existe (anti-enumeración).
     */
    void requestPasswordReset(String email);

    /**
     * Valida el token y actualiza la contraseña del usuario asociado.
     */
    void resetPassword(String token, String newPassword);
}
