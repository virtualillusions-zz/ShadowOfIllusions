/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.controller.character;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.spectre.controller.character.impl.SpectreControl;
import com.spectre.deck.Hand;
import com.spectre.deck.card.Card;
import com.spectre.deck.card.CardCharacteristics;
import com.spectre.deck.card.CardCharacteristics.CardStats;
import com.spectre.director.Director;
import java.util.LinkedList;

/**
 * TODO: WTF WAS I THINKING WITH THIS CONTROLLER!! This Controller This
 * Controller handles Life And Focus meters as well as its GUI Representation
 * TODO: Focus should equal 0 and be augmented over time when FOCUS passed
 * threshold state is tired when health pass threshold state is injured and
 * takes precedence over FOCUS Attached To Spatial via {@link GameState}
 *
 * @author Kyle Williams
 */
//TODO:Implement All stats in this class this includes speed attack speed as well as cardregeneration time
public class SpectreDuelController extends AbstractControl implements SpectreControl {

    private SpectreAnimationController sac;//set in SpectrePlayerController    
    private static final int STARTING_LIFE_POINTS = 40;
    private static final int STARTING_MANA_LEVEL = 20;
    private static final float FOCUS_REPLENISH_RATE = 5f;
    private SpectreCharacterAttributes attributes;
    private LinkedList<String> deck;
    private Hand cards = new Hand();
    private float cfiTime = 0;//current Focus Increment Time is the value used to check against
    private boolean increaseFocusRate = false;//if true increases focus rate by 1.5%
    private boolean isPressed = false;
    private boolean overTotem = false;//is character touching totem? 
    private int cState;//character state healthy tired or injured used to prevent reseting of idle
    private CardManifestController cmc = null;

    public void startUp() {
        cmc = new CardManifestController();
        spatial.addControl(cmc);
        attributes = new SpectreCharacterAttributes();
    }

    public void cleanUp() {
        attributes = null;
        spatial.removeControl(cmc);
        spatial.removeControl(this);
    }

    public boolean isOverTotem() {
        return overTotem;
    }

    @Override
    protected void controlUpdate(float tpf) {
//Replenishes characters MP
        if (attributes.FOCUS < attributes.FOCUS_LEVEL) {
            if ((cfiTime) >= attributes.REPLENISH_RATE) {
                attributes.FOCUS++;
                cfiTime = 0;
            } else {
                if (!increaseFocusRate) {
                    cfiTime += tpf * 1.5;
                } else {
                    cfiTime += tpf;
                }
            }
        }

//Checks Character State and updates idle animation if any substantial changes
        int characterState = 1; //is the character 1.healthy, 2.tired or 3.injured
        if (attributes.FOCUS <= attributes.FOCUS_LEVEL / 2) {
            characterState = 3;
        } else if (attributes.HP <= attributes.BEGINING_HP / 2) {
            characterState = 2;
        }
        if (characterState != cState) {
            getAnimCont().changeCharState(characterState);
            cState = characterState;
        }
    }

    /**
     * Displays Card Info or new Card Info(if Over Totem)
     *
     * @param i Card Info To Display
     * @param pressed
     */
    public void cardInfo(int i, boolean isPressed) {
        //TODO: SpectreDuelController.cardInfo nifty
        System.out.println(SpectreDuelController.class.getName()
                + ".cardInfo(" + i + ", " + isPressed + ")");
    }

    public void cardPressed(int cardNum, boolean isPressed) {
        //TODO: Somehow get card player is standing over then remove it from scene.
        if (overTotem) {
            remap(cardNum, null);
        } else {
            play(cardNum, isPressed);
        }
    }

