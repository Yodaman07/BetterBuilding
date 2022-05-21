package legobrosbuild.betterbuilding;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import javax.swing.text.AbstractDocument;
import java.util.HashMap;
import java.util.UUID;


public class DetectBoundWand{
    public static boolean check(UUID uuid, ItemStack stackInHand) {
        return BetterBuilding.boundWand.get(uuid) == Registry.ITEM.getId(stackInHand.getItem());  // false if no bound wand (null != Identifier)
    }
}
