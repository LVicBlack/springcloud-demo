package com.zk.demo.tools;


public class OrderIncrease extends Thread{
//    private static AtomicInteger applecount = new AtomicInteger(50);
    private static Integer applecount = 50;
    // 控制器 true通行
    private static boolean flag = true;

    @Override
    public void run() {
        for(int i = 0 ; i< 50; i++){
            try {
                eat();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

//    private synchronized void eat(){
//        if(applecount.get() > 0){
//            System.out.println(getName()+"第"+applecount+"个");
//            applecount.decrementAndGet();
//        }
//    }

    private synchronized void eat() throws InterruptedException {
        while(!flag){
            wait();
        }
        flag = false;
        if(applecount > 0){
            System.out.println(getName()+"第"+applecount+"个");
            applecount--;
        }
        flag = true;
        notifyAll();
    }

    public static void main(String[] args) {
        OrderIncrease t1 = new OrderIncrease();
        OrderIncrease t2 = new OrderIncrease();
        t1.start();
        t2.start();
    }
}
