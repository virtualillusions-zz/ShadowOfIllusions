/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.zay_es.basic;

import java.sql.SQLException;
import org.hsqldb.util.DatabaseManagerSwing;

/**
 *
 * @author Kyle Williams
 */
public class TestDatabaseViewer {

    public static void main(String[] args) throws SQLException {
        DatabaseManagerSwing dms = new DatabaseManagerSwing();
        dms.main();
    }
}
