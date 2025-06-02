package andrehsvictor.mooral.image;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import andrehsvictor.mooral.exception.BadRequestException;
import andrehsvictor.mooral.minio.MinioProperties;
import andrehsvictor.mooral.minio.MinioService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final MinioService minioService;
    private final MinioProperties minioProperties;
    private final ImageDeletionProducer imageDeletionProducer;

    private static final Integer MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB

    public String upload(MultipartFile file) {
        if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
            throw new BadRequestException("Invalid file type. Only image files are allowed.");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("File size exceeds the maximum limit of 10 MB.");
        }
        return minioService.uploadFile(file);
    }

    public void delete(String url) {
        if (!url.startsWith(minioProperties.getEndpoint())) {
            return;
        }
        imageDeletionProducer.sendImageDeletionMessage(url);
    }
}
