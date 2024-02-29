package btw.community.waila;

import btw.AddonHandler;
import btw.BTWAddon;
import mcp.mobius.waila.ProxyClient;
import mcp.mobius.waila.command.ConfigCommand;
import mcp.mobius.waila.gui.BaseWindowGui;
import mcp.mobius.waila.network.Packet0x00ServerPing;
import mcp.mobius.waila.network.WailaPacketHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.waila.api.PacketDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

import java.util.Arrays;
import java.util.Map;

public class WailaAddon extends BTWAddon {

    public static WailaAddon instance;
    private KeyBinding keyBinding;
    public boolean serverPresent = false;
    private WailaPacketHandler wailaPacketHandler;
    private BaseWindowGui baseWindowGui;
    public static ProxyClient proxy;
    private boolean enableKeybind;

    public WailaAddon() {
        super("Waila", "1.0.6 beta", "WAILA");
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
        keyBinding = new KeyBinding(StatCollector.translateToLocal("key.waila.hidden"), keyBind);
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

    @Deprecated
    public boolean serverCustomPacketReceived(NetServerHandler handler, Packet250CustomPayload packet) {
        if(wailaPacketHandler == null) {
            wailaPacketHandler = new WailaPacketHandler();
        }
        wailaPacketHandler.handleCustomPacket(handler, packet);
        return false;
    }

    public void serverPlayerConnectionInitialized(NetServerHandler serverHandler, EntityPlayerMP playerMP) {
        PacketDispatcher.sendPacketToPlayer(Packet0x00ServerPing.create(), playerMP);
    }

//    @Environment(EnvType.CLIENT)
//    @Deprecated
//    public boolean clientCustomPacketReceived(Minecraft mcInstance, Packet250CustomPayload packet) {
//        if(wailaPacketHandler == null) {
//            wailaPacketHandler = new WailaPacketHandler();
//        }
//        wailaPacketHandler.handleCustomPacket(packet);
//        return true;
//    }

    @Environment(EnvType.CLIENT)
    public boolean interceptCustomClientPacket(Minecraft mc, Packet250CustomPayload packet) {
        if(wailaPacketHandler == null) {
            wailaPacketHandler = new WailaPacketHandler();
        }
        wailaPacketHandler.handleCustomPacket(packet);
        return false;
    }

    @Override
    public void preInitialize() {
        registerConfigProperties();
        // registerAddonCommandClientOnly(new ConfigCommand());
    }

    private void registerConfigProperties() {
        this.registerProperty("showSpawnerType", "True", "+ for both server and client enabled, - for client only\n" +
                "[-]Show mobspawner types");
        this.registerProperty("showGrowthValue", "True", "[-]Show growth value");
        this.registerProperty("showLeverState", "True", "[-]Show lever state");
        this.registerProperty("showRepeater", "True", "[-]Show repeater delay");
        this.registerProperty("showComparator", "True", "[-]Show comparator mode");
        this.registerProperty("showRedstone", "True", "[-]Show redstone power");
        this.registerProperty("showSkull", "True", "[+]Show skull info");
        this.registerProperty("showOvenBlock", "True", "[+]Show ovenblock state");
        this.registerProperty("showCampfire", "True", "[+]Show campfire state");
        this.registerProperty("showFiniteTorch", "True", "[+]Show finitetorch state");
        this.registerProperty("keyBind", "35", "To enable or disable the GUI's keys, please fill in the values of the LWJGL keycode table, please refer to https://minecraft.fandom.com/wiki/Key_codes#Keyboard_codes(only client)");
        this.registerProperty("backgroundTransparency", "False", "[-]Set the GUI background transparent,no background drawn");
    }

    @Override
    public void handleConfigProperties(Map<String, String> propertyValues) {
        showSpawnerType = Boolean.parseBoolean(propertyValues.get("showSpawnerType"));
        showGrowthValue = Boolean.parseBoolean(propertyValues.get("showGrowthValue"));
        showLeverState = Boolean.parseBoolean(propertyValues.get("showLeverState"));
        showRepeater = Boolean.parseBoolean(propertyValues.get("showRepeater"));
        showComparator = Boolean.parseBoolean(propertyValues.get("showComparator"));
        showFiniteTorch = Boolean.parseBoolean(propertyValues.get("showFiniteTorch"));
        showRedstone = Boolean.parseBoolean(propertyValues.get("showRedstone"));
        showSkull = Boolean.parseBoolean(propertyValues.get("showSkull"));
        showOvenBlock = Boolean.parseBoolean(propertyValues.get("showOvenBlock"));
        showCampfire = Boolean.parseBoolean(propertyValues.get("showCampfire"));
        keyBind = Integer.parseInt(propertyValues.get("keyBind"));
        backgroundTransparency = Boolean.parseBoolean(propertyValues.get("backgroundTransparency"));
    }

    public static boolean showSpawnerType;
    public static boolean showGrowthValue;
    public static boolean showLeverState;
    public static boolean showRepeater;
    public static boolean showComparator;
    public static boolean showRedstone;

    public static boolean showSkull;
    public static boolean showOvenBlock;
    public static boolean showCampfire;
    public static boolean showFiniteTorch;

    public static int keyBind;
    public static boolean backgroundTransparency;
}
