package cn.ruc.edu.hola.index;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public final class PropsConfig {

    private static Properties props;

    private static final Logger LOGGER = Logger.getLogger(PropsConfig.class.getName());

    private static final String USING_INDEX2 = "USING_INDEX2";

    /**
     * Obtains configuration specifically for running the chaincode and settable on
     * a per chaincode basis rather than taking properties from the Peers'
     * configuration.
     *
     * @return Configuration
     */
    public static Properties getPropsConfig() {
        if (props == null) {

            final ClassLoader cl = PropsConfig.class.getClassLoader();
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
}
