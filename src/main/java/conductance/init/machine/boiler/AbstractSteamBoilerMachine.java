package conductance.init.machine.boiler;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.NCMaterialGenerationHandlers;
import conductance.api.NCMaterials;
import conductance.api.NCRecipeElementTypes;
import conductance.api.machine.CapIO;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineFluidHandler;
import conductance.api.machine.MachineRecipeCapability;
import conductance.api.machine.MachineRecipeCapabilityFluids;
import conductance.api.machine.MachineType;
import conductance.api.machine.event.MachineRecipeModifier;
import conductance.api.recipe.RecipeElementType;
import conductance.api.util.IO;

public abstract class AbstractSteamBoilerMachine<T extends AbstractSteamBoilerMachine<T>> extends MachineBlockEntity<T> implements BoilerFakeRecipeCapabilityHolder {

	private final @Getter MachineRecipeCapabilityFluids waterTank;
	private final @Getter MachineRecipeCapabilityFluids steamTank;
	private final @Getter BoilerFakeRecipeHandler boilerHandler;

	public AbstractSteamBoilerMachine(final MachineType<T> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
		this.boilerHandler = new BoilerFakeRecipeHandler(this, this);
		this.waterTank = new MachineRecipeCapabilityFluids(this, 1, IO.IN, CapIO.IN, tanks -> new MachineFluidHandler(tanks, CAPI.BUCKET * 4));
		this.waterTank.getHandler().setFilter((tank, stack) -> stack.is(CAPI.materials().getFluidTag(NCMaterials.WATER, NCMaterialGenerationHandlers.LIQUID)));
		this.waterTank.addChangedListener(this::setChanged);
		this.waterTank.addChangedListener(this.getBoilerHandler()::revalidateTick);
		this.steamTank = new MachineRecipeCapabilityFluids(this, 1, IO.OUT, CapIO.OUT, tanks -> new MachineFluidHandler(tanks, CAPI.BUCKET * 4));
		this.steamTank.getHandler().setFilter((tank, stack) -> stack.is(CAPI.materials().getFluidTag(NCMaterials.STEAM, NCMaterialGenerationHandlers.GAS)));
		this.steamTank.addChangedListener(this::setChanged);
		this.steamTank.addChangedListener(this.getBoilerHandler()::revalidateTick);
	}

	@Override
	public List<MachineRecipeCapability<?>> getRecipeCapabilities(final RecipeElementType<?> elementType, final IO io) {
		if (elementType == NCRecipeElementTypes.FLUID) {
			return io == IO.IN ? List.of(this.waterTank) : List.of(this.steamTank);
		}
		return List.of();
	}

	@Override
	public @Nullable MachineRecipeModifier getRecipeModifier() {
		return this.getMachineType().getRecipeModifier();
	}
}
