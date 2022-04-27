package legobrosbuild.betterbuilding;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BetterBuilding implements ModInitializer {
    public static final Identifier SET_WOOD_TYPE_ID = new Identifier("betterbuilding", "wood_wand");
    public static final WoodWand WOOD_WAND = new WoodWand(new FabricItemSettings().group(ItemGroup.MISC));
    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier("betterbuilding", "wood_wand"), WOOD_WAND);

    }
}
