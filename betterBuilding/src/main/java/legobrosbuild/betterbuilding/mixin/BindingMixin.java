package legobrosbuild.betterbuilding.mixin;


import legobrosbuild.betterbuilding.BetterBuilding;
import legobrosbuild.betterbuilding.DetectBoundWand;
import legobrosbuild.betterbuilding.WoodWand;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;


@Mixin(Item.class)
public abstract class BindingMixin {
    private final WoodWand woodWand = BetterBuilding.WOOD_WAND;

    @Inject(at = @At("HEAD"), method = "use", cancellable = true)
    private void init(World world, PlayerEntity player, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        // server only
        if (world.isClient) return;
        if (DetectBoundWand.check(player.getUuid(), player.getStackInHand(hand))) {
            woodWand.use(world, player, hand);
            cir.setReturnValue(TypedActionResult.success(player.getStackInHand(hand)));  // better
        }
    }


    @Inject(at= @At("HEAD"), method = "appendTooltip")
    private void addTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci){

        if (!BetterBuilding.boundWand.isEmpty()) {
            PlayerEntity player = MinecraftClient.getInstance().player;
            Identifier boundWand = BetterBuilding.boundWand.get(player.getUuid());
            if (stack.getItem() == Registry.ITEM.get(boundWand)) {
                tooltip.add(new LiteralText("Bound Wand").formatted(Formatting.GOLD));
            }
        }
     }
}
