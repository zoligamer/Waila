package net.fabricmc.waila.api;

public interface BreakingProgress {
    default float getCurrentBreakingProgress() { return 0; }
}
