package com.spectre.deck_old;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.spectre.deck;
//
//import com.spectre.deck.stats.StatCapsule;
//import com.jme3.audio.AudioNode;
//import com.jme3.effect.ParticleEmitter;
//import com.spectre.deck.stats.SpecialCapsule;
//import com.spectre.deck.stats.TranslationCapsule;
//import com.spectre.deck.CardStats.*;
//import com.spectre.deck.stats.SpecialCapsule;
//import com.spectre.deck.stats.StatCapsule;
//import com.spectre.deck.stats.TranslationCapsule;
///**
// * Used Primarily By The importer to set all the fields of every card
// * @author Kyle Williams
// */
//public class CardSetter extends Card {
//
//    public void setName(String title) {
//        cardName = title;
//        picLoc = "Deck/CardImages/"+title+".dds";
//    }
//
//    public void setMaxUses(String mx) {
//        maxUses = Integer.parseInt(mx);
//    }
//
//    public void setPredecessor(String prev) {
//        predecessor = prev;
//    }
//
//    public void addSuccessor(String next) {
//        successor.add(next);
//    }
//
//    public void setDescription(String description) {
//        desc = description;
//    }
//
//    public void setAnimName(String animationName) {
//        animName = animationName;
//    }
//
//    public void setSeries(String s) {
//        series = CardSeries.valueOf(s);
//    }
//
//    public void setTrait(String t) {
//        trait = CardTrait.valueOf(t);
//    }
//
//    public void setRange(String r) {
//        range = CardRange.valueOf(r);
//    }
//
//    public void setAngle(String a) {
//        angle = Float.parseFloat(a);
//    }
//
//    public void setHPStat(String value) {
//        HP = new StatCapsule(value);
//    }
//
//    public void setMPStat(String value) {
//        MP = new StatCapsule(value);
//    }
//
//    public void setKnockbackStat(String value) {
//        knockback = new StatCapsule(value);
//    }
//
//    public void setSpeedStat(String value) {
//        speed = new StatCapsule(value);
//    }
//
//    public void setDeffenseStat(String value) {
//        deffense = new StatCapsule(value);
//    }
//
//    public void setSpecialStat(String value) {
//        special = new SpecialCapsule(value);
//    } 
//
//    public void setTranslationStat(String value) {
//        trans = new TranslationCapsule(value);
//    }
//
//    public void addEffect(ParticleEmitter effect) {
//        fx.add(effect);
//    }
//
//    public void addSoundEffect(AudioNode soundFx) {
//        sfx.add(soundFx);
//    }    
//}
