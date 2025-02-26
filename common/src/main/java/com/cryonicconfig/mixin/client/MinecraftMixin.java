package com.cryonicconfig.mixin.client;

import com.cryonicconfig.UtilityCryonicConfig;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftMixin {
    @Inject(method = "onDisconnected", at = @At("HEAD"))
    void clearServerConfig(CallbackInfo ci) {
        UtilityCryonicConfig.SERVER_CONFIG.clear();
    }
}
