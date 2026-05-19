package thetruetrident.cartcore.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thetruetrident.cartcore.CartcoreClient;
import thetruetrident.cartcore.CartcoreCore;

@Environment(EnvType.CLIENT)
@Mixin(BowItem.class)
public abstract class BowItemMixin {
   @Inject(method = "method_7840", at = @At("HEAD"))
   private void onShoot(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfoReturnable<Boolean> cir) {
      if (world.method_8608()) {
         MinecraftClient client = MinecraftClient.getInstance();
         if (CartcoreCore.checkIfInHotbar(client.player, "rail") && CartcoreCore.checkIfInHotbar(client.player, "tnt_minecart")) {
            if (user instanceof PlayerEntity player) {
               int usedTicks = ((BowItem)this).method_7881(stack, user) - remainingUseTicks;
               float pull = BowItem.getPullProgress(usedTicks);
               if (pull >= 0.1F) {
                  CartcoreClient.shotBow = true;
               }
            }
         }
      }
   }
}
