package dev.fire.mixin.hud;

import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ToastManager.class)
public class MToastManager {
    @Inject(method = "add", at = @At("HEAD"), cancellable = true)
    public void add(Toast toast, CallbackInfo ci) {
        if (toast.getType().equals(SystemToast.Type.UNSECURE_SERVER_WARNING)) ci.cancel();
    }
}
