import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;
public class GetPropertyValues {
    String result = "";
    InputStream inputStream;
    final static Logger logger = Logger.getLogger(GetPropertyValues.class);
    Properties prop;
    public GetPropertyValues()  {
        try {
            prop = new Properties();
            String propFileName = "config.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

        }
        catch (IOException ix) {
            logger.error(ix.getStackTrace());
        }
        finally {
            try {
            inputStream.close();
        }
        catch (IOException ex)
        {
            logger.error("Error",ex);
        }
        }
    }
    public String getPropValues(String propertyName)  {
            try{
            // get the property value and print it out
            return  prop.getProperty(propertyName);


        } catch (Exception e) {
            logger.error("Error",e);
            return  "";
        }

    }
}
