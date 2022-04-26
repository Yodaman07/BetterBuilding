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
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
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

        Block[] plankList = { Blocks.CRIMSON_PLANKS, Blocks.WARPED_PLANKS, Blocks.DARK_OAK_PLANKS, Blocks.ACACIA_PLANKS,
                Blocks.JUNGLE_PLANKS, Blocks.OAK_PLANKS, Blocks.BIRCH_PLANKS, Blocks.SPRUCE_PLANKS };
        Block[] logList = { Blocks.CRIMSON_STEM, Blocks.WARPED_STEM, Blocks.DARK_OAK_LOG, Blocks.ACACIA_LOG,
                Blocks.JUNGLE_LOG, Blocks.OAK_LOG, Blocks.BIRCH_LOG, Blocks.SPRUCE_LOG };

        switch (hit.getType()) {
            case MISS:
                break;
            case BLOCK:
                BlockHitResult blockHit = (BlockHitResult) hit;
                BlockPos blockPos = blockHit.getBlockPos();
                BlockState blockState = world.getBlockState(blockPos);
                Block block = blockState.getBlock();

                System.out.println(block);

                if (block.toString().contains("planks")) { // Makes sure the block is a plank

                    String selectedBlock = plankList[woodNum].toString(); // Full name of the selected block
                    Pattern pattern = Pattern.compile("(\\w)+_(\\w)+"); // REGEX
                    Matcher matcher = pattern.matcher(selectedBlock); // REGEX match

                    if (matcher.find() == true) {
                        String result = matcher.group();
                        String sel = "Currently Selected: " + result + "    Locked: " + status; // Selected block
                        playerEntity.sendMessage(Text.of(sel), true);
                    }

                    world.setBlockState(blockPos, plankList[woodNum].getDefaultState()); // Sets block

                    woodNum++;
                }

                break;
        }
        ServerPlayNetworking.registerGlobalReceiver(BetterBuildingClient.LOCK_WAND_ID, (server, player, handler, buf, responseSender) -> { // This line [17:41:22] [Server thread/ERROR] (Minecraft) Failed to handle packet net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket@271963c2, suppressing error
            //java.lang.RuntimeException: Cannot load class legobrosbuild.betterbuilding.client.BetterBuildingClient in environment type SERVER

            int val = buf.readInt();
            System.out.println(val);
           server.execute(() -> {
            player.sendMessage(new LiteralText("Packet received"), false);
           });
        });

        return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }

}
