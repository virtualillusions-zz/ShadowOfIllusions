/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.basic;

/**
 * The try-with-resources statement is a try statement that declares one or more
 * resources. A resource is an object that must be closed after the program is
 * finished with it. The try-with-resources statement ensures that each resource
 * is closed at the end of the statement. Any object that implements
 * java.lang.AutoCloseable, which includes all objects which implement
 * java.io.Closeable, can be used as a resource.
 *
 * To address the problem of programmers leaving far too many heavyweight
 * objects lounging around unclosed on the JVM, the Java 1.7 API has introduced
 * a new interface named java.lang.AutoCloseable which defines a single, simple
 * method to implement.
 *
 * THIS IS MORE USFEUL THAN FINALLY IF THE INTERFACE IS DEFINED
 *
 * @author Kyle
 */
public class TryWithResources {

    public static void main(String[] args) throws Exception {
        try (OpenDoor door = new OpenDoor()) {
            /**
             * One thing that might throw Java developers off is the fact that
             * we are using the try keyword without either a catch or a finally
             * block, which wouldn't be valid with prior versions of the JDK.
             * Just remember that when we have a try without a catch or finally
             * block, it is only valid when providing resource declarations
             * after the try. If you tried to write a try block without a catch,
             * finally, or resource declaration, such as this:
             *
             * try { } you would encounter the following compiler problem:
             *
             * error: 'try' without 'catch', 'finally' or resource declarations
             *
             * By the way, it should be mentioned that using a resource
             * declaration doesn't mean you can't include a catch or finally
             * block. So long as the resource declaration is used properly,
             * standard rules regarding the use of catch and finally blocks
             * continue to apply, so the following code would be completely
             * valid:
             * <code>
             * try (OpenDoor door = new OpenDoor()) {}
             * catch(Exception e) {} // do something
             * finally {} // do something else
             * </code>
             */
        }


        /**
         * Multiple resource declarations are also completely valid. So let's
         * say we added an OpenWindow class into the mix:
         */
        try (OpenDoor door = new OpenDoor();
                OpenWindow window = new OpenWindow()) {
            /**
             * Invocation order of AutoCloseable resources
             *
             * Now, you should be wondering which resource will get closed first
             * when code that lists multiple resources runs.
             *
             * Resources are closed in the reverse order to which they were
             * created, so the window is closed first, and the door is closed
             * second.
             *
             * However;All of the initialization code within the resource
             * declaration is executed in sequential order
             */
        }
    }

    public static class OpenDoor implements AutoCloseable {

        public OpenDoor() {
            System.out.println("The door is open.");
        }

        @Override
        public void close() throws Exception {
            System.out.println("The door is closed.");
        }
    }

    public static class OpenWindow implements AutoCloseable {

        public OpenWindow() {
            System.out.println("The window is open.");
        }

        @Override
        public void close() throws Exception {
            System.out.println("The window is closed.");
        }
    }
}
