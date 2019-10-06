package tech.v2c.minecraft.plugins.lode.RESTful.actions;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import tech.v2c.minecraft.plugins.lode.Lode;
import tech.v2c.minecraft.plugins.lode.RESTful.global.BaseAction;
import tech.v2c.minecraft.plugins.lode.RESTful.global.annotations.ApiRoute;
import tech.v2c.minecraft.plugins.lode.RESTful.global.entities.server.BanEntryDTO;
import tech.v2c.minecraft.plugins.lode.RESTful.global.entities.user.OnlineUserDTO;
import tech.v2c.minecraft.plugins.lode.RESTful.global.entities.user.PlayerInventoryDTO;
import tech.v2c.minecraft.plugins.lode.RESTful.global.entities.user.UserPositionDTO;
import tech.v2c.minecraft.plugins.lode.tools.results.JsonResult;
import tech.v2c.minecraft.plugins.lode.tools.gameUtils.UserUtils;

import java.util.*;

public class UserAction extends BaseAction {
    @ApiRoute(Path = "/api/User/GetUserByName")
    public JsonResult GetUserByName(Map data) {
        String searchName = data.get("name").toString();

        Player user = UserUtils.GetPlayerByName(searchName);
        if (user == null) return new JsonResult(null, 404, "Error: user not found.");

        OnlineUserDTO onlineUser = new OnlineUserDTO();
        onlineUser.setName(user.getName());
        onlineUser.setDisplayName(user.getDisplayName());
        onlineUser.setId(user.getEntityId());
        onlineUser.setUid(user.getUniqueId());
        onlineUser.setGameMode(user.getGameMode().getValue());
        onlineUser.setHeight(user.getHeight());
        onlineUser.setHealth(user.getHealth());
        onlineUser.setMaxHealth(user.getMaxHealth());
        onlineUser.setPing(UserUtils.GetPlayerPing(user));
        onlineUser.setOp(user.isOp());
        onlineUser.setExperience(user.getTotalExperience());
        onlineUser.setExperienceLevel(user.getExpToLevel());
        onlineUser.setCurrentWorld(user.getWorld().getName());
        onlineUser.setLevel(user.getLevel());

        UserPositionDTO up = new UserPositionDTO();
        up.setX(user.getLocation().getX());
        up.setY(user.getLocation().getY());
        up.setZ(user.getLocation().getZ());

        onlineUser.setPosition(up);

        return new JsonResult(onlineUser);
    }

    @ApiRoute(Path = "/api/User/GetUserByUuid")
    public JsonResult GetUserByUuid(Map data) {
        UUID uuid = UUID.fromString(data.get("uuid").toString());

        Player user = UserUtils.GetPlayerByUuid(uuid);
        if (user == null) return new JsonResult(null, 404, "Error: user not found.");

        OnlineUserDTO onlineUser = new OnlineUserDTO();
        onlineUser.setName(user.getName());
        onlineUser.setDisplayName(user.getDisplayName());
        onlineUser.setId(user.getEntityId());
        onlineUser.setUid(user.getUniqueId());
        onlineUser.setGameMode(user.getGameMode().getValue());
        onlineUser.setHeight(user.getHeight());
        onlineUser.setHealth(user.getHealth());
        onlineUser.setMaxHealth(user.getMaxHealth());
        onlineUser.setPing(UserUtils.GetPlayerPing(user));
        onlineUser.setOp(user.isOp());
        onlineUser.setExperience(user.getTotalExperience());
        onlineUser.setExperienceLevel(user.getExpToLevel());
        onlineUser.setCurrentWorld(user.getWorld().getName());
        onlineUser.setLevel(user.getLevel());

        UserPositionDTO up = new UserPositionDTO();
        up.setX(user.getLocation().getX());
        up.setY(user.getLocation().getY());
        up.setZ(user.getLocation().getZ());

        onlineUser.setPosition(up);

        return new JsonResult(onlineUser);
    }

