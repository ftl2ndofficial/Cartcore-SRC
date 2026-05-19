package thetruetrident.cartcore;

import me.fzzyhmstrs.fzzy_config.annotations.Comment;
import me.fzzyhmstrs.fzzy_config.config.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class cartConfig extends Config {
   @Comment("\ue006§7 Soon to be implemented tick variance for all actions (avoids macro detection)")
   public boolean tickVariance = true;
   @Comment(
      "\ue006§7 When holding a bow, automatically shoot at the right time, and place a rail and tnt minecart (if applicable). Also applies a soft temporary carty aim assist (configurable) to place the minecart and position your field of view at the correct angle."
   )
   public boolean instaCarting = true;
   @Comment(
      "\ue006§7 [PREMIUM EXCLUSIVE] if turned on with instacarting enabled, places more than one tnt minecart (when possible) on an applicable rail. You can only turn this off with cartcore premium"
   )
   public boolean cartOverloading = true;
   @Comment(
      "\ue006§7 When you press a keybind (configurable) while having a fully loaded crossbow in your inventory (or just try to crossbow the ground), automatically place a rail, tnt minecart, then a flint and steel, and then finally shoot (igniting the tnt minecart)"
   )
   public boolean crossbowCarting = true;
   @Comment(
      "\ue006§7 When you hold the right click button while targeting a player, whilst also having a cobweb in your hand, your viewpoint will flick downwards in order to perfectly cobweb them."
   )
   public boolean autoWebbing = true;
   @Comment("\ue006§7 coming soon. Patreon members are always first to get the new features.")
   public boolean cartReplenishing = false;
   @Comment("\ue006§7 cartcore's sound effects. You can only turn this off in the premium version.")
   public boolean soundEffects = true;
   @Comment("\ue006§7 cartcore's chat logs. You can only turn this off in the premium version.")
   public boolean chatLogs = true;

   public cartConfig() {
      super(Identifier.of("cartcore", "cart_config"));
   }
}
