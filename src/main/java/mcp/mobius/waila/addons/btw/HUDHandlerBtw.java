package mcp.mobius.waila.addons.btw;

import btw.block.BTWBlocks;
import btw.block.blocks.CampfireBlock;
import btw.block.tileentity.CampfireTileEntity;
import btw.block.tileentity.OvenTileEntity;
import btw.community.waila.WailaAddon;
import btw.item.BTWItems;
import mcp.mobius.waila.addons.ExternalModulesHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntityFurnace;

import java.util.List;

import static net.minecraft.src.TileEntityFurnace.DEFAULT_COOK_TIME;

public class HUDHandlerBtw implements IWailaDataProvider {

    static int idleOven = BTWBlocks.idleOven.blockID;
    static int burningOven = BTWBlocks.burningOven.blockID;
    static int unlitCampfire = BTWBlocks.unlitCampfire.blockID;
    static int smallCampfire = BTWBlocks.smallCampfire.blockID;
    static int mediumCampfire = BTWBlocks.mediumCampfire.blockID;
    static int largeCampfire = BTWBlocks.largeCampfire.blockID;
    static int goldOreChunkStorage = BTWBlocks.goldOreChunkStorage.blockID;
    static int ironOreChunkStorage = BTWBlocks.ironOreChunkStorage.blockID;

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor) {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor) {
        int blockID = accessor.getBlockID();
        return currenttip;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor)
    {
        int blockID = accessor.getBlockID();

        if (WailaAddon.showOvenBlock && (blockID == burningOven || blockID == idleOven)
                && accessor.getTileEntity() instanceof OvenTileEntity && WailaAddon.instance.serverPresent)
        {
            NBTTagCompound tag = accessor.getNBTData();

            int remainingCookingTime = 0;

            if(tag.getTagList("Items").tagCount() != 0 && FurnaceRecipes.smelting().getSmeltingResult(
                    (((NBTTagCompound)tag.getTagList("Items").tagAt(0)).getShort("id"))) != null)
            {
                int iCookTimeShift = DEFAULT_COOK_TIME << FurnaceRecipes.smelting().getCookTimeBinaryShift((
                        (NBTTagCompound)tag.getTagList("Items").tagAt(0)).getShort("id"));
                remainingCookingTime = iCookTimeShift * 4 - tag.getInteger("fcCookTimeEx");
            }
            currenttip.add(String.format("FuelTime: %s CookTime: %s",
                    (blockID == burningOven? tag.getInteger("fcBurnTimeEx"): tag.getInteger("fcUnlitFuel")) / 20
                    , remainingCookingTime / 20));
        }

        if (WailaAddon.showCampfire && (blockID == unlitCampfire || blockID == smallCampfire
                || blockID == mediumCampfire || blockID == largeCampfire) && accessor.getTileEntity()
                instanceof CampfireTileEntity && WailaAddon.instance.serverPresent)
        {
            NBTTagCompound tag = accessor.getNBTData();

            int TIME_TO_COOK = (TileEntityFurnace.DEFAULT_COOK_TIME * 8 * 3 / 2 );
            int TIME_TO_BURN_FOOD = (TIME_TO_COOK / 2 );

            if(tag.getInteger("fcCookCounter") == 0) TIME_TO_COOK = 0;

            currenttip.add(String.format("BurnTime: %s CookTime: %s BurnedTime: %s", tag.getInteger("fcBurnCounter") / 20,
                    (TIME_TO_COOK - tag.getInteger("fcCookCounter")) / 20, (((CampfireBlock)accessor.getBlock()).fireLevel >= 3 &&
                            tag.getCompoundTag("fcCookStack").getShort("id") != BTWItems.burnedMeat.itemID)?
                            (TIME_TO_BURN_FOOD - tag.getInteger("fcCookBurning")) / 20: "null"));
        }

        if (WailaAddon.showOreChunkStorage && (blockID == ironOreChunkStorage || blockID == goldOreChunkStorage))
        {
            String mode = String.valueOf(accessor.getMetadata());

            currenttip.add("Mode: " + mode);
        }
        return currenttip;
    }

    public static void register() {
        ExternalModulesHandler.instance().registerBodyProvider(new HUDHandlerBtw(), idleOven);
        ExternalModulesHandler.instance().registerBodyProvider(new HUDHandlerBtw(), burningOven);
        ExternalModulesHandler.instance().registerBodyProvider(new HUDHandlerBtw(), unlitCampfire);
        ExternalModulesHandler.instance().registerBodyProvider(new HUDHandlerBtw(), smallCampfire);
        ExternalModulesHandler.instance().registerBodyProvider(new HUDHandlerBtw(), mediumCampfire);
        ExternalModulesHandler.instance().registerBodyProvider(new HUDHandlerBtw(), largeCampfire);
        ExternalModulesHandler.instance().registerBodyProvider(new HUDHandlerBtw(), ironOreChunkStorage);
        ExternalModulesHandler.instance().registerBodyProvider(new HUDHandlerBtw(), goldOreChunkStorage);
    }
}
