package legobrosbuild.betterbuilding.mixin;




import legobrosbuild.betterbuilding.BetterBuilding;


import legobrosbuild.betterbuilding.WoodWand;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Item.class)
public abstract class BindingMixin {

    private static final WoodWand woodWand = BetterBuilding.WOOD_WAND;


    @Inject(at = @At("HEAD"), method = "use", cancellable = true)
    private void init(World world, PlayerEntity player, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {

//        ServerPlayNetworking.registerGlobalReceiver(BetterBuilding.WAND_BIND_ID, (server, player1, handler, buf, responseSender) -> {
//             Item bound = buf.readItemStack().getItem();
//             System.out.println("Packet Received");
//             Identifier id = Registry.ITEM.getId(bound);
//             Item isBoundItem = player.getStackInHand(hand).getItem();
//             System.out.println(isBoundItem + ";" + id);
//
//              if (id == Registry.ITEM.getId(isBoundItem) && world.isClient()) { //True is logical client false is logical server
//                  boundWand.use(world, player, hand);
//              }
//          });

//or
//              Item bound = BetterBuilding.boundWand.get(player.getUuid());

              Item isBoundItem = player.getStackInHand(hand).getItem();
              Identifier bound = BetterBuilding.boundWand.get(player.getUuid());
              System.out.println(bound);
              if (bound == Registry.ITEM.getId(isBoundItem) && world.isClient()) { //True is logical client false is logical server
                  woodWand.use(world, player, hand);
              }

    }
}