    @ApiRoute(Path = "/api/User/GetOnlineList")
    public JsonResult GetOnlineUserList() {
        Collection<? extends Player> users = server.getOnlinePlayers();
        ArrayList<OnlineUserDTO> userList = new ArrayList<OnlineUserDTO>();

        for (Player user : users) {
            OnlineUserDTO onlineUser = new OnlineUserDTO();
            onlineUser.setName(user.getName());
            onlineUser.setDisplayName(user.getDisplayName());
            onlineUser.setId(user.getEntityId());
            onlineUser.setUid(user.getUniqueId());
            onlineUser.setGameMode(user.getGameMode().getValue());
            onlineUser.setHeight(user.getHeight());
            onlineUser.setHealth(user.getHealth());
            onlineUser.setMaxHealth(user.getMaxHealth());
            onlineUser.setPing(UserUtils.GetPlayerPing(user));
            onlineUser.setOp(user.isOp());
            onlineUser.setExperience(user.getTotalExperience());
            onlineUser.setExperienceLevel(user.getExpToLevel());
            onlineUser.setCurrentWorld(user.getWorld().getName());
            onlineUser.setLevel(user.getLevel());

            UserPositionDTO up = new UserPositionDTO();
            up.setX(user.getLocation().getX());
            up.setY(user.getLocation().getY());
            up.setZ(user.getLocation().getZ());

            onlineUser.setPosition(up);

            userList.add(onlineUser);
        }

        return new JsonResult(userList);
    }

    @ApiRoute(Path = "/api/User/BanByName")
    public JsonResult BanUserByName(Map data) {
        String userName = data.get("name").toString();
        Object reason = data.get("reason");
        Object endTime = data.get("expirationDate");

        server.getBanList(BanList.Type.NAME).addBan(userName, reason == null ? "" : reason.toString(), endTime != null ? new Date(Long.parseLong(endTime.toString())) : null, null);

        return new JsonResult();
    }

    @ApiRoute(Path = "/api/User/BanByIp")
    public JsonResult BanUserByIp(Map data) {
        String userIp = data.get("ip").toString();
        Object reason = data.get("reason");
        Object endTime = data.get("expirationDate");

        server.getBanList(BanList.Type.IP).addBan(userIp, reason == null ? "" : reason.toString(), endTime != null ? new Date(Long.parseLong(endTime.toString())) : null, null);

        return new JsonResult();
    }

    @ApiRoute(Path = "/api/User/RemoveNameBan")
    public JsonResult RemoveNameBan(Map data) {
        String userName = data.get("target").toString();
        server.getBanList(BanList.Type.NAME).pardon(userName);

        return new JsonResult();
    }

    @ApiRoute(Path = "/api/User/RemoveIpBan")
    public JsonResult RemoveIpBan(Map data) {
        String ip = data.get("target").toString();
        server.unbanIP(ip);

        return new JsonResult();
    }

    @ApiRoute(Path = "/api/User/GetNameBanList")
    public JsonResult GetNameBanList() {
        List<BanEntryDTO> banList = new ArrayList<BanEntryDTO>();
        server.getBanList(BanList.Type.NAME).getBanEntries().forEach(banUser -> {
            BanEntryDTO banEntry = new BanEntryDTO();

            banEntry.setName(banUser.getTarget());
            banEntry.setReason(banUser.getReason());
            banEntry.setExpirationDate(banUser.getExpiration());
            banEntry.setCreationDate(banUser.getCreated());

            banList.add(banEntry);
        });

        return new JsonResult(banList);
    }

    @ApiRoute(Path = "/api/User/GetIpBanList")
    public JsonResult GetIpBanList() {
        List<BanEntryDTO> banList = new ArrayList<BanEntryDTO>();

        server.getBanList(BanList.Type.IP).getBanEntries().forEach(banUser -> {
            BanEntryDTO banEntry = new BanEntryDTO();

            banEntry.setName(banUser.getTarget());
            banEntry.setReason(banUser.getReason());
            banEntry.setExpirationDate(banUser.getExpiration());
            banEntry.setCreationDate(banUser.getCreated());

            banList.add(banEntry);
        });

        return new JsonResult(banList);
    }

