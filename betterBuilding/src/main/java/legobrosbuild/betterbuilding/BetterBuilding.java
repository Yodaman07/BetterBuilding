package legobrosbuild.betterbuilding;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemGroup;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BetterBuilding implements ModInitializer {
    public static final Identifier SET_WOOD_TYPE_ID = new Identifier("betterbuilding", "wood_wand");
    public static final WoodWand WOOD_WAND = new WoodWand(new FabricItemSettings().group(ItemGroup.MISC));

    public static final Identifier LOCK_WAND_ID = new Identifier("betterbuilding", "lockwand");
    public static final Identifier SET_PLANK_ID = new Identifier("betterbuilding", "setplank");
    public static final Identifier GET_CURRENT_PLANK = new Identifier("betterbuilding", "getplank");
    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier("betterbuilding", "wood_wand"), WOOD_WAND);

        ServerPlayNetworking.registerGlobalReceiver(LOCK_WAND_ID, (server, player, handler, buf, responseSender) -> {

            boolean lockedState = buf.readBoolean();
            if (WoodWand.lockedState.containsKey(player.getUuid())){
                WoodWand.lockedState.replace(player.getUuid(),lockedState); //Updates HashMap
            }
            else {
                WoodWand.lockedState.put(player.getUuid(), lockedState); //Only used first time the button is pressed
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(SET_PLANK_ID, (server, player, handler, buf, responseSender) -> {

            int plank = buf.readInt();
            if (WoodWand.nextPlank.containsKey(player.getUuid())){
                WoodWand.nextPlank.replace(player.getUuid(), plank); // Update if for some reason the player is cached
            }
            else {
                WoodWand.nextPlank.put(player.getUuid(), plank); // Initialize with value
            }
        });

    }
}
