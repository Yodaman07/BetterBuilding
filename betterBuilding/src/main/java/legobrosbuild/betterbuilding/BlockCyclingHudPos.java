package legobrosbuild.betterbuilding;

import net.minecraft.client.MinecraftClient;

import java.util.Arrays;
import java.util.List;


public enum BlockCyclingHudPos {

    BOTTOM_LEFT( (MinecraftClient.getInstance().getWindow().getWidth()/2)-640, 0),
    BOTTOM_RIGHT(-600,0),
    TOP_LEFT(BOTTOM_LEFT.w, (MinecraftClient.getInstance().getWindow().getHeight()/3)+30 ),
    TOP_RIGHT(-600, TOP_LEFT.h);
    private final int w;
    private final int h;
    BlockCyclingHudPos(int w, int h) {
        this.w = w;
        this.h = h;
    }

    public int getW() {return this.w;}

    public int getH() {return this.h;}

    public BlockCyclingHudPos cycle(int current){
        List<BlockCyclingHudPos> posList = Arrays.stream(BlockCyclingHudPos.values()).toList();
        for (BlockCyclingHudPos pos:BlockCyclingHudPos.values()) {
            if (pos.ordinal() == current && current != 3) { //Checking if current pos is the same as the pos in the block cycle
                //Return the index of the next pos
                return posList.get(current + 1);
            }
            else if (current>=3){
                current = 0;
                return posList.get(current);
            }
        }
        return null;
    }
}
