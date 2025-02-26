package com.cryonicconfig.mixin;

import com.cryonicconfig.CryonicConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    int counter = 0;
    @Inject(method = "tick", at = @At("HEAD"))
    void SYNCTEST(CallbackInfo ci) {
        counter++;
        if (counter > 100) {
            counter = 0;
            if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
                CryonicConfig.getConfig("cc_sync_test_mod").setInt("var", 100);
                CryonicConfig.getConfig("cc_sync_test_mod").sync("var", (PlayerEntity) (Object) this);
            } else {
                System.out.println(CryonicConfig.getConfig("cc_sync_test_mod").getInt("var", 15));
            }
        }
    }
}
