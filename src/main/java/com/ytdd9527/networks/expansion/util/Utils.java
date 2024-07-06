package com.ytdd9527.networks.expansion.util;


import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;


public final class Utils {

    public static String color(String str) {
        if (str == null) {
            return null;
        }

        return ChatColor.translateAlternateColorCodes('&', str);
    }
    public static void giveOrDropItem(Player p, ItemStack toGive) {
        for (int i = 0; i < 64; i++) {
            if (toGive.getAmount() <= 0) {
                return;
            }
            ItemStack incoming = toGive.clone();
            incoming.setAmount(Math.min(toGive.getMaxStackSize(), toGive.getAmount()));
            toGive.setAmount(toGive.getAmount() - incoming.getAmount());
            Collection<ItemStack> leftover = p.getInventory().addItem(incoming).values();
            for (ItemStack itemStack : leftover) {
                p.getWorld().dropItemNaturally(p.getLocation(), itemStack);
            }
        }
    }
    public static void send(CommandSender p, String message) {
        p.sendMessage(color("&7[&6网络拓展&7] &r" + message));
    }
}
