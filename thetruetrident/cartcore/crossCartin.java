package thetruetrident.cartcore;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.StartTick;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil.Key;
import net.minecraft.client.util.InputUtil.Type;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class crossCartin implements ClientModInitializer {
   public static int crossbowTimer = -1;
   public static int pitchMemory = 1;
   public static Vec3d blockPosMem = null;
   public static boolean ejacBoolean = false;

   public void onInitializeClient() {
      UseBlockCallback.EVENT
         .register(
            (UseBlockCallback)(player, world, hand, hitResult) -> {
               BlockPos pos = hitResult.getBlockPos();
               MinecraftClient client = MinecraftClient.getInstance();
               if (CartcoreCore.checkIfCrossbowLoadedInHotbar(client.player)
                  && (
                     keyBinding.CROSSCART.isPressed() && crossbowTimer < 0
                        || client.player.method_6047().getItem() instanceof CrossbowItem && CrossbowItem.isCharged(client.player.method_6047())
                  )) {
                  if (!CartcoreCore.checkIfInHotbar(client.player, "rail")) {
                     return ActionResult.PASS;
                  }

                  if (!CartcoreCore.checkIfInHotbar(client.player, "tnt_minecart")) {
                     return ActionResult.PASS;
                  }

                  if (!CartcoreCore.checkIfInHotbar(client.player, "flint_and_steel")) {
                     return ActionResult.PASS;
                  }

                  CartcoreCore.playSoundSlow(SoundEvents.BLOCK_CONDUIT_ACTIVATE, client, 0.5F);
                  CartcoreCore.playSoundSlow(SoundEvents.ENTITY_BREEZE_CHARGE, client, 2.0F);
                  CartcoreCore.sendLog("Attempted a crossbow cart using a flint and steel, tnt minecart, and applicable rail.", client.player);
                  crossbowTimer = 5;
                  if (client.player.method_6047().getItem() instanceof CrossbowItem) {
                     return ActionResult.FAIL;
                  }

                  if (client.player.method_6079().getItem() instanceof CrossbowItem) {
                     return ActionResult.FAIL;
                  }
               }

               return ActionResult.PASS;
            }
         );
      ClientTickEvents.START_CLIENT_TICK.register((StartTick)client -> {
         if (CartcoreCore.cartConfig.crossbowCarting) {
            Key rightClickKey = Type.MOUSE.createFromCode(1);
            if (keyBinding.CROSSCART.isPressed() && CartcoreCore.checkIfCrossbowLoadedInHotbar(client.player)) {
               KeyBinding.onKeyPressed(rightClickKey);
               KeyBinding.setKeyPressed(rightClickKey, false);
            }

            if (client.world != null) {
               if (ejacBoolean) {
                  if (CartcoreCore.swapSlot(client.player, "crossbow")) {
                     if (CartcoreCore.targetPitch == null) {
                        KeyBinding.onKeyPressed(rightClickKey);
                        KeyBinding.setKeyPressed(rightClickKey, false);
                        ejacBoolean = false;
                     }
                  } else {
                     ejacBoolean = false;
                  }
               }

               if (blockPosMem != null && CartcoreCore.getTargetedBlockPos().toCenterPos() != blockPosMem) {
                  KeyBinding.onKeyPressed(rightClickKey);
                  KeyBinding.setKeyPressed(rightClickKey, false);
                  CartcoreCore.targetPitch = pitchMemory - 5;
                  CartcoreCore.pitchSpeed = 10.0F;
                  blockPosMem = null;
                  ejacBoolean = true;
               }

               if (crossbowTimer == 5) {
                  CartcoreCore.swapSlot(client.player, "rail");
                  KeyBinding.onKeyPressed(rightClickKey);
                  KeyBinding.setKeyPressed(rightClickKey, false);
               }

               if (crossbowTimer == 4) {
                  if (!CartcoreCore.swapSlot(client.player, "tnt_minecart") && client.player.method_6079().getItem() == Items.TNT_MINECART) {
                     CartcoreCore.sendLog("attempted an offhand cart by swapping to sword to force cart placement on rail", client.player);
                     CartcoreCore.swapSlot(client.player, "_sword");
                  }

                  KeyBinding.onKeyPressed(rightClickKey);
                  KeyBinding.setKeyPressed(rightClickKey, false);
               }

               if (crossbowTimer == 3) {
                  CartcoreCore.swapSlot(client.player, "flint_and_steel");
                  CartcoreCore.targetPitch = (int)(client.player.method_36455() + 90.0F);
                  blockPosMem = CartcoreCore.getTargetedBlockPos().toCenterPos();
                  pitchMemory = (int)client.player.method_36455();
               }

               crossbowTimer--;
            }
         }
      });
   }
}
