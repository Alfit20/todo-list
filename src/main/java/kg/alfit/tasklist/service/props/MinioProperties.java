package kg.alfit.tasklist.service.props;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {
    String bucket;
    String url;
    String accessKey;
    String secretKey;
}
