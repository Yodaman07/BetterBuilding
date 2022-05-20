package legobrosbuild.betterbuilding;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import javax.swing.text.AbstractDocument;
import java.util.HashMap;
import java.util.UUID;


public class DetectBoundWand{
    public static HashMap<UUID, Identifier> boundWand = new HashMap<>();

    public static boolean init(){
        final WoodWand woodWand = BetterBuilding.WOOD_WAND;

        MinecraftClient client = MinecraftClient.getInstance();

        Identifier boundItemId = boundWand.get(client.player.getUuid()); //Gets held item when cmd is run

        System.out.println(boundItemId + " Bound");

        Item heldItem = client.player.getStackInHand(client.player.getActiveHand()).getItem(); //Gets currently active item
        Identifier heldItemId = Registry.ITEM.getId(heldItem);
        System.out.println(heldItemId + " Held");


        if (boundItemId == heldItemId) {
            return true;
        }
        else {
            return false;
        }

    }

    public static void save(Identifier boundItem){
        MinecraftClient client = MinecraftClient.getInstance();

        if (boundWand.containsKey(client.player.getUuid())) {
            boundWand.replace(client.player.getUuid(), boundItem);
        }
        else{
            boundWand.put(client.player.getUuid(), boundItem);
            System.out.println("Saved " + boundWand.values());
        }

    }

    public Integer getWorld(){

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.isInSingleplayer()){ return 1; }
        else if (client.getCurrentServerEntry() != null) {return 2; }
        else {return -1; }
    }

}
