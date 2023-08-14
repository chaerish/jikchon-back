package smu.likelion.jikchon.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import smu.likelion.jikchon.domain.Image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {
    @Value("${BUCKET_NAME}")
    private String bucket;
    private final AmazonS3 amazonS3;
    public String s3Upload(String folderPath, Long domainId, MultipartFile multipartFile) {
        String fileName = createFileName(domainId.toString(), multipartFile.getOriginalFilename());

        System.out.println("S3Uploader.s3Upload");

        File uploadFile = convert(multipartFile)  // 파일 변환할 수 없으면 에러
                .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));

        String storePath = folderPath + "/" + fileName;
        //s3로 업로드
        String imageUrl = putS3(uploadFile, storePath);

        removeNewFile(uploadFile);

        return imageUrl;
    }
    public void delete(List<Image> images) {
        try {
            for(Image image: images){
                String imageUrl = image.getImageUrl();
                String storeKey = imageUrl.replace("https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/", "");
                System.out.println("imageUrl: " + imageUrl);
                System.out.println("storeKey: " + storeKey);
                amazonS3.deleteObject(new DeleteObjectRequest(bucket, URLDecoder.decode(storeKey, "UTF-8")));
            }
        } catch (Exception e) {
            log.error("delete file error ::: " + e.getMessage());
        }
    }
    public void deleteByUrl(String imageUrl) {
        try {
            String storeKey = imageUrl.replace("https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/", "");
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, URLDecoder.decode(storeKey, "UTF-8")));
        } catch (Exception e) {
            log.error("delete file error ::: " + imageUrl + e.getMessage());
        }
    }
    //S3 업로드
    private String putS3(File uploadFile, String storeKey) {
        amazonS3.putObject(new PutObjectRequest(bucket, storeKey, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3.getUrl(bucket, storeKey).toString();
    }
    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    private Optional<File> convert(MultipartFile multipartFile) {
        //파일 이름
        String originalFilename = multipartFile.getOriginalFilename();

        //파일 저장 이름
        String storeFileName = UUID.randomUUID() + "_" + originalFilename;

        File convertFile = new File(System.getProperty("user.dir") + "/" + storeFileName);

        System.out.println("S3Uploader.convert");
        try {
            if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
                try (FileOutputStream fos = new FileOutputStream(convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                    fos.write(multipartFile.getBytes());
                }
                return Optional.of(convertFile);
            }
        } catch (IOException e) {
            log.error(e.getMessage() + ": 이미지 변환중 오류가 발생했습니다.");
        }

        return Optional.empty();
    }

    private String createFileName(String frontName, String originalFileName) {

        String uuid = UUID.randomUUID().toString();

        return frontName + "_" + uuid + "_" + originalFileName;
    }

}
