package com.zk.demo.tools;

import org.apache.zookeeper.KeeperException;

import java.io.IOException;

public class TicketSeller extends Thread {

    private String zkServer;
    private Integer sessionTimeoutMs;

    public TicketSeller(String zkServer, Integer sessionTimeoutMs) {
        this.zkServer = zkServer;
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    private void sell() {
        System.out.println("-----售票开始");
//        // 线程随机休眠，模拟现实的费时操作
//        int sleepMillis = (int) (Math.random() * 20);
//        try {
//            // 代表复杂逻辑执行了一段时间
//            Thread.sleep(sleepMillis);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        System.out.println("售票结束");
    }

    private void sellTicketWithLock() throws KeeperException, InterruptedException, IOException {
        LockSample lock = new LockSample(zkServer, sessionTimeoutMs);
        lock.acquireLock();
        sell();
        lock.releaseLock();
    }

    @Override
    public void run() {
        for (int i = 0; i < 50; i++) {
            try {
                System.out.println("************线程" + Thread.currentThread().getName() + "************ ::::: " + i);
                sellTicketWithLock();
            } catch (InterruptedException | KeeperException | IOException e) {
                e.printStackTrace();
            }
        }
    }

}
