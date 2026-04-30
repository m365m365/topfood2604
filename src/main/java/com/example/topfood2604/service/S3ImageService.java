package com.example.topfood2604.service;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@Service
public class S3ImageService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${app.image.base-url}")
    private String imageBaseUrl;

    public S3ImageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public ImageResult uploadRestaurantImage(Long restaurantId, MultipartFile photo) throws Exception {

        BufferedImage originalImage = ImageIO.read(photo.getInputStream());

        byte[] coverBytes = resize(originalImage, 1200, 800, 0.75);
        byte[] thumbBytes = resize(originalImage, 400, 300, 0.65);

        String coverKey = "restaurant/" + restaurantId + "/cover.jpg";
        String thumbKey = "restaurant/" + restaurantId + "/thumb.jpg";

        upload(coverKey, coverBytes);
        upload(thumbKey, thumbBytes);

        return new ImageResult(
                imageBaseUrl + "/" + coverKey,
                imageBaseUrl + "/" + thumbKey
        );
    }

    private byte[] resize(
            BufferedImage image,
            int width,
            int height,
            double quality
    ) throws Exception {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Thumbnails.of(image)
                .size(width, height)
                .outputFormat("jpg")
                .outputQuality(quality)
                .toOutputStream(outputStream);

        return outputStream.toByteArray();
    }

    private void upload(String key, byte[] bytes) {

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType("image/jpeg")
                .build();

        s3Client.putObject(
                request,
                RequestBody.fromBytes(bytes)
        );
    }

    public record ImageResult(
            String imageUrl,
            String thumbUrl
    ) {}
}