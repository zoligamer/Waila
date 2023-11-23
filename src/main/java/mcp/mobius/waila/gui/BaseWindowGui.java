package mcp.mobius.waila.gui;

import btw.community.waila.WailaAddon;
import btw.item.items.PlaceAsBlockItem;
import com.prupe.mcpatcher.mal.block.RenderBlocksUtils;
import net.fabricmc.waila.api.ItemInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BaseWindowGui extends Gui {

    private final Minecraft theGame = Minecraft.getMinecraft();
    private int WindowWidth;
    private int WindowHeight;

    private RenderItem renderItem;

    private void updateAchievementWindowScale() {
        GL11.glViewport(0, 0, this.theGame.displayWidth, this.theGame.displayHeight);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        this.WindowWidth = this.theGame.displayWidth;
        this.WindowHeight = this.theGame.displayHeight;
        ScaledResolution var1 = new ScaledResolution(this.theGame.gameSettings, this.theGame.displayWidth, this.theGame.displayHeight);
        this.WindowWidth = var1.getScaledWidth();
        this.WindowHeight = var1.getScaledHeight();
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, this.WindowWidth, this.WindowHeight, 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
    }

    public void updateAchievementWindow() {

        List<String> info = ItemInfo.getText(theGame.thePlayer.getHeldItem(), theGame.theWorld, theGame.thePlayer, theGame.objectMouseOver);

        Item item = Item.diamond;
        int[] itemId = new int[0];

        if (info.size() > 1) {

            if (info.get(0).split(":")[0].equals("0")) {
                return;
            }

            itemId = Arrays.stream(info.get(0).split(":")).mapToInt(Integer::parseInt).toArray();

            item = Item.itemsList[itemId[0]];

            if(item instanceof ItemSlab)
            {
                itemId[1] = itemId[1] % 4;
            }

            info.add(0, item.getItemDisplayName(new ItemStack(item, 1, itemId[1])));
            if(itemId[0] == 1010 || itemId[0] == 3206 || itemId[0] == 3207 || itemId[0] == 3208)
            {
                itemId[1] = itemId[1] % 4;
            }
        }

        if (info.size() == 0 || theGame.objectMouseOver.entityHit != null || theGame.isGamePaused || theGame.thePlayer.isDead ||
                theGame.thePlayer.openContainer != theGame.thePlayer.inventoryContainer ||
                theGame.thePlayer.isPlayerSleeping()) {
            return;
        }

        this.updateAchievementWindowScale();

        int var5 = this.WindowWidth / 2 - 80;
        int var6 = 0;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
//        this.theGame.renderEngine.bindTexture("/gui/demo_bg.png");
        GL11.glDisable(GL11.GL_LIGHTING);
//        this.drawTexturedModalRect(var5, var6, 96, 202 + 100, 160 + 100, 32 + 10);

        int progress = (int)(Float.parseFloat(info.get(2)) * 100F);

        int lineHeight = var6 + 25;
        int len = 0;
        if(info.size() >= 4) len = info.get(3).replaceAll("[^:]", "").length();
        if(len > 2 && !this.theGame.fontRenderer.getUnicodeFlag()) lineHeight += ((len - 1) / 2 * 10);

        if (!WailaAddon.backgroundTransparency) {
            drawBackgroundBox(var5 + 5, var6 + 5, var5 + 140, lineHeight, 0XFF100010, 0XFF5000ff, 0XFF28007f);
        }

        //drawRect(var5 + 6, var6 + 3, var5 + 160, var6 + 10 + lineHeight, 0xFF3F1996);
        //drawRect(var5 + 8, var6 + 5, var5 + 150, var6 + lineHeight, 0xFF3A314C);


        drawHorizontalLine(var5 + 10, var5 + 110, lineHeight, 0xFFFFFFFF);
        drawHorizontalLine(var5 + 10, progress + var5 + 10, lineHeight, 0xFF000000);

        info.remove(2);

        this.theGame.fontRenderer.drawSplitString(String.join(", ", info), var5 + 25, var6 + 7, 120, 0X50A0A0A0);

        RenderHelper.enableGUIStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glEnable(GL11.GL_LIGHTING);
        // new RenderBlocks().renderBlockAsItemVanilla(new Block());
        if (renderItem == null) {
            renderItem = new RenderItem();
        }
        renderItem.renderItemIntoGUI(this.theGame.fontRenderer, this.theGame.renderEngine, new ItemStack(item, 1, itemId[1]), var5 + 8, var6 + 8);
        GL11.glDisable(GL11.GL_LIGHTING);
        // GL11.glDepthMask(true);
        // GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    public void drawBackgroundBox(int x, int y, int w, int h, int bg, int grad1, int grad2) {

        this.drawGradientRect(x + 1, y, w - 1, 1, bg, bg);
        this.drawGradientRect(x + 1, y + h, w - 1, 1, bg, bg);
        this.drawGradientRect(x + 1, y + 1, w - 1, h - 1, bg, bg);//center
        this.drawGradientRect(x, y + 1, 1, h - 1, bg, bg);
        this.drawGradientRect(x + w, y + 1, 1, h - 1, bg, bg);
        this.drawGradientRect(x + 1, y + 2, 1, h - 3, grad1, grad2);
        this.drawGradientRect(x + w - 1, y + 2, 1, h - 3, grad1, grad2);

        this.drawGradientRect(x + 1, y + 1, w - 1, 1, grad1, grad1);
        this.drawGradientRect(x + 1, y + h - 1, w - 1, 1, grad2, grad2);
    }
}
