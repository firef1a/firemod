package dev.fire.features.commands;

import dev.fire.Mod;
import dev.fire.features.Feature;
import dev.fire.helper.CommandQueue;
import dev.fire.helper.CommandQueueHelper;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

public class QueueOnJoin extends Feature {
    public QueueOnJoin() {
        init("queueonjoin", "Queue On Join", "Runs /queue on join");
    }

    public void onChatMessage(Text message, CallbackInfo ci) {
        String msg = message.getString();
        if (msg.equals("◆ Welcome back to DiamondFire! ◆")) {
            CommandQueueHelper.addCommand(new CommandQueue("/support queue"));

            Mod.log("Attemping to run /support queue");
        }
    }
}
