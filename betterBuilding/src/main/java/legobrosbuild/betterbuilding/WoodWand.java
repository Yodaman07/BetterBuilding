package legobrosbuild.betterbuilding;


import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TO ADD: Locking wood types (check)

//TO ADD: GUI to sort wood order

//TO ADD: Locked status on actionbar (check)

public class WoodWand extends Item {

    public WoodWand(Settings settings) {
        super(settings);
    }

    boolean status = false;

    public static HashMap <UUID, Boolean> lockedState = new HashMap<>();
    public static HashMap <UUID, Integer> nextPlank = new HashMap<>();  // Per-player.
    
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {

        if (world.isClient())
            return super.use(world, playerEntity, hand);

        HitResult hit = playerEntity.raycast(5, 0, false);

        List<Block> logList = List.of(Blocks.CRIMSON_STEM, Blocks.WARPED_STEM, Blocks.DARK_OAK_LOG, Blocks.ACACIA_LOG,
                Blocks.JUNGLE_LOG, Blocks.OAK_LOG, Blocks.BIRCH_LOG, Blocks.SPRUCE_LOG);

        List<Block> plankList = List.of(Blocks.CRIMSON_PLANKS, Blocks.WARPED_PLANKS, Blocks.DARK_OAK_PLANKS, Blocks.ACACIA_PLANKS,
                Blocks.JUNGLE_PLANKS, Blocks.OAK_PLANKS, Blocks.BIRCH_PLANKS, Blocks.SPRUCE_PLANKS);

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
                    if (!lockedState.containsKey(playerEntity.getUuid())) {
                        // default off
                        lockedState.put(playerEntity.getUuid(), false);
                    }
                    if (!nextPlank.containsKey(playerEntity.getUuid())) {
                        // default off
                        nextPlank.put(playerEntity.getUuid(), 0);
                    }
                    int woodNum = nextPlank.get(playerEntity.getUuid());
                    String selectedBlock = Registry.BLOCK.getId(plankList.get(woodNum)).getPath(); // Registry id of the block (without namespace, e.g. "oak_planks")

                    Pattern pattern = Pattern.compile("(\\w)+_(\\w)+"); // REGEX
                    Matcher matcher = pattern.matcher(selectedBlock); // REGEX match



                    if (matcher.find()) {
                        String result = matcher.group();
                        String sel = "Selected: " + result + " Locked: " + lockedState.get(playerEntity.getUuid()); // Selected block
                        playerEntity.sendMessage(Text.of(sel), true);
                    }

                    world.setBlockState(blockPos, plankList.get(woodNum).getDefaultState()); // Sets block


                    if (!lockedState.get(playerEntity.getUuid())){
                        nextPlank.replace(playerEntity.getUuid(), (woodNum + 1) % 8); // Increments the next block
                    }
                }
                break;
        }

        return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }

}