    @ApiRoute(Path = "/api/User/GetWhiteList")
    public JsonResult GetWhiteList() {
        ArrayList<String> whiteList = new ArrayList<String>();
        server.getWhitelistedPlayers().forEach(whiteListUser -> {
            whiteList.add(whiteListUser.getName());
        });

        return new JsonResult(whiteList);
    }

    @ApiRoute(Path = "/api/User/AddWhiteList")
    public JsonResult AddWhiteList(Map data) {
        String userName = data.get("name").toString();
        OfflinePlayer player = server.getOfflinePlayer(userName);
        player.setWhitelisted(true);
        return GetWhiteList();
    }

    @ApiRoute(Path = "/api/User/RemoveWhiteList")
    public JsonResult RemoveWhiteList(Map data) {
        String userName = data.get("name").toString();
        OfflinePlayer player = server.getOfflinePlayer(userName);
        player.setWhitelisted(false);
        return GetWhiteList();
    }

    @ApiRoute(Path = "/api/User/GetOPList")
    public JsonResult GetOpList() {
        ArrayList<String> opList = new ArrayList<String>();
        server.getOperators().forEach(op -> {
            opList.add(op.getName());
        });
        return new JsonResult(opList);
    }

    @ApiRoute(Path = "/api/User/AddOp")
    public JsonResult AddOp(Map data) {
        String userName = data.get("name").toString();
        OfflinePlayer player = server.getOfflinePlayer(userName);
        player.setOp(true);

        return GetOpList();
    }

    @ApiRoute(Path = "/api/User/RemoveOp")
    public JsonResult RemoveOp(Map data) {
        String userName = data.get("name").toString();
        OfflinePlayer player = server.getOfflinePlayer(userName);
        player.setOp(false);

        return GetOpList();
    }

    @ApiRoute(Path = "/api/User/SetGameMode")
    public JsonResult SetGameMode(Map data) {
        String userName = data.get("name").toString();
        int gameMode = (int) Double.parseDouble(data.get("gameMode").toString());
        Player user = UserUtils.GetPlayerByName(userName);
        if (user == null) return new JsonResult(null, 404, "Error: user not found.");

        server.getScheduler().scheduleSyncDelayedTask(Lode.instance, () -> {
            user.setGameMode(GameMode.getByValue(gameMode));
        });

        return new JsonResult();
    }

    @ApiRoute(Path = "/api/User/SendChat")
    public JsonResult SendChat(Map data) {
        String userName = data.get("name").toString();
        String message = data.get("message").toString();

        Player player = UserUtils.GetPlayerByName(userName);
        if (player == null) return new JsonResult(null, 404, "Error: user not found.");

        player.chat(message);

        return new JsonResult();
    }

    @ApiRoute(Path = "/api/User/SendMessage")
    public JsonResult SendMessage(Map data) {
        String userName = data.get("name").toString();
        String message = data.get("message").toString();

        Player player = UserUtils.GetPlayerByName(userName);
        if (player == null) return new JsonResult(null, 404, "Error: user not found.");

        player.sendMessage(message);

        return new JsonResult();
    }

    @ApiRoute(Path = "/api/User/SendExperience")
    public JsonResult SendExperience(Map data) {
        String userName = data.get("name").toString();
        int expType = (int) Double.parseDouble(data.get("type").toString());
        int value = (int) Double.parseDouble(data.get("value").toString());
        Object msg = data.get("message");

        Player player = UserUtils.GetPlayerByName(userName);
        if (player == null) return new JsonResult(null, 404, "Error: user not found.");

        if (expType == 0) {
            player.giveExp(value);
        } else {
            player.giveExpLevels(value);
        }

        if (msg != null) {
            player.sendMessage(msg.toString());
        }

        return new JsonResult();
    }

