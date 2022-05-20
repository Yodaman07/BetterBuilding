package legobrosbuild.betterbuilding;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.datafixer.fix.StriderGravityFix;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import java.util.HashMap;
import java.util.UUID;


import static net.minecraft.server.command.CommandManager.literal;

public class BetterBuilding implements ModInitializer {
    public static final WoodWand WOOD_WAND = new WoodWand(new FabricItemSettings().group(ItemGroup.MISC));

    public static final Identifier LOCK_WAND_ID = new Identifier("betterbuilding", "lockwand");
    public static final Identifier SET_WOOD_ID = new Identifier("betterbuilding", "setwood");
    public static final Identifier GET_WOOD_ID = new Identifier("betterbuilding", "getwood");
    public static final Identifier USE_DIAGONALS_ID = new Identifier("betterbuilding", "usediagonals");

    public static String test = "test text";
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

        ServerPlayNetworking.registerGlobalReceiver(USE_DIAGONALS_ID, (server, player, handler, buf, responseSender) -> { //Receives the packet from "BBSettingsScreen.java"
            boolean useDiagonalsBool = buf.readBoolean();

            if (WoodWand.useDiagonalsHash.containsKey(player.getUuid())){
                WoodWand.useDiagonalsHash.replace(player.getUuid(),useDiagonalsBool);
            }
            else{
                WoodWand.useDiagonalsHash.put(player.getUuid(), useDiagonalsBool);
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(SET_WOOD_ID, (server, player, handler, buf, responseSender) -> { // Receives the packet on join from BetterBuildingClient
            int data = buf.readInt();
            if (WoodWand.woodNum.containsKey(player.getUuid())) {
                WoodWand.woodNum.replace(player.getUuid(), data);
            } else {
                WoodWand.woodNum.put(player.getUuid(), data);
            }
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) ->  { //Registers the command
            dispatcher.register(literal("wand") //Base Level Command
                    .then(literal("bind") //Sub Command
                        .executes(context -> {
                            final ServerCommandSource source = context.getSource();

                            ItemStack stackInHand = source.getPlayer().getStackInHand(source.getPlayer().getActiveHand());
                            Identifier bound = Registry.ITEM.getId(stackInHand.getItem());

                            System.out.println("Saving " + bound);

                            DetectBoundWand.save(bound); //Somehow run the function.

                            source.getPlayer().sendMessage(new LiteralText("Wand Bound to " + stackInHand), false);

                            return 1;
                  })
               )
            );
        });



    }
}
