package legobrosbuild.betterbuilding.client;

import legobrosbuild.betterbuilding.BetterBuilding;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.impl.item.ItemExtensions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.StickyKeyBinding;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class BetterBuildingClient implements ClientModInitializer {


    public boolean locked = false;
    public int nextPlank = 0;

    public void setLocked(boolean state, MinecraftClient client) {
        locked = state;
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(locked);
        ClientPlayNetworking.send(BetterBuilding.LOCK_WAND_ID, buf);
        assert client.player != null;
        // condition ? (result if true) : (result if false)
        // Look up "ternary operator"
        client.player.sendMessage(new LiteralText(locked ? "Locked" : "Unlocked").formatted(locked ? Formatting.GREEN : Formatting.RED), true);
    }
    public void setNextPlank(int state, MinecraftClient client) {
        nextPlank = state;
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(nextPlank);
        ClientPlayNetworking.send(BetterBuilding.SET_PLANK_ID, buf);
    }

    public void restoreState(MinecraftClient client) {
        setLocked(locked, client);
        setNextPlank(nextPlank, client);
    }

    @Override
    public void onInitializeClient() {

        KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new StickyKeyBinding(
                "legobrosbuild.betterbuilding.lock",
                GLFW.GLFW_KEY_M,
                "category.legorbrosbuild.locking",
                () -> true));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                setLocked(!locked, client);
            }
        });
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> restoreState(client));

        // send plank id from server to client
        ClientPlayNetworking.registerGlobalReceiver(BetterBuilding.GET_CURRENT_PLANK, (client, handler, buf, responseSender) -> nextPlank = buf.readInt());
    }

}
