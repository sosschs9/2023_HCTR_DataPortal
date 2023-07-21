package com.HCTR.data_portal.service;

import com.HCTR.data_portal.config.HadoopConfigChecker;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Component
public class MyService {

    private final HadoopConfigChecker hadoopConfigChecker;

    @Autowired
    public MyService(HadoopConfigChecker hadoopConfigChecker) {
        this.hadoopConfigChecker = hadoopConfigChecker;
    }

    public void doSomething() throws Exception {
        // Hadoop 구성 확인 작업 호출
        hadoopConfigChecker.checkHadoopConfiguration();
        hadoopConfigChecker.setHdfsACL("/test","test");

    }

    // 파일 업로드 함수
    public void uploadFileToHDFS(MultipartFile file, String hdfsFilePath) {
        Configuration hadoopConfig = hadoopConfigChecker.getHadoopConfig();

        try {
            FileSystem fs = FileSystem.get(hadoopConfig);
            Path filePath = new Path(hdfsFilePath);

            // 파일을 HDFS로 업로드
            try (InputStream inputStream = file.getInputStream();
                 OutputStream outputStream = fs.create(filePath)) {

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            System.out.println("파일 업로드 완료: " + hdfsFilePath);
        } catch (IOException e) {
            System.err.println("HDFS 파일 업로드 오류: " + e.getMessage());
        }
    }
}
