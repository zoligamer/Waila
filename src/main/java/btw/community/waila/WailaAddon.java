package btw.community.waila;

import btw.AddonHandler;
import btw.BTWAddon;
import mcp.mobius.waila.ProxyClient;
import net.minecraft.src.ItemStack;

public class WailaAddon extends BTWAddon {

    public static WailaAddon instance;

    public boolean serverPresent = false;

    public static ProxyClient proxy;

    private WailaAddon() {
        super("Waila", "beta 1.0.0", "Ex");
    }

    @Override
    public void initialize() {
        load();
        ModLogger(this.getName() + " Version " + this.getVersionString()
                + " Initializing...");
    }

    public static void ModLogger(String s) {
        AddonHandler.logMessage(s);
    }

    public String getModName(ItemStack itemStack) {
        if(itemStack != null)
            return itemStack.getItem().getItemDisplayName(itemStack);
        else return "BTW";
    }

    private void load() {
        instance = new WailaAddon();
        proxy = new ProxyClient();
        proxy.registerHandlers();
    }

}
