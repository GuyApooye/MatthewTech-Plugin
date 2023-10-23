package org.guyapooye.mtp;

import lombok.Getter;
import net.minecraft.world.item.Items;
import org.bukkit.plugin.java.JavaPlugin;
import org.guyapooye.mtp.loaders.recipes.RecipeHandler;
import org.guyapooye.mtp.events.MTBlockInteraction;
import org.guyapooye.mtp.loaders.recipes.RecipeManager;
import org.guyapooye.mtp.utils.MTItems;
import org.guyapooye.mtp.utils.MTLocationFiles;


public final class MatthewTechPlugin extends JavaPlugin {
    @Getter
    static MatthewTechPlugin instance;


    @Override
    public void onEnable() {
        instance = this;
        CommandManager cm = new CommandManager();
        CommandManager.registerCommands();
        getServer().getPluginManager().registerEvents(new MTBlockInteraction(),this);
        MTLocationFiles.setupFiles();
        MTLocationFiles.load();
        RecipeManager.init();
    }

    @Override
    public void onDisable() {
        MTBlockInteraction.BIDWPM.forEach(MTLocationFiles::saveToFile);
    }

}
