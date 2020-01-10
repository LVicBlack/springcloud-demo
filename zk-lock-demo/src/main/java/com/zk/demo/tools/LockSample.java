package com.zk.demo.tools;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class LockSample {

    private ZooKeeper zkClient;
    private static final String LOCK_ROOT_PATH = "/Locks";
    private static final String LOCK_NODE_NAME = "Lock_";
    private String lockPath;

    /**
     * 可以设置观察的操作：exists,getChildren,getData
     * 可以触发观察的操作：create,delete,setData
     */
    private Watcher watcher = new Watcher() {
        @Override
        public void process(WatchedEvent watchedEvent) {
            System.out.println(Thread.currentThread().getName() + watchedEvent.getPath() + " 前锁释放");
            synchronized (this) {
                notifyAll();
            }
        }
    };

    public LockSample(@Value("${zookeeper.server}") String zkServer,
                      @Value("${zookeeper.sessionTimeoutMs}") int sessionTimeoutMs) throws IOException {
        zkClient = new ZooKeeper(zkServer, sessionTimeoutMs, event -> {
            if (event.getState() == Watcher.Event.KeeperState.Disconnected) {
                System.out.println("失去连接");
            }
        });
    }

    public void acquireLock() throws KeeperException, InterruptedException {
        // 创建锁节点
        createLock();
        // 尝试获取锁
        attemptLock();
    }

    private void createLock() throws KeeperException, InterruptedException {
        // 如果根节点不存在，创建根节点
        Stat stat = zkClient.exists(LOCK_ROOT_PATH, false);
        if (stat == null) {
            zkClient.create(LOCK_ROOT_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        // 创建PHEMERAL_SEQUENTIAL类型(临时顺序)节点
        String lockPath = zkClient.create(LOCK_ROOT_PATH + "/" + LOCK_NODE_NAME,
                Thread.currentThread().getName().getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(Thread.currentThread().getName() + " 锁创建: " + lockPath);
        this.lockPath = lockPath;
    }

    /**
     * 1.每个客户端往/exlusive_lock下创建有序临时节点/exlusive_lock/lock_。
     * 创建成功后/exlusive_lock下面会有每个客户端对应的节点，
     * 如/exlusive_lock/lock_000000001
     * <p>
     * 2.客户端取得/exlusive_lock下子节点，并进行排序，判断排在最前面的是否为自己。
     * <p>
     * 3.如果自己的锁节点在第一位，代表获取锁成功，此客户端执行业务逻辑
     * <p>
     * 4.如果自己的锁节点不在第一位，则监听自己前一位的锁节点。
     * 例如，自己锁节点lock_000000002，那么则监听lock_000000001.
     * <p>
     * 5.当前一位锁节点（lock_000000001）对应的客户端执行完成，释放了锁，
     * 将会触发监听客户端（lock_000000002）的逻辑。
     * <p>
     * 6.监听客户端重新执行第2步逻辑，判断自己是否获得了锁。
     */
    private void attemptLock() throws KeeperException, InterruptedException {
        // 获取Lock所有子节点，按照节点序号排序
        List<String> lockPaths = null;
        lockPaths = zkClient.getChildren(LOCK_ROOT_PATH, false);
        Collections.sort(lockPaths);
        int index = lockPaths.indexOf(lockPath.substring(LOCK_ROOT_PATH.length() + 1));

        //如果lockpath是序号最小的节点，则获取锁
        if (index == 0) {
            System.out.println(Thread.currentThread().getName() + " 锁获得, lockpath: " + lockPath);
            return;
        } else {
            // lockPath不是序号最小的节点，监听前一个节点
            String preLockPath = lockPaths.get(index - 1);

            Stat stat = zkClient.exists(LOCK_ROOT_PATH + "/" + preLockPath, watcher);
            // 假如前一个节点不存在了，比如说执行完毕，或者执行节点掉线，重新获取锁
            if (stat == null) {
                attemptLock();
            } else { // 阻塞当前进程，直到preLockPath释放锁，被watcher观察到，notifyAll后，重新acquireLock
                System.out.println(Thread.currentThread().getName() + " 等待前锁释放，prelocakPath：" + preLockPath);
                synchronized (watcher) {
                    watcher.wait();
                }
            }
            attemptLock();
        }
    }

    //释放锁的原语实现
    public void releaseLock() throws KeeperException, InterruptedException {
        zkClient.delete(lockPath, -1);
        zkClient.close();
        System.out.println(Thread.currentThread().getName() + " 锁释放：" + lockPath);
    }
}
