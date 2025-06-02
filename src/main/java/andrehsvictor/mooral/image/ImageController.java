package andrehsvictor.mooral.image;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Images", description = "Image upload and management operations")
public class ImageController {

    private final ImageService imageService;

    @Operation(summary = "Upload an image", description = "Uploads an image file and returns the public URL of the uploaded image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image uploaded successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class), examples = @ExampleObject(value = "{\"url\": \"https://storage.example.com/images/123456.jpg\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid file format or file too large (max 10MB)", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Valid JWT token is required", content = @Content),
            @ApiResponse(responseCode = "413", description = "File size exceeds maximum limit", content = @Content)
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/api/v1/images")
    public Map<String, String> upload(
            @Parameter(description = "Image file to upload (PNG, JPG, JPEG - maximum 10MB)", required = true) @RequestParam("file") MultipartFile file) {
        String imageUrl = imageService.upload(file);
        return Map.of("url", imageUrl);
    }
}