    /**
     * Add card to CardManifestController which handles the process of enabling
     * the card
     *
     * @param cardNum
     * @param pressed
     */
    public void play(int cardNum, boolean pressed) {
        if (cards.getCard(cardNum).equals("")) {
            //This if statement is hollow to allow the method to reach after if statement
        } else if (!pressed) {
            cmc.feedBack(cards.getCard(cardNum));
            return;
        } else {
            String cardName = cards.getCard(cardNum);
            Card c = Director.getCard(cardName);
            //first check if card can be played
            int mpCost = c.getData(CardStats.MP_COST);
            int hpCost = c.getData(CardStats.HP_COST);
            String requireCard = c.getData(CardStats.REQUIRE_CARD);
            boolean canPlay = attributes.FOCUS >= mpCost;
            canPlay = canPlay && attributes.HP >= hpCost;

            //Need to run a full check for required cards all cards must be present before user can play card
            boolean requiredCardCheck = true;
            Card[] rCP = null;//used later on in the event of requiredcards to avoid searching for cards again

            if (requireCard.length() != 0) {
                rCP = new Card[4];//required card positions
                int i = 0;//should not pass 3 if it does then theirs something wrong
                String tempString = requireCard;
                while (true) {
                    int handPos = cards.getCardPosition(tempString);
                    //if -1 then this shows that the card has already been removed from hand
                    if (handPos == -1) {
                        requiredCardCheck = false;
                        break;
                    }
                    Card tC = Director.getCard(tempString);
                    rCP[i] = tC;//if throw array out of bounds then problem with card requirements setup
                    tempString = tC.getData(CardStats.REQUIRE_CARD);
                    if (tempString.length() == 0 || tempString.equals(cardName)) {
                        break;
                    }
                    i++;
                }
            }
            
            canPlay = canPlay && requiredCardCheck;

            if (canPlay == true) {
                //add to card queue and check to see if it can be played 
                //this call is how the card is enabled
                canPlay = cmc.addToActiveQueue(c);
                //if it can play then take requirements
                if (canPlay == true) {
                    attributes.FOCUS -= mpCost;
                    attributes.HP -= hpCost;
                    //must check and remove cards from hand if their are card requirements
                    //use null check over boolean check to avoid new boolean above
                    if (rCP != null) {
                        for (int i = 0; i < rCP.length; i++) {
                            Card tC = rCP[i];
                            String tempString = tC.getName();
                            int handPos = cards.getCardPosition(tempString);
                            //increase use counter if has exceeded max use then remove from hand
                            int maxUse = tC.getData(CardStats.MAX_USE);
                            int use = cards.increaseUses(handPos);
                            if (maxUse != CardCharacteristics.MAX_USE_INFINITE && use >= maxUse) {
                                cards.setCard(handPos, "");
                            }
                            tempString = tC.getData(CardStats.REQUIRE_CARD);
                            if (tempString.length() == 0 || tempString.equals(cardName)) {
                                break;
                            }
                        }
                    }
                    //finally increase use counter if has exceeded max use then remove from hand
                    int use = cards.increaseUses(cardNum);
                    int maxUse = c.getData(CardStats.MAX_USE);
                    if (maxUse != CardCharacteristics.MAX_USE_INFINITE && use >= maxUse) {
                        cards.setCard(cardNum, "");
                    }
                    return;
                }
            }
        }
        //if it reaches here then card was not able to be played
        System.out.println("Cannot Play card at this time");
        //TODO:play some sound and show notification or something
    }

    /**
     * Remaps the card binded to a specified position to a new card
     *
     * @param cardNum
     * @param newCardName
     */
    public void remap(int cardNum, String newCardName) {
        cards.setCard(cardNum, newCardName);
    }

    /**
     * Increases FOCUS rate of replenish
     *
     * @param pressed
     */
    public void gatherFocus(boolean pressed) {
        increaseFocusRate = pressed;
    }

    /**
     * Shuffles Active Players Deck and hand
     */
    public void reshuffleDeck() {
        cards.shuffleDeck(deck);
    }

    public void setActiveDeck(LinkedList<String> newDeck) {
        deck = newDeck;
        cards.shuffleDeck(deck);
    }

    public String getNextCard() {
        return deck.poll();
    }

    /**
     * A class that handles all animation for the model Set at characterLoading
     *
     * @return CharacterAnimationController
     */
    public SpectreAnimationController getAnimCont() {
        if (sac == null) {
            sac = spatial.getControl(SpectreAnimationController.class);
        }
        return sac;
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        //SpectreDuelController secTwo = new SpectreDuelController();        
        //TODO: WHATS REALLY NEEDED?
        //secTwo.setSpatial(spatial);
        throw new UnsupportedOperationException("Not supported yet.");
        //return secTwo;
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    /**
     * A supplementary class which holds all character stats
     */
    public final class SpectreCharacterAttributes {

        /**
         * Characters Life Points at the beginning of a match
         */
        public int BEGINING_HP = STARTING_LIFE_POINTS;
        /**
         * The Characters current Life Point value
         */
        public int HP = BEGINING_HP;
        /**
         * Indicates the speed at which the character replenishes MP
         */
        public float REPLENISH_RATE = FOCUS_REPLENISH_RATE;
        /**
         * The Characters current focus level which indicates the current
         * maximum MP the character can gather at one time
         */
        public int FOCUS_LEVEL = STARTING_MANA_LEVEL;
        /**
         * The Characters current usable MP
         */
        public int FOCUS = 0;
        /**
         * Characters current attack power<br/> 0 indicates normal and a
         * positive or negative value indicates a tenth percentile change
         */
        public int POWER = 0;
        /**
         * Characters current projectile accuracy 0 indicates normal and a
         * positive or negative value indicates a tenth percentile change
         */
        public int ACC = 0;
        /**
         * Characters movement speed. <br/> 0 indicates normal and a positive or
         * negative value indicates a tenth percentile change
         */
        public int DEX = 0;
        /**
         * Characters Defense. <br/> 0 indicates normal and a positive or
         * negative value indicates a tenth percentile change
         */
        public int CON = 0;
    }
}
