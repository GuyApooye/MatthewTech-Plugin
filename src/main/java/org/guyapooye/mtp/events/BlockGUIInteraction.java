package org.guyapooye.mtp.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class BlockGUIInteraction implements Listener {
    @EventHandler
    public void guiClick(InventoryClickEvent e) {
        Bukkit.broadcastMessage(e.getView().getTitle());
    }
}
