package smu.likelion.jikchon.service;

import org.springframework.web.multipart.MultipartFile;
import smu.likelion.jikchon.domain.enumurate.Target;

import java.util.List;

public interface ImageService {
    void saveImageList(Long targetId, Target target, List<MultipartFile> imageList);
    void deleteImages(Long targetId, Target target);
}
