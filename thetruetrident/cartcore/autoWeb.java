package thetruetrident.cartcore;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.StartTick;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil.Key;
import net.minecraft.client.util.InputUtil.Type;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

@Environment(EnvType.CLIENT)
public class autoWeb implements ClientModInitializer {
   private static boolean cobWebBool;
   private static int pitchMemoryWeb;

   public void onInitializeClient() {
      UseBlockCallback.EVENT.register((UseBlockCallback)(player, world, hand, hitResult) -> {
         if (!CartcoreCore.cartConfig.autoWebbing) {
            return ActionResult.PASS;
         } else if (player == null) {
            return ActionResult.PASS;
         } else if (player.method_6047().getItem() != Items.COBWEB) {
            System.out.println("copweb golder?");
            return ActionResult.PASS;
         } else {
            BlockPos placePos = hitResult.getBlockPos().offset(hitResult.getSide());
            Box blockBox = new Box(placePos);
            Box playerBox = player.method_5829();
            return (ActionResult)(playerBox.intersects(blockBox) ? ActionResult.FAIL : ActionResult.PASS);
         }
      });
      ClientTickEvents.START_CLIENT_TICK.register((StartTick)client -> {
         if (CartcoreCore.cartConfig.autoWebbing) {
            if (client.world != null) {
               if (client.player.method_6047().getItem() == Items.COBWEB) {
                  Key rightClickKey = Type.MOUSE.createFromCode(1);
                  if (cobWebBool && client.targetedEntity == null && CartcoreCore.getTargetedBlockPos().method_10264() < client.player.method_23318()) {
                     KeyBinding.onKeyPressed(rightClickKey);
                     KeyBinding.onKeyPressed(rightClickKey);
                     CartcoreCore.targetPitch = (int)client.player.method_36455();
                     CartcoreCore.pitchSpeed = 5.0F;
                     CartcoreCore.playSoundSlow(SoundEvents.ENTITY_EVOKER_PREPARE_ATTACK, client, 0.5F);
                     cobWebBool = false;
                  }

                  if (client.options.useKey.wasPressed() && client.player.method_6047().getItem() == Items.COBWEB && client.targetedEntity != null) {
                     CartcoreCore.targetPitch = 90;
                     CartcoreCore.pitchSpeed = 20.0F;
                     cobWebBool = true;
                     pitchMemoryWeb = (int)client.player.method_36455();
                     CartcoreCore.playSoundSlow(SoundEvents.BLOCK_CONDUIT_ACTIVATE, client, 2.0F);
                     CartcoreCore.sendLog("Attempted to auto cobweb your target since you right clicked them with cobwebs.", client.player);
                  }
               }
            }
         }
      });
   }
}
