package com.cryonicconfig.mixin.server;

import com.cryonicconfig.UtilityCryonicConfig;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void initCryonicConfig(CallbackInfo ci) {
        UtilityCryonicConfig.init(System.getProperty("user.dir"));
    }
}
