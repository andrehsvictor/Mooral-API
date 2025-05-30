package andrehsvictor.mooral.minio;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioProperties minioProperties;
    private final MinioClient minioClient;

    public String uploadFile(MultipartFile file) {
        try {
            String objectName = generateObjectName(file.getOriginalFilename());

            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioProperties.getBucket().get("name"))
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());

            return String.format("%s/%s/%s", minioProperties.getEndpoint(), minioProperties.getBucket().get("name"),
                    objectName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    public void deleteFile(String url) {
        try {
            String[] parts = url.split("/");
            String bucketName = parts[parts.length - 2];
            String objectName = parts[parts.length - 1];

            RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build();
            minioClient.removeObject(removeObjectArgs);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file", e);
        }
    }

    private String generateObjectName(String originalFilename) {
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String timestamp = String.valueOf(System.currentTimeMillis());
        return timestamp + fileExtension;
    }
}