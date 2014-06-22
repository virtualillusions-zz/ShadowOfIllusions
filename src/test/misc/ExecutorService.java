/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.misc;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Kyle Williams
 */
public class ExecutorService {

    private static int count = 0;
    private static int execCount = 0;

    public static void main(String[] arg) {
        ScheduledExecutorService exec;
        exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Test(), 0, 20, TimeUnit.MILLISECONDS);
        while (true) {
            count++;
            System.out.println("out: " + count);
        }
    }

    public static class Test implements Runnable {

        public void run() {
            while (true) {
                execCount++;
                System.out.println("Exec: " + execCount);
            }
        }
    }
}
