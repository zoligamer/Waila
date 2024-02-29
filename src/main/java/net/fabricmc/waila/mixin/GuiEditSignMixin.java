package net.fabricmc.waila.mixin;

import net.minecraft.src.ChatAllowedCharacters;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiEditSign;
import net.minecraft.src.TileEntitySign;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiEditSign.class)
public abstract class GuiEditSignMixin {

    @Shadow private int editLine;

    @Shadow private TileEntitySign entitySign;

    @Shadow private GuiButton doneBtn;
    @Shadow protected abstract void actionPerformed(GuiButton par1GuiButton);

    /**
     * @author limingzxc
     * @reason fix input
     */
    @Overwrite
    public void keyTyped(char par1, int par2)
    {
        if (par2 == 200)
        {
            this.editLine = this.editLine - 1 & 3;
        }

        if (par2 == 208 || par2 == 28)
        {
            this.editLine = this.editLine + 1 & 3;
        }

        if (par2 == 14 && !this.entitySign.signText[this.editLine].isEmpty())
        {
            this.entitySign.signText[this.editLine] = this.entitySign.signText[this.editLine].substring(0,
                    this.entitySign.signText[this.editLine].length() - 1);
        }

        if (ChatAllowedCharacters.isAllowedCharacter(par1) && this.entitySign.signText[this.editLine].length() < 15)
        {
            this.entitySign.signText[this.editLine] = this.entitySign.signText[this.editLine] + par1;
        }

        if (par2 == 1)
        {
            this.actionPerformed(this.doneBtn);
        }
    }
}
