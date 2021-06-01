package org.jguitart.notification.push.service.notification;

import org.jguitart.notification.push.dto.notification.PushNotification;

public interface ISender {

    public void sendNotification(PushNotification notification);

}
