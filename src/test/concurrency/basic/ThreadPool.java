/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.concurrency.basic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Thread pools manage a pool of worker threads. The thread pools contains a
 * work queue which holds tasks waiting to get executed.
 *
 * A thread pool can be described as a collection of Runnable objects (work
 * queue) and a connections of running threads. These threads are constantly
 * running and are checking the work query for new work. If there is new work to
 * be done they execute this Runnable. The Thread class itself provides a
 * method, e.g. execute(Runnable r) to add a new Runnable object to the work
 * queue.
 *
 * The Executor framework provides example implementation of the
 * java.util.concurrent.Executor interface, e.g.
 * Executors.newFixedThreadPool(int n) which will create n worker threads. The
 * ExecutorService adds lifecycle methods to the Executor, which allows to
 * shutdown the Executor and to wait for termination.
 *
 *
 * TIP: If you want to use one thread pool with one thread which executes
 * several runnables you can use the Executors.newSingleThreadExecutor() method.
 *
 * @author Kyle
 */
public class ThreadPool {

    private static final int NTHREDS = 10;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(NTHREDS);
        for (int i = 0; i < 500; i++) {
            Runnable worker = new MyRunnable(10000000L + i);
            executor.execute(worker);
        }
        // This will make the executor accept no new threads
        // and finish all existing threads in the queue
        executor.shutdown();
        // Wait until all threads are finish
        while (!executor.isTerminated()) {
        }
        System.out.println("Finished all threads");
    }
}
