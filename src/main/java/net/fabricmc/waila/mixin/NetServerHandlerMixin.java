package net.fabricmc.waila.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(NetServerHandler.class)
public class NetServerHandlerMixin {

    @Shadow @Final public MinecraftServer mcServer;

    @Shadow public EntityPlayerMP playerEntity;

    /**
     * @author limingzxc
     * @reason fix the input of the sign
     */
    @Overwrite
    public void handleUpdateSign(Packet130UpdateSign par1Packet130UpdateSign) {
        WorldServer var2 = this.mcServer.worldServerForDimension(this.playerEntity.dimension);

        if (var2.blockExists(par1Packet130UpdateSign.xPosition, par1Packet130UpdateSign.yPosition, par1Packet130UpdateSign.zPosition))
        {
            TileEntity var3 = var2.getBlockTileEntity(par1Packet130UpdateSign.xPosition, par1Packet130UpdateSign.yPosition, par1Packet130UpdateSign.zPosition);

            if (var3 instanceof TileEntitySign)
            {
                TileEntitySign var4 = (TileEntitySign)var3;

                if (!var4.isEditable())
                {
                    this.mcServer.logWarning("Player " + this.playerEntity.username + " just tried to change non-editable sign");
                    return;
                }
            }

            int var6;
            int var8;

            for (var8 = 0; var8 < 4; ++var8)
            {
                boolean var5 = true;

                if (par1Packet130UpdateSign.signLines[var8].length() > 15)
                {
                    var5 = false;
                }
                else
                {
                    for (var6 = 0; var6 < par1Packet130UpdateSign.signLines[var8].length(); ++var6)
                    {
                        if (!ChatAllowedCharacters.isAllowedCharacter(par1Packet130UpdateSign.signLines[var8].charAt(var6)))
                        {
                            var5 = false;
                        }
                    }
                }

                if (!var5)
                {
                    par1Packet130UpdateSign.signLines[var8] = "!?";
                }
            }

            if (var3 instanceof TileEntitySign)
            {
                var8 = par1Packet130UpdateSign.xPosition;
                int var9 = par1Packet130UpdateSign.yPosition;
                var6 = par1Packet130UpdateSign.zPosition;
                TileEntitySign var7 = (TileEntitySign)var3;
                System.arraycopy(par1Packet130UpdateSign.signLines, 0, var7.signText, 0, 4);
                var7.onInventoryChanged();
                var2.markBlockForUpdate(var8, var9, var6);
            }
        }
    }
}
