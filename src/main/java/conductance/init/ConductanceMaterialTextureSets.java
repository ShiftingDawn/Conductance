package conductance.init;

import net.minecraft.Util;
import conductance.api.CAPI;
import conductance.api.material.MaterialTextureSet;
import conductance.api.plugin.MaterialTextureSetRegister;
import static conductance.api.NCTextureSets.BRIGHT;
import static conductance.api.NCTextureSets.DULL;
import static conductance.api.NCTextureSets.METALLIC;
import static conductance.api.NCTextureSets.SHINY;

public final class ConductanceMaterialTextureSets {

	public static void init(MaterialTextureSetRegister register) {
		DULL = Util.make(new MaterialTextureSet("dull", null), set -> CAPI.REGS.materialTextureSets().register(set.getRegistryKey(), set));
		METALLIC = register.register("metallic");
		SHINY = register.register("shiny", METALLIC);
		BRIGHT = register.register("bright", SHINY);
	}
}
