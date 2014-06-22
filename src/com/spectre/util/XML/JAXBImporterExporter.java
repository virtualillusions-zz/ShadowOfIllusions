package com.spectre.util.XML;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
//import org.openide.util.Exceptions;

/**
 *
 * @author Kyle Williams
 */
public final class JAXBImporterExporter {

    private static JAXBContext jc;
   
    private static JAXBContext getJAXBContext() {
        try {
            return jc = JAXBContext.newInstance("com.spectre.deck.card", JAXBImporterExporter.class.getClassLoader());
        } catch (JAXBException ex) {
            // Exceptions.printStackTrace(ex);
            System.out.println("FCK!!");
            return null;
        }
    }

    /**
     * Saves an unaltered object to a specified location
     *
     * @param <T> The Generic Type Being Loaded
     * @param savable The Specific Java Object beign saved
     * @param type The Specified type of a class
     * @param rootName The Specified Name for the root node of the xml
     * @param path The path in which the file should be saved to
     */
    public static <T> void write(T savable, Class<T> type, String rootName, Object path) {
        try {
            Marshaller marshaller = getJAXBContext().createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            JAXBElement<T> jaxbElement = new JAXBElement<T>(new QName(rootName), type, savable);
            if (path instanceof File) {
                marshaller.marshal(jaxbElement, (File) path);
            } else if (path instanceof OutputStream) {
                marshaller.marshal(jaxbElement, (OutputStream) path);
            } else {
                throw new NoSuchMethodException("The Field Path must be of either type File or OutputStream");
            }
        } catch (NoSuchMethodException ex) {
            // Exceptions.printStackTrace(ex); 
        } catch (JAXBException ex) {
            // Exceptions.printStackTrace(ex);
        }
    }

    /**
     * Saves an unaltered object to a specified location
     * @param <T>       The Generic Type Being Loaded
     * @param savable   The Specific Java Object beign saved
     * @param type The Specified type of a class
     * @param path      The path in which the file should be saved to
     */
    public static <T> void write(T savable, Class<T> type, Object path) {
        try {
            Marshaller marshaller = getJAXBContext().createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            if (path instanceof File) {
                marshaller.marshal(savable, (File) path);
            } else if (path instanceof OutputStream) {
                marshaller.marshal(savable, (OutputStream) path);
            } else {
                throw new NoSuchMethodException("The Field Path must be of either type File or OutputStream");
            }
        } catch (NoSuchMethodException ex) {
            // Exceptions.printStackTrace(ex);
        } catch (JAXBException ex) {
            // Exceptions.printStackTrace(ex);
        }
    }

    /**
     * Loads an XML file previously saved through JAXB to be used methods have been 
     * encorporated to load custom root tags
     * @param <T>  The generic Type Being loaded
     * @param type The Specified Type of the class
     * @param path The path in which to locate the file to be loaded
     * @return 
     */
    @SuppressWarnings("unchecked")
    public static <T> T read(Class<T> type, Object path) {
        try {
            Unmarshaller unmarshaller = getJAXBContext().createUnmarshaller();
            JAXBElement root = null;
            if (path instanceof File) {
                root = unmarshaller.unmarshal(new StreamSource((File) path), type);
            } else if (path instanceof InputStream) {
                root = unmarshaller.unmarshal(new StreamSource((InputStream) path), type);
            } else {
                throw new NoSuchMethodException("The Field Path must be of either type File or InputStream");
            }

            return (T) root.getValue();
        } catch (NoSuchMethodException ex) {
            // Exceptions.printStackTrace(ex);
        } catch (JAXBException ex) {
            // Exceptions.printStackTrace(ex);
        }
        return null;
    }
}
