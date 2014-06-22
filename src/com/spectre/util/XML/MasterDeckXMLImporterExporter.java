package com.spectre.util.XML;

import com.spectre.deck.Card;
import com.spectre.deck.SupplyDeck;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
//import org.openide.util.Exceptions;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Exports a Deck of cards to an XML database for storage
 *
 * @author Kyle Williams
 */
public class MasterDeckXMLImporterExporter {

    /**
     * SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy'T'HH:mm:ss");
     * String date = sdf.format(new Date()); System.out.println(date); Date d =
     * sdf.parse(date);
     */
    private static SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy hh:mm:ss.SS a, z");

    /**
     * SHOULD ONLY BE USED FOR LOCALE EXPORT
     * @param map
     * @param file 
     */
    public static void Export(SupplyDeck deck, File file) {
        ZipOutputStream zos = null;
        try {
            //Set Up The Main File
            zos = new ZipOutputStream(new FileOutputStream(file));
            ZipEntry ze;
            HashMap<String, Date> map = new HashMap<String, Date>();
            //First Export The Cards while setting up for the next section
            for (java.util.Map.Entry<String, Card> entry : deck.entrySet()) {
                //Set Up Export
                ze = new ZipEntry(entry.getValue().getName());
                zos.putNextEntry(ze);
                JAXBImporterExporter.write(entry.getValue(), Card.class, zos);
                //Finally add to export map for final step
                map.put(entry.getKey(), entry.getValue().getDateModified());
            }
            //Then Export The Main List of Card Names
            ze = new ZipEntry("GrandList");
            zos.putNextEntry(ze);
            XMLStreamWriter xsw = XMLOutputFactory.newInstance().createXMLStreamWriter(zos);
            //xsw = new com.sun.xml.internal.txw2.output.IndentingXMLStreamWriter(xsw);
            xsw.writeStartDocument();
            xsw.writeStartElement("MasterDeck");
            for (Map.Entry<String, Date> entry : map.entrySet()) {
                xsw.writeEmptyElement("Card");
                xsw.writeAttribute("Name", entry.getKey());
                xsw.writeAttribute("DateModified", sdf.format(entry.getValue()));
            }
            xsw.writeEndElement();//End For Series
            xsw.writeEndDocument();
            xsw.close();

        } catch (IOException ex) {
            Logger.getLogger(MasterDeckXMLImporterExporter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLStreamException ex) {
            // Exceptions.printStackTrace(ex);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException ex) {
                    // Exceptions.printStackTrace(ex);
                }
            }
        }

    }

    /**
     * @Remember For now do load everything but later only load names and then load only what is needed
     * @param file
     * @return 
     */
    public static SupplyDeck Import(File file) {
        ZipFile zipFile = null;
        InputStream inputStream = null;
        SupplyDeck deck = null;
        try {
            deck = new SupplyDeck();
            int supposedCard = 0;
            zipFile = new ZipFile(file);
            Enumeration<? extends ZipEntry> oEnum = zipFile.entries();
            ZipEntry zipEntry = null;

            while (oEnum.hasMoreElements()) {
                zipEntry = oEnum.nextElement();
                inputStream = zipFile.getInputStream(zipEntry);
                if (!zipEntry.getName().equals("GrandList")) {
                    Card card = JAXBImporterExporter.read(Card.class, inputStream);
                    deck.put(card);
                } else {
                    MDHandler handler = new MDHandler();
                    final SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
                    saxParser.parse(inputStream, handler);
                    supposedCard = handler.cardNames.size();
                }
            }

            if (deck.size() != supposedCard) {
                throw new IOException("Their are either cards missing or extra cards that was not their previously \n this deck should be carefully revaluated for consistency");
            } else {
                return deck;
            }
        } catch (IOException ex) {
            // Exceptions.printStackTrace(ex);
        } catch (ParserConfigurationException ex) {
            // Exceptions.printStackTrace(ex);
        } catch (SAXException ex) {
            // Exceptions.printStackTrace(ex);
        } finally {
            try {
                if (zipFile != null) {
                    zipFile.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException problemsDuringClose) {
                System.out.println("Problems during cleaning up the I/O.");
            }
        }
        throw new NullPointerException("Unable To Properly Load MasterDeck");
    }

    private static class MDHandler extends DefaultHandler {

        public ArrayList<String> cardNames = new ArrayList<String>();

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException {
            if (qName.equals("Card")) {
                cardNames.add(attributes.getValue("Name"));
            }
        }
    };
}