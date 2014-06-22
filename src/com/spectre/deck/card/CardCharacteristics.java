/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.deck.card;

/**
 * <code>CardCharacteristics</code> defines all traits pertaining to
 * <Card>card</code>. It takes extra measures to provide properties to properly
 * set up an editor
 *
 * @author Kyle D. Williams
 */
public class CardCharacteristics {

    /**
     * Set property CardStats.MP_COST to this variable, to identify cards that
     * can be used an infinite amount of times
     */
    public final static int MAX_USE_INFINITE = 0;
    /**
     * Used as default value for CardStats.REQUIRE_CARD to identify cards that
     * do not rely on other cards <br><b>Internal Note: </b> Please do not
     * change value their are hard coded checks based on length in
     * SpectreDuelController that doesn't reference this value
     */
    public final static String NULL_CARD_REQUIREMENT = "";

    /**
     * This is a list of all supported Series in the game it should be noted
     * that each has a subset of series most notable ones are Creationism and
     * Matter Control
     */
    // <editor-fold defaultstate="collapsed" desc="Card Series">                          
    public enum CardSeries {

        RAW, NATURE, TECHNOLOGY, FAITH, CELESTIAL;
    }// </editor-fold> 

    /**
     * This is a list of all Card Traits supported in the game
     */
    // <editor-fold defaultstate="collapsed" desc="Card Trait">                          
    public enum CardTrait {

        DAMAGE(new CardRange[]{
    CardRange.SHORT,
    CardRange.MED,
    CardRange.LONG}),
        DEFENSE(new CardRange[]{CardRange.SELF}),
        ENHANCE(new CardRange[]{CardRange.MISC}),
        CURSE(new CardRange[]{CardRange.MISC}); /*Special, Summon, Transform*/

        private final CardRange[] cr;

        private CardTrait(CardRange[] cardRange) {
            cr = cardRange;
        }
    }// </editor-fold> 

    /**
     * This is a list of all Card Ranges supported in the game
     */
    // <editor-fold defaultstate="collapsed" desc="Card Range">                          
    public enum CardRange {

        LONG,
        MED,
        SHORT,
        SELF(
        new SkillType[]{
    SkillType.BARRIER,
    SkillType.SHELTER,
    SkillType.COUNTER,
    SkillType.PARRY},
        new SkillTypeExtended[]{
    SkillTypeExtended.BRUSH,
    SkillTypeExtended.REFLECT,
    SkillTypeExtended.ANTI_PIERCE}),
        MISC(
        new SkillType[]{SkillType.INSTANT,
    SkillType.TEAM_INSTANT},
        new SkillTypeExtended[]{
    SkillTypeExtended.USER,
    SkillTypeExtended.OPP,
    SkillTypeExtended.BOTH});
        private final SkillType[] sT;
        private final SkillTypeExtended[] sTE;

        private CardRange() {
            sT = new SkillType[]{
                SkillType.PROJECTILE,
                SkillType.MINE,
                SkillType.MELEE
            };
            sTE = new SkillTypeExtended[]{
                SkillTypeExtended.PIERCE,
                SkillTypeExtended.KNOCKBACK,
                SkillTypeExtended.INCREASE_KNOCKBACK_CHANCE
            };
        }

        private CardRange(SkillType[] skillTypes, SkillTypeExtended[] skillTypesExtended) {
            sT = skillTypes;
            sTE = skillTypesExtended;
        }

        /**
         * @return sT an array of supported Skill Types for this Card Range
         */
        public SkillType[] supportedSkillTypes() {
            return sT;
        }

        /**
         * @return sTE an array of supported Skill Types Extended for this Card
         * Range
         */
        public SkillTypeExtended[] supportedSkillTypesExtended() {
            return sTE;
        }
    }// </editor-fold> 

    /**
     * This is a list of all Card Characteristics supported in the game
     */
    // <editor-fold defaultstate="collapsed" desc="Card Stats">                          
    public enum CardStats {
        /////////////////////////////////////////BASIC ATTRIBUTES

