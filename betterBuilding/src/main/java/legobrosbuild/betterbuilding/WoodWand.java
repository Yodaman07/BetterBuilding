package legobrosbuild.betterbuilding;


import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TO ADD: Locking wood types (check)

//TO ADD: GUI to sort wood order

//TO ADD: Locked status on actionbar (check)

public class WoodWand extends Item {

    public WoodWand(Settings settings) {
        super(settings);
    }

    public int woodNum = 0;
    public static final long MAX_CHECKS = 2048;

    public static HashMap <UUID, Boolean> lockedState = new HashMap<> ();




    int chainSwap(World world, BlockPos target, BlockState result, boolean useDiagonals) {
        BlockState match = world.getBlockState(target);
        ArrayList<BlockPos> used = new ArrayList<>();  // Blocks we've already changed. Don't change them again.
        ArrayList<BlockPos> next = new ArrayList<>();  // Blocks we need to check next time.
        int count = 0;  // Number of blocks *checked*. (Not the number changed.)
        int changes = 0;  // Number of blocks changed.
        next.add(target);  // Initialize the queue with the start block.
        while (next.size() > 0) {
            ArrayList<BlockPos> now = new ArrayList<>(next);  // Blocks we're checking now.
            next.clear();  // clear for later
            ArrayList<BlockPos> possible = new ArrayList<>(); // Blocks that *could* change. (including ones we've already changed)
            for (BlockPos check : now) {
                // Check that it's the same block as the target (the block that you clicked on)
                boolean isCorrect = world.getBlockState(check).equals(match);
                if (isCorrect) {
                    // We're going to change this block
                    changes += 1;
                    world.setBlockState(check, result);
                    // Add all the surrounding blocks to the list of blocks we need to the "possible" ArrayList.
                    // Depending on the "useDiagonals" parameter, we'll add either
                    if (useDiagonals) {
                        // Don't ask how long this took.
                        possible.add(check.add(-1, -1, -1));
                        possible.add(check.add(-1, -1, 0));
                        possible.add(check.add(-1, -1, 1));
                        possible.add(check.add(-1, 0, -1));
                        possible.add(check.add(-1, 0, 0));
                        possible.add(check.add(-1, 0, 1));
                        possible.add(check.add(-1, 1, -1));
                        possible.add(check.add(-1, 1, 0));
                        possible.add(check.add(-1, 1, 1));
                        possible.add(check.add(0, -1, -1));
                        possible.add(check.add(0, -1, 0));
                        possible.add(check.add(0, -1, 1));
                        possible.add(check.add(0, 0, -1));
                        possible.add(check.add(0, 0, 1));
                        possible.add(check.add(0, 1, -1));
                        possible.add(check.add(0, 1, 0));
                        possible.add(check.add(0, 1, 1));
                        possible.add(check.add(1, -1, -1));
                        possible.add(check.add(1, -1, 0));
                        possible.add(check.add(1, -1, 1));
                        possible.add(check.add(1, 0, -1));
                        possible.add(check.add(1, 0, 0));
                        possible.add(check.add(1, 0, 1));
                        possible.add(check.add(1, 1, -1));
                        possible.add(check.add(1, 1, 0));
                        possible.add(check.add(1, 1, 1));
                    } else {
                        possible.add(check.add(1, 0, 0));
                        possible.add(check.add(-1, 0, 0));
                        possible.add(check.add(0, 1, 0));
                        possible.add(check.add(0, -1, 0));
                        possible.add(check.add(0, 0, 1));
                        possible.add(check.add(0, 0, -1));
                    }
                }
                count++;
                if (count > MAX_CHECKS) {      // Limit max check count. (so you don't crash the server)
                    return -1;
                }
            }
            used.addAll(now);  // move all to the "used" list
            possible.forEach(
                    bp -> {if (!used.contains(bp) && !next.contains(bp)) next.add(bp);}  // change: don't add repeats
            );
        }
        return changes;
    }
    
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {

        if (world.isClient())
            return super.use(world, playerEntity, hand);

        woodNum %= 8;


        HitResult hit = playerEntity.raycast(5, 0, false);

        // plankList made obsolete by tag system + registry
        List<Block> logList = List.of(Blocks.CRIMSON_STEM, Blocks.WARPED_STEM, Blocks.DARK_OAK_LOG, Blocks.ACACIA_LOG,
                Blocks.JUNGLE_LOG, Blocks.OAK_LOG, Blocks.BIRCH_LOG, Blocks.SPRUCE_LOG);

        List<Block> plankList = List.of(Blocks.CRIMSON_PLANKS, Blocks.WARPED_PLANKS, Blocks.DARK_OAK_PLANKS, Blocks.ACACIA_PLANKS,
                Blocks.JUNGLE_PLANKS, Blocks.OAK_PLANKS, Blocks.BIRCH_PLANKS, Blocks.SPRUCE_PLANKS, Blocks.CRIMSON_PLANKS);
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

                    if (!lockedState.get(playerEntity.getUuid())){
                        woodNum++;
                    }

                    String selectedBlock = Registry.BLOCK.getId(plankList.get(woodNum)).getPath(); // Registry id of the block (without namespace, e.g. "oak_planks")

                    Pattern pattern = Pattern.compile("(\\w)+_(\\w)+"); // REGEX
                    Matcher matcher = pattern.matcher(selectedBlock); // REGEX match


                    if (matcher.find()) {
                        String result = matcher.group();
                        String sel = "Currently Selected: " + result; // Selected block
                        playerEntity.sendMessage(Text.of(sel), true);
                    }

                    if (playerEntity.isSneaking()) {
                        /*
                        HOW TO IMPROVE PERFORMANCE WITH THIS
                        1. turn off useDiagonals - will increase performance by ~4x
                        2. turn down MAX_CHECKS (will affect the maximum number of blocks modified)
                         */
                        int result = chainSwap(world, blockPos, plankList.get(woodNum).getDefaultState(), true);
                        playerEntity.sendMessage(new LiteralText(result != -1 ? "Changed " + result + " blocks." : "Too many blocks!").formatted(result != -1 ? Formatting.GREEN : Formatting.RED), true);
                    } else {
                        world.setBlockState(blockPos, plankList.get(woodNum).getDefaultState()); // Sets block
                    }
                }
                break;
        }

        return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }

}
