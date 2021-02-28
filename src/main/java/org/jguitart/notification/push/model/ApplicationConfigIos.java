package org.jguitart.notification.push.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name  = "application_config_ios")
public class ApplicationConfigIos extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String bundleId;

    @Lob
    byte[] apnsCertificate;

    String apnsCertificatePassword;

}
