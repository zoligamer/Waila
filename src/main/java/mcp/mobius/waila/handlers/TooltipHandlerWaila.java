package mcp.mobius.waila.handlers;

import btw.community.vanilla.VanillaAddon;
import net.minecraft.src.GuiContainer;

import java.util.List;

import net.minecraft.src.ItemStack;

public class TooltipHandlerWaila {
    private final VanillaAddon waila = VanillaAddon.instance;


    public List<String> handleTooltipFirst(GuiContainer gui, int mousex, int mousey, List<String> currenttip) {
        return currenttip;
    }


    public List<String> handleItemTooltip(GuiContainer gui, ItemStack itemstack, List<String> currenttip) {
        String canonicalName = this.waila.getModName(itemstack);
        if (canonicalName != null && !canonicalName.equals(""))
            currenttip.add("§9§o" + canonicalName);
        return currenttip;
    }
}


