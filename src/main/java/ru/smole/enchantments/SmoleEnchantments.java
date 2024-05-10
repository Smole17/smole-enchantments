package ru.smole.enchantments;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import ru.smole.enchantments.command.GiveEnchantmentCommand;
import ru.smole.enchantments.applier.EnchantmentApplierListener;
import ru.smole.enchantments.data.EnchantmentData;
import ru.smole.enchantments.data.impl.DiamondDiggerEnchantment;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SmoleEnchantments extends JavaPlugin {

    public final static Map<String, EnchantmentData> ENCHANTMENTS = new HashMap<>();

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(DiamondDiggerEnchantment.class);

        saveDefaultConfig();

        loadEnchantments();

        Bukkit.getPluginManager().registerEvents(new EnchantmentApplierListener(), this);

        Bukkit.getCommandMap().register("giveenchantments", new GiveEnchantmentCommand());
    }

    public void loadEnchantments() {
        Map<String, EnchantmentData> enchantments = getConfig().getKeys(false)
                .stream()
                .map(s -> getConfig().getSerializable(s, EnchantmentData.class))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(EnchantmentData::toString, Function.identity()));

        ENCHANTMENTS.putAll(enchantments);
    }
}
