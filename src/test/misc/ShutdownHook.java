/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.misc;

/**
 *
 * @author Kyle Williams
 */
public class ShutdownHook {

    public static void main(String[] args) {
        //When the virtual machine begins its shutdown sequence it will start all registered 
        //shutdown hooks in some unspecified order and let them run concurrently. 
        //When all the hooks have finished it will then run all uninvoked finalizers if 
        //finalization-on-exit has been enabled. Finally, the virtual machine will halt.
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("GOOD BYE");
            }
        });
    }
}
