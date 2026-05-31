package com.periut.cryonicconfig.mixin.client;

import com.periut.cryonicconfig.UtilityCryonicConfig;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "clearDownloadedResourcePacks", at = @At("HEAD"))
    void clearServerConfig(CallbackInfo ci) {
        UtilityCryonicConfig.SERVER_CONFIG.clear();
    }
}
