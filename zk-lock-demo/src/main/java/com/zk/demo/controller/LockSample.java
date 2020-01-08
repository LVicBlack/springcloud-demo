package com.zk.demo.controller;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.List;

public class LockSample {

    @Value("${zookeeper.server}")
    private String zkServer;
    @Value("${zookeeper.sessionTimeoutMs}")
    private int sessionTimeoutMs;

    private ZooKeeper zkClient;
    private static final String LOCK_ROOT_PATH = "/Locks";
    private static final String LOCK_NODE_NAME = "Lock_";
    private String lockPath;

    public LockSample() throws IOException {
        zkClient = new ZooKeeper(zkServer, sessionTimeoutMs, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if (event.getState() == Event.KeeperState.Disconnected) {
                    System.out.println("失去连接");
                }
            }
        });
    }

    public void acquireLock() throws KeeperException, InterruptedException {
        // 创建锁节点
        createLock();
        // 尝试获取锁
        attempLock();
    }

    private void createLock() throws KeeperException, InterruptedException {
        // 如果根节点不存在，创建根节点
        Stat stat = zkClient.exists(LOCK_ROOT_PATH, false);
        if(stat == null){
            zkClient.create(LOCK_ROOT_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);
        }
        // 创建PHEMERAL_SEQUENTIAL类型(临时顺序)节点
        String lockPath = zkClient.create(LOCK_ROOT_PATH+"/"+LOCK_NODE_NAME,
                Thread.currentThread().getName().getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(Thread.currentThread().getName()+" 锁创建: "+lockPath);
        this.lockPath = lockPath;
    }

    /**
     * 1.每个客户端往/exlusive_lock下创建有序临时节点/exlusive_lock/lock_。
     创建成功后/exlusive_lock下面会有每个客户端对应的节点，
     如/exlusive_lock/lock_000000001

     2.客户端取得/exlusive_lock下子节点，并进行排序，判断排在最前面的是否为自己。

     3.如果自己的锁节点在第一位，代表获取锁成功，此客户端执行业务逻辑

     4.如果自己的锁节点不在第一位，则监听自己前一位的锁节点。
     例如，自己锁节点lock_000000002，那么则监听lock_000000001.

     5.当前一位锁节点（lock_000000001）对应的客户端执行完成，释放了锁，
     将会触发监听客户端（lock_000000002）的逻辑。

     6.监听客户端重新执行第2步逻辑，判断自己是否获得了锁。
     */
    private void attempLock() throws KeeperException, InterruptedException {
        // 获取Lock所有子节点，按照节点序号排序
        List<String> lockPaths = null;
        lockPaths = zkClient.getChildren(LOCK_ROOT_PATH,false);

    }

}
