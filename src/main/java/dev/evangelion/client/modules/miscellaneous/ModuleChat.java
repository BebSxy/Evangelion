package dev.evangelion.client.modules.miscellaneous;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.evangelion.Evangelion;
import net.minecraftforge.client.event.ClientChatEvent;
import java.util.HashMap;
import dev.evangelion.client.values.impl.ValueString;
import dev.evangelion.client.values.impl.ValueEnum;
import dev.evangelion.client.values.impl.ValueCategory;
import dev.evangelion.client.values.impl.ValueBoolean;
import java.util.Map;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "Chat", description = "Modifiers for the in game chat.", category = Module.Category.MISCELLANEOUS)
public class ModuleChat extends Module
{
    public static ModuleChat INSTANCE;
    Map<String, String> dictionary;
    public ValueBoolean noBackground;
    ValueBoolean greenText;
    ValueCategory suffixCategory;
    ValueBoolean suffix;
    ValueEnum suffixMode;
    ValueString customSuffix;
    private String eva;
    private String japanese;
    private String custom;
    
    public ModuleChat() {
        this.dictionary = new HashMap<String, String>();
        this.noBackground = new ValueBoolean("NoBackground", "No Background", "Removes the background of the chat.", true);
        this.greenText = new ValueBoolean("GreenText", "GreenText", "Makes all your messages green by adding '>' at the beggining.", false);
        this.suffixCategory = new ValueCategory("Suffix", "Suffix category.");
        this.suffix = new ValueBoolean("ChatSuffix", "Suffix", "Add a suffix after every message you send.", this.suffixCategory, false);
        this.suffixMode = new ValueEnum("SuffixMode", "Mode", "Mode for the chat suffix.", this.suffixCategory, SuffixModes.Eva);
        this.customSuffix = new ValueString("CustomSuffix", "Custom Suffix", "The custom suffix value.", this.suffixCategory, "Onigiri");
        this.eva = " \u23d0 \u1d07\u1d20\u1d00\u0274\u0262\u1d07\u029f\u026a\u1d0f\u0274";
        this.japanese = " \u23d0 \u30a8\u30f4\u30a1";
        this.dictionary.put("a", "\u1d00");
        this.dictionary.put("b", "\u0299");
        this.dictionary.put("c", "\u1d04");
        this.dictionary.put("d", "\u1d05");
        this.dictionary.put("e", "\u1d07");
        this.dictionary.put("f", "\ua730");
        this.dictionary.put("g", "\u0262");
        this.dictionary.put("h", "\u029c");
        this.dictionary.put("i", "\u026a");
        this.dictionary.put("j", "\u1d0a");
        this.dictionary.put("k", "\u1d0b");
        this.dictionary.put("l", "\u029f");
        this.dictionary.put("m", "\u1d0d");
        this.dictionary.put("n", "\u0274");
        this.dictionary.put("o", "\u1d0f");
        this.dictionary.put("p", "\u1d18");
        this.dictionary.put("q", "Q");
        this.dictionary.put("r", "\u0280");
        this.dictionary.put("s", "\ua731");
        this.dictionary.put("t", "\u1d1b");
        this.dictionary.put("u", "\u1d1c");
        this.dictionary.put("v", "\u1d20");
        this.dictionary.put("w", "\u1d21");
        this.dictionary.put("x", "x");
        this.dictionary.put("y", "\u028f");
        this.dictionary.put("z", "\u1d22");
        ModuleChat.INSTANCE = this;
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        this.custom = " \u23d0 " + this.convertToUnicode(this.customSuffix.getValue());
    }
    
    public String convertToUnicode(final String string) {
        String finalString = "";
        for (int i = 0; i < string.length(); ++i) {
            final String customMsg = string.toLowerCase();
            if (this.dictionary.containsKey(customMsg.charAt(i) + "")) {
                finalString += this.dictionary.get(customMsg.charAt(i) + "");
            }
            else {
                finalString = finalString + customMsg.charAt(i) + "";
            }
        }
        return finalString;
    }
    
    @SubscribeEvent
    public void onChat(final ClientChatEvent event) {
        if (this.suffix.getValue()) {
            if (event.getMessage().startsWith(".")) {
                return;
            }
            if (event.getMessage().startsWith(",")) {
                return;
            }
            if (event.getMessage().startsWith("/")) {
                return;
            }
            if (event.getMessage().startsWith("*")) {
                return;
            }
            if (event.getMessage().startsWith(Evangelion.COMMAND_MANAGER.getPrefix())) {
                return;
            }
            switch ((SuffixModes)this.suffixMode.getValue()) {
                case Eva: {
                    event.setMessage(event.getMessage() + this.eva);
                    break;
                }
                case Japanese: {
                    event.setMessage(event.getMessage() + this.japanese);
                    break;
                }
                case Custom: {
                    event.setMessage(event.getMessage() + this.custom);
                    break;
                }
            }
        }
        if (this.greenText.getValue()) {
            final String green = "> ";
            if (event.getMessage().startsWith(".")) {
                return;
            }
            if (event.getMessage().startsWith(",")) {
                return;
            }
            if (event.getMessage().startsWith("/")) {
                return;
            }
            if (event.getMessage().startsWith("*")) {
                return;
            }
            if (event.getMessage().startsWith(Evangelion.COMMAND_MANAGER.getPrefix())) {
                return;
            }
            event.setMessage(green + event.getMessage());
        }
    }
    
    public enum SuffixModes
    {
        Eva, 
        Japanese, 
        Custom;
    }
}
