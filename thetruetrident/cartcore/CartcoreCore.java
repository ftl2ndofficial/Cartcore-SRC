package thetruetrident.cartcore;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

@Environment(EnvType.CLIENT)
public class CartcoreCore {
   public static cartConfig cartConfig = (cartConfig)ConfigApiJava.registerAndLoadConfig(cartConfig::new, RegisterType.CLIENT);
   public static Integer targetPitch = null;
   public static float pitchSpeed = 5.0F;

   public static BlockPos getTargetedBlockPos() {
      MinecraftClient client = MinecraftClient.getInstance();
      ClientPlayerEntity player = client.player;
      if (player == null) {
         return null;
      }

      double reach = player.method_55754();
      Vec3d eyePos = player.method_5836(1.0F);
      Vec3d lookVec = player.method_5828(1.0F);
      Vec3d targetPos = eyePos.add(lookVec.multiply(reach));
      BlockHitResult hit = client.world.method_17742(new RaycastContext(eyePos, targetPos, ShapeType.OUTLINE, FluidHandling.NONE, player));
      if (hit == null) {
         return null;
      }

      BlockState state = client.world.method_8320(hit.getBlockPos());
      Identifier blockId = Registries.BLOCK.method_10221(state.method_26204());
      NbtCompound nbt = new NbtCompound();
      nbt.putString("BlockID", blockId.toString());
      state.method_11656().forEach((prop, val) -> nbt.putString(prop.getName(), val.toString()));
      return hit.getBlockPos();
   }

   public static boolean isBlockInFrontOfFeet(PlayerEntity player) {
      BlockPos frontPos = player.method_24515().offset(player.method_5735());
      return !player.method_73183().method_8320(frontPos).method_26215();
   }

   public static boolean checkIfCrossbowLoadedInHotbar(PlayerEntity player) {
      int bresh = 0;

      for (ItemStack itemStack : player.getInventory()) {
         if (String.valueOf(itemStack.getItem()).contains("crossbow") && itemStack.getItem() instanceof CrossbowItem) {
            return CrossbowItem.isCharged(itemStack);
         }

         if (++bresh > 8) {
            break;
         }
      }

      return false;
   }

   public static boolean checkIfInHotbar(PlayerEntity player, String itemname) {
      int bresh = 0;

      for (ItemStack itemStack : player.getInventory()) {
         if (String.valueOf(itemStack.getItem()).contains(itemname)) {
            return true;
         }

         if (++bresh > 8) {
            break;
         }
      }

      return MinecraftClient.getInstance().player.method_6079().getItem() == Items.TNT_MINECART && itemname == "tnt_minecart";
   }

   public static void sendLog(String text, PlayerEntity player) {
      MinecraftClient client = MinecraftClient.getInstance();
      if (!CartcoreClient.flopperFish) {
         player.sendMessage(Text.literal(" \ue006§7 " + text + "§8 chat logs can only be disabled with Cartcore Premium (Patreon-exclusive)"), false);
         playSoundSlow(SoundEvents.UI_HUD_BUBBLE_POP, client, 2.0F);
      } else if (cartConfig.chatLogs) {
         player.sendMessage(Text.literal("\ue006§7 " + text), false);
         playSoundSlow(SoundEvents.UI_HUD_BUBBLE_POP, client, 2.0F);
      }
   }

   public static boolean swapSlot(PlayerEntity player, String itemname) {
      int bresh = 0;

      for (ItemStack itemStack : player.getInventory()) {
         if (String.valueOf(itemStack.getItem()).contains(itemname)) {
            try {
               MinecraftClient.getInstance().player.method_31548().setSelectedSlot(bresh);
            } catch (Throwable var6) {
            }

            return true;
         }

         if (++bresh > 8) {
            break;
         }
      }

      return false;
   }

   public static void playSoundSlow(SoundEvent sound, MinecraftClient client, float Pitch) {
      if (CartcoreClient.flopperFish) {
         if (cartConfig.soundEffects) {
            client.getSoundManager().play(PositionedSoundInstance.master(sound, 0.0F, Pitch));
         }
      } else {
         client.getSoundManager().play(PositionedSoundInstance.master(sound, 0.0F, Pitch));
      }
   }

   public static boolean checkIfPlaceAbove(MinecraftClient client) {
      if (client.crosshairTarget instanceof BlockHitResult hitResult && client.player != null) {
         BlockPos placePos = hitResult.getBlockPos().offset(hitResult.getSide());
         if (placePos.method_10264() <= client.player.method_31478()) {
            return true;
         }
      }

      return false;
   }
}
