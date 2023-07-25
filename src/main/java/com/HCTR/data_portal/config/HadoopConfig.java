package com.HCTR.data_portal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.config.annotation.EnableHadoop;
import org.springframework.data.hadoop.config.annotation.SpringHadoopConfigurerAdapter;
import org.springframework.data.hadoop.config.annotation.builders.HadoopConfigConfigurer;

@Configuration
@EnableHadoop
class HadoopConfig extends SpringHadoopConfigurerAdapter {
    @Override
    public void configure(HadoopConfigConfigurer config) throws Exception {
        config
                .fileSystemUri("hdfs://155.230.118.225:9000");
        config
                .withProperties()
                .property("dfs.nameservices", "mycluster")
                .property("dfs.ha.namenodes.mycluster", "cluster_n1")
                .property("dfs.namenode.rpc-address.mycluster.cluster_n1", "nameNode1:900")
                .property(
                        "dfs.client.failover.proxy.provider.mycluster",
                        "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider"
                );
    }
}