package net.sevecek.util;

import java.io.*;
import java.util.*;
import javax.naming.*;
import javax.naming.spi.*;

public class JavaUtilPropertiesJNDIFactory implements ObjectFactory {

    public static final String DEFAULT_PROPERTIES_FILE_ENCODING = "UTF-8";
    public static final String ATTR_PROPERTIES_FILE_LOCATION = "propertiesFileLocation";
    public static final String ATTR_PROPERTIES_FILE_ENCODING = "propertiesFileEncoding";
    public static final String ATTR_PROPERTIES_XML_FILE_LOCATION = "propertiesXmlFileLocation";

    @Override
    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) throws Exception {
        Properties bean = new Properties();

        Reference ref = (Reference) obj;
        loadPropertiesFileIfConfigured(bean, ref, name);
        addInlineProperties(bean, ref);

        return (bean);
    }


    private void loadPropertiesFileIfConfigured(Properties bean, Reference ref, Name name) throws NamingException {
        String propertiesFileLocation = null;
        String propertiesFileEncoding = DEFAULT_PROPERTIES_FILE_ENCODING;
        String propertiesXmlFileLocation = null;

        Enumeration attributes = ref.getAll();
        while (attributes.hasMoreElements()) {
            RefAddr attribute = (RefAddr) attributes.nextElement();
            String attributeName = attribute.getType();
            String attributeValue = (String) attribute.getContent();
            if (attributeName.equals(ATTR_PROPERTIES_FILE_LOCATION)) {
                propertiesFileLocation = attributeValue;
            }
            if (attributeName.equals(ATTR_PROPERTIES_FILE_ENCODING)) {
                propertiesFileEncoding = attributeValue;
            }
            if (attributeName.equals(ATTR_PROPERTIES_XML_FILE_LOCATION)) {
                propertiesXmlFileLocation = attributeValue;
            }
        }

        if (propertiesFileLocation != null) {
            loadPropertiesFile(bean, name, propertiesFileLocation, propertiesFileEncoding);
        }

        if (propertiesXmlFileLocation != null) {
            loadPropertiesXmlFile(bean, name, propertiesXmlFileLocation);
        }
    }


    private void loadPropertiesFile(Properties bean, Name name, String propertiesFileLocation, String propertiesFileEncoding) throws NamingException {
        try {
            Reader input = new InputStreamReader(
                    new FileInputStream(propertiesFileLocation),
                    propertiesFileEncoding);
            try {
                bean.load(input);
            } finally {
                input.close();
            }
        } catch (IOException ex) {
            throw new RuntimeException("Unable to load the properties file '" + propertiesFileLocation + "' configured for a JNDI entry <Resource name=\"" + name + "\" ...>. Nested exception is: " + ex.getClass().getName() + ": " + ex.getMessage(), ex);
        }
    }


    private void loadPropertiesXmlFile(Properties bean, Name name, String propertiesXmlFileLocation) throws NamingException {
        try {
            InputStream input = new FileInputStream(propertiesXmlFileLocation);
            try {
                bean.loadFromXML(input);
            } finally {
                input.close();
            }
        } catch (IOException ex) {
            throw new RuntimeException("Unable to load the properties xml file '" + propertiesXmlFileLocation + "' configured for a JNDI entry <Resource name=\"" + name + "\" ...>. Nested exception is: " + ex.getClass().getName() + ": " + ex.getMessage(), ex);
        }
    }


    private void addInlineProperties(Properties bean, Reference ref) {
        Enumeration attributes = ref.getAll();
        while (attributes.hasMoreElements()) {
            RefAddr attribute = (RefAddr) attributes.nextElement();
            String attributeName = attribute.getType();
            String attributeValue = (String) attribute.getContent();
            bean.setProperty(attributeName, attributeValue);
        }
    }
}
