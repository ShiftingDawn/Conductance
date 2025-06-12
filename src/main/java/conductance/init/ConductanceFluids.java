package conductance.init;

import net.minecraft.world.item.Items;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import conductance.api.CAPI;
import conductance.core.material.MaterialRegistryImpl;
import conductance.core.material.TaggedMaterialSetImpl;
import conductance.init.fluid.MaterialBucketItem;
import conductance.init.fluid.MaterialFluidType;
import static conductance.core.register.RegisterCore.REGISTRATE;

public final class ConductanceFluids {

	@SuppressWarnings("UnstableApiUsage")
	public static void init() {
		CAPI.regs().materials().forEach(material -> CAPI.regs().materialTaggedSets().values().stream().filter(set -> set.canGenerateFluid(material)).forEach(set -> {
			final String name = set.getUnlocalizedName(material);
			REGISTRATE.object(name);
			//noinspection DataFlowIssue
			final FluidBuilder<BaseFlowingFluid.Flowing, Registrate> fluidBuilder = REGISTRATE.fluid(name, ((properties, stillTexture, flowingTexture) -> new MaterialFluidType(material, set, properties)))
					.noBlock().fluidProperties(p -> p.block(null))
					.noBucket();
			fluidBuilder.getOwner().item(fluidBuilder, name + "_bucket", p -> new MaterialBucketItem(fluidBuilder.getEntry(), p)).properties(p -> p.craftRemainder(Items.BUCKET).stacksTo(1))
					.color(() -> MaterialBucketItem::handleColorTint).model(NonNullBiConsumer.noop()).build();
			if (((TaggedMaterialSetImpl) set).getFluidGeneratorCallback() != null) {
				((TaggedMaterialSetImpl) set).getFluidGeneratorCallback().accept(material, fluidBuilder);
			}
			MaterialRegistryImpl.INSTANCE.register(set, material, fluidBuilder.register());
		}));
	}

	private ConductanceFluids() {
	}
}
