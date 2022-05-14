package legobrosbuild.betterbuilding.client;


import legobrosbuild.betterbuilding.BetterBuilding;
import legobrosbuild.betterbuilding.WoodWand;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.StickyKeyBinding;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static legobrosbuild.betterbuilding.BetterBuilding.GET_WOOD_ID;
import static legobrosbuild.betterbuilding.WoodWand.useDiagonals;


@Environment(EnvType.CLIENT)
public class BetterBuildingClient implements ClientModInitializer {


    public boolean locked = false;
    public int currentWoodId = 0;
    void saveSettings(boolean useDiagonals, boolean isLocked, int woodNum, String boundWand){

            try{
                FileWriter config = new FileWriter("config.txt"); //Saved in "/run/config.txt"
                config.write("useDiagonals = " + useDiagonals + "\nisLocked = " + isLocked + "\nwoodNum = " + woodNum + "\nboundWand = " + boundWand);
                config.close();
                System.out.println("Config file successfully closed");

            } catch (IOException e) {
                System.out.println("An error has occurred");
                throw new RuntimeException(e);
            }
    }

    ArrayList<String> loadSettings(){
        try{
            File config = new File("config.txt");
            Scanner scanner = new Scanner(config);
            Pattern settingsPatter = Pattern.compile("= (.*)");
            ArrayList<String> matchList = new ArrayList<>();

            while (scanner.hasNextLine()) {
                Matcher settingsMatch = settingsPatter.matcher(scanner.nextLine());
                if (settingsMatch.find()){
                    matchList.add(settingsMatch.group(1));
                }
                else{
                    System.out.println("There was no match. There may be an error with your config file");
                }
            }
             return matchList;

        } catch (IOException e) {
            System.out.println("An error has occurred");
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onInitializeClient() {

        KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new StickyKeyBinding(
                "legobrosbuild.betterbuilding.lock",
                GLFW.GLFW_KEY_B,
                "category.legorbrosbuild.locking",
                () -> true));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                locked = !locked;  // Flip *before* sending??
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeBoolean(locked);
                ClientPlayNetworking.send(BetterBuilding.LOCK_WAND_ID, buf); //Send a packet to the server containing the locked state
                assert client.player != null;
                // condition ? (result if true) : (result if false)
                // Look up "ternary operator"
                client.player.sendMessage(new LiteralText(locked ? "Locked" : "Unlocked").formatted(locked ? Formatting.GREEN : Formatting.RED), true);
            }
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> { //Sends the packet when you join the world
            ArrayList<String> matchList = loadSettings();

            WoodWand.useDiagonals = Boolean.parseBoolean(matchList.get(0));
            WoodWand.useDiagonalsHash.putIfAbsent(MinecraftClient.getInstance().player.getUuid(), WoodWand.useDiagonals); //Prevents an error as at this stage, the code in BetterBuilding hasnt run putting a value in for an empty hashmap
            locked = Boolean.parseBoolean(matchList.get(1));
            currentWoodId = Integer.parseInt(matchList.get(2));

            System.out.println("Joined World");

            // Send the lock status to the server
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeBoolean(locked);
            ClientPlayNetworking.send(BetterBuilding.LOCK_WAND_ID, buf);

            // Send the useDiagonals value to the server
            buf = PacketByteBufs.create(); //Makes the packet
            buf.writeBoolean(useDiagonals);
            ClientPlayNetworking.send(BetterBuilding.USE_DIAGONALS_ID, buf);

            // Send the wood id to the server
            buf = PacketByteBufs.create();
            buf.writeInt(currentWoodId);
            ClientPlayNetworking.send(BetterBuilding.SET_WOOD_ID, buf);


        });

        ClientPlayNetworking.registerGlobalReceiver(GET_WOOD_ID, (client, handler, buf, responseSender) -> currentWoodId = buf.readInt());  // recv wood id from server

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            saveSettings(WoodWand.useDiagonals, locked, currentWoodId, "NA");
        });

    }
}
