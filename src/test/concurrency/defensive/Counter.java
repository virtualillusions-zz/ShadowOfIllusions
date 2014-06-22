/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.concurrency.defensive;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Java 5.0 provides supports for additional atomic operations. This allows to
 * develop algorithm which are non-blocking algorithm, e.g. which do not require
 * synchronization, but are based on low-level atomic hardware primitives such
 * as compare-and-swap (CAS). A compare-and-swap operation check if the variable
 * has a certain value and if it has this value it will perform this operation.
 *
 * Non-blocking algorithm are usually much faster then blocking algorithms as
 * the synchronization of threads appears on a much finer level (hardware).
 *
 * For example this created a non-blocking counter which always increases. This
 * example is contained in the project called
 * de.vogella.concurrency.nonblocking.counter.
 *
 * @author Kyle
 */
public class Counter {

    private AtomicInteger value = new AtomicInteger();

    public int getValue() {
        return value.get();
    }

    /**
     * The interesting part is how the incrementAndGet() method is implemented.
     * It uses a CAS operation.
     * <code>
     * public final int incrementAndGet() {
     *      for (;;) {
     *          int current = get();
     *          int next = current + 1;
     *          if (compareAndSet(current, next))
     *              return next;
     *          }
     * }
     * </code>
     *
     * The JDK itself makes more and more use of non-blocking algorithms to
     * increase performance for every developer. Developing correct non-blocking
     * algorithm is not a trivial task.
     *
     * @return
     */
    public int increment() {
        return value.incrementAndGet();
    }

    // Alternative implementation as increment but just make the 
    // implementation explicit
    public int incrementLongVersion() {
        int oldValue = value.get();
        while (!value.compareAndSet(oldValue, oldValue + 1)) {
            oldValue = value.get();
        }
        return oldValue + 1;
    }
}