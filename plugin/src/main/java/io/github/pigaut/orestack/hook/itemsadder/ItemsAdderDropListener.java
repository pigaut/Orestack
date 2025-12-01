package io.github.pigaut.orestack.hook.itemsadder;

import dev.lone.itemsadder.api.*;
import io.github.pigaut.orestack.api.event.*;
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
