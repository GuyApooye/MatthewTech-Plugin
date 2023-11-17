package org.guyapooye.mtp.events;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.guyapooye.mtp.blocks.tileentities.MTTileEntity;
import org.guyapooye.mtp.blocks.tileentities.TieredTileEntity;
import org.guyapooye.mtp.blocks.MTBlocks;
import org.guyapooye.mtp.utils.MTUtils;

import java.util.*;


public class MTBlockInteraction implements Listener {
    public static Map<String, Map<String, List<String>>> BIDWPM = new HashMap<>();
    @EventHandler
    public void blockPlace(BlockPlaceEvent e) {
        ItemStack i = CraftItemStack.asNMSCopy(e.getItemInHand());
        if (!i.getOrCreateTag().contains("MT")) return;
        String id = i.getOrCreateTag().getCompound("MT").getString("id");
        if(MTBlocks.registeredBlocks.get(id) instanceof MTTileEntity block) {
            CraftWorld w = (CraftWorld) e.getBlock().getWorld();
            BlockEntity b = w.getHandle().getBlockEntity(new BlockPos(e.getBlock().getX(),e.getBlock().getY(),e.getBlock().getZ()));
            CompoundTag t = new CompoundTag();
            t.put("PublicBukkitValues",new CompoundTag());
            t.getCompound("PublicBukkitValues").put("MT",new CompoundTag());
            if(block instanceof TieredTileEntity tiered) t.getCompound("PublicBukkitValues").getCompound("MT").putString("id",id+tiered.getVoltage());
            else t.getCompound("PublicBukkitValues").getCompound("MT").putString("id",id);
            b.load(t);
            return;
        }
        if(!BIDWPM.containsKey(id)) {
            List<String> vecs = new ArrayList<>();
            vecs.add(MTUtils.locationToString(e.getBlock().getLocation()));
            Map<String, List<String>> m = new HashMap<>();
            m.put(e.getBlock().getWorld().getName(), vecs);
            BIDWPM.put(id, m);
            return;
        }
        if (BIDWPM.get(id).get(e.getBlock().getWorld().getName()) == null || !BIDWPM.get(id).containsKey(e.getBlock().getWorld().getName())) {
            List<String> vecs = new ArrayList<>();
            vecs.add(MTUtils.locationToString(e.getBlock().getLocation()));
            BIDWPM.get(id).put(e.getBlock().getWorld().getName(),vecs);
            return;
        }
        BIDWPM.get(id).get(e.getBlock().getWorld().getName()).add(MTUtils.locationToString(e.getBlock().getLocation()));
    }
    @EventHandler
    public void blockBreak(BlockBreakEvent e) {
        BIDWPM.forEach((id, map) -> {
            if(!map.containsKey(e.getBlock().getWorld().getName())) return;
            if(map.get(e.getBlock().getWorld().getName()).contains(MTUtils.locationToString(e.getBlock().getLocation()))) {
                map.get(e.getBlock().getWorld().getName()).remove(MTUtils.locationToString(e.getBlock().getLocation()));
            }
        });
    }
    @EventHandler
    public void blockInteract(PlayerInteractEvent e) {
        if(e.getClickedBlock() == null || e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        CraftWorld w = (CraftWorld) e.getClickedBlock().getWorld();
        BlockEntity b = w.getHandle().getBlockEntity(new BlockPos(e.getClickedBlock().getX(),e.getClickedBlock().getY(),e.getClickedBlock().getZ()));
        if(b!=null) {
            if(!b.saveWithFullMetadata().getCompound("PublicBukkitValues").contains("MT")) return;
            MTTileEntity tileEntity = ((MTTileEntity) MTBlocks.registeredBlocks.get(b.saveWithFullMetadata().getCompound("PublicBukkitValues").getCompound("MT").getString("id")));
            tileEntity.rightClick(e.getPlayer(), e.getClickedBlock().getLocation());
            e.setCancelled(true);
        }
    }
}
