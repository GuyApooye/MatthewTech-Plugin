package org.guyapooye.mtp;


import lombok.Getter;
import org.bukkit.Bukkit;
import org.guyapooye.mtp.items.MTItem;
import org.guyapooye.mtp.utils.Command;

import java.util.ArrayList;
import java.util.List;


public class CommandManager {
    @Getter
    final static List<Command> Commands = new ArrayList<>();
    public static void registerCommands() {
        Command TEST = new Command("Test","Test","Test",player -> {});
        TEST.setOnCommand(player -> {
            Bukkit.broadcastMessage("TEST");
        });
        Commands.add(TEST);
        Command mtgive = new Command("MTGive","Gives mt items from ID.","/mtgive <item_id>",player -> {});
        mtgive.setOnCommand(player -> {
            String[] args = mtgive.getArgs();
            player.getInventory().setItemInMainHand(MTItem.fromID(args[0]).asBukkitCopy());
        });
        Commands.add(mtgive);
    }
}
