//package legobrosbuild.betterbuilding.mixin;
//
//import net.minecraft.entity.Entity;
//import net.minecraft.nbt.NbtByte;
//import net.minecraft.nbt.NbtCompound;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//@Mixin(Entity.class)
//public abstract class SaveEntityDataMixin {
//    NbtCompound data;
//
//    @Inject(at = @At("HEAD"), method = "writeNbt")
//    private void init(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
//        nbt.putString("testDataKey", "NBTData");
//    }
//
//}
