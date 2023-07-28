package com.HCTR.data_portal.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.permission.AclEntry;
import org.apache.hadoop.fs.permission.AclEntryScope;
import org.apache.hadoop.fs.permission.AclEntryType;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import java.util.concurrent.Future;

import org.apache.hadoop.conf.Configuration;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
public class HdfsService {

    @Autowired
    private Configuration hadoopConfiguration;

    // 파일 업로드 (멀티 스레딩)
    @Async("threadPoolTaskExecutor")
    public Future<String> uploadHdfs(MultipartFile file, String fileDir) {
        try {
            System.out.println(Thread.currentThread().getName() + ": Start file upload");
            // Hdfs 파일 시스템 객체 생성
            FileSystem hdfs = FileSystem.get(hadoopConfiguration);
            Path dirPath = new Path(fileDir);
            // 디렉토리 생성 -> ex) /EQ/2021/1_경상남도
            if (!hdfs.exists(dirPath))
                hdfs.mkdirs(new Path(fileDir));

            // 한글 파일일 경우 인코딩 한 후에 디코딩한 파일명을 받아와야 함.
            String encodedFileName = URLEncoder.encode(file.getOriginalFilename(), StandardCharsets.UTF_8.toString());
            String decodedFileName = URLDecoder.decode(encodedFileName, StandardCharsets.UTF_8.toString());
            // 파일 경로 생성
            Path filePath = new Path(fileDir + "/" + decodedFileName);
            // 이미 있는 파일이면 exception
            if (hdfs.exists(filePath)) {
                System.out.println("이미 존재하는 파일 입니다: " + filePath);
                return null;
            }

            FSDataOutputStream fout = hdfs.create(filePath);
            IOUtils.copyBytes(file.getInputStream(), fout, hadoopConfiguration);
            fout.close();

            System.out.println(Thread.currentThread().getName() + ": Success file upload");
            return new AsyncResult<>("File upload completed");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    // 다운로드 할 파일 Zip으로 압축해 클라이언트에게 전달
    public void readHdfsFile(String dirPath, HttpServletResponse response) {
        try {
            System.out.println("search hdfs file");
            // Hdfs 파일 시스템 객체 생성
            FileSystem hdfs = FileSystem.get(hadoopConfiguration);
            // 디렉토리 경로 생성
            Path directory = new Path(dirPath);
            // 디렉토리 존재 여부 확인
            if (!hdfs.exists(directory)) {
                System.out.println("해당 디렉토리가 존재하지 않습니다: " + dirPath);
                return;
            }

            OutputStream fos = response.getOutputStream();
            ZipOutputStream zipOut = new ZipOutputStream(fos);

            // 디렉토리 내에 있는 파일 목록 가져오기
            RemoteIterator<FileStatus> fileStatusIterator = hdfs.listStatusIterator(directory);
            while (fileStatusIterator.hasNext()) {
                FileStatus fileStatus = fileStatusIterator.next();
                if (fileStatus.isFile()) {
                    Path filePath = fileStatus.getPath();
                    System.out.println("파일명: " + filePath.getName());

                    // 파일을 읽어오는 로직을 추가하면 됨
                    InputStream inputStream = hdfs.open(filePath);
                    ZipEntry zipEntry = new ZipEntry(filePath.getName());
                    zipOut.putNextEntry(zipEntry);

                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) > 0) {
                        zipOut.write(buffer, 0, bytesRead);
                    }
                    inputStream.close();
                }
            }
            zipOut.close();
            fos.close();
        } catch (IOException e) {
            log.error("hdfs IOException. message: {}", e.getMessage());
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 해당 경로에 저장된 파일디렉토리 몇개있는지 (현재 디렉토리도 포함됨)
    public long countHdfsFile(String filePath) {
        try {
            FileSystem hdfs = FileSystem.get(hadoopConfiguration);
            Path _filePath = new Path(filePath);

            if (!hdfs.exists(_filePath)) {
                throw new Exception("Not Exist hdfs file");
            }

            long fileCount = hdfs.getContentSummary(_filePath).getDirectoryCount();
            System.out.println("Total number of directory: " + fileCount);
            hdfs.close();

            return fileCount;
        } catch (IOException e) {
            log.error("hdfs IOException. message: {}", e.getMessage());
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setHdfsACL(String filePath, String userName) throws Exception {
        // Hadoop 클라이언트 생성
        FileSystem fs = FileSystem.get(hadoopConfiguration);

        // ACL 설정을 원하는 파일 경로
        Path path = new Path(filePath);

        // ACL 항목 생성
        AclEntry aclEntry = new AclEntry.Builder()
                .setType(AclEntryType.USER)
                .setScope(AclEntryScope.DEFAULT)
                .setName(userName)
                .setPermission(FsAction.WRITE_EXECUTE)
                .build();

        // ACL 설정
        fs.modifyAclEntries(path, Arrays.asList(new AclEntry[]{aclEntry}));
        System.out.println("ACL 설정이 완료되었습니다.");
    }
}