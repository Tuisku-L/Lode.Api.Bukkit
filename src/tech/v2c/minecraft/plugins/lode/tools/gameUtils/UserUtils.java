package tech.v2c.minecraft.plugins.lode.tools.gameUtils;

import org.bukkit.entity.Player;
import tech.v2c.minecraft.plugins.lode.Lode;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.UUID;

public class UserUtils {
    public static Player GetPlayerByName(String name){
        Collection<? extends Player> onlinePlayers = Lode.instance.getServer().getOnlinePlayers();
        Player player = null;
        for (Player item : onlinePlayers) {
            if(item.getName().equalsIgnoreCase(name)){
                player = item;
                break;
            }
        }

        return player;
    }

    public static Player GetPlayerByUuid(UUID uuid){
        Player player = Lode.instance.getServer().getPlayer(uuid);

        return player;
    }

    public static int GetPlayerPing(Player player){
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            return entityPlayer.getClass().getField("ping").getInt(entityPlayer);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
