package com.zk.demo.controller;

import com.zk.demo.tools.TicketSeller;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class TestController {

    @Value("${zookeeper.server}")
    private String zkServer;
    @Value("${zookeeper.sessionTimeoutMs}")
    private Integer sessionTimeoutMs;

    @GetMapping("/zk")
    public String findById() {
        TicketSeller ticketSeller1 = new TicketSeller(zkServer, sessionTimeoutMs);
        TicketSeller ticketSeller2 = new TicketSeller(zkServer, sessionTimeoutMs);
        TicketSeller ticketSeller3 = new TicketSeller(zkServer, sessionTimeoutMs);
        ticketSeller1.start();
        ticketSeller2.start();
        ticketSeller3.start();
        return "zk";
    }

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        ZooKeeper zk = new ZooKeeper("120.76.130.43:2182", 6000,
                watchedEvent -> System.out.println("**************已经触发: " + watchedEvent.getType() + "事件"));
        // 创建目录节点
        zk.create("/team", "team".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
        // 创建子节点
        zk.create("/team/western", "western".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
        System.out.println("-----------" + new String(zk.getData("/team", false, null)));

        // 取出子节点数据
        System.out.println("-----------" + zk.getChildren("/team", true));

        // 修改子节点数据
        zk.setData("/team/western", "Northwestern-Southeast-Pacific".getBytes(), -1);
        System.out.println("-----------目录节点状态: [" + zk.exists("/team", true) + "]");

        // 创建另一个子节点
        zk.create("/team/eastern", "eastern".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
        System.out.println("-----------" + new String(zk.getData("/team/eastern", false, null)));

        // 删除子节点
        zk.delete("/team/western", -1);
        zk.delete("/team/eastern", -1);

        // 删除父节点
        zk.delete("/team", -1);

        // 关闭连接
        zk.close();

    }
}
