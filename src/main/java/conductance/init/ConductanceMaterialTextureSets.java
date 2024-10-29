package conductance.init;

import net.minecraft.Util;
import conductance.api.CAPI;
import conductance.api.material.MaterialTextureSet;
import conductance.api.plugin.MaterialTextureSetRegister;
import static conductance.api.NCTextureSets.AMETHYST;
import static conductance.api.NCTextureSets.BRIGHT;
import static conductance.api.NCTextureSets.DIAMOND;
import static conductance.api.NCTextureSets.DULL;
import static conductance.api.NCTextureSets.EMERALD;
import static conductance.api.NCTextureSets.FINE;
import static conductance.api.NCTextureSets.FLINT;
import static conductance.api.NCTextureSets.GEM_HORIZONTAL;
import static conductance.api.NCTextureSets.LAPIS;
import static conductance.api.NCTextureSets.LIGNITE;
import static conductance.api.NCTextureSets.MAGNETIC;
import static conductance.api.NCTextureSets.METALLIC;
import static conductance.api.NCTextureSets.NETHER_STAR;
import static conductance.api.NCTextureSets.QUARTZ;
import static conductance.api.NCTextureSets.ROUGH;
import static conductance.api.NCTextureSets.SAND;
import static conductance.api.NCTextureSets.SHINY;
import static conductance.api.NCTextureSets.WOOD;

public final class ConductanceMaterialTextureSets {

	public static void init(final MaterialTextureSetRegister register) {
		DULL = Util.make(new MaterialTextureSet("dull", null), set -> CAPI.regs().materialTextureSets().register(set.getRegistryKey(), set));
		METALLIC = register.register("metallic");
		MAGNETIC = register.register("magnetic", METALLIC);
		SHINY = register.register("shiny", METALLIC);
		BRIGHT = register.register("bright", SHINY);

		DIAMOND = register.register("diamond", SHINY);
		EMERALD = register.register("emerald", DIAMOND);
		GEM_HORIZONTAL = register.register("gem_horizontal", EMERALD);
		AMETHYST = register.register("ruby", EMERALD);
		NETHER_STAR = register.register("nether_star", AMETHYST);

		FINE = register.register("fine");
		WOOD = register.register("wood", FINE);
		SAND = register.register("sand", FINE);
		ROUGH = register.register("rough", FINE);
		QUARTZ = register.register("quartz", ROUGH);
		LAPIS = register.register("lapis", QUARTZ);
		FLINT = register.register("flint", ROUGH);
		LIGNITE = register.register("lignite", ROUGH);
	}

	private ConductanceMaterialTextureSets() {
	}
}
