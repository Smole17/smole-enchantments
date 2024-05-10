package ru.smole.enchantments.data;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

public class EnchantmentData implements ConfigurationSerializable {

    public final static String ENCHANTMENT_KEY = "smole_enchantments";
    public final static PersistentDataType<Byte, Boolean> ENCHANTMENT_DATA_TYPE = PersistentDataType.BOOLEAN;

    private final String id;
    private final int level;
    private final String displayName;
    private final ItemStack itemStack;
    private final NamespacedKey key;

    public EnchantmentData(Map<String, Object> args) {
        this.level = (int) args.get("level");
        this.id = (String) args.get("id");
        this.displayName = (String) args.get("display");
        this.itemStack = (ItemStack) args.get("item");
        this.key = new NamespacedKey(ENCHANTMENT_KEY, toString());

        itemStack.editMeta(itemMeta -> itemMeta.getPersistentDataContainer().set(key, ENCHANTMENT_DATA_TYPE, false));
    }

    @Override
    public String toString() {
        return "%s_%s".formatted(id, level);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of();
    }

    public int getLevel() {
        return level;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ItemStack getItemStack() {
        return itemStack.clone();
    }

    public NamespacedKey getKey() {
        return key;
    }

    public boolean hasEnchantment(ItemStack itemStack) {
        if (itemStack == null) return false;

        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta == null) return false;

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if (!container.has(getKey(), ENCHANTMENT_DATA_TYPE)) return false;

        boolean isBook = Boolean.FALSE.equals(container.get(getKey(), ENCHANTMENT_DATA_TYPE));

        return !isBook;
    }

    public boolean apply(ItemStack itemStack) {
        if (itemStack == null || hasEnchantment(itemStack)) return false;

        itemStack.editMeta(itemMeta -> {
            PersistentDataContainer container = itemMeta.getPersistentDataContainer();

            Optional<NamespacedKey> foundSameEnchantmentKey = container.getKeys()
                    .stream()
                    .filter(key -> key.namespace().equals(ENCHANTMENT_KEY))
                    .findFirst();

            foundSameEnchantmentKey.ifPresent(container::remove);

            container.set(key, ENCHANTMENT_DATA_TYPE, true);
        });
        return true;
    }
}
