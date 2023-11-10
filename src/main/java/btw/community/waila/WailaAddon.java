package btw.community.waila;

import btw.AddonHandler;
import btw.BTWAddon;
import mcp.mobius.waila.ProxyClient;
import mcp.mobius.waila.gui.BaseWindowGui;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.Objects;

public class WailaAddon extends BTWAddon {

    public static WailaAddon instance;
    private KeyBinding keyBinding;
    public boolean serverPresent = false;
    private BaseWindowGui baseWindowGui;
    public static ProxyClient proxy;
    private boolean enableKeybind;

    public WailaAddon() {
        super("Waila", "1.0.4 beta", "Ex");
    }

    @Override
    public void initialize() {
        ModLogger(this.getName() + " Version " + this.getVersionString()
                + " Initializing...");
    }

    public static void ModLogger(String s) {
        AddonHandler.logMessage(s);
    }

    public String getModName(ItemStack itemStack) {
        if(itemStack != null) {
            return itemStack.getItem().getItemDisplayName(itemStack);
        }
        else return "";
    }

    public void load() {
        instance = new WailaAddon();
        proxy = new ProxyClient();
        proxy.registerHandlers();
        initKeybind();
    }

    public void initKeybind()
    {
        enableKeybind = true;
        baseWindowGui = new BaseWindowGui();
        keyBinding = new KeyBinding(StatCollector.translateToLocal("key.waila.hidden"), Keyboard.KEY_H);
        GameSettings settings = Minecraft.getMinecraft().gameSettings;
        KeyBinding[] keyBindings = settings.keyBindings;
        keyBindings = Arrays.copyOf(keyBindings, keyBindings.length + 1);
        keyBindings[keyBindings.length - 1] = keyBinding;
        settings.keyBindings = keyBindings;
    }

    public void checkKeybind() {
        if(keyBinding.isPressed())
        {
            enableKeybind = !enableKeybind;
        }
        if(enableKeybind)
        {
            baseWindowGui.updateAchievementWindow();
        }
    }
}
