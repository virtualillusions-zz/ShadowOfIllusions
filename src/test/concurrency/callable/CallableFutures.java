/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.concurrency.callable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * The executor framework presented in the last chapter works with Runnables.
 * Runnable do not return result.
 *
 * In case you expect your threads to return a computed result you can use
 * java.util.concurrent.Callable. The Callable object allows to return values
 * after competition.
 *
 * The Callable object uses generics to define the type of object which is
 * returned.
 *
 * If you submit a Callable object to an Executor the framework returns an
 * object of type java.util.concurrent.Future. This Future object can be used to
 * check the status of a Callable and to retrieve the result from the Callable.
 *
 * On the Executor you can use the method submit to submit a Callable and to get
 * a future. To retrieve the result of the future use the get() method.
 *
 * @author Kyle
 */
public class CallableFutures {

    private static final int NTHREDS = 10;

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(NTHREDS);
        List<Future<Long>> list = new ArrayList<Future<Long>>();
        for (int i = 0; i < 20000; i++) {
            Callable<Long> worker = new MyCallable();
            Future<Long> submit = executor.submit(worker);
            list.add(submit);
        }
        long sum = 0;
        System.out.println(list.size());
        // Now retrieve the result
        for (Future<Long> future : list) {
            try {
                sum += future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        System.out.println(sum);
        executor.shutdown();
    }
}
