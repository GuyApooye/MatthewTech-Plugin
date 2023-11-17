package org.guyapooye.mtp.utils;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.guyapooye.mtp.MatthewTechPlugin;
import org.guyapooye.mtp.blocks.MTBlocks;
import org.guyapooye.mtp.blocks.tileentities.MTTileEntity;
import org.guyapooye.mtp.events.MTBlockInteraction;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.apache.logging.log4j.core.util.FileUtils.mkdir;


public class MTLocationFiles {
    public static Map<String, File> fileMap = new HashMap<>();
    @Getter
    public static Map<String, FileConfiguration>  locationFileMap = new HashMap<>();
    public static void setupFiles() {
        MTBlocks.registeredBlocks.forEach((s, b) -> {
            if(b instanceof MTTileEntity) return;
            File file = new File(Bukkit.getServer().getPluginManager().getPlugin(MatthewTechPlugin.getInstance().getName()).getDataFolder().getPath() + "\\Blocks",s+".gtpblock");
            fileMap.put(s,file);
            if (!file.exists()) {
                try {
                    mkdir(new File(Bukkit.getServer().getPluginManager().getPlugin(MatthewTechPlugin.getInstance().getName()).getDataFolder().getPath() + "\\Blocks"),true);
                    file.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            locationFileMap.put(s, YamlConfiguration.loadConfiguration(file));
        });
    }
    public static void saveToFile(String id,Map<String, List<String>> worldLocationsMap){
        FileConfiguration locationFile = locationFileMap.get(id);
        AtomicInteger i = new AtomicInteger();
        worldLocationsMap.forEach((w,vecs) -> {
            locationFile.createSection(w);
            vecs.forEach(v -> {
                locationFile.getConfigurationSection(w).set(i.toString(),v);
                i.getAndIncrement();
            });
        });
        try {
            locationFile.save(fileMap.get(id));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void load() {
        MTBlocks.registeredBlocks.forEach((id, b) -> {
            if(b instanceof MTTileEntity) return;
            FileConfiguration locationFile = locationFileMap.get(id);
            AtomicInteger i = new AtomicInteger();
            Map<String,List<String>> m = new HashMap<>();
            Bukkit.getServer().getWorlds().forEach(w -> {
                if(locationFile.getConfigurationSection(w.getName())==null) return;
                List<String> vecs = new ArrayList<>();
                while(locationFile.getConfigurationSection(w.getName()).getString(i.toString())!=null) {
                    vecs.add(locationFile.getConfigurationSection(w.getName()).getString(i.toString()));
                    i.getAndIncrement();
                }
                m.put(w.getName(),vecs);
            });
            MTBlockInteraction.BIDWPM.put(id,m);
        });
    }
}
