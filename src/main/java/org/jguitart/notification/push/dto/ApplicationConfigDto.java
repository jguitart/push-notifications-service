package org.jguitart.notification.push.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;

import javax.validation.constraints.NotNull;

@RegisterForReflection
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationConfigDto {

    Long id;
    String name;
    ApplicationConfigIosDto iosConfig;
    ApplicationConfigAndroidDto androidConfig;

}
