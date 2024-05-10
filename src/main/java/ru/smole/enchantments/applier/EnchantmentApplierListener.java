package ru.smole.enchantments.applier;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.smole.enchantments.data.EnchantmentData;
import ru.smole.enchantments.util.EnchantmentFormatter;

public class EnchantmentApplierListener implements Listener {

    @EventHandler
    public void onDrag(InventoryClickEvent event) {
        ItemStack currentItem = event.getCurrentItem();

        if (currentItem == null) return;

        ItemStack cursor = event.getCursor();

        if (cursor == null) return;

        HumanEntity whoClicked = event.getWhoClicked();

        EnchantmentFormatter.getEnchantments(cursor)
                .forEach(enchantmentData -> {
                    if (!applyEnchantment(enchantmentData, currentItem)) return;

                    whoClicked.setItemOnCursor(null);
                    event.setCancelled(true);
                });
    }

    private boolean applyEnchantment(EnchantmentData enchantmentData, ItemStack itemStack) {
        if (!enchantmentData.apply(itemStack)) return false;

        EnchantmentFormatter.applyEnchantmentFormat(itemStack);
        return true;
    }
}
