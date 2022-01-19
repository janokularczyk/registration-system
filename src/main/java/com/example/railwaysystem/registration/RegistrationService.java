package com.example.railwaysystem.registration;

import com.example.railwaysystem.appuser.AppUser;
import com.example.railwaysystem.appuser.AppUserRole;
import com.example.railwaysystem.appuser.AppUserService;
import com.example.railwaysystem.email.EmailSender;
import com.example.railwaysystem.registration.token.ConfirmationToken;
import com.example.railwaysystem.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());

        if (!isValidEmail) {
            throw new IllegalStateException("email not valid");
        }

        String token =  appUserService.signUpUser(
                new AppUser(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        AppUserRole.USER
                )
        );
        String link = "http://localhost:8080/api/v1/registration/confirm?token=" + token;
        emailSender.send(request.getEmail(), buildEmail(request.getFirstName(), link));
        return token;
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(
                confirmationToken.getAppUser().getEmail());
        return "confirmed";
    }

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family: Arial, Helvetica, sans-serif; width: 700px; font-size: 17px;\">\n" +
                "    <div style=\"background: #000000; padding: 5px 20px 5px 20px; box-shadow: 5px 5px 20px #353535;\">\n" +
                "        <p style=\"color: #FFFFFF; font-size: 30px;\">Welcome " + name + "!</p>\n" +
                "    </div>\n" +
                "    <div style=\"margin-left: 20px;\">\n" +
                "        <p>Thank you for registering on our website, please confirm your email to get full access.</p>\n" +
                "        <br/>" +
                "        <a href=\"" + link + "\">Activate account</a>\n" +
                "        <p style=\"font-weight: bold;\">The link will expire in 20 minutes.</p>\n" +
                "        </p>Best regards, the railway system team</p>\n" +
                "        <form action=\"contact_us.html\">\n" +
                "            <input type=\"submit\" value=\"Contact Us\" style=\"background: #70A2A6; border: none; color: white; padding: 15px 32px; text-align: center; text-decoration: none; display: inline-block; font-size: 16px; font-weight: bold; margin: 4px 2px; cursor: pointer; border-radius: 4px;\"/>\n" +
                "        </form>\n" +
                "    </div>\n" +
                "</div>";
    }
}
