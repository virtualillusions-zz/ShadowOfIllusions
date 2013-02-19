package com.spectre.util.JSON;

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
public final class MOXyImporterExporter {

    private static JAXBContext jc;
    private static Marshaller marshaller;
    private static Unmarshaller unmarshaller;

    static {
        setJAXBContext(null);
    }

    private static void setJAXBContext(String indexPath) {
        try {
            String s = indexPath == null ? "com.spectre.deck.card" : indexPath;
            jc = JAXBContext.newInstance(s, MOXyImporterExporter.class.getClassLoader());

            marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty("eclipselink.media-type", "application/json");

            unmarshaller = jc.createUnmarshaller();
            unmarshaller.setProperty("eclipselink.media-type", "application/json");
        } catch (JAXBException ex) {
            com.spectre.app.SpectreApplication.logger.log(
                    java.util.logging.Level.SEVERE,
                    ex.toString(),
                    ex);
        }
    }

    /**
     * Saves an unaltered object to a specified location
     *
     * @param <T> The Generic Type Being Loaded
     * @param savable The Specific Java Object being saved
     * @param type The Specified type of a class
     * @param rootName The Specified Name for the root node of the JSON
     * @param path The path in which the file should be saved to
     */
    public static <T> void write(T savable, Class<T> type, Object path, String rootName) {
        try {


            JAXBElement<T> jaxbElement = new JAXBElement<T>(new QName(rootName), type, savable);
            if (path instanceof File) {
                marshaller.marshal(jaxbElement, (File) path);
            } else if (path instanceof OutputStream) {
                marshaller.marshal(jaxbElement, (OutputStream) path);
            } else {
                throw new NoSuchMethodException("The Field Path must be of either type File or OutputStream");
            }
            setJAXBContext(null);
        } catch (Exception ex) {
            com.spectre.app.SpectreApplication.logger.log(
                    java.util.logging.Level.SEVERE,
                    ex.toString(),
                    ex);
        }
    }

    public static <T> void write(T savable, Class<T> type, Object path, String rootName, String indexPath) {
        setJAXBContext(indexPath);
        write(savable, type, path, rootName);
        setJAXBContext(null);
    }

    /**
     * Saves an unaltered object to a specified location
     *
     * @param <T> The Generic Type Being Loaded
     * @param savable The Specific Java Object beign saved
     * @param type The Specified type of a class
     * @param path The path in which the file should be saved to
     */
    public static <T> void write(T savable, Class<T> type, Object path) {
        try {

            if (path instanceof File) {
                marshaller.marshal(savable, (File) path);
            } else if (path instanceof OutputStream) {
                marshaller.marshal(savable, (OutputStream) path);
            } else {
                throw new NoSuchMethodException("The Field Path must be of either type File or OutputStream");
            }
            setJAXBContext(null);
        } catch (Exception ex) {
            com.spectre.app.SpectreApplication.logger.log(
                    java.util.logging.Level.SEVERE,
                    ex.toString(),
                    ex);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T read(Class<T> type, Object path, String indexPath) {
        setJAXBContext(indexPath);
        Object o = read(type, path);
        setJAXBContext(null);
        return (T) o;
    }

    /**
     * Loads an XML file previously saved through JAXB to be used methods have
     * been encorporated to load custom root tags
     *
     * @param <T> The generic Type Being loaded
     * @param type The Specified Type of the class
     * @param path The path in which to locate the file to be loaded
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T read(Class<T> type, Object path) {
        try {

            JAXBElement root = null;
            if (path instanceof File) {
                root = unmarshaller.unmarshal(new StreamSource((File) path), type);
            } else if (path instanceof InputStream) {
                root = unmarshaller.unmarshal(new StreamSource((InputStream) path), type);
            } else {
                throw new NoSuchMethodException("The Field Path must be of either type File or InputStream");
            }

            return (T) root.getValue();
        } catch (Exception ex) {
            com.spectre.app.SpectreApplication.logger.log(
                    java.util.logging.Level.SEVERE,
                    ex.toString(),
                    ex);
        }
        return null;
    }
}
