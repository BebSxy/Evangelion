package dev.evangelion;

import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.Display;
import dev.evangelion.api.utilities.TPSUtils;
import dev.evangelion.api.utilities.sounds.SoundRegisterListener;
import net.minecraftforge.common.MinecraftForge;
import dev.evangelion.client.gui.font.FontRenderer;
import java.awt.Font;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import dev.evangelion.api.manager.miscellaneous.InitializerManager;
import dev.evangelion.api.manager.miscellaneous.ConfigManager;
import dev.evangelion.client.gui.hud.HudEditorScreen;
import dev.evangelion.client.gui.click.ClickGuiScreen;
import dev.evangelion.api.manager.miscellaneous.FontManager;
import dev.evangelion.api.manager.event.EventManager;
import dev.evangelion.api.manager.miscellaneous.PlayerManager;
import dev.evangelion.api.manager.friend.FriendManager;
import dev.evangelion.api.manager.command.CommandManager;
import dev.evangelion.api.manager.element.ElementManager;
import dev.evangelion.api.manager.module.ModuleManager;
import java.awt.Color;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.Mod;

@Mod(name = "Evangelion", version = "190622", modid = "evangelion")
public class Evangelion
{
    public static final String NAME = "Evangelion";
    public static final String VERSION = "190622";
    public static final Logger LOGGER;
    public static Color COLOR_CLIPBOARD;
    public static ModuleManager MODULE_MANAGER;
    public static ElementManager ELEMENT_MANAGER;
    public static CommandManager COMMAND_MANAGER;
    public static FriendManager FRIEND_MANAGER;
    public static PlayerManager PLAYER_MANAGER;
    public static EventManager EVENT_MANAGER;
    public static FontManager FONT_MANAGER;
    public static ClickGuiScreen CLICK_GUI;
    public static HudEditorScreen HUD_EDITOR;
    public static ConfigManager CONFIG_MANAGER;
    public static InitializerManager INITIALIZER_MANAGER;
    
    @Mod.EventHandler
    public void initialize(final FMLInitializationEvent event) {
        Evangelion.LOGGER.info("Initialization process for Evangelion v190622 has started!");
        Evangelion.MODULE_MANAGER = new ModuleManager();
        Evangelion.ELEMENT_MANAGER = new ElementManager();
        Evangelion.COMMAND_MANAGER = new CommandManager();
        Evangelion.FRIEND_MANAGER = new FriendManager();
        Evangelion.PLAYER_MANAGER = new PlayerManager();
        Evangelion.EVENT_MANAGER = new EventManager();
        Evangelion.FONT_MANAGER = new FontManager();
        FontManager.FONT_RENDERER = new FontRenderer(new Font("Arial", 0, 40));
        Evangelion.CLICK_GUI = new ClickGuiScreen();
        Evangelion.HUD_EDITOR = new HudEditorScreen();
        (Evangelion.INITIALIZER_MANAGER = new InitializerManager()).initialize();
        MinecraftForge.EVENT_BUS.register((Object)new SoundRegisterListener());
        new TPSUtils();
        (Evangelion.CONFIG_MANAGER = new ConfigManager()).load();
        Evangelion.CONFIG_MANAGER.attach();
        Evangelion.LOGGER.info("Initialization process for Evangelion v190622 has finished!");
        Display.setTitle("Evangelion Build - 190622 - Jorgito Version");
    }
    
    static {
        LOGGER = LogManager.getLogger("Evangelion");
    }
}
