package dev.fire.features.commands;

import dev.fire.Mod;
import dev.fire.features.Feature;
import dev.fire.helper.CommandQueue;
import dev.fire.helper.CommandQueueHelper;
import net.minecraft.client.sound.Sound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.PlaySoundFromEntityS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class CommandHider extends Feature {
    public static ArrayList<ArrayList<String>> singleHiderList;
    public static ArrayList<String> multiHiderList;
    public static ArrayList<String> entitySoundHiderList;
    public CommandHider() {
        init("commandhider", "Automated Command Hider", "Internal Feature, disable if it is hiding things it shouldn't");
        singleHiderList = new ArrayList<>();
        multiHiderList = new ArrayList<>();
        entitySoundHiderList = new ArrayList<>();
    }

    /*
        single means from the list of commands to hide, it will match with the first one it matches with and discard the rest
        multi means it will match all commands in the list

     */
    public static void addSingleHiddenCommand(ArrayList<String> text ) { singleHiderList.add(text); }
    public static void addMultiHiddenCommand(ArrayList<String> text ) { multiHiderList.addAll(text); }

    public static void addHiddenSoundFromEntity(ArrayList<String> sound ) { entitySoundHiderList.addAll(sound); }

    @Override
    public void onChatMessage(Text message, CallbackInfo ci) {
        String text = message.getString();
        ArrayList<ArrayList<String>> removeList;

        removeList = new ArrayList<>();

        for (ArrayList<String> hider : singleHiderList) {
            for (String match : hider) {
                if (Pattern.compile(match, Pattern.CASE_INSENSITIVE).matcher(text).find()) {
                    ci.cancel();
                    removeList.add(hider);

                    break;
                }
            }
        }
        for (ArrayList<String> remove : removeList) {
            singleHiderList.remove(remove);
        }

        ArrayList<Integer> multiRemoveList = new ArrayList<>();

        int i = 0;
        for (String hider : multiHiderList) {
            if (Pattern.compile(hider, Pattern.CASE_INSENSITIVE).matcher(text).find()) {
                ci.cancel();
                multiRemoveList.addFirst(i);
                break;
            }
            i++;
        }
        for (int num : multiRemoveList) {
            multiHiderList.remove(num);
        }
    }

    @Override
    public void handlePacket(Packet<?> packet, CallbackInfo ci) {
        if (packet instanceof PlaySoundFromEntityS2CPacket playSoundFromEntityS2CPacket) {
            String sound = playSoundFromEntityS2CPacket.getSound().getIdAsString();
            if (entitySoundHiderList.contains(sound)) {
                entitySoundHiderList.remove(sound);
                ci.cancel();;
            }
        }
    }
}
