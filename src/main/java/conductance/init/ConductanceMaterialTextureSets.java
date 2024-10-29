package conductance.init;

import net.minecraft.Util;
import conductance.api.CAPI;
import conductance.api.NCTextureSets;
import conductance.api.material.MaterialTextureSet;
import conductance.api.plugin.MaterialTextureSetRegister;

public final class ConductanceMaterialTextureSets {

	public static void init(MaterialTextureSetRegister register) {
		NCTextureSets.DULL = Util.make(new MaterialTextureSet("dull", null), set -> CAPI.REGS.materialTextureSets().register(set.getRegistryKey(), set));
	}
}
