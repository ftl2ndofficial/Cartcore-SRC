package thetruetrident.cartcore.mixin.client;

import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thetruetrident.cartcore.CartcoreCore;
import thetruetrident.cartcore.cartAssistMixin;

@Environment(EnvType.CLIENT)
@Mixin(value = ClientPlayerEntity.class, priority = 700)
public abstract class cartAssister extends AbstractClientPlayerEntity implements cartAssistMixin {
   @Shadow
   @Final
   protected MinecraftClient field_3937;
   private float lastPitch;

   public cartAssister(ClientWorld world, GameProfile profile) {
      super(world, profile);
   }

   @Inject(method = "method_5695", at = @At("RETURN"), cancellable = true)
   private void modifyPitch(float tickDelta, CallbackInfoReturnable<Float> cir) {
      if (CartcoreCore.targetPitch == null) {
         CartcoreCore.pitchSpeed = 5.0F;
      } else {
         float currentPitch = (Float)cir.getReturnValue();
         float maxChangePerFrame = CartcoreCore.pitchSpeed / 20.0F;
         float base = this.lastPitch == 0.0F ? currentPitch : this.lastPitch;
         float newPitch = this.approach(base, CartcoreCore.targetPitch.floatValue(), maxChangePerFrame);
         this.lastPitch = newPitch;
         cir.setReturnValue(newPitch);
      }
   }

   private float approach(float current, float target, float maxChange) {
      float delta = MathHelper.wrapDegrees(target - current);
      return Math.abs(delta) <= maxChange ? target : current + MathHelper.clamp(delta, -maxChange, maxChange);
   }

   @Override
   public void setLastPitch(float pitch) {
      this.lastPitch = pitch;
   }

   @Override
   public float getLastPitch() {
      return this.lastPitch;
   }
}
