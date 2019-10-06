package tech.v2c.minecraft.plugins.lode.tools;

import tech.v2c.minecraft.plugins.lode.Lode;

import java.util.logging.Level;

public class LogUtils {
    public static void Log(Level level, String msg){
        Lode.instance.getLogger().log(level, msg);
    }

    public static void Debug(String msg){
        if(Lode.instance.isDebugMode){
            Lode.instance.getLogger().info(msg);
        }
    }

    public static void Info(String msg){
        Lode.instance.getLogger().info(msg);
    }
}