package com.periut.cryonicconfig.mixin.client;

import com.periut.cryonicconfig.UtilityCryonicConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftApplet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void initCryonicConfig(Component canvas, Canvas applet, MinecraftApplet width, int height, int fullscreen, boolean par6, CallbackInfo ci) {
        UtilityCryonicConfig.init(System.getProperty("user.dir"));
        System.out.println("test:" + System.getProperty("user.dir"));
    }

    @Inject(method = "setWorld*", at = @At("HEAD"))
    void clearServerConfig(CallbackInfo ci) {
        UtilityCryonicConfig.SERVER_CONFIG.clear();
    }
}