    @ApiRoute(Path = "/api/User/SetPlayerFire")
    public JsonResult SetPlayerFire(Map data) {
        String userName = data.get("name").toString();
        int time = (int) Double.parseDouble(data.get("time").toString());
        Object msg = data.get("message");

        Player player = UserUtils.GetPlayerByName(userName);
        if (player == null) return new JsonResult(null, 404, "Error: user not found.");

        player.setFireTicks(time / 1000 * 20);

        if (msg != null) {
            player.sendMessage(msg.toString());
        }

        return new JsonResult();
    }

    @ApiRoute(Path = "/api/User/KillPlayer")
    public JsonResult KillPlayer(Map data) {
        String userName = data.get("name").toString();
        Object msg = data.get("message");

        Player player = UserUtils.GetPlayerByName(userName);
        if (player == null) return new JsonResult(null, 404, "Error: user not found.");

        server.getScheduler().scheduleSyncDelayedTask(Lode.instance, () -> {
            player.setHealth(0);
        });

        if (msg != null) {
            player.sendMessage(msg.toString());
        }

        return new JsonResult();
    }

    @ApiRoute(Path = "/api/User/KickPlayer")
    public JsonResult KickPlayer(Map data) {
        String userName = data.get("name").toString();
        Object reason = data.get("reason");

        Player player = UserUtils.GetPlayerByName(userName);
        if (player == null) return new JsonResult(null, 404, "Error: user not found.");

        server.getScheduler().scheduleSyncDelayedTask(Lode.instance, () -> {
            player.kickPlayer(reason == null ? "" : reason.toString());
        });

        return new JsonResult();
    }

    @ApiRoute(Path = "/api/User/ClearPlayerInventory")
    public JsonResult ClearPlayerInventory(Map data) {
        String userName = data.get("name").toString();
        Object msg = data.get("message");

        Player player = UserUtils.GetPlayerByName(userName);
        if (player == null) return new JsonResult(null, 404, "Error: user not found.");

        player.getInventory().clear();

        if (msg != null) {
            player.sendMessage(msg.toString());
        }

        return new JsonResult();
    }

    @ApiRoute(Path = "/api/User/GetPlayerInventory")
    public JsonResult GetPlayerInventory(Map data) {
        String userName = data.get("name").toString();
        ArrayList<PlayerInventoryDTO> list = new ArrayList<PlayerInventoryDTO>();

        Player player = UserUtils.GetPlayerByName(userName);
        if (player == null) return new JsonResult(null, 404, "Error: user not found.");

        PlayerInventory playerInventory = player.getInventory();
        for (int i = 0; i < playerInventory.getSize(); i++) {
            ItemStack item = playerInventory.getItem(i);
            if (item != null) {
                if (item.getType() != Material.getMaterial("AIR")) {
                    PlayerInventoryDTO playerInventoryDTO = new PlayerInventoryDTO();
                    playerInventoryDTO.setIndex(i);
                    playerInventoryDTO.setId(item.getType().getId());
                    playerInventoryDTO.setName(item.getType().name());
                    playerInventoryDTO.setCount(item.getAmount());

                    list.add(playerInventoryDTO);
                }
            }
        }

        return new JsonResult(list);
    }

    @ApiRoute(Path = "/api/User/GetInHandItem")
    public JsonResult GetInHandItem(Map data) {
        String userName = data.get("name").toString();

        Player player = UserUtils.GetPlayerByName(userName);
        if (player == null) return new JsonResult(null, 404, "Error: user not found.");

        PlayerInventory playerInventory = player.getInventory();

        ItemStack item = playerInventory.getItemInMainHand();
        PlayerInventoryDTO playerInventoryDTO = new PlayerInventoryDTO();
        // playerInventoryDTO.setIndex(playerInventory.getItemInHand().);
        playerInventoryDTO.setId(item.getType().getId());
        playerInventoryDTO.setName(item.getType().name());
        playerInventoryDTO.setCount(item.getAmount());

        return new JsonResult(playerInventoryDTO);
    }

