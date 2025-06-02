package andrehsvictor.mooral.image;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ImageDeletionProducer {

    private final RabbitTemplate rabbitTemplate;
    private static final String QUEUE_NAME = "images.v1.delete";

    public void sendImageDeletionMessage(String imageUrl) {
        rabbitTemplate.convertAndSend(QUEUE_NAME, imageUrl);
    }
}
