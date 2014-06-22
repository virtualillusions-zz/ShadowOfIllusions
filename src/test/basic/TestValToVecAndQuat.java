/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.basic;

import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import com.spectre.util.math.Vector3fPiece;
import com.spectre.util.math.MathUtil;
        
        /**
         *
         * @author Kyle
         */

public class TestValToVecAndQuat {

    public static void main(String[] args) {
        TempVars vars = TempVars.get();
        Vector3f vec = vars.vect1;
        System.out.println(vec.toString());
        MathUtil.pieceToVec(vec, v);
        System.out.println(vec.toString());        
        vars.release();
    }
    private static Vector3fPiece v = new Vector3fPiece(1,1,1);
}
