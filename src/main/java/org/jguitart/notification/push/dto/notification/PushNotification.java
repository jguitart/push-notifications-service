package org.jguitart.notification.push.dto.notification;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;

@RegisterForReflection
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PushNotification {

    /**
     * Target device token to send notification
     */
    String token;

    /**
     * App name of ApplicationConfig where the notifications is sent
     */
    String appName;

    /**
     * Notification type: IOS/ANDROID
     */
    String type;

    /**
     * Notification payload
     */
    String notificationPayload;

}
