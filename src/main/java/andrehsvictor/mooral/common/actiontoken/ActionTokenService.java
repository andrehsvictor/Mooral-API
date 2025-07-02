package andrehsvictor.mooral.common.actiontoken;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import andrehsvictor.mooral.common.encryption.EncryptionService;
import andrehsvictor.mooral.common.exception.GoneException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActionTokenService {

    private final EncryptionService encryptionService;

    @Value("${mooral.action-token.format}")
    private String format = "%s:%s:%d:%s";

    public String issue(String email, String action, long expiresAt) {
        String extra = "";
        String payload = String.format(format, email, action, expiresAt, extra);
        return encryptionService.encrypt(payload);
    }

    public String issue(String email, String action, long expiresAt, String extra) {
        String payload = String.format(format, email, action, expiresAt, extra);
        return encryptionService.encrypt(payload);
    }

    public String[] process(String token) {
        String decrypted = encryptionService.decrypt(token);
        String[] parts = decrypted.split(":");
        if (parts.length < 4) {
            throw new IllegalArgumentException("Invalid action token format");
        }
        String email = parts[0];
        String action = parts[1];
        long expiresAt = Long.parseLong(parts[2]);
        String extra = parts[3];
        String[] payloadParts = { email, action, String.valueOf(expiresAt), extra };
        if (System.currentTimeMillis() > expiresAt) {
            throw new GoneException("Action token has expired");
        }
        return payloadParts;
    }

}
