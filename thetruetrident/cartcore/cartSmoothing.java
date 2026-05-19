package thetruetrident.cartcore;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.StartTick;

@Environment(EnvType.CLIENT)
public class cartSmoothing implements ClientModInitializer {
   public void onInitializeClient() {
      ClientTickEvents.START_CLIENT_TICK.register((StartTick)game -> {
         if (game.world != null) {
            cartAssistMixin localPlayer = (cartAssistMixin)game.player;
            if (CartcoreCore.targetPitch != null) {
               float pitch = ((cartAssistMixin)game.player).getLastPitch();
               game.player.method_36457(pitch);
               localPlayer.setLastPitch(game.player.method_36455());
               if (CartcoreCore.targetPitch.intValue() == game.player.method_36455()) {
                  CartcoreCore.targetPitch = null;
               }
            }

            localPlayer.setLastPitch(game.player.method_36455());
         }
      });
   }
}