    @ApiRoute(Path = "/api/User/GetPlayerPosition")
    public JsonResult GetPlayerPosition(Map data) {
        String userName = data.get("name").toString();

        Player player = UserUtils.GetPlayerByName(userName);
        if (player == null) return new JsonResult(null, 404, "Error: user not found.");

        Location location = player.getLocation();
        UserPositionDTO userPositionDTO = new UserPositionDTO();
        userPositionDTO.setX(location.getX());
        userPositionDTO.setY(location.getY());
        userPositionDTO.setZ(location.getZ());

        return new JsonResult(userPositionDTO);
    }

    @ApiRoute(Path = "/api/User/SetPlayerPosition")
    public JsonResult SetPlayerPosition(Map data) {
        String userName = data.get("name").toString();
        double x = Double.parseDouble(data.get("x").toString());
        double y = Double.parseDouble(data.get("y").toString());
        double z = Double.parseDouble(data.get("z").toString());

        Object msg = data.get("message");

        Player player = UserUtils.GetPlayerByName(userName);
        if (player == null) return new JsonResult(null, 404, "Error: user not found.");

        player.teleport(new Location(player.getWorld(), x, y, z));

        if (msg != null) {
            player.sendMessage(msg.toString());
        }

        return new JsonResult();
    }

    @ApiRoute(Path = "/api/User/GetPlayerHealth")
    public JsonResult GetPlayerHealth(Map data) {
        String userName = data.get("name").toString();

        Player player = UserUtils.GetPlayerByName(userName);
        if (player == null) return new JsonResult(null, 404, "Error: user not found.");

        return new JsonResult(player.getHealth());
    }

    @ApiRoute(Path = "/api/User/SetPlayerHealth")
    public JsonResult SetPlayerHealth(Map data) {
        String userName = data.get("name").toString();
        float healthValue = Float.parseFloat(data.get("value").toString());

        Player player = UserUtils.GetPlayerByName(userName);
        if (player == null) return new JsonResult(null, 404, "Error: user not found.");

        server.getScheduler().scheduleSyncDelayedTask(Lode.instance, () -> {
            player.setHealth(healthValue);
        });

        Object msg = data.get("message");
        if (msg != null) {
            player.sendMessage(msg.toString());
        }

        return new JsonResult();
    }

    @ApiRoute(Path = "/api/User/GetPlayerHunger")
    public JsonResult GetPlayerHunger(Map data) {
        String userName = data.get("name").toString();

        Player player = UserUtils.GetPlayerByName(userName);
        if (player == null) return new JsonResult(null, 404, "Error: user not found.");

        return new JsonResult(player.getFoodLevel());
    }

    @ApiRoute(Path = "/api/User/SetPlayerHunger")
    public JsonResult SetPlayerHunger(Map data) {
        String userName = data.get("name").toString();
        int hungerValue = (int) Double.parseDouble(data.get("value").toString());

        Player player = UserUtils.GetPlayerByName(userName);
        if (player == null) return new JsonResult(null, 404, "Error: user not found.");

        server.getScheduler().scheduleSyncDelayedTask(Lode.instance, () -> {
            player.setFoodLevel(hungerValue);
        });

        Object msg = data.get("message");
        if (msg != null) {
            player.sendMessage(msg.toString());
        }

        return new JsonResult();
    }

    @ApiRoute(Path = "/api/User/SetAllowFlight")
    public JsonResult SetAllowFlight(Map data){
        String userName = data.get("name").toString();
        boolean canFly = Boolean.parseBoolean(data.get("state").toString());
        Object msg = data.get("message");

        Player player = UserUtils.GetPlayerByName(userName);
        if (player == null) return new JsonResult(null, 404, "Error: user not found.");

        server.getScheduler().scheduleSyncDelayedTask(Lode.instance, () -> {
            player.setAllowFlight(canFly);
        });

        if (msg != null) {
            player.sendMessage(msg.toString());
        }

        return new JsonResult();
    }
}
