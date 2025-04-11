package dev.fire.features.chat;

import dev.fire.features.Feature;
import net.minecraft.text.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SupportAccept extends Feature {
    public SupportAccept() {
        init("supportaccept", "Support Accept");
    }

    @Override
    public Text modifyChatMessage(Text base, Text modified) {
        String text = base.getString();
        Matcher matcher = Pattern.compile("w3schools", Pattern.CASE_INSENSITIVE).matcher(text);
        return modified;
    }
}
