package com.zk.demo.controller;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.zookeeper.ClientCnxn;
import org.apache.zookeeper.KeeperException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class TicketSeller extends Thread {

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
        LockSample lock = new LockSample();
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

    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
        Logger logger = LogManager.getLogger(ClientCnxn.class);
        logger.setLevel(Level.OFF);

        TicketSeller ticketSeller1 = new TicketSeller();
        TicketSeller ticketSeller2 = new TicketSeller();
        TicketSeller ticketSeller3 = new TicketSeller();
        ticketSeller1.start();
        ticketSeller2.start();
        ticketSeller3.start();
    }

    @GetMapping("/zk")
    public String findById() {
        TicketSeller ticketSeller1 = new TicketSeller();
        TicketSeller ticketSeller2 = new TicketSeller();
        TicketSeller ticketSeller3 = new TicketSeller();
        ticketSeller1.start();
        ticketSeller2.start();
        ticketSeller3.start();
        return "zk";
    }
}
