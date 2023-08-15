package smu.likelion.jikchon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import smu.likelion.jikchon.domain.Image;
import smu.likelion.jikchon.domain.enumurate.Target;
import smu.likelion.jikchon.exception.CustomBadRequestException;
import smu.likelion.jikchon.exception.ErrorCode;
import smu.likelion.jikchon.repository.ImageRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocalImageService implements ImageService {
    private final ImageRepository imageRepository;

    private String saveImage(Target target, MultipartFile image) {

        try {
            String fullPath = getFullPath(target, image);
            byte[] fileBytes = image.getBytes();
            Path imagePath = Paths.get(fullPath);
            Files.write(imagePath, fileBytes);

            return fullPath;
        } catch (IOException e) {
            throw new CustomBadRequestException(ErrorCode.BAD_REQUEST);
        }
    }

    @Override
    public void saveImageList(Long targetId, Target target, List<MultipartFile> imageList) {
        if (imageList != null) {
            for (MultipartFile multipartFile : imageList) {
                if (multipartFile.isEmpty() ||
                        !StringUtils.startsWithIgnoreCase(multipartFile.getContentType(), "image")) {
                    throw new CustomBadRequestException(ErrorCode.BAD_REQUEST);
                }
            }
            for (MultipartFile image : imageList) {
                if (!image.isEmpty()) {
                    String url = saveImage(target, image);
                    imageRepository.save(Image.builder()
                            .imageUrl(url)
                            .target(target)
                            .targetId(targetId)
                            .build());
                }
            }
        }
    }

    private String getBaseFilePath() {
        String projectPath = new File(".").getAbsolutePath();
        return projectPath.substring(0, projectPath.length() - 1) + "src/main/resources/static/";
    }

    private String createFileName(MultipartFile image) {
        return UUID.randomUUID() + "_" + image.getOriginalFilename();
    }

    private String getFullPath(Target target, MultipartFile image) {
        return getBaseFilePath() + target.toString().toLowerCase(Locale.ROOT) + "/" + createFileName(image);
    }

    @Override
    public void deleteImages(Long targetId, Target target) {

    }
}
