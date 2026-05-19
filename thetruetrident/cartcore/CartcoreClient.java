package thetruetrident.cartcore;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.StartTick;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil.Key;
import net.minecraft.client.util.InputUtil.Type;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

@Environment(EnvType.CLIENT)
public class CartcoreClient implements ClientModInitializer {
   public static int rightClickRail = -1;
   public static boolean shotBow = false;
   public static boolean flopperFish = true;
   public static int useStartTicker = -1;
   public static boolean msgSent = false;
   public static boolean useStartBoolean = false;
   public static boolean underBoolean = false;
   private static boolean placedAboveBoolean;

   public void onInitializeClient() {
      new keyBinding();
      ClientTickEvents.START_CLIENT_TICK
         .register(
            (StartTick)client -> {
               if (client.world != null && !flopperFish && !msgSent) {
                  CartcoreCore.sendLog(
                     "§cYou are using the free version of cartcore§7. This version has the limitation of not being able to disable logs or visual effects, less optimized crossbow carting, and is vulnerable to servers that specifically target the free version of cartcore or use my anti-cheat. You can bypass this by getting §r cartcore premium §r§7for just $3. Those who get it from my patreon also get access to in-dev & advanced features entirely free. Get it on my Patreon at https://www.patreon.com/c/thetruetrident.",
                     client.player
                  );
                  msgSent = true;
               }

               if (CartcoreCore.cartConfig.instaCarting) {
                  rightClickRail--;
                  if (rightClickRail > 0 && client.targetedEntity == null) {
                     Key rightClickKey = Type.MOUSE.createFromCode(1);
                     KeyBinding.onKeyPressed(rightClickKey);
                     KeyBinding.setKeyPressed(rightClickKey, false);
                     if (rightClickRail < 4) {
                        if (!CartcoreCore.swapSlot(client.player, "tnt_minecart")) {
                           if (client.player.method_6079().getItem() == Items.TNT_MINECART) {
                              CartcoreCore.sendLog("attempted an offhand cart by swapping to sword to force cart placement on rail", client.player);
                              CartcoreCore.swapSlot(client.player, "_sword");
                              if (!CartcoreCore.cartConfig.cartOverloading) {
                                 rightClickRail = 1;
                              }
                           }
                        } else if (!CartcoreCore.cartConfig.cartOverloading) {
                           rightClickRail = 1;
                        }
                     }
                  }
               }
            }
         );
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)client -> {
         if (CartcoreCore.targetPitch != null && CartcoreCore.targetPitch > 90) {
            CartcoreCore.targetPitch = 90;
         }

         if (CartcoreCore.cartConfig.instaCarting) {
            if (client.world != null) {
               if (client.player.method_36455() < 12.0F) {
                  shotBow = false;
               } else {
                  double floaterFace = CartcoreCore.getTargetedBlockPos().method_19770(client.player.method_33571());
                  if (!CartcoreCore.checkIfInHotbar(client.player, "rail")) {
                     shotBow = false;
                  } else if (!CartcoreCore.checkIfInHotbar(client.player, "tnt_minecart") && client.player.method_6079().getItem() != Items.TNT_MINECART) {
                     shotBow = false;
                  } else {
                     if (useStartBoolean) {
                        useStartTicker++;
                     }

                     int tempTicker = 6;
                     if (!CartcoreCore.checkIfPlaceAbove(client)) {
                        tempTicker = 4;
                        if (underBoolean) {
                           tempTicker = 2;
                        }
                     }

                     if (client.player.method_36455() > 27.0F) {
                        if (useStartTicker >= 3) {
                           Key rightClickKey = Type.MOUSE.createFromCode(1);
                           KeyBinding.setKeyPressed(rightClickKey, false);
                           useStartBoolean = false;
                           useStartTicker = 0;
                           CartcoreCore.playSoundSlow((SoundEvent)SoundEvents.ITEM_TRIDENT_THROW.value(), client, 2.0F);
                           CartcoreCore.sendLog("Automatically releasing bow to prevent cart overcharge. Earlier since up close.", client.player);
                        }
                     } else if (useStartTicker == tempTicker) {
                        Key rightClickKey = Type.MOUSE.createFromCode(1);
                        KeyBinding.setKeyPressed(rightClickKey, false);
                        useStartBoolean = false;
                        useStartTicker = 0;
                        CartcoreCore.playSoundSlow((SoundEvent)SoundEvents.ITEM_TRIDENT_THROW.value(), client, 2.0F);
                        CartcoreCore.sendLog("Automatically releasing bow to prevent cart overcharge.", client.player);
                     }

                     if (client.player.method_6047().getItem() == Items.BOW) {
                        if (client.options.useKey.isPressed()) {
                           if (!useStartBoolean) {
                              useStartBoolean = true;
                              useStartTicker = 0;
                              CartcoreCore.playSoundSlow(SoundEvents.BLOCK_BUBBLE_COLUMN_WHIRLPOOL_INSIDE, client, 0.5F);
                              CartcoreCore.sendLog("Charging up a bow with the intent of carting (pitch is above 10)", client.player);
                              placedAboveBoolean = false;
                              if (CartcoreCore.isBlockInFrontOfFeet(client.player)) {
                                 if (CartcoreCore.checkIfPlaceAbove(client)) {
                                    CartcoreCore.targetPitch = 17;
                                 } else {
                                    if (floaterFace > 9.0) {
                                       CartcoreCore.targetPitch = (int)(client.player.method_36455() - 15.0F);
                                    } else if (floaterFace < 5.0) {
                                       CartcoreCore.targetPitch = (int)(client.player.method_36455() - 10.0F);
                                       CartcoreCore.pitchSpeed = 15.0F;
                                       underBoolean = true;
                                    }

                                    placedAboveBoolean = true;
                                 }
                              }
                           }
                        } else {
                           useStartBoolean = false;
                        }
                     }

                     if (shotBow) {
                        if (CartcoreCore.checkIfInHotbar(client.player, "rail")) {
                           if (!CartcoreCore.checkIfInHotbar(client.player, "tnt_minecart") && client.player.method_6079().getItem() != Items.TNT_MINECART) {
                              shotBow = false;
                              return;
                           }

                           rightClickRail = 5;
                           if (placedAboveBoolean) {
                              CartcoreCore.targetPitch = (int)(client.player.method_36455() + 15.0F);
                              CartcoreCore.pitchSpeed = 20.0F;
                              placedAboveBoolean = false;
                              if (floaterFace > 9.0) {
                                 rightClickRail = 6;
                              }

                              if (underBoolean) {
                                 CartcoreCore.targetPitch = (int)(client.player.method_36455() + 10.0F);
                                 CartcoreCore.pitchSpeed = 10.0F;
                                 underBoolean = false;
                              }
                           } else if (!CartcoreCore.isBlockInFrontOfFeet(client.player)) {
                              CartcoreCore.targetPitch = (int)(client.player.method_36455() + 10.0F);
                              CartcoreCore.pitchSpeed = 10.0F;
                           } else if (CartcoreCore.checkIfPlaceAbove(client)) {
                              CartcoreCore.targetPitch = (int)(client.player.method_36455() + 5.0F);
                           }

                           CartcoreCore.swapSlot(client.player, "rail");
                           shotBow = false;
                        } else {
                           shotBow = false;
                        }
                     }
                  }
               }
            }
         }
      });
   }
}
