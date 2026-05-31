package com.periut.cryonicconfig.mixin.client;

import com.periut.cryonicconfig.UtilityCryonicConfig;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPlayNetworkHandlerMixin {

    @Inject(method = "handleSystemChat", at = @At("HEAD"), cancellable = true)
    private void interceptSyncMessage(ClientboundSystemChatPacket packet, CallbackInfo ci) {
        String msgContent = packet.content().getString();

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
}
