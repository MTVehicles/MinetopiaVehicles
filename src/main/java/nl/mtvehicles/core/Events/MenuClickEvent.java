package nl.mtvehicles.core.Events;

import nl.mtvehicles.core.Infrastructure.Helpers.ItemFactory;
import nl.mtvehicles.core.Infrastructure.Helpers.TextUtils;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MenuClickEvent implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if(e.getView().getTitle().contains("Vehicle Menu")){
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            p.sendMessage("vte");

            if(!e.getCurrentItem().hasItemMeta()){
                return;
            }

            List<Map<?, ?>> vehicles = Main.vehiclesConfig.getConfig().getMapList("voertuigen");
            Map<?, ?> skins = (Map<?, ?>) vehicles.get(e.getRawSlot()).get("cars");

            for (Map<?, ?> vehicle : Main.vehiclesConfig.getConfig().getMapList("voertuigen")) {
                System.out.println(vehicle);
                for (Map<?, ?> cars : Main.vehiclesConfig.getConfig().getMapList("cars")) {
                    System.out.println(vehicle);

                }
            }


//            Inventory inv = Bukkit.createInventory(null, 18, "Vehicle Menu");
//            List <Map<?, ?>> list = new ArrayList<Map<?, ?>>();
//            List <Map<?, ?>> listInList = new ArrayList<Map<?, ?>>();
//            list = Main.vehiclesConfig.getConfig().getMapList("voertuigen").get(e.getRawSlot());
//
//            for (Map<?, ?> m: list) {
//
//                //do stuff here with each list-entry, one of those entrys will be listinlist
//                if (m.get("cars") instanceof ArrayList<?>) {
//                    listInList = (ArrayList<Map<?, ?>>) m.get("cars");
//                    for (Map<?, ?> lm: listInList) {
//                        //System.out.println(lm);
//                        System.out.println(m);
//                        System.out.println(e.getCurrentItem().getItemMeta().getDisplayName());
//                        if (e.getCurrentItem().equals(m.get("name"))){
//                            System.out.println("JA");
//
//                        }
//                        inv.addItem(carItem((int) lm.get("itemDamage"), (String) lm.get("name"), (String) lm.get("SkinItem")));
//                    }
//                }
//            }
//
//            p.openInventory(inv);




            //System.out.println(cars.get("item"));
        }
    }

    public ItemStack carItem(int id, String name, String material){
        ItemStack car = (new ItemFactory(Material.getMaterial(material))).setDurability((short)id).unbreakable().setName(TextUtils.colorize(name)).toItemStack();

        return car;
    }
}
