package mcp.mobius.waila.addons.vanillamc;

import btw.block.BTWBlocks;
import btw.block.blocks.CampfireBlock;
import btw.block.blocks.CropsBlock;
import btw.block.tileentity.CampfireTileEntity;
import btw.block.tileentity.OvenTileEntity;
import btw.community.waila.WailaAddon;
import btw.entity.mob.SkeletonEntity;
import btw.item.BTWItems;
import mcp.mobius.waila.addons.ExternalModulesHandler;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;

import java.util.List;

import static net.minecraft.src.TileEntityFurnace.DEFAULT_COOK_TIME;

public class HUDHandlerVanilla implements IWailaDataProvider {
    static int mobSpawnerID = Block.mobSpawner.blockID;
    static int wheatCropID = BTWBlocks.wheatCrop.blockID;
    static int melonStemID = Block.melonStem.blockID;
    static int pumpkinStemID = Block.pumpkinStem.blockID;
    static int carrotCropID = BTWBlocks.carrotCrop.blockID;
    static int potatoID = Block.potato.blockID;
    static int hempCropID = BTWBlocks.hempCrop.blockID;
    static int netherStalkID = Block.netherStalk.blockID;
    static int leverID = Block.lever.blockID;
    static int repeaterIdle = Block.redstoneRepeaterIdle.blockID;
    static int repeaterActv = Block.redstoneRepeaterActive.blockID;
    static int comparatorIdl = Block.redstoneComparatorIdle.blockID;
    static int comparatorAct = Block.redstoneComparatorActive.blockID;
    static int redstone = Block.redstoneWire.blockID;

    static int skull = Block.skull.blockID;


    public ItemStack getWailaStack(IWailaDataAccessor accessor) {
        return null;
    }


    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor) {
        int blockID = accessor.getBlockID();
        return currenttip;
    }


    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor) {
        int blockID = accessor.getBlockID();

        if (WailaAddon.showGrowthValue && (Block.blocksList[blockID] instanceof CropsBlock ||
                        Block.blocksList[blockID] instanceof BlockCrops ||
                        Block.blocksList[blockID] instanceof BlockStem ||
                        blockID == netherStalkID)) {

            int metadata = accessor.getMetadata();
            float growthValue = blockID == netherStalkID? metadata / 3.0F * 100F: (metadata & 7) / 7.0F * 100.0F;

            if (growthValue != 100.0D) {
                currenttip.add(String.format("Growth: %.0f %%", growthValue));
            } else {
                if(blockID == wheatCropID || blockID == hempCropID) currenttip.add("Growth: Upward");
                else currenttip.add("Growth: Mature");
            }
            return currenttip;
        }

        if (WailaAddon.showLeverState && blockID == leverID) {
            String redstoneOn = ((accessor.getMetadata() & 0x8) == 0) ? "Off" : "On";
            currenttip.add("State: " + redstoneOn);
            return currenttip;
        }

        if (WailaAddon.showRepeater && (blockID == repeaterIdle || blockID == repeaterActv)) {
            int tick = (accessor.getMetadata() >> 2) + 1;
            if (tick == 1) {
                currenttip.add(String.format("Delay: %s tick", tick));
            } else {
                currenttip.add(String.format("Delay: %s ticks", tick));
            }
            return currenttip;
        }

        if (WailaAddon.showComparator && (blockID == comparatorIdl || blockID == comparatorAct)) {
            String mode = ((accessor.getMetadata() >> 2 & 0x1) == 0) ? "Comparator" : "Subtractor";

            currenttip.add("Mode: " + mode);

            return currenttip;
        }

        if (WailaAddon.showRedstone && blockID == redstone) {
            currenttip.add(String.format("Power: %s", accessor.getMetadata()));
            return currenttip;
        }

        if (WailaAddon.showSpawnerType && blockID == mobSpawnerID && accessor.getTileEntity() instanceof TileEntityMobSpawner) {
            currenttip.add(String.format("Type: %s", ((TileEntityMobSpawner) accessor.getTileEntity()).func_98049_a().getEntityNameToSpawn()));
        }

        if(WailaAddon.showSkull && blockID == skull && accessor.getTileEntity() instanceof TileEntitySkull && WailaAddon.instance.serverPresent) {
            NBTTagCompound tag = accessor.getNBTData();
            byte type = tag.getByte("SkullType");
            String skull = "";

            switch (type)
            {
                case 0:
                default:
                    skull = StatCollector.translateToLocal("item.skull.skeleton.name");
                    break;

                case 1:
                    skull = StatCollector.translateToLocal("item.skull.wither.name");
                    break;

                case 2:
                    skull = StatCollector.translateToLocal("item.skull.zombie.name");
                    break;

                case 3:
                    skull = String.format(StatCollector.translateToLocal("item.skull.player.name"),
                            tag.getString("ExtraType"));
                    break;

                case 4:
                    skull = StatCollector.translateToLocal("item.skull.creeper.name");
                    break;

                case 5:
                    skull = StatCollector.translateToLocal("entity.enderman.name");
                    break;
            }
            currenttip.add(skull);
        }

        return currenttip;
    }

    public static void register() {
        ExternalModulesHandler.instance().registerBodyProvider(new HUDHandlerVanilla(), mobSpawnerID);
        ExternalModulesHandler.instance().registerBodyProvider(new HUDHandlerVanilla(), wheatCropID);
        ExternalModulesHandler.instance().registerBodyProvider(new HUDHandlerVanilla(), melonStemID);
        ExternalModulesHandler.instance().registerBodyProvider(new HUDHandlerVanilla(), pumpkinStemID);
        ExternalModulesHandler.instance().registerBodyProvider(new HUDHandlerVanilla(), carrotCropID);
        ExternalModulesHandler.instance().registerBodyProvider(new HUDHandlerVanilla(), potatoID);
        ExternalModulesHandler.instance().registerBodyProvider(new HUDHandlerVanilla(), hempCropID);
        ExternalModulesHandler.instance().registerBodyProvider(new HUDHandlerVanilla(), netherStalkID);

        ExternalModulesHandler.instance().registerBodyProvider(new HUDHandlerVanilla(), leverID);
        ExternalModulesHandler.instance().registerBodyProvider(new HUDHandlerVanilla(), repeaterIdle);
        ExternalModulesHandler.instance().registerBodyProvider(new HUDHandlerVanilla(), repeaterActv);
        ExternalModulesHandler.instance().registerBodyProvider(new HUDHandlerVanilla(), comparatorIdl);
        ExternalModulesHandler.instance().registerBodyProvider(new HUDHandlerVanilla(), comparatorAct);
        // ExternalModulesHandler.instance().registerHeadProvider(new HUDHandlerVanilla(), redstone);
        ExternalModulesHandler.instance().registerBodyProvider(new HUDHandlerVanilla(), redstone);
        ExternalModulesHandler.instance().registerBodyProvider(new HUDHandlerVanilla(), skull);

        ExternalModulesHandler.instance().registerDocTextFile("D:/BTW/Test/src/java/mcp/mobius/waila/addons/vanillamc/WikiData.csv");
    }
}


