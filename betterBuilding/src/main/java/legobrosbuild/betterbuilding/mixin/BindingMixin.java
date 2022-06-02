package legobrosbuild.betterbuilding.mixin;


import legobrosbuild.betterbuilding.BetterBuilding;
import legobrosbuild.betterbuilding.WoodWand;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;


@Mixin(Item.class)
public abstract class BindingMixin {
    private final WoodWand woodWand = BetterBuilding.WOOD_WAND;

    @Inject(at = @At("HEAD"), method = "use", cancellable = true)
    private void init(World world, PlayerEntity player, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        // server only
        if (world.isClient) return;

        UUID uuid = player.getUuid();
        ItemStack stackInHand = player.getStackInHand(hand);
        if (Registry.ITEM.get(BetterBuilding.boundWand.get(uuid)) == stackInHand.getItem()) {
            woodWand.use(world, player, hand);
            cir.setReturnValue(TypedActionResult.success(player.getStackInHand(hand)));  // better
        }
    }
}
