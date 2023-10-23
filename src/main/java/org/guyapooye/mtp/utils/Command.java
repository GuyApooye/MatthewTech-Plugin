package org.guyapooye.mtp.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.entity.Player;
import org.guyapooye.mtp.MatthewTechPlugin;

import java.util.ArrayList;

@Getter
@Setter
public class Command extends BukkitCommand{

    private PlayerRunnable onCommand;
    private String arg;
    private String[] args;
    private Player player;

    public Command(String name, String description, String usageMessage, PlayerRunnable command) {
        super(name);
        this.description = description;
        this.usageMessage = usageMessage;
        this.setAliases(new ArrayList<>());
        this.onCommand = command;
        ((CraftServer) MatthewTechPlugin.getInstance().getServer()).getCommandMap().register("gtp", this);
    }

    @SneakyThrows
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof Player player) {
            this.arg = commandLabel;
            this.args = args;
            this.player = player;
            onCommand.run(player);
        }
        return false;
    }
}
