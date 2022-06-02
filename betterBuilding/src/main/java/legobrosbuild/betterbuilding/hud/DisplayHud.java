package legobrosbuild.betterbuilding;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static legobrosbuild.betterbuilding.BetterBuilding.*;
import static legobrosbuild.betterbuilding.client.BetterBuildingClient.currentWoodId;

public class DisplayHud {

    public static void renderHud(){
        optW = BBSettingsScreen.pos.getW();
        optH = BBSettingsScreen.pos.getH();

        HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
            int i = 30;
            MinecraftClient client = MinecraftClient.getInstance();
            int h = client.getWindow().getHeight();
            int w = client.getWindow().getWidth();
             //Adapt to chat hud setting
            int blockCount = 0;

            WoodWand.woodNum.putIfAbsent(client.player.getUuid(), currentWoodId);

            for (Block block: WoodWand.plankList) {

                if (WoodWand.plankList.get(currentWoodId) == block && blockCount <= 7){
                    RenderSystem.setShaderTexture(0, new Identifier("betterbuilding", "textures/gui/highlight.png"));
                    DrawableHelper.drawTexture(matrices, w/4-(optW), h/2-(i+optH), 0, 0, 32, 32, 32, 32);
                }

                Pattern pattern = Pattern.compile(":(.*)_");
                Matcher matcher = pattern.matcher(Registry.BLOCK.getId(block).toString());
                if (matcher.find() && blockCount <= 7){
                    RenderSystem.setShaderTexture(0, new Identifier("betterbuilding", "textures/gui/" + matcher.group(1) + ".png"));

                    DrawableHelper.drawTexture(matrices, w/4-(optW), h/2-(i+optH), 0, 0, 32, 32, 32, 32);
                    i+=26;
                    blockCount++;
                    }

                }

        });
    }
}
