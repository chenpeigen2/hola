package cn.ruc.edu.hola.client.utils;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public final class ChaincodeConfig {

    private static Properties props;

    private static final Logger LOGGER = Logger.getLogger(ChaincodeConfig.class.getName());

    private static final String CHAINCODE_SUPPORT_NET = "CHAINCODE_SUPPORT_NET";

    /**
     * Obtains configuration specifically for running the chaincode and settable on
     * a per chaincode basis rather than taking properties from the Peers'
     * configuration.
     *
     * @return Configuration
     */
    public static Properties getChaincodeConfig() {
        if (props == null) {

            final ClassLoader cl = ChaincodeConfig.class.getClassLoader();
            // determine the location of the properties file to control the metrics etc.

            props = new Properties();

            try (InputStream inStream = cl.getResourceAsStream("config.props")) {
                if (inStream != null) {
                    props.load(inStream);
                }
            } catch (final IOException e) {
                LOGGER.warning(() -> "Can not open the properties file for input " + e);
            }


            LOGGER.info("<<<<<<<<<<<<<Properties options>>>>>>>>>>>>");
            LOGGER.info(() -> props.toString());
        }

        return props;
    }

    public static boolean SUPPORT_NET = false;

    static {
        Properties properties = getChaincodeConfig();
        if (properties.containsKey(CHAINCODE_SUPPORT_NET)) {
            SUPPORT_NET = Boolean.parseBoolean((String) props.get(CHAINCODE_SUPPORT_NET));
        }
    }

}
