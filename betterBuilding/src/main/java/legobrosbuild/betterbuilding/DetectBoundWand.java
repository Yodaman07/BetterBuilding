package legobrosbuild.betterbuilding;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import javax.swing.text.AbstractDocument;
import java.util.HashMap;
import java.util.UUID;


public class DetectBoundWand{


    public static boolean init(){
        ClientPlayNetworking.registerGlobalReceiver(BetterBuilding.BIND_WAND_ID, (client, handler, buf, responseSender) -> {

            Identifier boundItemId = buf.readIdentifier();
            System.out.println(boundItemId + " Bound");

            Item heldItem = client.player.getStackInHand(client.player.getActiveHand()).getItem(); //Gets currently active item
            Identifier heldItemId = Registry.ITEM.getId(heldItem);
            System.out.println(heldItemId + " Held");


            if (boundItemId == heldItemId) {
                System.out.println("Swap");
            }
            else {
                System.out.println("Dont swap");
            }
        });

        return false;
    }


    public Integer getWorld(){

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.isInSingleplayer()){ return 1; }
        else if (client.getCurrentServerEntry() != null) {return 2; }
        else {return -1; }
    }

}
