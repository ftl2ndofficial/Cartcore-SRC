package thetruetrident.cartcore;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.KeyBinding.Category;
import net.minecraft.client.util.InputUtil.Type;

@Environment(EnvType.CLIENT)
public class keyBinding {
   public static final KeyBinding CROSSCART = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.cartcore.crossbowcart", Type.MOUSE, 72, Category.MISC));

   public static KeyBinding toggleCrystalCore() {
      return CROSSCART;
   }
}
