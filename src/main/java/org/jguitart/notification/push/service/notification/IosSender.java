package org.jguitart.notification.push.service.notification;


import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.ApnsClientBuilder;
import com.eatthepath.pushy.apns.PushNotificationResponse;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.eatthepath.pushy.apns.util.concurrent.PushNotificationFuture;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jguitart.notification.push.dto.notification.PushNotification;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
@Setter
@Builder
public class IosSender implements ISender {

    private static final Logger logger = Logger.getLogger(IosSender.class.getName());

    ApnsClient apnsClient;

    String bundleId;

    public void init(String apnsType, byte[] p12file, String password, String bundleId) throws IOException {

        apnsClient = new ApnsClientBuilder()
                .setApnsServer(apnsType)
                .setClientCredentials(new ByteArrayInputStream(p12file), password)
                .build();

        this.bundleId = bundleId;

    }

    @Override
    public void sendNotification(PushNotification notification) {
        final SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(notification.getToken(), getBundleId(), notification.getNotificationPayload());
        final PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>>
                sendNotificationFuture = apnsClient.sendNotification(pushNotification);

        sendNotificationFuture.whenComplete((response, cause) -> {
            if (response != null) {
                if(!response.isAccepted()) {
                    logger.severe(String.format("Push notification not sent: %s", response.getRejectionReason()));
                }
            } else {
                logger.log(Level.SEVERE, "Error sending push notification", cause);
            }
        });
    }
}
