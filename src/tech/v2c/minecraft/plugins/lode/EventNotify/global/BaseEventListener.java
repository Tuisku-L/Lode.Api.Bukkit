package tech.v2c.minecraft.plugins.lode.EventNotify.global;

import org.bukkit.Server;
import tech.v2c.minecraft.plugins.lode.Lode;

public class BaseEventListener {
    public final Server server;
    public BaseEventListener(){
        this.server = Lode.instance.getServer();
    }
}
