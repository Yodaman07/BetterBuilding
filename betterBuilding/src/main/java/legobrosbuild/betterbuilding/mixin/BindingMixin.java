package legobrosbuild.betterbuilding.mixin;




import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import java.util.HashMap;


@Mixin(Item.class) //Somehow implement the WoodWand class into the Item class for that specific item
public class BindingMixin {


//    @Inject(at = @At("HEAD"), method = " ")
    private void init(CallbackInfo cir) {

    }
}