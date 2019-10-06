package tech.v2c.minecraft.plugins.lode.RESTful.actions;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Server;
import tech.v2c.minecraft.plugins.lode.Lode;
import tech.v2c.minecraft.plugins.lode.RESTful.global.BaseAction;
import tech.v2c.minecraft.plugins.lode.RESTful.global.annotations.ApiRoute;
import tech.v2c.minecraft.plugins.lode.RESTful.global.entities.server.ServerDTO;
import tech.v2c.minecraft.plugins.lode.RESTful.global.entities.server.ServerStatusDTO;
import tech.v2c.minecraft.plugins.lode.tools.PropsUtils;
import tech.v2c.minecraft.plugins.lode.tools.results.JsonResult;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ServerAction extends BaseAction {
    @ApiRoute(Path = "/api/Server/GetServerInfo")
    public JsonResult GetServerInfo() {
        Server server = Lode.instance.getServer();

        ServerDTO serverInfo = new ServerDTO();
        serverInfo.setPort(server.getPort());
        serverInfo.setVersion(server.getVersion());
        serverInfo.setOnlinePlayerCount(server.getOnlinePlayers().size());
        serverInfo.setIp(server.getIp());
        serverInfo.setMaxPlayerCount(server.getMaxPlayers());
        serverInfo.setMotd(server.getMotd());
        serverInfo.setBukkitVersion(server.getBukkitVersion());
        serverInfo.setApiVersion(server.getVersion());
        serverInfo.setGameMode(server.getDefaultGameMode().getValue());
        serverInfo.setDifficulty(Integer.parseInt(PropsUtils.GetProps("difficulty")));
        serverInfo.setPluginCount(server.getPluginManager().getPlugins().length);
        serverInfo.setHasWhiteList(server.hasWhitelist());
        serverInfo.setServerType(server.getClass().getName());

        return new JsonResult(serverInfo);
    }

    @ApiRoute(Path = "/api/Server/ExecuteCommand")
    public JsonResult ExecuteCommand(Map data) {
        String cmd = data.get("command").toString();
        server.getScheduler().scheduleSyncDelayedTask(Lode.instance, () -> {
            server.dispatchCommand(Bukkit.getConsoleSender(), cmd);
        });

        return new JsonResult();
    }

    @ApiRoute(Path = "/api/Server/ReloadServer")
    public JsonResult ReloadServer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                server.reload();
            }
        }, 5000);

        return new JsonResult(null, 200, "Server will have reload after 5 seconds.");
    }

    @ApiRoute(Path = "/api/Server/SendBroadcastMessage")
    public JsonResult SendBroadcastMessage(Map data) {
        String message = data.get("message").toString();

        return new JsonResult(server.broadcastMessage(message));
    }

    @ApiRoute(Path = "/api/Server/SetServerProps")
    public JsonResult SetServerProps(Map data) {
        String key = data.get("key").toString();
        String value = data.get("value").toString();

        PropsUtils.SetProps(key, value);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                server.reload();
            }
        }, 5000);

        return new JsonResult(null, 200, "将在 5s 后重载配置");
    }

    @ApiRoute(Path = "/api/Server/SetWhitelistState")
    public JsonResult SetWhitelistState(Map data) {
        boolean state = Boolean.parseBoolean(data.get("state").toString());
        server.setWhitelist(state);
        return new JsonResult();
    }

    @ApiRoute(Path = "/api/Server/SetGameMode")
    public JsonResult SetGameMode(Map data) {
        int gameMode = (int) Double.parseDouble(data.get("gameMode").toString());
        server.setDefaultGameMode(gameMode == 0 ? GameMode.SURVIVAL : GameMode.CREATIVE);
        return new JsonResult();
    }

    @ApiRoute(Path = "/api/Server/GetStatus")
    public JsonResult GetStatus(){
        ServerStatusDTO state = new ServerStatusDTO();
        state.setMaxPlayer(server.getMaxPlayers());
        state.setOnlinePlayer(server.getOnlinePlayers().size());

        return new JsonResult(state);
    }
}
