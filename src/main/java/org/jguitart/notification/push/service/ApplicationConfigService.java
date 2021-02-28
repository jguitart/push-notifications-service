package org.jguitart.notification.push.service;

import org.jguitart.notification.push.dto.ApplicationConfigDto;
import org.jguitart.notification.push.model.ApplicationConfig;
import org.jguitart.notification.push.model.Errors;
import org.jguitart.notification.push.util.KeyStoreUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.EOFException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@ApplicationScoped
public class ApplicationConfigService {

    @Inject
    DtoTranslatorService dtoTranslator;

    public String validate(ApplicationConfigDto config) {
        if(config.getName() == null || config.getName().isEmpty()) {
            return Errors.ERROR_APP_CONFIG_NAME_MANDATORY;
        }

        if(config.getIosConfig()!=null) {
            if(config.getIosConfig().getBundleId() == null || config.getIosConfig().getBundleId().isEmpty()) {
                return Errors.ERROR_APP_CONFIG_IOS_BUNDLE_ID_MANDATORY;
            }

            if(config.getIosConfig().getApnsCertificate() == null || config.getIosConfig().getApnsCertificate().isEmpty()) {
                return Errors.ERROR_APP_CONFIG_IOS_CERTIFICATE_MANDATORY;
            }

            if(config.getIosConfig().getApnsCertificatePassword() == null || config.getIosConfig().getApnsCertificatePassword().isEmpty()) {
                return Errors.ERROR_APP_CONFIG_IOS_PASSWORD_INVALID;
            }

            try {
                KeyStore keysStore = KeyStoreUtils.getKeyStore(config.getIosConfig().getApnsCertificate(), config.getIosConfig().getApnsCertificatePassword());
            } catch (IOException | KeyStoreException e) {
                e.printStackTrace();
                return Errors.ERROR_APP_CONFIG_IOS_PASSWORD_INVALID;
            } catch (Exception e) {
                e.printStackTrace();
                return Errors.ERROR_APP_CONFIG_IOS_CERTIFICATE_INVALID;
            }

        }

        if(config.getAndroidConfig()!=null) {
            if(config.getAndroidConfig().getPackageName() == null ||config.getAndroidConfig().getPackageName().isEmpty()) {
                return Errors.ERROR_APP_CONFIG_ANDROID_PACKAGE_MANDATORY;
            }

            if(config.getAndroidConfig().getFirebaseSenderId() == null ||config.getAndroidConfig().getFirebaseSenderId().isEmpty()) {
                return Errors.ERROR_APP_CONFIG_ANDROID_SENDER_MANDATORY;
            }

            if(config.getAndroidConfig().getFirebaseKey() == null ||config.getAndroidConfig().getFirebaseKey().isEmpty()) {
                return Errors.ERROR_APP_CONFIG_ANDROID_KEY_MANDATORY;
            }
        }


        return null;
    }

    @Transactional
    public ApplicationConfigDto create(ApplicationConfigDto applicationConfigDto) {

        ApplicationConfig applicationConfig = dtoTranslator.getApplicationConfig(applicationConfigDto);

        if(applicationConfig.getIosConfig() != null) {
            applicationConfig.getIosConfig().persistAndFlush();
        }

        if(applicationConfig.getAndroidConfig() != null) {
            applicationConfig.getAndroidConfig().persistAndFlush();
        }


        applicationConfig.persistAndFlush();

        ApplicationConfigDto result = dtoTranslator.getApplicationConfigDto(applicationConfig);
        return result;
    }

    public ApplicationConfigDto get(Long id) {
        ApplicationConfig instance = ApplicationConfig.findById(id);
        ApplicationConfigDto result = dtoTranslator.getApplicationConfigDto(instance);
        return result;
    }

    public boolean exists(Long id) {
        long count = ApplicationConfig.count("id", id);
        return count > 0;
    }

    @Transactional
    public void delete(Long id) {
        ApplicationConfig.deleteById(id);
    }
}
