package com.zk.demo.tools;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

public class Mutex extends Thread{
    @Override
    public void run() {
        super.run();
    }

    public static void soldTickWithLock(CuratorFramework client) throws Exception {
        //创建分布式锁, 锁空间的根节点路径为/curator/lock
        InterProcessMutex mutex = new InterProcessMutex(client, "/curator/locks");
        mutex.acquire();

        //获得了锁, 进行业务流程
        //代表复杂逻辑执行了一段时间
        int sleepMillis = (int) (Math.random() * 2000);
        Thread.sleep(sleepMillis);

        //完成业务流程, 释放锁
        mutex.release();
    }
}
