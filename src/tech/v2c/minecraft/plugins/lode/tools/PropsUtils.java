package tech.v2c.minecraft.plugins.lode.tools;

import java.io.*;
import java.util.Properties;

public class PropsUtils {
    public static void SetProps(String pKey, String pValue) {
        Properties pps = new Properties();

        InputStream in = null;
        try {
            in = new FileInputStream("./server.properties");
            pps.load(in);
            OutputStream out = new FileOutputStream("./server.properties");
            pps.setProperty(pKey, pValue);
            pps.store(out, "Update " + pKey + " name");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String GetProps(String key) {
        Properties pps = new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream("./server.properties"));
            pps.load(in);
            String value = pps.getProperty(key);
            return value;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
