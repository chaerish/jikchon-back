package smu.likelion.jikchon.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import smu.likelion.jikchon.domain.Image;
import smu.likelion.jikchon.domain.Product;
import smu.likelion.jikchon.domain.enumurate.Target;
import smu.likelion.jikchon.exception.CustomBadRequestException;
import smu.likelion.jikchon.exception.CustomException;
import smu.likelion.jikchon.exception.ErrorCode;
import smu.likelion.jikchon.repository.ImageRepository;
import smu.likelion.jikchon.s3.S3Uploader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class S3ImageService implements ImageService{
    private final ImageRepository imageRepository;
    private final S3Uploader s3Uploader;

    @Override
    public void saveImageList(Long targetId, Target target, List<MultipartFile> imageList) {
        if (!imageList.isEmpty()) {
            List<String> urlList = s3Uploader.s3MultipleUploadOfFileNullSafe(Target.PRODUCT, imageList);
            for (String url : urlList) {
                    imageRepository.save(Image.builder()
                            .imageUrl(url)
                            .target(target)
                            .targetId(targetId)
                            .build());
            }
        }
    }

    @Override
    public void deleteImages(Long targetId, Target target) {

    }
}
