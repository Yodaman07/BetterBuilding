package legobrosbuild.betterbuilding;

import legobrosbuild.betterbuilding.client.BetterBuildingClient;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TO ADD: Locking wood types (check)

//TO ADD: GUI to sort wood order

//TO ADD: Locked status on actionbar (check)

public class WoodWand extends Item {

    public WoodWand(Settings settings) {
        super(settings);
    }

    int woodNum = 0;
    boolean status = false;

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {

        if (world.isClient())
            return super.use(world, playerEntity, hand);

        woodNum %= 8;

        // if (woodNum == 8){ The exact same thing
        // woodNum = 0;
        // }

        HitResult hit = playerEntity.raycast(5, 0, false);

        // plankList made obsolete by tag system + registry
        List<Block> logList = List.of(Blocks.CRIMSON_STEM, Blocks.WARPED_STEM, Blocks.DARK_OAK_LOG, Blocks.ACACIA_LOG,
                Blocks.JUNGLE_LOG, Blocks.OAK_LOG, Blocks.BIRCH_LOG, Blocks.SPRUCE_LOG);

        switch (hit.getType()) {
            case MISS:
                break;
            case BLOCK:
                BlockHitResult blockHit = (BlockHitResult) hit;
                BlockPos blockPos = blockHit.getBlockPos();
                BlockState blockState = world.getBlockState(blockPos);
                Block block = blockState.getBlock();

                System.out.println(block);

                // Update: use block tags instead of list. Helps with mod compatibility
                if (blockState.isIn(BlockTags.PLANKS)) { // Makes sure the block is a plank: new, modernized version

                    String selectedBlock = Registry.BLOCK.getId(block).getPath(); // Registry id of the block (without namespace, e.g. "oak_planks")
                    Pattern pattern = Pattern.compile("(\\w)+_(\\w)+"); // REGEX
                    Matcher matcher = pattern.matcher(selectedBlock); // REGEX match

                    if (matcher.find()) {
                        String result = matcher.group();
                        String sel = "Currently Selected: " + result + " Locked: " + status; // Selected block
                        playerEntity.sendMessage(Text.of(sel), true);
                    }

                    world.setBlockState(blockPos, block.getDefaultState()); // Sets block

                    woodNum++;
                }

                break;
        }
        /* Wrong place for this. Try BetterBuilding.onInitialize? This will run every time the player uses the item,
           and will create extra handlers. Also, the player won't be able to lock the item before the player uses it.
        */
//        ServerPlayNetworking.registerGlobalReceiver(BetterBuildingClient.LOCK_WAND_ID, (server, player, handler, buf, responseSender) -> { // This line [17:41:22] [Server thread/ERROR] (Minecraft) Failed to handle packet net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket@271963c2, suppressing error
//            //java.lang.RuntimeException: Cannot load class legobrosbuild.betterbuilding.client.BetterBuildingClient in environment type SERVER
//            // Miles says: store LOCK_WAND_ID in BetterBuilding.java, not BetterBuildingClient.java
//
//            int val = buf.readInt();
//            System.out.println(val);
//           server.execute(() -> {
//            player.sendMessage(new LiteralText("Packet received"), false);
//           });
//        });

        return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }

}
