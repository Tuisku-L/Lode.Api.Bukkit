package tech.v2c.minecraft.plugins.lode.RESTful.actions;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tech.v2c.minecraft.plugins.lode.RESTful.global.BaseAction;
import tech.v2c.minecraft.plugins.lode.RESTful.global.annotations.ApiRoute;
import tech.v2c.minecraft.plugins.lode.RESTful.global.entities.item.ItemDTO;
import tech.v2c.minecraft.plugins.lode.tools.gameUtils.UserUtils;
import tech.v2c.minecraft.plugins.lode.tools.results.JsonResult;

import java.util.ArrayList;
import java.util.Map;

public class ItemAction extends BaseAction {
    @ApiRoute(Path = "/api/Item/GetList")
    public JsonResult GetItemList() {
        ArrayList<ItemDTO> list = new ArrayList<ItemDTO>();

        Material[] items = Material.values();

        for (int i = 0; i < items.length; i++) {
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setId(items[i].getId());
            itemDTO.setName(items[i].name());
            list.add(itemDTO);
        }

        return new JsonResult(list);
    }

    @ApiRoute(Path = "/api/Item/GetItemInfo")
    public JsonResult GetItemInfo(Map data) {
        String itemName = data.get("item").toString();

        Material item = Material.getMaterial(itemName);
        if(item == null){
            return new JsonResult(null, 404, "Error: item not found.");
        }
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(item.getId());
        itemDTO.setName(item.name());

        return new JsonResult(itemDTO);
    }

    @ApiRoute(Path = "/api/Item/SendItemToUser")
    public JsonResult SendItem(Map data) {
        String userName = data.get("name").toString();
        String itemName = data.get("item").toString();
        Object count = data.get("count");
        Object msg = data.get("message");

        Player player = UserUtils.GetPlayerByName(userName);
        if (player == null) return new JsonResult(null, 404, "Error: user not found.");

        if (player.getInventory().firstEmpty() == -1) {
            return new JsonResult(null, 409, "Error: Player's Inventory is full.");
        } else {
            int firstEmpty = player.getInventory().firstEmpty();

            Material mate = Material.getMaterial(itemName);
            if (mate == null) {
                return new JsonResult(null, 404, "Error: item not found.");
            }

            ItemStack item = new ItemStack(mate, (int) Double.parseDouble(count.toString()));

            player.getInventory().setItem(firstEmpty, item);
            player.updateInventory();

            if (msg != null) {
                player.sendMessage(msg.toString());
            }

            return new JsonResult();
        }
    }
}