        /**
         * The name of the card
         */
        CARD_NAME(String.class),
        /**
         * The
         * <code>Timestamp</code> represented as a long since the card was last
         * modified. <b>This Attribute does not need to be set</b>
         */
        DATE_MODIFIED(Long.class),
        /**
         * The series the card belongs to
         */
        CARD_SERIES(CardSeries.class),
        /**
         * The trait specific to this card
         */
        CARD_TRAIT(CardTrait.class),
        /**
         * The range of affect to this card
         */
        CARD_RANGE(CardRange.class),
        /**
         * The type of skill this card is
         */
        SKILL_TYPE(SkillType.class),
        /**
         * extended specifications of the skill this card is
         */
        SKILL_TYPE_EXTENDED(SkillTypeExtended.class),
        /**
         * The total amount of times this card can be used<br/> -1 stands for
         * unlimited use please use CardCharacteristics.MAX_USE_INFINITE Range:
         * 0 to 3
         *
         */
        MAX_USE(0, 0, 3),
        /**
         * The amount of Focus it takes to use this card<br/> Range: 0 to 10
         */
        MP_COST(1, 0, 10),
        /**
         * The amount of HP it may takes to use this card <br/> Range: 0 to 10
         */
        HP_COST(0, 0, 10),
        /**
         * The cards the player is required to hold in hand to play this card.
         * In the case of multiple cards reference either card cyclically ie
         * card1>card2>card3>card1 <br/><b>Please use
         * CardCharacteristics.NULL_CARD_REQUIREMENT to identify </b>
         */
        REQUIRE_CARD(String.class),
        /**
         * A description of this card for background information
         */
        SKILL_DESCRIPTION(String.class),
        ///////////////////////////////////////////Common Type Stats   
        /**
         * Determines if the card is automatically deployed on contact(player
         * attacked)
         */
        AUTO_USE(Boolean.class),
        /**
         * Determines if the card can be performed in mid air<br> <b>This
         * generally means on descent</b>
         */
        IN_AIR(Boolean.class),
        /**
         * Lunges the user either forward or backward by a specified amount
         */
        LUNGE(0, -10, 10),
        /**
         * decreases user knockback probability
         */
        DECREASE_PLAYER_KNOCKBACK(Boolean.class),
        /**
         * Shuffles deck of target either user or opponent
         */
        SHUFFLE_SKILLS(Boolean.class),
        /**
         * Resets all duration stats of target
         */
        STATUS_RESET(Boolean.class),
        /**
         * Performs after button released
         */
        HOLD_AND_WAIT(Boolean.class),
        /**
         * Reduced target MP by amount <br/><b>Override MP_COST</b>
         */
        MP_COST_SPECIAL(MP.class),
        /**
         * Reduces user MP by amount <br/><b>Override MP_COST</b>
         */
        MP_INCREASE_SPECIAL(MP.class),
        /**
         * Special Erase Skills which affects targets hand.<br/> If
         * CardSeries,CardTrait,CardRange chosen please store in ERASE_SKILL_ETC
         */
        ERASE_SKILL(EraseSkill.class),
        /**
         * Extended property to ERASE_SKILL only valid if CardSeries,CardTrait
         * or CardRange was chosen to store enum value
         */
        ERASE_SKILL_ETC(String.class),
        //////////////////////////////////////////Attack Type Values  
        /**
         * The Attack power of the card, in other words the amount of hp that
         * will be negated if the attack makes contact
         */
        ATTACK_STRENGTH(3, 1, 10),
        /**
         * Special Attack Power Rating properties <br/><b>Overides Attack
         * Power</b>
         */
        SPECIAL_ATTACK_STRENGTH(SpecialAttackStrength.class),
        /**
         * The homing strength of the card attack
         */
        ATTACK_ACCURACY(0, 0, 5),
        /**
         * The speed of the card attack
         */
        ATTACK_VELOCITY(3, 1, 5),
        /**
         * Longer button held stronger the attack damage.
         */
        HOLD_TO_POWER(Boolean.class),
        /**
         * Area of Effect a widened area of effect upon contact<br/> The radius
         * of the A.O.E (blast radius)
         */
        A_O_E_Radius(1, 1, 10),
        /**
         * The length of time A.O.E. will be in effect, useful for lingering
         * effects such as a poison cloud, or a prolonged mine explosion
         */
        A_O_E_Duration(1, 1, 30),
        /**
         * Makes the attack travel on ground
         */
        ATTACK_CRAWLER(Boolean.class),
        /**
         * Determines the path shape the attack will take
         */
        ATTACK_PATH(AttackPath.class),
        /**
         * Makes the Attack a mine type
         */
        MINE_TYPE(MineType.class),
        ////////////////////////////////////////////////////////STATS tthese are all temporary stats that 
        /**
         * Increases or Decreases HP of user or target by a specified amount
         * over a length of time<br/><b>Overrides HP_COST</b>
         */
        HP_STAT,
        /**
         * Increases or Decreases Focus Level of user or target by a specified
         * amount over a length of time
         */
        MP_LEVEL_STAT,
        /**
         * Increases or decreases attack power of user or target by a specified
         * amount over a length of time
         */
        ATTACK_POWER_STAT,
        /**
         * Increases or decreases user or target movement speed by a specified
         * amount of time <br/> <b>Amount variable ignored</b>
         */
        DEXTERITY_STAT,
        /**
         * Immobilize user or target over a specified amount of time
         */
        IMMOBILIZE_STAT,
        /**
         * Prevents users from using cards in hand<br/> <b>Amount variable
         * ignored</b>
         */
        PARALYZE_STAT,
        /**
         * Increases or decreases user or target homing accuracy by a specified
         * amount of time <br/> <b>Amount variable ignored</b>
         */
        HOMING_ACCURACY_STAT,
        /**
         * Mixes user or targets movement and cards in hand for a specified
         * amount of time<br/> <b>Amount variable ignored</b>
         */
        CONFUSE_STAT,
        /**
         * Makes user or target invisible and prevents target lock on for a
         * specified amount of time.<br/> <b>Amount variable ignored</b>
         */
        INVISIBLE_STAT,
        /**
         * Allows user or target to involuntarily jump a specified distance from
         * the ground, This state also allows and individual to float by setting
         * the duration attribute
         */
        JUMP_STAT,
        /**
         * Increases or decreases user or target damage taken over a specified
         * amount of time by a specified amount
         */
        CONSTITUTION_STAT;
        private final Class cls;
        private final Class specialType;
        private final Object obj;//used only for CommonStatType

