/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.util;

import com.spectre.deck.card.Card;
import com.spectre.deck.card.CardCharacteristics;
import com.spectre.deck.card.CardCharacteristics.AttackPath;
import com.spectre.deck.card.CardCharacteristics.CardRange;
import com.spectre.deck.card.CardCharacteristics.CardSeries;
import com.spectre.deck.card.CardCharacteristics.CardStats;
import com.spectre.deck.card.CardCharacteristics.CardTrait;
import com.spectre.deck.card.CardCharacteristics.DurationStat;
import com.spectre.deck.card.CardCharacteristics.EraseSkill;
import com.spectre.deck.card.CardCharacteristics.MP;
import com.spectre.deck.card.CardCharacteristics.MineType;
import com.spectre.deck.card.CardCharacteristics.SkillType;
import com.spectre.deck.card.CardCharacteristics.SkillTypeExtended;
import com.spectre.deck.card.CardCharacteristics.SpecialAttackStrength;

/**
 *
 * @author Kyle Williams
 */
public class AbstractCard {

    public static Card createNewCard(final String name, final CardSeries series) {
        return new Card() {
            {

                lockData(false);
                getAnimation().setAnimationName("New Card");//fix to a reoccuring issue
                getPassiveEffect();
                getActiveEffect();
                getContactEffect();
                //Basic attribues
                setData(CardStats.CARD_NAME, name.toUpperCase());
                setData(CardStats.CARD_SERIES, series);
                setData(CardStats.CARD_TRAIT, CardTrait.ENHANCE);
                setData(CardStats.CARD_RANGE, CardRange.MISC);
                setData(CardStats.SKILL_TYPE, SkillType.INSTANT);
                setData(CardStats.SKILL_TYPE_EXTENDED, SkillTypeExtended.USER);
                setData(CardStats.MAX_USE, CardCharacteristics.MAX_USE_INFINITE);
                setData(CardStats.MP_COST, 1);
                setData(CardStats.HP_COST, 0);
                setData(CardStats.REQUIRE_CARD, CardCharacteristics.NULL_CARD_REQUIREMENT);
                setData(CardStats.SKILL_DESCRIPTION, "New Card Skill");
                //Common type stats                
                setData(CardStats.AUTO_USE, false);
                setData(CardStats.LUNGE, 0);
                setData(CardStats.DECREASE_PLAYER_KNOCKBACK, false);
                setData(CardStats.SHUFFLE_SKILLS, false);
                setData(CardStats.STATUS_RESET, false);
                setData(CardStats.HOLD_AND_WAIT, false);
                setData(CardStats.MP_COST_SPECIAL, MP.NONE);
                setData(CardStats.MP_INCREASE_SPECIAL, MP.NONE);
                setData(CardStats.ERASE_SKILL, EraseSkill.NONE);
                setData(CardStats.ERASE_SKILL_ETC, "None");
                //Attack Type Attributes
                setData(CardStats.ATTACK_STRENGTH, 3);
                setData(CardStats.ATTACK_ACCURACY, 0);
                setData(CardStats.ATTACK_VELOCITY, 3);
                setData(CardStats.SPECIAL_ATTACK_STRENGTH, SpecialAttackStrength.NONE);
                setData(CardStats.HOLD_TO_POWER, false);
                setData(CardStats.A_O_E, false);
                setData(CardStats.A_O_E_Radius, 3);
                setData(CardStats.ATTACK_CRAWLER, false);
                setData(CardStats.ATTACK_PATH, AttackPath.LINEAR);
                setData(CardStats.MINE_TYPE, MineType.REGULAR);
                //Duration stats                
                String s = new DurationStat().toString();
                setData(CardStats.HP_STAT, s);
                setData(CardStats.MP_LEVEL_STAT, s);
                setData(CardStats.ATTACK_POWER_STAT, s);
                setData(CardStats.DEXTERITY_STAT, s);
                setData(CardStats.IMMOBILIZE_STAT, s);
                setData(CardStats.PARALYZE_STAT, s);
                setData(CardStats.HOMING_ACCURACY_STAT, s);
                setData(CardStats.CONFUSE_STAT, s);
                setData(CardStats.INVISIBLE_STAT, s);
                setData(CardStats.JUMP_STAT, s);
                setData(CardStats.CONSTITUTION_STAT, s);
                lockData(true);
            }
        };
    }
}
