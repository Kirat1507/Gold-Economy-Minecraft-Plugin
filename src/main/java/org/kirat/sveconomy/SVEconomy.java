package org.kirat.sveconomy;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class SVEconomy extends JavaPlugin {

    private EconomyManager economyManager;

    @Override
    public void onEnable() {

        economyManager = new EconomyManager(this);

        getServer().getPluginManager().registerEvents(
                new EconomyListener(economyManager),
                this
        );

        if (getCommand("money") != null) {
            getCommand("money").setExecutor((sender, command, label, args) -> {

                if (!(sender instanceof Player player)) {
                    sender.sendMessage("§cЭту команду может использовать только игрок.");
                    return true;
                }

                long balance = economyManager.calculateBalance(player);

                player.sendMessage(
                        "§6§lSVEconomy §8» §fВаш баланс: §e"
                                + balance
                                + " монет"
                );

                return true;
            });
        }

        getLogger().info("SVEconomy успешно включён!");
    }

    @Override
    public void onDisable() {

        if (economyManager != null) {
            economyManager.removeAllBossBars();
        }

        getLogger().info("SVEconomy выключен!");
    }
}