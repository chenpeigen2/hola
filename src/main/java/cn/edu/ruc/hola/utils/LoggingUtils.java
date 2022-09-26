package cn.edu.ruc.hola.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public final class LoggingUtils {

    private final static boolean DEBUG = false;

    static {
        logger = Logger.getLogger("hola");
        if (DEBUG) {
            File f = new File("./log");
            if (!f.exists()) f.mkdir();
            env();
        }
    }

    private static void env() {
        if (DEBUG) return;
        StringBuilder logPath = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        logPath.append("./log/");
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

    private final static Logger logger;

    public static Logger logger() {
        return logger;
    }
}
