/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.IO;

import com.spectre.deck.SupplyDeck;
import com.spectre.util.AbstractCard;
import com.spectre.deck.card.CardCharacteristics.CardSeries;
import com.spectre.util.JSON.MasterDeckJSONImporterExporter;
import java.io.File;

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
            sD.put(AbstractCard.createNewCard("Card: " + i, CardSeries.Nature));
        }

        long startTime = System.nanoTime();
        long endTime;
        try {
            String desktopPath = System.getProperty("user.home") + "/Desktop/SeriesTemplate.series";
            File f = new File(desktopPath);
            MasterDeckJSONImporterExporter.Export(sD, f);
            sD = MasterDeckJSONImporterExporter.Import(f);
            System.out.println(sD.size() - 1);
        } finally {
            endTime = System.nanoTime();
        }
        long duration1 = endTime - startTime;

        System.out.println(duration1 * 1E-9);
    }
}
