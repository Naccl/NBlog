package top.naccl.alogorithm;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * CountDownLatch和CyclicBarrier区别：
 * 1.countDownLatch是一个计数器，线程完成一个记录一个，计数器递减，只能只用一次
 * 2.CyclicBarrier的计数器更像一个阀门，需要所有线程都到达，然后继续执行，计数器递增，提供reset功能，可以多次使用
 */
public class TurningPrint {

    public static void main(String[] args){
        // 方案1，使用Condition通知到具体的线程
        ThreadOdd threadOdd = new ThreadOdd("奇线程");
        ThreadEven threadEven = new ThreadEven("偶线程");
        // 即便是先让偶线程启动，还是能顺序打印，condition保证不该打印的时候，进入阻塞队列
        threadEven.start();
        threadOdd.start();

        // 方案2，使用countDownLetch保证打印奇数的线程先输出
        thread1.start();
        thread2.start();

        // 方案3，
        try {
            turning();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    // 方案1，使用Codition保证线程交替打印
    //java线程并发库中的锁相当与（synchronized）
    // 主/子线程的核心逻辑都要被此锁包裹住
    static Lock lock = new ReentrantLock();
    // 奇数条件
    static Condition condOdd = lock.newCondition();
    // 偶数条件
    static Condition condEven = lock.newCondition();
    // 优先打印奇数
    static volatile boolean bPrintOdd = true;

    // 奇数线程
    static class ThreadOdd extends Thread{
        private String name;
        public ThreadOdd(String name){
            this.name = name;
        }

        @Override
        public void run(){
            lock.lock();
            System.out.println("odd enter lock");
            try {
                // 如果是奇数,打印并加二
                for (int i = 1; i <= 101; i += 2) {
                    // 如果不该奇数线程打印
                    if (!bPrintOdd) {
                        // 通知偶数线程执行
                        condEven.signal();
                        System.out.println("odd wait");
                        if(i > 100){
                            break;
                        }
                        // 奇数线程等待
                        condOdd.await();
                    }
                    System.out.println("odd::"+i);
                    // 修改奇数打印条件为false，让偶数线程打印
                    bPrintOdd = false;
                }
            }catch(Exception e){
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
            // 线程等待时不会走到此处，等待也是上边的业务
            System.out.println("odd finished");
        }
    }

    // 偶数线程
    static class ThreadEven extends Thread{
        private String name;
        public ThreadEven(String name){
            this.name =name;
        }

        @Override
        public void run(){
            lock.lock();
            System.out.println("even enter lock");
            try {
                // 如果是奇数,打印并加二
                for (int i = 2; i <= 100; i += 2) {
                    // 如果应该奇数线程打印
                    if (bPrintOdd) {
                        // 通知偶数线程执行
                        condOdd.signal();
                        System.out.println("even wait");
                        if(i > 100){
                            break;
                        }
                        // 奇数线程等待
                        condEven.await();
                    }
                    System.out.println("even::"+i);
                    // 修改奇数打印条件为true，让奇数线程打印
                    bPrintOdd = true;
                }
            }catch(Exception e){
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
            // 线程等待时不会走到此处，等待也是上边的业务
            System.out.println("even finished");
        }
    }


    // 方案2，使用countDownLetch保证线程的启动顺序
    private static volatile int  i = 1;
    private static CountDownLatch cdl = new CountDownLatch(1);
    private static final Object objLock = new Object();

    static Thread thread1 = new Thread(() -> {
        // 第一个线程先开始打印
        while (i <= 100) {
            // 获取锁
            synchronized (objLock) {
                System.out.println("thread1:" + i);
                // 唤起其他线程
                objLock.notifyAll();
                i++;
                // 将计数器减一，告诉第二个线程可以启动
                cdl.countDown();
                if (i <= 100) {
                    // 如果未结束，让出当前锁，并休眠
                    try {
                        objLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    });

    static Thread thread2 = new Thread(() -> {
        // 第二个线程等待计数器为0才开始执行，保证不优先抢到锁
        try {
            cdl.await();
            while(i <= 100){
                // 获取锁
                synchronized (objLock) {
                    System.out.println("thread2:" + i);
                    // 唤起其他线程
                    objLock.notifyAll();
                    i++;
                    if (i <= 100) {
                        // 如果未结束，让出当前锁，并休眠
                        try {
                            objLock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    });


    // 方案3，线程合并
    public static void turning() throws InterruptedException {
        new Thread(new TurningRunner(), "奇数").start();
        // 确保奇数线程线先获取到锁
        Thread.sleep(1);
        new Thread(new TurningRunner(), "偶数").start();
    }

    static class TurningRunner implements Runnable {
        @Override
        public void run() {
            while (i <= 100) {
                // 获取锁
                synchronized (objLock) {
                    // 拿到锁就打印
                    System.out.println(Thread.currentThread().getName() + ": " + i++);
                    // 唤醒其他线程
                    objLock.notifyAll();
                    try {
                        if (i <= 100) {
                            // 如果任务还没有结束，则让出当前的锁并休眠
                            objLock.wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
