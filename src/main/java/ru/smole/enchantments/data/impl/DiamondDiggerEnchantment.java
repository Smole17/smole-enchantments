package ru.smole.enchantments.data.impl;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import ru.smole.enchantments.data.EnchantmentData;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class DiamondDiggerEnchantment extends EnchantmentData implements Listener {

    public final static ItemStack DUG_ITEM = new ItemStack(Material.DIAMOND);

    public DiamondDiggerEnchantment(Map<String, Object> args) {
        super(args);
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getProvidingPlugin(DiamondDiggerEnchantment.class));
    }

    public double getChanceByLevel() {
        return switch (getLevel()) {
            case 1 -> .1; // 0.1 * 100 = 10%
            case 2 -> .25; // 0.25 * 100 = 25%
            default -> .0; // 0.0 * 100 = 0%
        };
    }

    public int getAmountByLevel() {
        return getLevel();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        ItemStack itemInMainHand = event.getPlayer().getInventory().getItemInMainHand();

        if (!hasEnchantment(itemInMainHand) || getChanceByLevel() < ThreadLocalRandom.current().nextDouble()) return;

        Block block = event.getBlock();
        block.getWorld().dropItemNaturally(block.getLocation(), DUG_ITEM.asQuantity(getAmountByLevel()));
    }
}
