package thetruetrident.cartcore;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface cartAssistMixin {
   void setLastPitch(float var1);

   float getLastPitch();
}
