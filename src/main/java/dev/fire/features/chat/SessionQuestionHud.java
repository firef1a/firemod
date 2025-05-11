package dev.fire.features.chat;

import com.google.gson.JsonObject;
import dev.fire.Mod;
import dev.fire.config.Config;
import dev.fire.features.Feature;
import dev.fire.features.FeatureHudObjects;
import dev.fire.render.ARGB;
import dev.fire.render.Alignment;
import dev.fire.render.Scaler;
import dev.fire.render.hudElements.*;
import dev.fire.screens.HudFeatureMoveScreen;
import dev.fire.utils.ChatUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.*;
import net.minecraft.util.Colors;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SessionQuestionHud extends Feature {
    public static ArrayList<SessionEntry> sessionQueue = new ArrayList<>();
    public static Map<String, SupportQuestion> supportQuestions = new HashMap<>();

    private static final String queueHeader = "» Current Queue:";

    private static ColorRectContainer supportFeatureHudObject;
    private static TextList supportQueueHudObject;

    private static boolean isInSupportSession = false;
    private static SessionEntry currentSupportSession;

    public SessionQuestionHud() {
        init("supporthud", "Session & Questions HUD", "Shows session in queue and tracks support questions on your hud.");
        sessionQueue = new ArrayList<>();
        supportQuestions = new HashMap<>();

        Scaler hudPosition = Scaler.fromJsonOrDefault(getFeatureID() + ".supportFeatureHudObject", Config.configJSON, (new Scaler(0.9939236111111112, 0.019444444444444445)));

        supportFeatureHudObject = new ColorRectContainer(hudPosition, 3, new ARGB(0.5, 0x000000), 0, Alignment.RIGHT, Alignment.NONE, true);
        supportQueueHudObject = new TextList(new Scaler(0, 0), 0, Alignment.NONE, Alignment.NONE, true);
        updateSupportQueueHudText();

        supportFeatureHudObject.addSibling(supportQueueHudObject);
        FeatureHudObjects.registerObject(supportFeatureHudObject);
    }

    @Override
    public void saveConfig(JsonObject jsonObject) {
        supportFeatureHudObject.position.saveConfig(getFeatureID() + ".supportFeatureHudObject", jsonObject);
    }

    @Override
    public void renderHUD(DrawContext context, RenderTickCounter tickCounter) {
        updateSupportQueueHudText();
        //Mod.log(supportFeatureHudObject.position.toString());
    }

    private void updateSupportQueueHudText() {
        boolean hudOpen = HudFeatureMoveScreen.isOpen();
        supportFeatureHudObject.setEnabled(hudOpen || !sessionQueue.isEmpty() || !supportQuestions.isEmpty() || isInSupportSession);
        if (Mod.MC.textRenderer == null) return;
        int supportColor = 0x8f8fff;
        int lighterSupportColor = 0xaaaaff;
        int maxWidth = new Scaler(0.3472222222222222, 0.0).getScreenX();
        long currentTime = System.currentTimeMillis();
        String widthString = " ".repeat(maxWidth / Mod.MC.textRenderer.getWidth(" "));
        ArrayList<Text> textList = new ArrayList<>();

        int questionColor = 0x7fff7f;
        int lighterQuestion = 0xaaffaa;
        int lighter2Question = 0xd4ffd4;

        Set<String> keys = supportQuestions.keySet();
        for (String key : keys) {
            SupportQuestion question = supportQuestions.get(key);
            if (currentTime - question.timestamp > 60L * 60L * 1000L) {
                ChatUtils.displayMessage(Text.literal(question.name + "'s question was removed because it timed out."));
                supportQuestions.remove(key);
            }
        }

        if (hudOpen) {
            textList.add(Text.literal("Support Queue: ").withColor(supportColor).append(Text.literal("__").withColor(lighterSupportColor)));
            Text textEntry = Text.empty()
                    .append(Text.literal( "#" + 1 + " ").withColor(Colors.LIGHT_GRAY))
                    .append(Text.literal("Sputt").withColor(0xffd47f))
                    .append(Text.literal(" (1:20:42)").withColor(Colors.LIGHT_GRAY));
            textList.add(textEntry);
            Text reasonText = Text.empty()
                    .append(Text.literal(" ▶ ").withColor(lighterSupportColor))
                    .append(Text.literal("Reason: need help with code not working help pls!!!").withColor(Colors.WHITE));
            List<OrderedText> addText = Mod.MC.textRenderer.wrapLines(reasonText, maxWidth);
            int i = 0;
            for (OrderedText text : addText) {
                Text add = convertOrderedTextToTextWithStyle(text);
                if (i > 0) add = Text.literal("   ").append(add);
                textList.add(add);
                i++;
            }

            textList.add(Text.literal(widthString));

            String waiting = "1 question..";
            textList.add(Text.literal("Support Questions: ").withColor(questionColor).append(Text.literal(waiting).withColor(lighterQuestion)));

            Text questionTitle = Text.empty()
                    .append(Text.literal("Jeremaster").withColor(lighterQuestion))
                    .append(Text.literal(" [Owner]").withColor(Colors.LIGHT_GRAY))
                    .append(Text.literal(" (00:00:00)").withColor(Colors.LIGHT_GRAY));
            textList.add(questionTitle);

            textEntry = Text.empty()
                    .append(Text.literal(" - ").withColor(Colors.LIGHT_GRAY))
                    .append(Text.literal("i ned help starting the loader ;-;").withColor(lighter2Question));

            addText = Mod.MC.textRenderer.wrapLines(textEntry, maxWidth);
            i = 0;
            for (OrderedText text : addText) {
                Text add = convertOrderedTextToTextWithStyle(text);
                if (i > 0) add = Text.literal("   ").append(add);
                textList.add(add);
                i++;
            }

            supportQueueHudObject.setTextList(textList);
            return;
        }

        if (isInSupportSession) {
            textList.add(Text.literal("In Support Session:").withColor(supportColor));
            Text textEntry = Text.empty()
                    .append(Text.literal(currentSupportSession.name).withColor(0xffd47f))
                    .append(Text.literal(" (" + convertTimestampToHMS(currentTime - currentSupportSession.timestamp) + ")").withColor(Colors.LIGHT_GRAY));
            textList.add(textEntry);
            Text reasonText = Text.empty()
                    .append(Text.literal(" ▶ ").withColor(lighterSupportColor))
                    .append(Text.literal("Reason: " + currentSupportSession.reason).withColor(Colors.WHITE));
            List<OrderedText> addText = Mod.MC.textRenderer.wrapLines(reasonText, maxWidth);
            int i = 0;
            for (OrderedText text : addText) {
                Text add = convertOrderedTextToTextWithStyle(text);
                if (i > 0) add = Text.literal("   ").append(add);
                textList.add(add);
                i++;
            }

            supportQueueHudObject.setTextList(textList);
        }



        if (!sessionQueue.isEmpty() && !isInSupportSession) {
            String waiting = sessionQueue.size() +  " waiting...";
            textList.add(Text.literal("Support Queue: ").withColor(supportColor).append(Text.literal(waiting).withColor(lighterSupportColor)));
            int index = 1;
            for (SessionEntry entry : sessionQueue) {
                String name = entry.name;
                String time = convertTimestampToHMS(currentTime - entry.timestamp);
                String reason = entry.reason;

                Text textEntry = Text.empty()
                        .append(Text.literal( "#" + index + " ").withColor(Colors.LIGHT_GRAY))
                        .append(Text.literal(name).withColor(0xffd47f))
                        .append(Text.literal(" (" + time + ")").withColor(Colors.LIGHT_GRAY));
                textList.add(textEntry);
                Text reasonText = Text.empty()
                        .append(Text.literal(" ▶ ").withColor(lighterSupportColor))
                        .append(Text.literal("Reason: " + reason).withColor(Colors.WHITE));

                List<OrderedText> addText = Mod.MC.textRenderer.wrapLines(reasonText, maxWidth);
                int i = 0;
                for (OrderedText text : addText) {
                    Text add = convertOrderedTextToTextWithStyle(text);
                    if (i > 0) add = Text.literal("   ").append(add);
                    textList.add(add);
                    i++;
                }
                index++;
            }
        }


        if (!supportQuestions.isEmpty()) {
            if (!sessionQueue.isEmpty() || isInSupportSession) { textList.add(Text.empty()); }

            String waiting = supportQuestions.size() +  " question" + (supportQuestions.size() == 1 ? "" : "s") +"...";
            textList.add(Text.literal("Support Questions: ").withColor(questionColor).append(Text.literal(waiting).withColor(lighterQuestion)));

            for (String name : supportQuestions.keySet()) {
                SupportQuestion question = supportQuestions.get(name);
                String time = convertTimestampToHMS(currentTime - question.timestamp);
                String message = question.message;

                Text questionTitle = Text.empty()
                        .append(Text.literal(name).withColor(lighterQuestion))
                        .append(Text.literal(" " + question.rank).withColor(Colors.LIGHT_GRAY))
                        .append(Text.literal(" (" + time + ")").withColor(Colors.LIGHT_GRAY));
                textList.add(questionTitle);

                Text textEntry = Text.empty()
                        .append(Text.literal(" - ").withColor(Colors.LIGHT_GRAY))
                        .append(Text.literal(message).withColor(lighter2Question));

                List<OrderedText> addText = Mod.MC.textRenderer.wrapLines(textEntry, maxWidth);
                int i = 0;
                for (OrderedText text : addText) {
                    Text add = convertOrderedTextToTextWithStyle(text);
                    if (i > 0) add = Text.literal("   ").append(add);
                    textList.add(add);
                    i++;
                }
            }
        }
        supportQueueHudObject.setTextList(textList);
    }

    public static Text convertOrderedTextToTextWithStyle(OrderedText orderedText) {
        List<Text> components = new ArrayList<>();
        StringBuilder currentText = new StringBuilder();
        final Style[] currentStyle = {Style.EMPTY};

        orderedText.accept((index, style, codePoint) -> {
            if (!style.equals(currentStyle[0])) {
                if (!currentText.isEmpty()) {
                    components.add(Text.literal(currentText.toString()).setStyle(currentStyle[0]));
                    currentText.setLength(0);
                }
                currentStyle[0] = style;
            }
            currentText.appendCodePoint(codePoint);
            return true;
        });

        if (!currentText.isEmpty()) {
            components.add(Text.literal(currentText.toString()).setStyle(currentStyle[0]));
        }

        return Texts.join(components, Text.empty());
    }

    public void removeSupportQueue(String name) {
        ArrayList<SessionEntry> queue = sessionQueue;
        for (SessionEntry entry : queue) {
            if (entry.name.equals(name)) {
                sessionQueue.remove(entry);
                break;
            }
        }
    }

    public SessionEntry getSupportQueue(String name) {
        ArrayList<SessionEntry> queue = sessionQueue;
        for (SessionEntry entry : queue) {
            if (entry.name.equals(name)) {
                return entry;
            }
        }
        return null;
    }

    public static String convertTimestampToHMS(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }

    @Override
    public Text modifyChatMessage(Text base, Text modified) {
        String text = base.getString();
        Matcher matcher = Pattern.compile("^\\[SUPPORT] (.*) joined the support queue\\. ▶ Reason: (.*)", Pattern.CASE_INSENSITIVE).matcher(text);
        if (matcher.find()) {
            return Text.empty().append(modified).append(Text.literal(" [ACCEPT]").withColor(0xffdc7a))
                    .styled((style -> style
                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/support accept " + matcher.group(1)))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("/support accept " + matcher.group(1)).withColor(0xffdc7a)))
                    ));
        }
        return modified;
    }

    public static void resetCurrentSupport() {
        isInSupportSession = false;
        currentSupportSession = null;
    }

    @Override
    public void onChatMessage(Text message, CallbackInfo ci) {
        String text = message.getString();
        String playerName = Mod.getPlayerName();
        Matcher matcher;

        // queue command response
        if (text.equals(queueHeader)) { sessionQueue.clear(); }

        matcher = Pattern.compile("^#\\d* (.{3,16}) ▶ (\\d*):(\\d*):(\\d*)", Pattern.CASE_INSENSITIVE).matcher(text);
        if (matcher.find()) {
            sessionQueue.add(new SessionEntry(matcher.group(1), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)) ,Integer.parseInt(matcher.group(4))));
        }

        matcher = Pattern.compile("^ {2}▶ Reason: (.*)", Pattern.CASE_INSENSITIVE).matcher(text);
        if (matcher.find()) { sessionQueue.getLast().setReason(matcher.group(1)); }


        // queue missing kill event
        matcher = Pattern.compile("^\\[SUPPORT] (.{3,16}) joined the support queue\\. ▶ Reason: (.*)", Pattern.CASE_INSENSITIVE).matcher(text);
        if (matcher.find()) { sessionQueue.add(new SessionEntry(matcher.group(1), matcher.group(2), System.currentTimeMillis())); }

        matcher = Pattern.compile("^\\[SUPPORT] (.{3,16}) left the support queue\\.", Pattern.CASE_INSENSITIVE).matcher(text);
        if (matcher.find()) { removeSupportQueue(matcher.group(1)); }

        matcher = Pattern.compile("^\\[SUPPORT] (.{3,16}) entered a session with (.{3,16})\\..*", Pattern.CASE_INSENSITIVE).matcher(text);
        if (matcher.find()) {
            String supporteeName = matcher.group(2);
            //Mod.log(matcher.group(1) + " " + playerName);
            if (matcher.group(1).equals(playerName)) {
                isInSupportSession = true;
                currentSupportSession = new SessionEntry(supporteeName, getSupportQueue(supporteeName).reason, System.currentTimeMillis());
            }

            removeSupportQueue(supporteeName);
        }

        matcher = Pattern.compile("^\\[SUPPORT] (.{3,16}) terminated a session with (.{3,16})\\. ▶ .*", Pattern.CASE_INSENSITIVE).matcher(text);
        if (matcher.find() && matcher.group(1).equals(playerName)) {resetCurrentSupport();}

        matcher = Pattern.compile("^\\[SUPPORT] (.{3,16}) finished a session with (.{3,16})\\. ▶ .*", Pattern.CASE_INSENSITIVE).matcher(text);
        if (matcher.find() && matcher.group(1).equals(playerName)) {resetCurrentSupport();}

        // questions unfinished
        matcher = Pattern.compile("^» Support Question: \\(Click to answer\\)\\nAsked by (.{3,16}) (.{3,16})\\n(.*)", Pattern.CASE_INSENSITIVE).matcher(text);
        if (matcher.find()) {
            supportQuestions.put(matcher.group(1), new SupportQuestion(matcher.group(1), matcher.group(2), matcher.group(3), System.currentTimeMillis()));
        }

        matcher = Pattern.compile("^ {39}\\n» (.{3,16}) has answered (.{3,16})'(?:s|) question:\\n\\n.*\\n {39}", Pattern.CASE_INSENSITIVE).matcher(text);
        if (matcher.find()) { supportQuestions.remove(matcher.group(2)); }
        //                                       \n» CamTMH has answered N_Enders' question:\n\nNo.\n
    }
}