        private CardStats(int default_Amnt, int minimum_Amount, int max_Amount) {
            cls = Integer.class;//for Common Stat
            specialType = IntegerStat.class;
            obj = new Integer[]{default_Amnt, minimum_Amount, max_Amount};
        }

        private CardStats() {
            cls = String.class;//for duration stat
            specialType = DurationStat.class;
            obj = null;
        }

        private CardStats(Class classType) {
            cls = classType;
            specialType = null;
            obj = null;
        }

        public Class getClassType() {
            return cls;
        }

        public Class getSpecialType() {
            return specialType;
        }

        public Object getClassParams() {
            return obj;
        }

        public boolean sameClass(Object given) {
            return cls.equals(given.getClass());
        }
    }// </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="This is a list of all Supporting Card Characteristics">      
    public enum SkillType {

        PROJECTILE, MINE, MELEE, INSTANT, TEAM_INSTANT, BARRIER, SHELTER, PARRY, COUNTER;
    }

    public enum SkillTypeExtended {
        //defTypeStats

        BRUSH, REFLECT, ANTI_PIERCE,
        //instTypeStats
        USER, OPP, BOTH,
        //attkTypeStats
        PIERCE, KNOCKBACK, INCREASE_KNOCKBACK_CHANCE;
    }

    public enum MineType {

        REGULAR, REMOTE, TIMED_5, TIMED_10, TIMES_15, TIMES_20, TIMES_25, TIMES_30
    }

    public enum AttackPath {

        LINEAR, CURVED, ZIG_ZAG, SERPENTINE, RAIN;
    }

    public enum MP {

        NONE, DAMAGE_DEALT, PERCENT_25, PERCENT_50, PERCENT_75, PERCENT_100;
    }

    public enum SpecialAttackStrength {

        NONE, LENGTH_HELD, USER_MP, USER_MP_LEVEL, OPPONENT_MP, OPPONENT_MP_LEVEL;
    }

    public enum EraseSkill {

        NONE, SAME_BUTTON, RANDOM_SKILL, CARD_SERIES, CARD_TRAIT, CARD_RANGE;
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="Utility Classes">    
    /**
     * <code>DurationStat</code> is a utility class to support duration type
     * properties in CardStats
     */
    public static class DurationStat {

        private boolean userChanced;
        private boolean targetChanced;
        private int userAmount;
        private int targetAmount;
        private int userDuration;
        private int targetDuration;
        private static final int maxAmount = 10;
        private static final int maxDuration = 30;

        public DurationStat() {
            userChanced = false;
            targetChanced = false;
            userAmount = 0;
            targetAmount = 0;
            userDuration = 0;
            targetDuration = 0;
        }

