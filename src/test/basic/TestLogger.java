/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.basic;

/**
 *
 * @author Kyle Williams
 */
public class TestLogger {

    public static void main(String[] args) {
        //ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        //root.setLevel(ch.qos.logback.classic.Level.ERROR);
        
        java.util.logging.LogManager.getLogManager().reset();
        org.slf4j.bridge.SLF4JBridgeHandler.install();
        java.util.logging.Logger.getLogger("global").setLevel(java.util.logging.Level.FINEST);


        java.util.logging.Logger.getLogger(TestLogger.class.getName()).info("Hello BRO");

        org.slf4j.LoggerFactory.getLogger(TestLogger.class.getName()).trace("Hello world.");
        
        throw new UnsupportedOperationException("TOO MANY/FEW CAMERAS in CameraSystem.java ");
    }
}
