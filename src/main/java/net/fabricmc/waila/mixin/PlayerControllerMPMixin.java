package net.fabricmc.waila.mixin;

import net.fabricmc.waila.api.BreakingProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.PlayerControllerMP;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerControllerMP.class)
public abstract class PlayerControllerMPMixin implements BreakingProgress {

    @Shadow private float curBlockDamageMP;
    @Override
    public float getCurrentBreakingProgress() {
        return this.curBlockDamageMP;
    }
}
