package org.jguitart.notification.push.model;


import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="application_config", indexes = {@Index(name="application_config_name_unique", unique = true, columnList = "name")})
public class ApplicationConfig extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    @ManyToOne
    @JoinColumn( name = "ios_config_id")
    ApplicationConfigIos iosConfig;

    @ManyToOne
    @JoinColumn( name = "android_config_id")
    ApplicationConfigAndroid androidConfig;



}
