package org.guyapooye.mtp;

import lombok.Getter;
import net.minecraft.world.item.Items;
import org.bukkit.plugin.java.JavaPlugin;
import org.guyapooye.mtp.blocks.loaders.recipies.RecipeHandler;
import org.guyapooye.mtp.events.MTBlockInteraction;
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
        RecipeHandler.addShapedRecipe("test_ulv", MTItems.TEST_TIERED_ULV,"AAA","A A", "AAA",'A', Items.COPPER_INGOT);
        RecipeHandler.addShapedRecipe("test_lv", MTItems.TEST_TIERED_LV,"AAA","ABA", "AAA",'A', Items.IRON_INGOT, 'B', MTItems.TEST_TIERED_ULV);
    }

    @Override
    public void onDisable() {
        MTBlockInteraction.BIDWPM.forEach(MTLocationFiles::saveToFile);
    }

}
