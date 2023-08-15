package smu.likelion.jikchon.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import smu.likelion.jikchon.domain.enumurate.Target;
import smu.likelion.jikchon.exception.CustomBadRequestException;
import smu.likelion.jikchon.exception.ErrorCode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {
    @Value("${BUCKET_NAME}")
    private String bucket;
    private final AmazonS3 amazonS3;


    public List<String> s3MultipleUploadOfFileNullSafe(
            String domainName, List<MultipartFile> multipartFileList) {
        List<String> fileUrlList = new ArrayList<>();

        if (multipartFileList != null) {
            for (MultipartFile multipartFile : multipartFileList) {
                if (!multipartFile.isEmpty()) {
                    fileUrlList.add(s3UploadOfFile(domainName, multipartFile));
                }
            }
        }
        return fileUrlList;
    }

    public String s3UploadOfFile(String domainName, MultipartFile multipartFile) {
        if (multipartFile.isEmpty() ||
                !StringUtils.startsWithIgnoreCase(multipartFile.getContentType(), "image")) {
            throw new CustomBadRequestException(ErrorCode.BAD_REQUEST);
        }


        String fileName = createFileName(multipartFile.getOriginalFilename());

        return s3Upload(domainName, fileName, multipartFile);
    }

    /**
     * S3 업로드
     */
    private String s3Upload(String folderPath, String fileNm, MultipartFile multipartFile) {

        File uploadFile = convertThrow(multipartFile);

        //S3에 저장될 위치 + 저장파일명
        String storeKey = folderPath + "/" + fileNm;

        //s3로 업로드
        String imageUrl = putS3(uploadFile, storeKey);

        //File 로 전환되면서 로컬에 생성된 파일을 제거
        //todo: 꼭 로컬에 저장해야 하는가?
        removeNewFile(uploadFile);

        return imageUrl;
    }


    private String putS3(File uploadFile, String storeKey) {
        amazonS3.putObject(new PutObjectRequest(bucket, storeKey, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3.getUrl(bucket, storeKey).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    // 로컬에 파일 업로드 하기
    private File convertThrow(MultipartFile multipartFile) {

        //파일 저장 이름
        String storeFileName = createFileName(multipartFile.getOriginalFilename());

        File convertFile = new File(System.getProperty("user.dir") + "/" + storeFileName);

        try {
            if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
                try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                    fos.write(multipartFile.getBytes());
                }
            }
            return convertFile;
        } catch (IOException e) {
            throw new CustomBadRequestException(ErrorCode.INVALID_FILE_TYPE);
        }
    }

    private String createFileName(String originalFileName) {
        return UUID.randomUUID() + "_" + originalFileName;
    }
}
