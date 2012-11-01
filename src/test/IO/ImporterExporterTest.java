/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.IO;

import com.spectre.deck.SupplyDeck;
import com.spectre.deck.card.Card;
import com.spectre.deck.card.CardStats.*;
import com.spectre.util.JSON.MasterDeckJSONImporterExporter;
import java.io.File;
import java.util.Date;

/**
 *
 * @author Kyle Williams
 */
public class ImporterExporterTest {

    public static void main(String[] args) {
        ImporterExporterTest test = new ImporterExporterTest();
    }

    public ImporterExporterTest() {

        SupplyDeck sD = new SupplyDeck();
        for (int i = 0; i < 5; i++) {
            sD.put(createNewCard("Card: " + i, CardSeries.Nature));
        }

        long startTime = System.nanoTime();
        long endTime;
        try {
            String desktopPath = System.getProperty("user.home") + "/Desktop/SeriesTemplate.series";
            File f = new File(desktopPath);
            MasterDeckJSONImporterExporter.Export(sD, f);
            sD = MasterDeckJSONImporterExporter.Import(f);
            System.out.println(sD.size()-1);
        } finally {
            endTime = System.nanoTime();
        }
        long duration1 = endTime - startTime;

        System.out.println(duration1*1E-9);

    }

    public static Card createNewCard(final String name, final CardSeries series) {
        return new Card() {

            {
                lockData(false);
                getAnimation().setAnimationName("w/e");//fix to a reoccuring issue
                setData("cardName", name.toUpperCase());
                setData("dateModified", new Date().getTime());
                setData("cardSeries", series);
                setData("cardTrait", CardTrait.Enhance);
                setData("cardRange", CardRange.Misc);
                setData("maxUse", "1");
                setData("skillType", "instant");
                setData("skillTypeExtended", "user");
                setData("mineType", "Regular");
                setData("attackHoming", 0);
                setData("attakDamage", "Stat [Default]");
                setData("attackVelocity", 3);
                setData("attackCrawler", false);
                setData("attackPath", "Linear");
                setData("autoUse", false);
                setData("decreasePlayerKnockback", false);
                setData("shuffleSkills", false);
                setData("mpCost", "Stat [Default]");
                setData("skillCostIncrease", false);
                setData("StatusReset", false);
                setData("eraseSkill", "None");
                setData("mpIncrease", "Stat [Default]");
                setData("skillDescription", "New Card Skill");
                setData("A.O.E", false);
                setData("A.O.E_Radius", 3);
                setData("holdAndWait", false);
                setData("holdToPower", false);
                String s = "false:0:0";
                setData("hpUser", s);
                setData("hpOpponent", s);
                setData("speedUser", s);
                setData("speedOpponent", s);
                setData("immobolizeUser", s);
                setData("immobolizeOpponent", s);
                setData("paralyzeUser", s);
                setData("paralyzeOpponent", s);
                setData("mpLevelUser", s);
                setData("mpLevelOpponent", s);
                setData("increaseLockOnRangeUser", s);
                setData("increaseLockOnRangeOpponent", s);
                setData("mpUser", s);
                setData("mpOpponent", s);
                setData("confuseUser", s);
                setData("confuseOpponent", s);
                setData("invisibleUser", s);
                setData("invisibleOpponent", s);
                setData("dashUser", s);
                setData("dashOpponent", s);
                setData("jumpUser", s);
                setData("jumpOpponent", s);
                setData("defenseUser", s);
                setData("defenseOpponent", s);
                lockData(true);
            }
        };
    }
}
