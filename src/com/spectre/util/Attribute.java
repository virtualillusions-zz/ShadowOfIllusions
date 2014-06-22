/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.util;


/**
 * List of Game Types ranging from characters to Cards
 * @author Kyle Williams
 */
public class Attribute {
 
    /**
     * This is a list of all possible special player movement
     */
    public enum SpecialMovement {
        jump,dash,dive;
    }
    /**
     * This is a list of all possible 
     */
    public enum SpecialWeapon{
        pistol(2,4),sniper(3,1),submachine(1,10),sword(2),katana(2),gauntlet(1);
        int dmg=0;int spd=0;
        private SpecialWeapon(int d){dmg=d;}
        private SpecialWeapon(int d,int s){dmg=d;spd=s;}
        public int getDamage(){return dmg;}
        public int getAttackSpeed(){return spd!=0?spd:null;}
    }
}
