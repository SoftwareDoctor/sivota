/**
 * @Author: SoftwareDoctor andrea_italiano87@yahoo.com
 * @Date: 2024-07-31 07:40:12
 * @LastEditors: SoftwareDoctor andrea_italiano87@yahoo.com
 * @LastEditTime: 2024-08-02 08:59:52
 * @FilePath: src/main/java/it/softwaredoctor/sivota/service/TokenService.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package it.softwaredoctor.sivota.service;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

@Service
public class TokenService {

    private final SecureRandom secureRandom = new SecureRandom();

    public String generateToken(String email, UUID uuidVotazione) {
        String uniqueToken = UUID.randomUUID().toString();
        String token = encodeToken(uniqueToken, email, uuidVotazione);

        return token;
    }

    private String encodeToken(String token, String email, UUID uuidVotazione) {
        String combinedData = token + ":" + email + ":" + uuidVotazione;
        return Base64.getUrlEncoder().encodeToString(combinedData.getBytes(StandardCharsets.UTF_8));
    }

    public boolean validateToken(UUID uuidVotazione, String token) {
        try {
            String decodedData = new String(Base64.getUrlDecoder().decode(token), StandardCharsets.UTF_8);
            String[] parts = decodedData.split(":");
            if (parts.length != 3) {
                return false;
            }
            String originalToken = parts[0];
            String tokenEmail = parts[1];
            UUID tokenUuidVotazione = UUID.fromString(parts[2]);
            return tokenUuidVotazione.equals(uuidVotazione);
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        try {
            String decodedData = new String(Base64.getUrlDecoder().decode(token), StandardCharsets.UTF_8);
            String[] parts = decodedData.split(":");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid token format");
            }
            return parts[1];
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid token", e);
        }
    }
}


