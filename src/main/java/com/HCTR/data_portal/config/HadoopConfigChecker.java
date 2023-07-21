package com.HCTR.data_portal.config;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.AclEntry;
import org.apache.hadoop.fs.permission.AclEntryScope;
import org.apache.hadoop.fs.permission.AclEntryType;
import org.apache.hadoop.fs.permission.FsAction;


import java.util.Arrays;
import java.util.Map;

@org.springframework.context.annotation.Configuration
public class HadoopConfigChecker {

    private Configuration hadoopConfig;

    public void checkHadoopConfiguration() {
        hadoopConfig = new Configuration();
        hadoopConfig.set("fs.defaultFS", "hdfs://155.230.118.225:9000");

        // Print Hadoop configuration properties
        for (Map.Entry<String, String> entry : hadoopConfig) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }
    }

    // HDFS에 ACL을 설정하는 메서드
    public void setHdfsACL(String filePath, String userName) throws Exception {
        // Hadoop 클라이언트 생성
        FileSystem fs = FileSystem.get(hadoopConfig);

        // ACL 설정을 원하는 파일 경로
        Path path = new Path(filePath);

        // ACL 항목 생성
        AclEntry aclEntry = new AclEntry.Builder()
                .setType(AclEntryType.USER)
                .setScope(AclEntryScope.DEFAULT)
                .setName(userName)
                .setPermission(FsAction.READ_EXECUTE)
                .build();

        // ACL 설정
        fs.modifyAclEntries(path, Arrays.asList(new AclEntry[]{aclEntry}));

        // 클라이언트 닫기
        fs.close();

        System.out.println("ACL 설정이 완료되었습니다.");
    }

    public Configuration getHadoopConfig() {
        return hadoopConfig;
    }

    // Other methods in your Spring application...
}
