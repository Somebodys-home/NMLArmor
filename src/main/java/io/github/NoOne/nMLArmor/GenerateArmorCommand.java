package io.github.NoOne.nMLArmor;

import io.github.NoOne.nMLItems.ItemRarity;
import io.github.NoOne.nMLItems.ItemType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GenerateArmorCommand implements CommandExecutor, TabCompleter {
    private ArmorSystem armorSystem;

    public GenerateArmorCommand(NMLArmor nmlArmor) {
        armorSystem = nmlArmor.getArmorSystem();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            int level = Integer.parseInt(args[0]);
            String rarity = args[1];
            String type = args[2];
            String piece = args[3];
            ItemStack armor = armorSystem.generateArmor(player, ItemRarity.getItemRarityFromString(rarity), ItemType.getArmorTypeFromString(type), piece, level);

            player.getInventory().setItemInMainHand(armor);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(List.of("<level>")).stream()
                    .filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        } else if (args.length == 2) {
            return new ArrayList<>(List.of("common", "uncommon", "rare", "mythical")).stream()
                    .filter(s -> s.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        } else if (args.length == 3) {
            return new ArrayList<>(List.of("light", "medium", "heavy")).stream()
                    .filter(s -> s.toLowerCase().startsWith(args[2].toLowerCase()))
                    .collect(Collectors.toList());
        } else if (args.length == 4) {
            return new ArrayList<>(List.of("helmet", "chestplate", "leggings", "boots")).stream()
                    .filter(s -> s.toLowerCase().startsWith(args[3].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}