        /**
         *
         * Chanced: Boolean, determines if property is chanced Amount: Integer,
         * the of this property Duration: Integer, the duration of this property
         * String set = Chanced:Amount:Duration user,target = Set
         *
         * @param stat user_target
         */
        public DurationStat(String stat) {
            String[] s = stat.split("_");
            String[] user = s[0].split(":");
            userChanced = Boolean.getBoolean(user[0]);
            userAmount = Integer.getInteger(user[1]);
            userDuration = Integer.getInteger(user[2]);
            String[] target = s[1].split(":");
            targetChanced = Boolean.getBoolean(target[0]);
            targetAmount = Integer.getInteger(target[1]);
            targetDuration = Integer.getInteger(target[2]);
        }

        /**
         * @return if the user effects of the property are Chanced
         */
        public boolean isUserChanced() {
            return userChanced;
        }

        /**
         * @param userChanced are the effects for the user chanced
         */
        public void setUserChanced(boolean userChanced) {
            this.userChanced = userChanced;
        }

        /**
         * @return if the target effects of the property are Chanced
         */
        public boolean isTargetChanced() {
            return targetChanced;
        }

        /**
         * @param targetChanced are the effects for the target chanced
         */
        public void setTargetChanced(boolean targetChanced) {
            this.targetChanced = targetChanced;
        }

        /**
         * @return the userAmount
         */
        public int getUserAmount() {
            return userAmount;
        }

        /**
         *
         * @param userAmount the userAmount to set
         */
        public boolean setUserAmount(int usrAmnt) {
            if (usrAmnt >= -maxAmount && usrAmnt <= maxAmount) {
                userAmount = usrAmnt;
                return true;
            }
            return false;
        }

        /**
         * @return the targetAmount
         */
        public int getTargetAmount() {
            return targetAmount;
        }

        /**
         *
         * @param targetAmount the targetAmount to set
         */
        public boolean setTargetAmount(int trgtAmnt) {
            if (trgtAmnt >= -maxAmount && trgtAmnt <= maxAmount) {
                targetAmount = trgtAmnt;
                return true;
            }
            return false;
        }

        /**
         * @return the userDuration
         */
        public int getUserDuration() {
            return userDuration;
        }

        /**
         * -1 stands for permanent
         *
         * @param userDuration the userDuration to set
         */
        public boolean setUserDuration(int usrDrtn) {
            if (usrDrtn >= -1 && usrDrtn <= maxDuration) {
                userDuration = usrDrtn;
                return true;
            }
            return false;
        }

        /**
         * @return the targetDuration
         */
        public int getTargetDuration() {
            return targetDuration;
        }

        /**
         * -1 stands for permanent
         *
         * @param targetDuration the targetDuration to set
         */
        public boolean setTargetDuration(int trgtDrtn) {
            if (trgtDrtn >= -1 && trgtDrtn <= maxDuration) {
                targetDuration = trgtDrtn;
                return true;
            }
            return false;
        }

        /**
         * @return the maxAmount
         */
        public static int getMaxAmount() {
            return maxAmount;
        }

        /**
         * @return the maxDuration
         */
        public static int getMaxDuration() {
            return maxDuration;
        }

        @Override
        public String toString() {
            String user = isUserChanced() + ":" + getUserAmount() + ":" + getUserDuration();
            String target = isTargetChanced() + ":" + getTargetAmount() + ":" + getTargetDuration();
            return user + "_" + target;
        }
    }

    public static class IntegerStat {

        public final int minAmount;
        private int defAmnt;
        public final int maxAmount;

        public IntegerStat(Integer[] values) {
            defAmnt = values[0];
            minAmount = values[1];
            maxAmount = values[2];
        }

        public IntegerStat(int default_Amnt, int minimum_Amount, int max_Amount) {
            defAmnt = default_Amnt;
            minAmount = minimum_Amount;
            maxAmount = max_Amount;
        }

        /**
         * @return the defAmnt
         */
        public int getDefAmnt() {
            return defAmnt;
        }

        /**
         * @param defAmnt the defAmnt to set
         */
        public boolean setDefAmnt(int default_Amnt) {
            if (default_Amnt >= minAmount && default_Amnt <= maxAmount) {
                defAmnt = default_Amnt;
                return true;
            }
            return false;
        }
    }
    // </editor-fold> 
}
