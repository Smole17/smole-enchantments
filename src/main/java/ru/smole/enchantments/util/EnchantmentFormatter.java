package ru.smole.enchantments.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.smole.enchantments.SmoleEnchantments;
import ru.smole.enchantments.data.EnchantmentData;

import java.util.Collections;
import java.util.List;

public class EnchantmentFormatter {

    public static List<EnchantmentData> getEnchantments(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta == null) return Collections.emptyList();

        return itemMeta.getPersistentDataContainer()
                .getKeys()
                .stream()
                .filter(key -> key.namespace().equals(EnchantmentData.ENCHANTMENT_KEY))
                .map(key -> SmoleEnchantments.ENCHANTMENTS.get(key.value()))
                .toList();
    }

    public static void applyEnchantmentFormat(ItemStack itemStack) {
        itemStack.editMeta(itemMeta -> {
            itemMeta.lore(null);

            List<TextComponent> newLore = getEnchantments(itemStack)
                    .stream()
                    .map(EnchantmentData::getDisplayName)
                    .map(Component::text)
                    .toList();

            itemMeta.lore(newLore);
        });
    }
}
