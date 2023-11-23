package mcp.mobius.waila.network;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet250CustomPayload;

import java.io.*;

public class Packet0x03ConfigData {
    public byte header;
    public String file;

    public Packet0x03ConfigData(Packet250CustomPayload packet) {
        DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
        try {
            this.header = inputStream.readByte();
            this.file = inputStream.readUTF();
        } catch (IOException ignored) {
        }
    }

    public static Packet250CustomPayload create(String file) {
        Packet250CustomPayload packet = new Packet250CustomPayload();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(17);
        DataOutputStream outputStream = new DataOutputStream(bos);

        try {
            outputStream.writeByte(3);
            outputStream.writeUTF(file);
        } catch (IOException ignored) {
        }

        packet.channel = "Waila";
        packet.data = bos.toByteArray();
        packet.length = bos.size();

        return packet;
    }
}
