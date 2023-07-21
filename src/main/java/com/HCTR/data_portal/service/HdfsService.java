package com.HCTR.data_portal.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.conf.Configuration;

@Slf4j
@Service
public class HdfsService {

    @Autowired
    private Configuration hadoopConfiguration;

    public void uploadHdfs(MultipartFile file) {
        try {
            System.out.println("upload hdfs file");
            FileSystem hdfs = FileSystem.get(hadoopConfiguration);
            Path outputPath = new Path("/test");


            if (hdfs.exists(outputPath)){
                throw new Exception("Exist hdfs file");
            }

            System.out.println("Make file Path");

            OutputStream outStream = hdfs.create(outputPath);
            IOUtils.copyBytes(file.getInputStream(), outStream, hadoopConfiguration);

            System.out.println("Success file upload");
            hdfs.close();
        } catch (IOException e) {
            log.error("hdfs IOException. message: {}", e.getMessage());
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}