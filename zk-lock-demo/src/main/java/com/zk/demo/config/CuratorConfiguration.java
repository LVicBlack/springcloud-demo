package com.zk.demo.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CuratorConfiguration {

    @Value("${zookeeper.server}")
    private String zkServer;
    @Value("${zookeeper.sessionTimeoutMs}")
    private Integer sessionTimeoutMs;
    @Value("${zookeeper.baseSleepTimeMs}")
    private Integer baseSleepTimeMs;
    @Value("${zookeeper.maxRetries}")
    private Integer maxRetries;

    @Bean
    public CuratorFramework createZkClient() throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(zkServer)
                .sessionTimeoutMs(sessionTimeoutMs)
                .retryPolicy(retryPolicy).build();
        client.start();
        watchChildren(client,"/curator/locks");
        return client;
    }

    private void watchChildren(CuratorFramework client, String path) throws Exception {
        PathChildrenCache childrenCache = new PathChildrenCache(client, path, true);
        PathChildrenCacheListener childrenCacheListener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {
                System.out.println("::::::开始监听子节点::::::");
                ChildData data = event.getData();
                switch (event.getType()) {
                    case CHILD_ADDED:
                        System.out.println("*-*-*-*-*CHILD_ADDED:::::" + data.getPath() + "数据:::::" + data.getData() + "*-*-*-*-*");
                        break;
                    case CHILD_REMOVED:
                        System.out.println("*-*-*-*-*CHILD_REMOVED:::::" + data.getPath() + "数据:::::" + data.getData() + "*-*-*-*-*");
                        break;
                    case CHILD_UPDATED:
                        System.out.println("*-*-*-*-*CHILD_UPDATED:::::" + data.getPath() + "数据:::::" + data.getData() + "*-*-*-*-*");
                        break;
                    default:
                        break;
                }
            }
        };
        childrenCache.getListenable().addListener(childrenCacheListener);
        System.out.println("Register zk watcher successfully!");
        childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
    }
}
