package andrehsvictor.mooral.image;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import andrehsvictor.mooral.minio.MinioService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ImageDeletionConsumer {

    private final MinioService minioService;
    private static final String QUEUE_NAME = "images.v1.delete";

    @RabbitListener(queues = QUEUE_NAME)
    public void consume(String imageUrl) {
        minioService.deleteFile(imageUrl);
    }

}
