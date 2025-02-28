package com.periut.cryonicconfig.mixin.client;

import com.periut.cryonicconfig.UtilityCryonicConfig;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Inject(method = "onGameMessage", at = @At("HEAD"), cancellable = true)
    private void interceptSyncMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        String msgContent = packet.getMessage().getString();

        // Check if it's a config sync message
        if (msgContent.startsWith("ccsync:")) {
            ci.cancel(); // Prevent it from showing up in chat

            // Expected format: ccsync:mod_id:key:value
            String[] parts = msgContent.split(":", 4);
            try {
                if (parts.length == 4) {
                    String mod_id = parts[1];
                    String key = parts[2];
                    String value = parts[3];

                    // Process and store the value
                    UtilityCryonicConfig.handleSyncMessage(mod_id, key, value);
                }
            } catch (Exception ignored) {

            }
        }
    }

    @Inject(method = "clearWorld", at = @At("HEAD"))
    void clearServerConfig(CallbackInfo ci) {
        UtilityCryonicConfig.SERVER_CONFIG.clear();
    }
}
