package br.com.lucascaldas.authguard.infrastructure.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.lucascaldas.authguard.core.security.JwtQrCodeGenerator;
import br.com.lucascaldas.authguard.infrastructure.utils.QRCodeGenerator;
import com.google.zxing.WriterException;
import lombok.extern.slf4j.Slf4j;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

@Service
@Slf4j
public class OneTimeTokenGeneratorService {


    @Autowired
    private JwtQrCodeGenerator jwtQrCodeGenerator;

    @Autowired
    private SocketIOService socketIOService;

    private final Random random = new Random();

    public String generateOTT() {
        // Generate a room number (you can replace this with your own logic)
        Long room = generateRoomNumber();

        // Generate a JWT token that embeds the room id. The token is used in your QR code.
        String qrCodeUrl = jwtQrCodeGenerator.generateToken(room);

        log.info("Generated ROOM: " + room);
        log.info("Generated QR Code URL: " + qrCodeUrl);

        // Optionally, notify the (yet-to-be-joined) room.
        // Note: If no socket has joined yet, the room is empty. That’s fine —
        // once a client connects using the token, they can join the room.
        socketIOService.sendMessageToRoom(String.valueOf(room), "roomCreated", "Room created with id: " + room);

        try {
            byte[] qrCodeImage = QRCodeGenerator.getQRCodeImage(qrCodeUrl, 250, 250);
            return Base64.getEncoder().encodeToString(qrCodeImage);
        } catch (WriterException | IOException e) {
            log.error("Error generating QR Code image", e);
            return null;
        }
    }


    /**
     * Extracts the label and secret from the given QR code URL.
     *
     * For example, given:
     * otpauth://totp/8350112?secret=LLMOBATUGUDJB6XGG7H2PAVLBWOTO7FWOOOWZGN3CYFAKI7ZKEC4WDHVKQQRVPR2JLXX4L5UEGHRD3MR5NPXRK4OWVWMRWFTC2Z72UZLSAJUV6RDSPFOU6I7AFPMCCQA&issuer=Authguard&algorithm=SHA1&digits=6&period=30
     *
     * It will extract:
     * - Label: 8350112
     * - Secret:
     * LLMOBATUGUDJB6XGG7H2PAVLBWOTO7FWOOOWZGN3CYFAKI7ZKEC4WDHVKQQRVPR2JLXX4L5UEGHRD3MR5NPXRK4OWVWMRWFTC2Z72UZLSAJUV6RDSPFOU6I7AFPMCCQA
     *
     * @param qrCodeUrl the QR code URL containing the TOTP data
     * @return a String combining the extracted label and secret
     */
    public String getRoomAndLabelFromQrCode(String token) {
        Jws<Claims> claims = jwtQrCodeGenerator.decipherToken(token);
        String label = claims.getBody().get("label", String.class);
        Long room = claims.getBody().get("room", Long.class);

        log.info("Extracted Label: " + label);
        log.info("Extracted Secret: " + room);
        return "Label: " + label + ", Secret: " + room;
    }

    private long generateRoomNumber() {
        long bound = 100_000_000_000L;
        return Math.floorMod(random.nextLong(), bound);
    }

}


