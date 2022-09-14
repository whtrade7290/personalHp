package com.example.exam.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.example.exam.Mapper.LookbookMapper;
import com.example.exam.Model.LookbookModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class LookbookService {

    @Autowired
    private LookbookMapper lookbookMapper;

    private AmazonS3 amazonS3;

    @Value("${aws.s3.bucketname}")
    private String AWS_S3_THUMBNAIL_BUCKETNAME;

    @Value("${aws.s3.access_key}")
    private String AWS_S3_ACCESS_KEY_ID;

    @Value("${aws.s3.secret_key}")
    private String AWS_S3_SECRET_KEY_ID;

    @Value("${aws.s3.upload.dir.partners}")
    private String UPLOAD_DIR_PARTNERS;

    @Value("${aws.s3.upload.url.partners}")
    private String PARTNERS_URL;
    public List<LookbookModel> selectLookbook() {
        return lookbookMapper.selectLookbook();
    }

    public long lookbookInsert(MultipartHttpServletRequest mhr, LookbookModel lookbookModel, String url, String awddir) throws FileNotFoundException {
        long result = 0L;
        MultipartFile file = mhr.getFile("uploadFile");
        String filepath = "";
        String strResult = "";
        String uploadFilename = "";
        if (!file.isEmpty()) {
            URL s = ResourceUtils.getURL("classpath:static/");
            String path = s.getPath();
            path = path.startsWith("/") ? path.substring(1) : path;
            File dir = new File(path + "/upload", "/student_Photo");
            if (!dir.exists())
                dir.mkdirs();
            path = path + "upload/student_Photo";
            String filename = file.getOriginalFilename();
            int beginIndex = filename.lastIndexOf("\\") + 1;
            filename = filename.substring(beginIndex);
            String pattern = "^[a-zA-Z\\d\\s][^\\\\/:*?\"<>|]+\\.(?i)(png|jpg|jpeg)$";
            if (Pattern.matches(pattern, filename))
                try {

                    Path copyLocation = Paths.get(  path + File.separator, filename);
                    Files.copy(file.getInputStream(), copyLocation, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
                    strResult = awsS3FileUpload(copyLocation.toString(), awddir);

                    System.out.println(awddir);
                    System.out.println(url);
                    System.out.println(filepath);
                    if (strResult.equals("OK")) {
                        Files.delete(copyLocation);

                        lookbookModel.setImg_url(url);
                        lookbookModel.setFilename(filename);
                        lookbookModel.setWriter("jeong.guji");
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                        String strDate = sdf.format(date);
                        lookbookModel.setSave_time(strDate);
                        result = lookbookMapper.lookbookInsert(lookbookModel);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        return result;
    }

    public String awsS3FileUpload(String filename, String awddir) {
        File upFile = new File(filename);
        Regions clientRegion = Regions.AP_NORTHEAST_1;
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(this.AWS_S3_ACCESS_KEY_ID, this.AWS_S3_SECRET_KEY_ID);
        this

                .amazonS3 = (AmazonS3)((AmazonS3ClientBuilder)((AmazonS3ClientBuilder)AmazonS3ClientBuilder.standard().withRegion(clientRegion)).withCredentials((AWSCredentialsProvider)new AWSStaticCredentialsProvider((AWSCredentials)awsCreds))).build();
        try {
            TransferManager tm = TransferManagerBuilder.standard().withS3Client(this.amazonS3).build();
            Upload upload = (awddir.isEmpty()) ? tm.upload(AWS_S3_THUMBNAIL_BUCKETNAME, upFile.getName(), upFile)
                    : tm.upload(AWS_S3_THUMBNAIL_BUCKETNAME + awddir, upFile.getName(), upFile);
            upload.waitForCompletion();
        } catch (AmazonServiceException e) {
            e.printStackTrace();
            return "ERR";
        } catch (SdkClientException e) {
            e.printStackTrace();
            return "ERR";
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "ERR";
        }
        return "OK";
    }

    public LookbookModel selectLookbookDetail(int num) {
        return lookbookMapper.selectLookbookDetail(num);
    }
}
