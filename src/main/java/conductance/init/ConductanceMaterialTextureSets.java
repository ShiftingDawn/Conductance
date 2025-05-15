package conductance.init;

import net.minecraft.Util;
import net.neoforged.bus.api.EventPriority;
import conductance.api.CAPI;
import conductance.api.material.MaterialTextureSet;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.plugin.RegisterMaterialTextureSetEvent;
import conductance.Conductance;
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

@ConductancePluginListener(modid = Conductance.MODID)
public final class ConductanceMaterialTextureSets {

	@EventListener(priority = EventPriority.HIGH)
	private static void init(final RegisterMaterialTextureSetEvent event) {
		DULL = Util.make(new MaterialTextureSet("dull", null), set -> CAPI.regs().materialTextureSets().register(set.getRegistryKey(), set));
		METALLIC = event.register("metallic");
		MAGNETIC = event.register("magnetic", METALLIC);
		SHINY = event.register("shiny", METALLIC);
		BRIGHT = event.register("bright", SHINY);

		DIAMOND = event.register("diamond", SHINY);
		EMERALD = event.register("emerald", DIAMOND);
		GEM_HORIZONTAL = event.register("gem_horizontal", EMERALD);
		AMETHYST = event.register("ruby", EMERALD);
		NETHER_STAR = event.register("nether_star", AMETHYST);

		FINE = event.register("fine");
		WOOD = event.register("wood", FINE);
		SAND = event.register("sand", FINE);
		ROUGH = event.register("rough", FINE);
		QUARTZ = event.register("quartz", ROUGH);
		LAPIS = event.register("lapis", QUARTZ);
		FLINT = event.register("flint", ROUGH);
		LIGNITE = event.register("lignite", ROUGH);
	}

	private ConductanceMaterialTextureSets() {
	}
}
