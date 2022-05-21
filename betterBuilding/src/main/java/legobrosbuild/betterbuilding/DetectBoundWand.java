package legobrosbuild.betterbuilding;


import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;
import java.util.UUID;


public class DetectBoundWand{
    public static boolean check(UUID uuid, ItemStack stackInHand) {

        return Registry.ITEM.get(BetterBuilding.boundWand.get(uuid)) == stackInHand.getItem();  // false if no bound wand (null != Identifier)
    }
}
