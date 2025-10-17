package conductance.init.machine.boiler;

import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.Nullable;
import conductance.api.NCRecipeElementTypes;
import conductance.api.machine.MachineRecipeCapability;
import conductance.api.machine.multi.IMultiBlockPart;
import conductance.api.machine.multi.MultiControllerMachineBlockEntity;
import conductance.api.machine.multi.MultiMachineType;
import conductance.api.machine.multi.StructureCheckContext;
import conductance.api.recipe.MachineRecipeModifier;
import conductance.api.recipe.RecipeElementType;
import conductance.api.util.IO;

public class LargeBoilerMachine extends MultiControllerMachineBlockEntity<LargeBoilerMachine> implements BoilerFakeRecipeCapabilityHolder {

	private @Nullable MachineRecipeCapability<SizedIngredient> inputItems;
	private @Nullable MachineRecipeCapability<SizedFluidIngredient> inputFluids;
	private @Nullable MachineRecipeCapability<SizedFluidIngredient> outputFluids;
	private final BoilerFakeRecipeHandler recipeHandler;

	public LargeBoilerMachine(final MultiMachineType<LargeBoilerMachine> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
		this.recipeHandler = new BoilerFakeRecipeHandler(this, this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onStructureFormed(final StructureCheckContext ctx) {
		super.onStructureFormed(ctx);
		for (final IMultiBlockPart part : this.getParts()) {
			part.attachCapabilities((io, capability) -> {
				if (io == IO.IN && capability.getElementType() == NCRecipeElementTypes.ITEM) {
					this.inputItems = (MachineRecipeCapability<SizedIngredient>) capability;
				} else if (io == IO.IN && capability.getElementType() == NCRecipeElementTypes.FLUID) {
					this.inputFluids = (MachineRecipeCapability<SizedFluidIngredient>) capability;
				} else if (io == IO.OUT && capability.getElementType() == NCRecipeElementTypes.FLUID) {
					this.outputFluids = (MachineRecipeCapability<SizedFluidIngredient>) capability;
				}
			});
		}
		this.recipeHandler.revalidateTick();
	}

	@Override
	public @Nullable MachineRecipeCapability<SizedFluidIngredient> getWaterTank() {
		return this.inputFluids;
	}

	@Override
	public @Nullable MachineRecipeCapability<SizedFluidIngredient> getSteamTank() {
		return this.outputFluids;
	}

	private ItemStack getFirstItem() {
		if (this.inputItems != null) {
			assert this.level != null;
			for (final SizedIngredient sizedIngredient : this.inputItems.getAvailableContent()) {
				for (final Holder<Item> itemHolder : sizedIngredient.ingredient().getValues()) {
					final ItemStack stack = itemHolder.value().getDefaultInstance();
					if (!stack.isEmpty() && stack.getBurnTime(RecipeType.SMELTING, this.level.fuelValues()) > 0) {
						return stack;
					}
				}
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public int getFuelForInput() {
		assert this.level != null;
		final ItemStack stack = this.getFirstItem();
		return !stack.isEmpty() ? stack.getBurnTime(RecipeType.SMELTING, this.level.fuelValues()) : 0;
	}

	@Override
	public void addInputs(final BiConsumer<RecipeElementType<?>, Object> consumer) {
		final ItemStack stack = this.getFirstItem();
		consumer.accept(NCRecipeElementTypes.ITEM, SizedIngredient.of(stack.getItem(), 1));
	}

	@Override
	public @Nullable MachineRecipeModifier getRecipeModifier() {
		return this.getMachineType().getRecipeModifier();
	}

	@Override
	public List<MachineRecipeCapability<?>> getRecipeCapabilities(final RecipeElementType<?> elementType, final IO io) {
		if (elementType == NCRecipeElementTypes.ITEM && io == IO.IN) {
			return this.inputItems != null ? List.of(this.inputItems) : List.of();
		}
		if (elementType == NCRecipeElementTypes.FLUID) {
			return switch (io) {
				case IN -> this.getWaterTank() != null ? List.of(this.getWaterTank()) : List.of();
				case OUT -> this.getSteamTank() != null ? List.of(this.getSteamTank()) : List.of();
			};
		}
		return List.of();
	}
}
