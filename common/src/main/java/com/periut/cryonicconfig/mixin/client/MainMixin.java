package com.periut.cryonicconfig.mixin.client;

import com.periut.cryonicconfig.UtilityCryonicConfig;
import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Main.class)
public class MainMixin {
    @Inject(method = "main", at = @At("HEAD"), remap = false)
    private static void initCryonicConfig(String[] args, CallbackInfo ci) {
        UtilityCryonicConfig.init(System.getProperty("user.dir"));
    }
}
