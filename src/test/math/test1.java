/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.math;

/**
 *
 * @author Kyle
 */
public class test1 {
 
    public static void main(String[] args){
        test1 test = new test1();
        while(true){
            test.play();
        }
    }
    
    public void play(){       
        System.out.println((3 % 7 + 7) % 7);
    }
}
