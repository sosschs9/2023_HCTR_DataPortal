package com.HCTR.data_portal.controller;

import lombok.RequiredArgsConstructor;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/dataportal")
public class FileUploadController {

    private final Configuration hadoopConfig;

    @Autowired
    public FileUploadController(Configuration hadoopConfig) {
        this.hadoopConfig = hadoopConfig;
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestPart("file") MultipartFile file) {
        try {
            // HDFS로 연결
            FileSystem fs = FileSystem.get(hadoopConfig);

            // HDFS에 업로드할 경로 설정
            String hdfsUploadPath = "/hdfs/upload/" + file.getOriginalFilename();
            Path hdfsFilePath = new Path(hdfsUploadPath);

            // 파일 HDFS에 업로드
            fs.copyFromLocalFile(new Path(file.getOriginalFilename()), hdfsFilePath);

            return "File uploaded to HDFS successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload file to HDFS.";
        }
    }
}

