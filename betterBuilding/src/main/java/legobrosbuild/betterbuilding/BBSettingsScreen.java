package legobrosbuild.betterbuilding;


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
import static legobrosbuild.betterbuilding.WoodWand.useDiagonals;


public class BBSettingsScreen extends GameOptionsScreen {


    public BBSettingsScreen(Screen parent, GameOptions options) {
        super(parent, options, new TranslatableText("options.bbSettings.title"));
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        //Code for the BetterBuilding settings screen goes here

        super.renderBackground(matrices);
        BBSettingsScreen.drawCenteredText(new MatrixStack(), super.textRenderer, "UseDiagonals",this.width/2-80, this.height/6, 0xffffff);
        BBSettingsScreen.drawCenteredText(new MatrixStack(), super.textRenderer, "OtherSetting",this.width/2+80, this.height/6, 0xffffff);


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

        super.addDrawableChild(new ButtonWidget(this.width / 2 + 5, this.height / 6 -12 + 24, 150, 20, new TranslatableText("bbSettings.otherOption"), button1 -> System.out.println("Toggled")));

        super.addDrawableChild(new ButtonWidget(this.width /2 - 100,this.height / 6 - 12 + 54, 200, 20, ScreenTexts.DONE, button -> this.client.setScreen(this.parent)));
//        super.renderBackgroundTexture(10); //Background Toggle
        super.render(matrices, mouseX, mouseY, delta);

    }

}
