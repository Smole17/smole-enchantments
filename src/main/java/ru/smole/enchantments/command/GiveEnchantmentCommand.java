package ru.smole.enchantments.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.smole.enchantments.SmoleEnchantments;
import ru.smole.enchantments.data.EnchantmentData;

import java.util.Collections;
import java.util.List;

public class GiveEnchantmentCommand extends Command {

    public GiveEnchantmentCommand() {
        super("giveenchantment", "", "/", List.of("ge"));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (args.length < 2) {
            sender.sendMessage(Component.text("Please use right signature of command: /giveenchantment <target> <enchantment_id>").color(NamedTextColor.RED));
            return false;
        }

        String targetName = args[0];
        Player target = Bukkit.getPlayer(targetName);

        if (target == null) {
            sender.sendMessage(Component.text("Target player is offline.").color(NamedTextColor.RED));
            return false;
        }

        String enchantmentId = args[1];
        EnchantmentData enchantmentData = SmoleEnchantments.ENCHANTMENTS.get(enchantmentId);

        if (enchantmentData == null) {
            sender.sendMessage(Component.text("Unknown enchantment.").color(NamedTextColor.RED));
            return false;
        }

        target.getInventory().addItem(enchantmentData.getItemStack());
        sender.sendMessage(Component.text("Successfully!").color(NamedTextColor.GREEN));
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        return switch (args.length) {
            case 1 -> Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
            case 2 -> SmoleEnchantments.ENCHANTMENTS.keySet().stream().toList();
            default -> Collections.emptyList();
        };
    }
}
