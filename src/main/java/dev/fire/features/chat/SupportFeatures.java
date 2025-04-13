package dev.fire.features.chat;

import dev.fire.features.Feature;
import dev.fire.features.FeatureHudObjects;
import dev.fire.render.ARGB;
import dev.fire.render.Alignment;
import dev.fire.render.Scaler;
import dev.fire.render.impl.ColorRectFeature;
import dev.fire.render.impl.ColorRectFeatureContainer;
import dev.fire.render.impl.RenderObject;
import dev.fire.render.impl.TextList;
import dev.fire.utils.ColorUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SupportFeatures extends Feature {
    public static Map<String, String> sessionQueue = new HashMap<>();
    public static Map<String, SupportQuestion> supportQuestions = new HashMap<>();

    public static ArrayList<SessionEntry> queueCommandEntries = new ArrayList<>();
    private static final String queueHeader = "» Current Queue:";

    private static ColorRectFeatureContainer supportFeatureHudObject;
    private static TextList supportQueueHudObject;

    public SupportFeatures() {
        init("supportaccept", "Support Accept");
        sessionQueue = new HashMap<>();
        supportQuestions = new HashMap<>();

        supportFeatureHudObject = new ColorRectFeatureContainer(new Scaler(0.3, 0.1), 2, new ARGB(0.5, 0x000000), 0, Alignment.NONE, this);
        supportQueueHudObject = new TextList(new Scaler(0, 0), 0, Alignment.NONE, true);
        updateSupportQueueHudText();

        supportFeatureHudObject.addSibling(supportQueueHudObject);
        FeatureHudObjects.registerObject(supportFeatureHudObject);
    }

    private void updateSupportQueueHudText() {
        ArrayList<Text> textList = new ArrayList<>();

        textList.add(Text.literal("Support Queue: "))


        supportQueueHudObject.setTextList(textList);
    }

    @Override
    public Text modifyChatMessage(Text base, Text modified) {
        String text = base.getString();
        Matcher matcher = Pattern.compile("^\\[SUPPORT] (.*) joined the support queue\\. ▶ Reason: (.*)", Pattern.CASE_INSENSITIVE).matcher(text);
        if (matcher.find()) {
            return Text.empty().append(modified).append(Text.literal(" ≔ [ACCEPT]").withColor(0xffdc7a))
                    .styled((style -> style
                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/support accept " + matcher.group(1)))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("/support accept " + matcher.group(1)).withColor(0xffdc7a)))
                    ));
        }
        return modified;
    }

    @Override
    public void onChatMessage(Text message, CallbackInfo ci) {
        String text = message.getString();
        Matcher matcher;

        // queue command response
        if (text.equals(queueHeader)) { queueCommandEntries.clear(); }

        matcher = Pattern.compile("^#\\d* (.{3,16}) ▶ (\\d*):(\\d*):(\\d*)", Pattern.CASE_INSENSITIVE).matcher(text);
        if (matcher.find()) {
            queueCommandEntries.add(new SessionEntry(matcher.group(1), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)) ,Integer.parseInt(matcher.group(4))));
        }

        matcher = Pattern.compile("^ {2}▶ Reason: (.*)", Pattern.CASE_INSENSITIVE).matcher(text);
        if (matcher.find()) { queueCommandEntries.getLast().setReason(matcher.group(1)); }


        // queue missing kill event
        matcher = Pattern.compile("^\\[SUPPORT] (.{3,16}) joined the support queue\\. ▶ Reason: (.*)", Pattern.CASE_INSENSITIVE).matcher(text);
        if (matcher.find()) { sessionQueue.put(matcher.group(1), matcher.group(2)); }

        matcher = Pattern.compile("^\\[SUPPORT] (.{3,16}) left the support queue\\.", Pattern.CASE_INSENSITIVE).matcher(text);
        if (matcher.find()) { sessionQueue.remove(matcher.group(1)); }

        matcher = Pattern.compile("^\\[SUPPORT] (.{3,16}) entered a session with (.{3,16})\\..*", Pattern.CASE_INSENSITIVE).matcher(text);
        if (matcher.find()) { sessionQueue.remove(matcher.group(2)); }

        matcher = Pattern.compile("^\\[SUPPORT] (.{3,16}) interrupted a session with (.{3,16}) \\(\\d*:\\d*:\\d*\\)", Pattern.CASE_INSENSITIVE).matcher(text);
        if (matcher.find()) { sessionQueue.remove(matcher.group(2)); }

        matcher = Pattern.compile("^\\[SUPPORT] (.{3,16}) completed session with (.{3,16}) \\(\\d*:\\d*:\\d*\\)", Pattern.CASE_INSENSITIVE).matcher(text);
        if (matcher.find()) { sessionQueue.remove(matcher.group(2)); }

        // questions unfinished
        matcher = Pattern.compile("^» Support Question: \\(Click to answer\\)\\\\nAsked by (.{3,16}) (.*)\\\\n(.*)", Pattern.CASE_INSENSITIVE).matcher(text);
        if (matcher.find()) { supportQuestions.put(matcher.group(1), new SupportQuestion(matcher.group(1), matcher.group(2), matcher.group(3), System.currentTimeMillis())); }

        matcher = Pattern.compile("^ {40}\\\\n» (.{3,16}) has answered (.{3,16})'s question:\\\\n\\\\n.*\\\\n {39}\n", Pattern.CASE_INSENSITIVE).matcher(text);
        if (matcher.find()) { supportQuestions.remove(matcher.group(2)); }

        updateSupportQueueHudText();
    }
}
