package com.spectre.util.JSON;

import com.spectre.deck.SupplyDeck;
import com.spectre.deck.card.Card;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Map;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Exports a Deck of cards to an XML database for storage
 *
 * @author Kyle Williams
 */
public class MasterDeckJSONImporterExporter {

    private final static SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy hh:mm:ss.SS a, z");

    /**
     * SHOULD ONLY BE USED FOR LOCALE EXPORT
     *
     * @param map
     * @param file
     */
    public static void Export(SupplyDeck deck, File file) {
        ZipOutputStream zos = null;
        try {
            //Set Up The Main File
            zos = new ZipOutputStream(new FileOutputStream(file));
            ZipEntry ze;
            JAXBHashMap mp = new JAXBHashMap();
            //First Export The Cards while setting up for the next section
            for (java.util.Map.Entry<String, Card> entry : deck.entrySet()) {
                //Set Up Export
                ze = new ZipEntry(entry.getValue().getName());
                zos.putNextEntry(ze);
                MOXyImporterExporter.write(entry.getValue(), Card.class, zos);
                //Finally add to export map for final step
                mp.map.put(entry.getKey(), sdf.format(entry.getValue().getDateModified()));
            }

            //Then Export The Main List of Card Names//used primarily to check for consistency when loading
            ze = new ZipEntry("GrandList");
            zos.putNextEntry(ze);
            MOXyImporterExporter.write(mp, JAXBHashMap.class, zos, "GrandList", "com.spectre.util.JSON");

        } catch (IOException ex) {
            com.spectre.app.SpectreApplication.logger.log(
                    java.util.logging.Level.INFO,
                    ex.toString(),
                    new java.io.IOException());
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException ex) {
                    com.spectre.app.SpectreApplication.logger.log(
                            java.util.logging.Level.SEVERE,
                            "Problems during cleaning up the I/O.",
                            ex);
                }
            }
        }

    }

    /**
     * @Remember For now do load everything but later only load names and then
     * load only what is needed
     * @param file
     * @return
     */
    public static SupplyDeck Import(File file) {
        ZipFile zipFile = null;
        InputStream inputStream = null;
        SupplyDeck deck = null;
        try {
            deck = new SupplyDeck();
            zipFile = new ZipFile(file);
            Enumeration<? extends ZipEntry> oEnum = zipFile.entries();
            ZipEntry zipEntry = null;
            JAXBHashMap mp = null;
            while (oEnum.hasMoreElements()) {
                zipEntry = oEnum.nextElement();
                inputStream = zipFile.getInputStream(zipEntry);
                if (!zipEntry.getName().equals("GrandList")) {
                    Card card = MOXyImporterExporter.read(Card.class, inputStream);
                    deck.put(card);

                } else {
                    mp = MOXyImporterExporter.read(JAXBHashMap.class, inputStream, "com.spectre.util.JSON");
                }
            }

            //FIRST CHECK SIZE
            if (deck.size() != mp.size()) {
                throw new IOException("Their are either cards missing or extra cards that was not their previously \n this deck should be carefully revaluated for consistency");
            }
            //THEN CHECK CARD DATES and make sure all cards where found
            for (Map.Entry<String, String> m : mp.map.entrySet()) {
                String key = m.getKey();
                if (deck.containsKey(key)) {
                    String date = sdf.format(deck.get(key).getDateModified());
                    if (!date.equals(m.getValue())) {
                        throw new IOException("Their are either cards missing or extra cards that was not their previously \n this deck should be carefully revaluated for consistency");
                    }
                } else {
                    throw new IOException("Their are either cards missing or extra cards that was not their previously \n this deck should be carefully revaluated for consistency");
                }
            }

            //IF Everything Golden then return deck
            return deck;
        } catch (IOException ex) {
            com.spectre.app.SpectreApplication.logger.log(Level.SEVERE,
                    ex.toString(),ex);
        } finally {
            try {
                if (zipFile != null) {
                    zipFile.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException problemsDuringClose) {
                com.spectre.app.SpectreApplication.logger.log(
                        java.util.logging.Level.WARNING,
                        "Problems during cleaning up the I/O.",
                        problemsDuringClose);
            }
        }
        return null;
    }
}