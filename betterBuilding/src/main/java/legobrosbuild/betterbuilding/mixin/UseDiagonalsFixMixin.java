package legobrosbuild.betterbuilding.mixin;

import com.mojang.authlib.GameProfile;
import legobrosbuild.betterbuilding.BetterBuilding;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static legobrosbuild.betterbuilding.WoodWand.useDiagonals;

//WorldSaveHandler? For the future
@Mixin(ClientPlayerEntity.class)
public abstract class UseDiagonalsFixMixin extends AbstractClientPlayerEntity {


    public UseDiagonalsFixMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(at = @At("TAIL"), method = "init")
    private void serverCheck(CallbackInfo ci) {
        System.out.println("In World"); //When you join a world. Server or singleplayer


        PacketByteBuf buf = PacketByteBufs.create(); //Makes the packet
        buf.writeBoolean(useDiagonals);
        ClientPlayNetworking.send(BetterBuilding.USE_DIAGONALS_ID, buf);


        
    }
}
