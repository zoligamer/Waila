package mcp.mobius.waila.handlers;

import net.fabricmc.waila.api.BreakingProgress;
import net.fabricmc.waila.api.ItemInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.src.World;
import net.minecraft.src.MovingObjectPosition;

import java.util.List;

import net.fabricmc.waila.api.IHighlightHandler;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;

public class HUDHandlerWaila implements IHighlightHandler {
    public ItemStack identifyHighlight(World world, EntityPlayer player, MovingObjectPosition mop) {
        return null;
    }


    public List<String> handleTextData(ItemStack itemStack, World world, EntityPlayer player, MovingObjectPosition mop, List<String> currenttip, ItemInfo.Layout layout) {
        if (layout == ItemInfo.Layout.HEADER) {
//       if (currenttip.size() == 0) {
//         currenttip.add("< Unnamed >");
//       } else {
//         String name = currenttip.get(0);
//         currenttip.set(0, name + String.format(" %s:%s", world.getBlockId(mop.blockX, mop.blockY, mop.blockZ), world.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ)));
//       }
            if (mop != null) {
                int blockId = world.getBlockId(mop.blockX, mop.blockY, mop.blockZ);
                if (blockId == 149 || blockId == 150) blockId = 404;
                else if (blockId == 93 || blockId == 94) blockId = 356;
                currenttip.add(String.format("%s:%s", blockId, world.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ)));
                currenttip.add(String.format(" %f ", ((BreakingProgress) Minecraft.getMinecraft().playerController).getCurrentBreakingProgress()));
            }
        } else if (layout == ItemInfo.Layout.FOOTER) {
//            String modName = WailaAddon.instance.getModName(itemStack);
//            if (modName != null && !modName.equals(""))
//                currenttip.add("§9§o" + modName);
        }

        return currenttip;
    }
}


