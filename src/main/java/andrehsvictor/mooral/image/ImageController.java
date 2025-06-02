package andrehsvictor.mooral.image;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/api/v1/images")
    public Map<String, String> upload(MultipartFile file) {
        String imageUrl = imageService.upload(file);
        return Map.of("url", imageUrl);
    }

}
