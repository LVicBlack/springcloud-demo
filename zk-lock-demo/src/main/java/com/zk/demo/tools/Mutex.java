package com.zk.demo.tools;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Mutex extends Thread {
    private CuratorFramework client;

    public Mutex(CuratorFramework client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 50; i++) {
                System.out.println("************线程" + Thread.currentThread().getName() + "************ ::::: " + i);
                Mutex.soldTickWithLock(client);
            }
        } catch (Exception ignored) {
        }
    }

    public static void soldTickWithLock(CuratorFramework client) throws Exception {
        //创建分布式锁, 锁空间的根节点路径为/curator/lock
        InterProcessMutex mutex = new InterProcessMutex(client, "/curator/locks");
        mutex.acquire();

        //获得了锁, 进行业务流程
        //代表复杂逻辑执行了一段时间
        System.out.println("---------do thx---------");
        int sleepMillis = (int) (Math.random() * 2000);
        Thread.sleep(sleepMillis);

        //完成业务流程, 释放锁
        mutex.release();
    }

    public static void main(String[] args) {
        int[] nums = {-1, 0, 1, 2, -1, -4
        };
        List<List<Integer>> lists = Solution.threeSum(nums);
        System.out.println(lists);
    }

    static class Solution {
        public static List<List<Integer>> threeSum(int[] nums) {
            List<List<Integer>> ls = new ArrayList<>();
            if(nums.length<3) return ls;
            // 排序
            Arrays.sort(nums);
            if (nums[0] <= 0 && nums[nums.length - 1] >= 0) { // 优化1: 整个数组同符号，则无解
                for (int i = 0; i < nums.length - 2; i++) {
                    // 优化2: 最左值为正数则一定无解
                    if (nums[i] > 0) break;
                    if(i==0 || nums[i] != nums[i-1]){// 去重
                        int l = i + 1;
                        int r = nums.length - 1;
                        while (l < r) {
                            int sum = nums[i] + nums[r] + nums[l];
                            if (sum == 0) {
                                ls.add(Arrays.asList(nums[i], nums[l], nums[r]));
                                while (l < r && nums[l] == nums[l+1]) l++;// 去重
                                while (l < r && nums[r] == nums[r-1]) r--;// 去重
                                l++;
                                r--;
                            } else if (sum < 0) {
                                l++;
                            } else {
                                r--;
                            }
                        }
                    }
                }
            }
            return ls;
        }
    }
}
