package org.kirat.sveconomy;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EconomyManager {

    private final JavaPlugin plugin;

    private final Map<UUID, BossBar> bossBars = new HashMap<>();

    public EconomyManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public long calculateBalance(Player player) {

        long balance = 0;

        for (ItemStack item : player.getInventory().getContents()) {

            if (item == null || item.getType() == Material.AIR) {
                continue;
            }

            switch (item.getType()) {

                case GOLD_NUGGET -> {
                    balance += item.getAmount();
                }

                case GOLD_INGOT -> {
                    balance += (long) item.getAmount() * 9;
                }

                case GOLD_BLOCK -> {
                    balance += (long) item.getAmount() * 81;
                }

                default -> {
                }
            }
        }

        return balance;
    }

    public void createBossBar(Player player) {

        if (bossBars.containsKey(player.getUniqueId())) {
            return;
        }

        BossBar bossBar = Bukkit.createBossBar(
                "",
                BarColor.YELLOW,
                BarStyle.SOLID
        );

        bossBar.addPlayer(player);

        bossBars.put(player.getUniqueId(), bossBar);

        updateBossBar(player);
    }

    public void updateBossBar(Player player) {

        BossBar bossBar = bossBars.get(player.getUniqueId());

        if (bossBar == null) {
            return;
        }

        long balance = calculateBalance(player);

        bossBar.setTitle(
                "§6🪙 §e" + balance + " монет"
        );

        bossBar.setProgress(1.0);
    }

    public void refresh(Player player) {
        updateBossBar(player);
    }

    public void removeBossBar(Player player) {

        BossBar bossBar = bossBars.remove(player.getUniqueId());

        if (bossBar != null) {
            bossBar.removeAll();
        }
    }

    public void removeAllBossBars() {

        for (BossBar bossBar : bossBars.values()) {
            bossBar.removeAll();
        }

        bossBars.clear();
    }
}