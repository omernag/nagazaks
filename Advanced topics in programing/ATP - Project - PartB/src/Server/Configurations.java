package Server;

import java.io.*;
import java.util.Properties;

public class Configurations {

    public static void setSearchAlgorithm(String sAlgorithm) {
        try (OutputStream output = new FileOutputStream("resources/config.properties")) {
            Properties prop = new Properties();
            prop.setProperty("searchAlgorithm", sAlgorithm);
            prop.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public static void setGeneratorAlgorithm(String gAlgorithm) {
        try (OutputStream output = new FileOutputStream("resources/config.properties")) {
            Properties prop = new Properties();
            prop.setProperty("generatorAlgorithm", gAlgorithm);
            prop.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public static void setThreadPoolAmount(String poolSize) {
        try (OutputStream output = new FileOutputStream("resources/config.properties")) {
            Properties prop = new Properties();
            prop.setProperty("poolSize", poolSize);
            prop.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public static String loadSearchAlgorithm(){
        try (InputStream input = new FileInputStream("resources/config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            return  prop.getProperty("searchAlgorithm");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String loadGeneratorAlgorithm(){
        try (InputStream input = new FileInputStream("resources/config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            return  prop.getProperty("generatorAlgorithm");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String getThreadPoolAmount(){
        try (InputStream input = new FileInputStream("resources/config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            return  prop.getProperty("poolSize");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void setProperty(String propertyName,String propertyValue) {
       try{
        FileInputStream inStream = new FileInputStream("resources/config.properties");
        Properties props = new Properties();
        props.load(inStream);
        inStream.close();

        FileOutputStream outStream = new FileOutputStream("resources/config.properties");
        props.setProperty(propertyName, propertyValue);
        props.store(outStream, null);
        outStream.close();
       }
       catch (IOException ex) {
            ex.printStackTrace();
       }
    }

}
