package legobrosbuild.betterbuilding.mixin;


import legobrosbuild.betterbuilding.BetterBuilding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.AddServerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.realms.util.JsonUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.logging.Logger;


@Mixin(OptionsScreen.class)
public abstract class OptionScreenMixin extends Screen {

    public Screen bbSettings = new Screen(new TranslatableText("bbSettings.title")) {
        @Override
        public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            //Code for the BetterBuilding settings screen goes here
            super.addDrawableChild(new ButtonWidget(this.width / 2 - 155, this.height / 6 -12 + 24, 150, 20, new TranslatableText("bbSettings.toggleUseDiagonals"), button1 -> System.out.println("Toggled")));
            super.addDrawableChild(new ButtonWidget(this.width / 2 + 5, this.height / 6 -12 + 24, 150, 20, new TranslatableText("bbSettings.otherOption"), button1 -> System.out.println("Toggled")));
            super.renderBackground(matrices);
            super.render(matrices, mouseX, mouseY, delta);
        }

        @Override
        protected <T extends Element & Drawable & Selectable> T addDrawableChild(T drawableElement) {
            return super.addDrawableChild(drawableElement);
        }

    };

    public Mouse mouse = MinecraftClient.getInstance().mouse;
    public MatrixStack matrixStack = new MatrixStack();

    protected OptionScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "init()V")
    private void init(CallbackInfo info) {
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 155, this.height / 6 + 144 - 6, 150, 20, new TranslatableText("options.BetterBuilding"), button ->{
            assert this.client != null;
            this.client.setScreen(bbSettings);
            render(matrixStack, (int) mouse.getX(), (int) mouse.getY(), 10);
            }));


    }
}
