package cn.edu.ruc.hola.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggingUtils {
    private static void env(Logger logger) {
        StringBuilder logPath = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        logPath.append(sdf.format(new Date())).append(".log");
        try {
            FileHandler fileHandler = new FileHandler(logPath.toString(), true);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Logger logger(String TAG) {
        Logger logger = Logger.getLogger(TAG);
        env(logger);
        return logger;
    }
}
