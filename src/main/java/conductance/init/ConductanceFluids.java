package conductance.init;

import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.FluidBuilder;
import conductance.api.CAPI;
import conductance.content.fluid.MaterialFluidType;
import conductance.core.apiimpl.ApiBridge;
import conductance.core.apiimpl.MaterialTaggedSet;

public final class ConductanceFluids {

	public static void init() {
		CAPI.REGS.materials().forEach(material -> CAPI.REGS.materialTaggedSets().values().stream().filter(set -> set.canGenerateFluid(material)).forEach(set -> {
			final String name = set.getUnlocalizedName(material);
			ApiBridge.REGISTRATE.object(name);
			final FluidBuilder<BaseFlowingFluid.Flowing, Registrate> fluidBuilder = ApiBridge.REGISTRATE.fluid(name, ((properties, stillTexture, flowingTexture) -> new MaterialFluidType(material, set, properties))).noBlock();
			if (((MaterialTaggedSet) set).getFluidGeneratorCallback() != null) {
				((MaterialTaggedSet) set).getFluidGeneratorCallback().accept(material, fluidBuilder);
			}
			CAPI.MATERIALS.register(set, material, fluidBuilder.register());
		}));
	}
}
