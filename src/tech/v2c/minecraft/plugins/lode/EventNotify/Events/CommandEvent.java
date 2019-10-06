package tech.v2c.minecraft.plugins.lode.EventNotify.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;
import tech.v2c.minecraft.plugins.lode.EventNotify.global.BaseEventListener;
import tech.v2c.minecraft.plugins.lode.EventNotify.global.LodeWebSocketServer;
import tech.v2c.minecraft.plugins.lode.tools.results.JsonResult;

import java.util.Date;
import java.util.HashMap;

public class CommandEvent extends BaseEventListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void OnCommand(ServerCommandEvent event){
        HashMap<Object, Object> result = new HashMap<Object, Object>();
        result.put("sender", event.getSender().getName());
        result.put("command", event.getCommand());
        result.put("eventTime", new Date());

        LodeWebSocketServer.SendMsg(new JsonResult(result));
    }
}
