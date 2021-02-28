package org.jguitart.notification.push.service;

import org.jguitart.notification.push.dto.ApplicationConfigAndroidDto;
import org.jguitart.notification.push.dto.ApplicationConfigDto;
import org.jguitart.notification.push.dto.ApplicationConfigIosDto;
import org.jguitart.notification.push.model.ApplicationConfig;
import org.jguitart.notification.push.model.ApplicationConfigAndroid;
import org.jguitart.notification.push.model.ApplicationConfigIos;

import javax.enterprise.context.ApplicationScoped;
import java.util.Base64;

@ApplicationScoped
public class DtoTranslatorService {

    public ApplicationConfigDto getApplicationConfigDto(ApplicationConfig applicationConfig) {
        ApplicationConfigDto result = null;
        if(applicationConfig != null) {
            result = ApplicationConfigDto.builder()
                    .id(applicationConfig.getId())
                    .name(applicationConfig.getName())
                    .androidConfig(this.getApplicationConfigAndroidDto(applicationConfig.getAndroidConfig()))
                    .iosConfig(this.getApplicationConfigIosDto(applicationConfig.getIosConfig()))
                    .build();
        }
        return result;
    }

    public ApplicationConfig getApplicationConfig(ApplicationConfigDto applicationConfigDto) {
        ApplicationConfig result = null;
        if(applicationConfigDto != null) {
            result = ApplicationConfig.builder()
                    .id(applicationConfigDto.getId())
                    .name(applicationConfigDto.getName())
                    .androidConfig(this.getApplicationConfigAndroid(applicationConfigDto.getAndroidConfig()))
                    .iosConfig(this.getApplicationConfigIos(applicationConfigDto.getIosConfig()))
                    .build();;
        }
        return result;
    }

    public ApplicationConfigIos getApplicationConfigIos(ApplicationConfigIosDto applicationConfigIosDto) {
        ApplicationConfigIos result = null;
        if(applicationConfigIosDto != null) {
            byte[] certificate = null;
            if(applicationConfigIosDto.getApnsCertificate()!=null && !applicationConfigIosDto.getApnsCertificate().isEmpty()) {
                certificate = Base64.getDecoder().decode(applicationConfigIosDto.getApnsCertificate());
            }
            result =  ApplicationConfigIos.builder()
                    .id(applicationConfigIosDto.getId())
                    .apnsCertificate(certificate)
                    .apnsCertificatePassword(applicationConfigIosDto.getApnsCertificatePassword())
                    .bundleId(applicationConfigIosDto.getBundleId())
                    .build();
        }
        return result;
    }

    public ApplicationConfigAndroid getApplicationConfigAndroid(ApplicationConfigAndroidDto applicationConfigAndroidDto) {
        ApplicationConfigAndroid result = null;
        if(applicationConfigAndroidDto != null) {
            result = ApplicationConfigAndroid.builder()
                    .id(applicationConfigAndroidDto.getId())
                    .firebaseKey(applicationConfigAndroidDto.getFirebaseKey())
                    .firebaseSenderId(applicationConfigAndroidDto.getFirebaseSenderId())
                    .packageName(applicationConfigAndroidDto.getPackageName())
                    .build();
        }
        return result;
    }

    public ApplicationConfigAndroidDto getApplicationConfigAndroidDto(ApplicationConfigAndroid applicationConfigAndroid) {
        ApplicationConfigAndroidDto result = null;
        if(applicationConfigAndroid != null) {
            result = ApplicationConfigAndroidDto.builder()
                    .id(applicationConfigAndroid.getId())
                    .firebaseKey(applicationConfigAndroid.getFirebaseKey())
                    .firebaseSenderId(applicationConfigAndroid.getFirebaseSenderId())
                    .packageName(applicationConfigAndroid.getPackageName())
                    .build();
        }
        return result;
    }

    public ApplicationConfigIosDto getApplicationConfigIosDto(ApplicationConfigIos applicationConfigIos) {
        ApplicationConfigIosDto result = null;
        if(applicationConfigIos != null) {
            String certificate = null;
            if(applicationConfigIos.getApnsCertificate() != null ) {
                certificate = Base64.getEncoder().encodeToString(applicationConfigIos.getApnsCertificate());
            }
            result = ApplicationConfigIosDto.builder()
                    .id(applicationConfigIos.getId())
                    .apnsCertificate(certificate)
                    .apnsCertificatePassword(applicationConfigIos.getApnsCertificatePassword())
                    .bundleId(applicationConfigIos.getBundleId())
                    .build();
        }
        return result;
    }

}
