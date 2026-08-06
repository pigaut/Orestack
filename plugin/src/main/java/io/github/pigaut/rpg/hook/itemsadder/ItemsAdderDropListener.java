package io.github.pigaut.rpg.hook.itemsadder;

import dev.lone.itemsadder.api.*;
import io.github.pigaut.rpg.api.event.generator.*;
import io.github.pigaut.rpg.api.event.generator.*;
import org.bukkit.event.*;
import org.bukkit.inventory.*;

public class ItemsAdderDropListener implements Listener {

    @EventHandler
    public void onGeneratorMine(GeneratorMineEvent event) {
        CustomBlock customBlock = CustomBlock.byAlreadyPlaced(event.getBlockMined());
        if (customBlock != null) {
            ItemStack tool = event.getPlayer().getInventory().getItemInMainHand();
            event.setItemDrops(customBlock.getLoot(tool, true));
        }
    }

}
