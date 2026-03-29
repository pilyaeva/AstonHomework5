package org.notificationservice.service;

import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.store.FolderException;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.Duration;

@SpringBootTest
@ActiveProfiles("test")
class EmailServiceIntegrationTest {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP);

    @Autowired
    private EmailServiceImpl emailService;

    @BeforeEach
    void cleanup() throws FolderException {
        greenMail.purgeEmailFromAllMailboxes();
    }

    @Test
    @DisplayName("notifyUser. Создание аккаунта")
    void notifyUser_CreateOperation() throws MessagingException, IOException {
        emailService.notifyUser("user@localhost", "CREATE");

        await().atMost(Duration.ofSeconds(5))
                .until(() -> greenMail.getReceivedMessages().length > 0);

        var message = greenMail.getReceivedMessages()[0];
        assertEquals("Создание аккаунта", message.getSubject());
        assertTrue(message.getContent().equals("Здравствуйте! Ваш аккаунт был успешно создан."));
    }

    @Test
    @DisplayName("notifyUser. Удаление аккаунта")
    void notifyUser_DeleteOperation() throws MessagingException, IOException {
        emailService.notifyUser("admin@localhost", "DELETE");

        await().atMost(Duration.ofSeconds(5))
                .until(() -> greenMail.getReceivedMessages().length > 0);

        var message = greenMail.getReceivedMessages()[0];
        assertEquals("Удаление аккаунта", message.getSubject());
        assertTrue(message.getContent().equals("Здравствуйте! Ваш аккаунт был удалён."));
    }

    @Test
    @DisplayName("notifyUser. Неизвестная операция - письмо не отправляется")
    void notifyUser_UnknownOperation() {
        emailService.notifyUser("user@localhost", "UPDATE");

        await().pollDelay(Duration.ofMillis(100))
                .atMost(Duration.ofSeconds(1))
                .until(() -> true);

        assertEquals(0, greenMail.getReceivedMessages().length);
    }
}