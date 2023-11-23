package mcp.mobius.waila.network;

import btw.AddonHandler;
import btw.BTWAddon;
import btw.block.tileentity.CampfireTileEntity;
import btw.block.tileentity.OvenTileEntity;
import btw.community.waila.WailaAddon;
import mcp.mobius.waila.WailaExceptionHandler;
import mcp.mobius.waila.handlers.DataAccessor;
import net.fabricmc.waila.api.PacketDispatcher;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WailaPacketHandler {

    public void handleCustomPacket(Packet250CustomPayload packet) {
        if (packet.channel.equals("Waila")) {
            try {
                byte header = getHeader(packet);

                if (header == 0) {
                    WailaAddon.ModLogger("Received server authentication msg. Remote sync will be activated");
                    WailaAddon.instance.serverPresent = true;

                } else if (header == 2) {
                    Packet0x02TENBTData castedPacket = new Packet0x02TENBTData(packet);
                    DataAccessor.instance.remoteNbt = castedPacket.tag;
                }

            } catch (Exception ignored) {
            }
        }
    }

    public void handleCustomPacket(NetServerHandler handler, Packet250CustomPayload packet) {
        if (packet.channel.equals("Waila")) {
            try {
                byte header = getHeader(packet);

                if (header == 1) {
                    Packet0x01TERequest castedPacket = new Packet0x01TERequest(packet);
                    MinecraftServer server = MinecraftServer.getServer();
                    TileEntity entity = server.worldServers[castedPacket.worldID].getBlockTileEntity(castedPacket.posX, castedPacket.posY, castedPacket.posZ);

                    if (entity instanceof OvenTileEntity || entity instanceof CampfireTileEntity) {
                        if (!WailaAddon.showOvenBlock && entity instanceof OvenTileEntity) return;
                        else if (!WailaAddon.showCampfire && entity instanceof CampfireTileEntity) return;
                        try {
                            NBTTagCompound tag = new NBTTagCompound();
                            entity.writeToNBT(tag);
                            PacketDispatcher.sendPacketToPlayer(Packet0x02TENBTData.create(tag), handler.playerEntity);
                        } catch (Throwable e) {
                            WailaExceptionHandler.handleErr(e, entity.getClass().toString(), null);
                        }
                    }
                }
//                else if(header == 3) {
//                    Packet0x03ConfigData filePacket = new Packet0x03ConfigData(packet);
//                    Map<String, String> config = new HashMap<>();
//                    for (Object addon: AddonHandler.modList.values().toArray()) {
//                        if(Objects.equals(((BTWAddon) addon).getLanguageFilePrefix(), filePacket.file)) {
//                            config = ((BTWAddon)addon).loadConfigProperties();
//                            break;
//                        }
//                    }
//                    handler.playerEntity.sendChatToPlayer(config.toString());
//                }
            } catch (Exception ignored) {
            }
        }
    }

    public byte getHeader(Packet250CustomPayload packet) {
        DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
        try {
            return inputStream.readByte();
        } catch (IOException e) {
            return -1;
        }
    }
}


