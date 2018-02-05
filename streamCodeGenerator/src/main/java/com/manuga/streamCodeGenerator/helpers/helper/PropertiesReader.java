package com.manuga.streamCodeGenerator.helpers.helper;

/**
 * Created with IntelliJ IDEA.
 * User: vbojovic
 * Date: 05.12.13.
 * Time: 12:33
 * To change this template use File | Settings | File Templates.
 */

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;

public class PropertiesReader {
    private static Logger logger = Logger.getLogger(PropertiesReader.class);
    private static PropertiesReader instance = null;

    private Hashtable<String, Properties> propertiesHash = new Hashtable<String, Properties>();

    // podatak kad je pojedini file zadnji put mijenjan
    private Hashtable<String, Long> propertiesLastModifiedHash = new Hashtable<String, Long>();

    private PropertiesReader()
    {
    }

    public static PropertiesReader getInstance()
    {
        if (instance == null)
        {
            instance = new PropertiesReader();
        }
        return instance;
    }

    public Properties getProperties(String propFilePath)
    {
        // cuvaj podatke kad je svaki od property file-ova zadnji put mijenjan
        Long datePropFileModified = (Long) this.propertiesLastModifiedHash.get(propFilePath);
        // dohvati URL trazenog file-a
        URL url = this.getClass().getResource(propFilePath);
        if (url == null)
        {
            throw new RuntimeException("Property file " + propFilePath + " nije pronadjen!");
        }

        File propFile = null;
        Properties prop = (Properties) this.propertiesHash.get(propFilePath);
        try
        {
            propFile = new File(url.toString());
            logger.debug("URL:" + url.toString());
            Long lastModified = new Long(propFile.lastModified());

            // ako u memoriji nema tog file-a ili se u medjuvremenu promijenio na disku, ucitaj file ponovo
            if (prop == null || datePropFileModified != null && !datePropFileModified.equals(lastModified))
            {
                // ovo je singleton klasa, ne smije se desiti da vise threadova ide istovremeno citati file
                // i mijenjati prop objekt. ovo se eventualno moze desiti ako vise threadova istovremeno pozove
                // metode ali samo kod prije nego je prvi put kreirana
                synchronized (this)
                {
                    logger.debug("Loading properties file: " + propFilePath + " - last modified: " + new Date(lastModified.longValue()));

                    InputStream is = url.openStream();
                    prop = new Properties();
                    try
                    {
                        prop.load(is);
                    }
                    catch (IOException e)
                    {
                        throw new RuntimeException(e);
                    }
                    try
                    {
                        is.close();
                    }
                    catch (IOException e)
                    {
                        logger.error(e.getMessage(), e);
                    }
                }

                // hashtable je synchronized pa ovo ne mora biti u syncronized bloku
                this.propertiesHash.put(propFilePath, prop);
                this.propertiesLastModifiedHash.put(propFilePath, lastModified);
            }
        }
        catch (IOException e)
        {
            logger.error(e);
            throw new RuntimeException("Greska pri dohvatu property file-a: " + propFilePath, e);
        }

        return prop;
    }
}