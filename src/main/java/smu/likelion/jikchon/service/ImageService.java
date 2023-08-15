package smu.likelion.jikchon.service;

import org.springframework.web.multipart.MultipartFile;
import smu.likelion.jikchon.domain.Product;
import smu.likelion.jikchon.domain.enumurate.Target;

import java.util.List;

public interface ImageService {
    void saveProductImageList(Product product, List<MultipartFile> imageList);
    void deleteImages(Long targetId, Target target);
}
