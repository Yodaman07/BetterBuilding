package legobrosbuild.betterbuilding.mixin;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import static legobrosbuild.betterbuilding.client.BetterBuildingClient.bound;

@Mixin(Item.class)
public class BindingTooltipMixin {
    @Inject(at= @At("HEAD"), method = "appendTooltip")
    private void addTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci){

        if (stack.getItem() == Registry.ITEM.get(bound)) {
            tooltip.add(new LiteralText("Bound Wand").formatted(Formatting.GOLD));
        }
    }
}
