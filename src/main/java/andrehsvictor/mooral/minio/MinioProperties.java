package andrehsvictor.mooral.minio;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "mooral.minio")
public class MinioProperties {

    private Map<String, String> admin;
    private String endpoint;
    private Map<String, String> bucket;

}