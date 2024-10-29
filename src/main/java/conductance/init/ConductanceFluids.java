package conductance.init;

import net.minecraft.world.item.Items;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import conductance.api.CAPI;
import conductance.client.resourcepack.MaterialFluidModelHandler;
import conductance.content.fluid.MaterialBucketItem;
import conductance.content.fluid.MaterialFluidType;
import conductance.core.apiimpl.ApiBridge;
import conductance.core.apiimpl.MaterialTaggedSet;

public final class ConductanceFluids {

	public static void init() {
		CAPI.regs().materials().forEach(material -> CAPI.regs().materialTaggedSets().values().stream().filter(set -> set.canGenerateFluid(material)).forEach(set -> {
			final String name = set.getUnlocalizedName(material);
			ApiBridge.getRegistrate().object(name);
			final FluidBuilder<BaseFlowingFluid.Flowing, Registrate> fluidBuilder = ApiBridge.getRegistrate().fluid(name, ((properties, stillTexture, flowingTexture) -> new MaterialFluidType(material, set, properties)))
					.noBlock().noBucket();
			fluidBuilder.getOwner().item(fluidBuilder, name + "_bucket", p -> new MaterialBucketItem(fluidBuilder.getEntry(), p)).properties(p -> p.craftRemainder(Items.BUCKET).stacksTo(1))
					.color(() -> MaterialBucketItem::handleColorTint).model(NonNullBiConsumer.noop()).build();
			if (((MaterialTaggedSet) set).getFluidGeneratorCallback() != null) {
				((MaterialTaggedSet) set).getFluidGeneratorCallback().accept(material, fluidBuilder);
			}
			if (CAPI.isClient()) {
				fluidBuilder.onRegister(fluid -> MaterialFluidModelHandler.add(fluid, material, set));
			}
			CAPI.materials().register(set, material, fluidBuilder.register());
		}));
	}

	private ConductanceFluids() {
	}
}
