package mcp.mobius.waila.command;

import btw.AddonHandler;
import btw.BTWAddon;
import mcp.mobius.waila.network.Packet0x01TERequest;
import mcp.mobius.waila.network.Packet0x03ConfigData;
import net.fabricmc.waila.api.PacketDispatcher;
import net.minecraft.src.ICommand;
import net.minecraft.src.ICommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigCommand implements ICommand {
    @Override
    public String getCommandName() {
        return "config";
    }

    @Override
    public String getCommandUsage(ICommandSender par1ICommandSender) {
        return par1ICommandSender.translateString("commands.config.usage");
    }

    @Override
    public List getCommandAliases() {
        return null;
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) {
        if (var2.length == 2)
        {
            if(var2[0].equals("get"))
            {
                PacketDispatcher.sendPacketToServer(Packet0x03ConfigData.create(var2[1]));
            }
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender var1) {
        return true;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
        if(var2.length == 1) {
            return new ArrayList<>(Collections.singleton("get"));
        }
        else if(var2.length == 2) {
            return AddonHandler.modList.values().stream().map(BTWAddon::getLanguageFilePrefix).collect(Collectors.toList());
        }
        else {
            return null;
        }
    }

    @Override
    public boolean isUsernameIndex(String[] var1, int var2) {
        return false;
    }

    /**
     * Compares the name of this command to the name of the given command.
     */
    public int compareTo(ICommand par1ICommand)
    {
        return this.getCommandName().compareTo(par1ICommand.getCommandName());
    }

    @Override
    public int compareTo(@NotNull Object par1Obj)
    {
        return this.compareTo((ICommand)par1Obj);
    }
}
