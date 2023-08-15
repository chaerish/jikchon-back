package smu.likelion.jikchon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import smu.likelion.jikchon.domain.Product;
import smu.likelion.jikchon.domain.ProductImage;
import smu.likelion.jikchon.domain.enumurate.Target;
import smu.likelion.jikchon.repository.ImageRepository;
import smu.likelion.jikchon.s3.S3Uploader;

import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
public class S3ImageService implements ImageService {
    private final ImageRepository imageRepository;
    private final S3Uploader s3Uploader;


    @Override
    public void saveProductImageList(Product product, List<MultipartFile> imageList) {
        if (imageList != null) {
            List<String> imageUrlList = s3Uploader.s3MultipleUploadOfFileNullSafe("product", imageList);
            for (String imageUrl : imageUrlList) {
                imageRepository.save(new ProductImage(product, imageUrl));
            }
        }
    }

    @Override
    public void deleteImages(Long targetId, Target target) {

    }
}
