package legobrosbuild.betterbuilding.client;



import legobrosbuild.betterbuilding.BetterBuilding;
import legobrosbuild.betterbuilding.hud.BlockCyclingHudPos;
import legobrosbuild.betterbuilding.WoodWand;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static legobrosbuild.betterbuilding.WoodWand.useDiagonals;


public class BBSettingsScreen extends GameOptionsScreen {
    public static ArrayList<String> matchList = BetterBuildingClient.loadSettings();

    public static BlockCyclingHudPos pos = BlockCyclingHudPos.valueOf(matchList.get(4));

    int next = pos.ordinal();

    public BBSettingsScreen(Screen parent, GameOptions options) {
        super(parent, options, new TranslatableText("options.BetterBuilding"));
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        //Code for the BetterBuilding settings screen goes here

        super.renderBackground(matrices);
        BBSettingsScreen.drawCenteredText(new MatrixStack(), super.textRenderer, "Use Diagonals",this.width/2-80, this.height/6, 0xffffff);
        BBSettingsScreen.drawCenteredText(new MatrixStack(), super.textRenderer, "Hud Position",this.width/2+80, this.height/6, 0xffffff);


        super.addDrawableChild(CyclingButtonWidget.onOffBuilder(new LiteralText(""), new LiteralText("")).build(this.width / 2 - 155, this.height / 6 - 12 + 24, 150, 20, new LiteralText("ERROR"), (button, value) -> {
            MinecraftClient client = MinecraftClient.getInstance();

            useDiagonals =! useDiagonals;

            PacketByteBuf buf = PacketByteBufs.create(); //Makes the packet
            buf.writeBoolean(useDiagonals);


            if(client.isInSingleplayer() || client.getCurrentServerEntry() != null || client.isConnectedToRealms()) {
                ClientPlayNetworking.send(BetterBuilding.USE_DIAGONALS_ID, buf); //Sends the packet
            }
            super.clearChildren();

        })).setMessage(new LiteralText(useDiagonals ? "True" : "False").formatted(useDiagonals? Formatting.GREEN: Formatting.RED));

        BlockHudPos();

        super.addDrawableChild(new ButtonWidget(this.width /2 - 100,this.height / 6 - 12 + 54, 200, 20, ScreenTexts.DONE, button -> this.client.setScreen(this.parent)));

//        super.renderBackgroundTexture(10); //Background Toggle
        super.render(matrices, mouseX, mouseY, delta);

    }



    public void BlockHudPos(){
        List<BlockCyclingHudPos> posList = Arrays.stream(BlockCyclingHudPos.values()).toList();
        pos = posList.get(next);


        super.addDrawableChild(new ButtonWidget(this.width / 2 + 5, this.height / 6 - 12 + 24, 150, 20, new LiteralText(pos.name()), button -> {

            BlockCyclingHudPos result = pos.cycle(pos.ordinal());
            next = result.ordinal();

            BetterBuildingClient.saveSettings(WoodWand.useDiagonals, BetterBuildingClient.locked, BetterBuildingClient.currentWoodId, Identifier.tryParse(matchList.get(3)), result);

            BetterBuilding.optW = result.getW();
            BetterBuilding.optH = result.getH();

            super.clearChildren();


        }));
    }

}
