package conductance.compat.jei;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.NCRecipeElementTypes;
import conductance.api.machine.gui.GuiTheme;
import conductance.api.machine.gui.IGuiWidget;
import conductance.api.machine.gui.WidgetGroup;
import conductance.api.recipe.MachineRecipe;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.recipe.RecipeElement;
import conductance.api.util.IO;
import conductance.api.util.TextHelper;
import conductance.init.item.ProgramCircuitItem;
import conductance.init.machine.GenericRecipeMachineGuiSetup;

final class GenericRecipeMachineCategory implements IRecipeCategory<MachineRecipe> {

	private static final int BASE_LINE_COUNT = 2;
	static final Function<MachineRecipeType, IRecipeType<MachineRecipe>> RECIPE_TYPES = Util.memoize(machineRecipeType ->
		new IRecipeType.JeiRecipeType<>(machineRecipeType.getId(), MachineRecipe.class)
	);
	private final MachineRecipeType recipeType;
	private final IRecipeType<MachineRecipe> jeiRecipeType;
	private final WidgetGroup rootGroup;
	private final int width;
	private final int height;

	GenericRecipeMachineCategory(final MachineRecipeType recipeType, final IRecipeType<MachineRecipe> jeiRecipeType, final GuiTheme theme) {
		this.recipeType = recipeType;
		this.jeiRecipeType = jeiRecipeType;
		this.rootGroup = GenericRecipeMachineGuiSetup.makeDummyRootGroup(
			theme,
			this.recipeType.getLimit(IO.IN, NCRecipeElementTypes.ITEM), this.recipeType.getLimit(IO.OUT, NCRecipeElementTypes.ITEM),
			this.recipeType.getLimit(IO.IN, NCRecipeElementTypes.FLUID), this.recipeType.getLimit(IO.OUT, NCRecipeElementTypes.FLUID),
			this.recipeType, new JeiProgressProvider()
		);
		this.width = Math.max(140, this.rootGroup.getWidth() + 40);
		int lineCount = GenericRecipeMachineCategory.BASE_LINE_COUNT;
		if (recipeType.getLimit(IO.IN, NCRecipeElementTypes.ITEM) > 0 || recipeType.getLimit(IO.OUT, NCRecipeElementTypes.ITEM) > 0) {
			++lineCount;
		}
		this.height = this.rootGroup.getHeight() + 3 + (lineCount * 10);
	}

	@Override
	public void setRecipe(final IRecipeLayoutBuilder builder, final MachineRecipe recipe, final IFocusGroup focuses) {
		final int xOffset = (this.width - this.rootGroup.getWidth()) / 2;
		if (recipe.getProgram() >= 0) {
			builder.addSlot(RecipeIngredientRole.RENDER_ONLY, xOffset - 18, 1)
				.addItemStacks(List.of(ProgramCircuitItem.makeStack(recipe.getProgram())))
				.addRichTooltipCallback((recipeSlotView, tooltip) -> {
					tooltip.clear();
					tooltip.add(Component.translatable("info.conductance.jei.requires_program_circuit", recipe.getProgram()));
				});
		}
		final SimulatedRecipeCapabilityHolder holder = new SimulatedRecipeCapabilityHolder(recipe);
		final Map<String, IGuiWidget> allWidgets = this.rootGroup.getWidgetsFlattened();
		for (final Map.Entry<String, IGuiWidget> entry : allWidgets.entrySet()) {
			final String key = entry.getKey();
			if (key.startsWith("items_")) {
				final IO io = key.startsWith("items_in_") ? IO.IN : key.startsWith("items_out_") ? IO.OUT : null;
				if (io == null) {
					continue;
				}
				final List<Tuple<List<ItemStack>, RecipeElement>> mapping = io == IO.IN ? holder.getInputItems() : holder.getOutputItems();
				GenericRecipeMachineCategory.makeRecipeSlotEntry(builder, xOffset, 0, entry.getValue(), key, io, mapping, false,
					(slotBuilder, itemStacks) -> slotBuilder.addIngredients(VanillaTypes.ITEM_STACK, itemStacks),
					null);
				final List<Tuple<List<ItemStack>, RecipeElement>> perTickMapping = io == IO.IN ? holder.getPerTickInputItems() : holder.getPerTickOutputItems();
				GenericRecipeMachineCategory.makeRecipeSlotEntry(builder, xOffset, mapping.size(), entry.getValue(), key, io, perTickMapping, true,
					(slotBuilder, itemStacks) -> slotBuilder.addIngredients(VanillaTypes.ITEM_STACK, itemStacks),
					null);
			} else if (key.startsWith("fluids_")) {
				final IO io = key.startsWith("fluids_in_") ? IO.IN : key.startsWith("fluids_out_") ? IO.OUT : null;
				if (io == null) {
					continue;
				}
				final List<Tuple<List<FluidStack>, RecipeElement>> mapping = io == IO.IN ? holder.getInputFluids() : holder.getOutputFluids();
				GenericRecipeMachineCategory.makeRecipeSlotEntry(builder, xOffset, 0, entry.getValue(), key, io, mapping, false,
					(slotBuilder, fluidStacks) -> slotBuilder.addIngredients(NeoForgeTypes.FLUID_STACK, fluidStacks).setFluidRenderer(1, false, 16, 16),
					fluidStacks -> !fluidStacks.isEmpty() ? Component.literal(TextHelper.getFormattedFluidAmount(fluidStacks.getFirst().getAmount())) : null
				);
				final List<Tuple<List<FluidStack>, RecipeElement>> perTickMapping = io == IO.IN ? holder.getPerTickInputFluids() : holder.getPerTickOutputFluids();
				GenericRecipeMachineCategory.makeRecipeSlotEntry(builder, xOffset, mapping.size(), entry.getValue(), key, io, perTickMapping, true,
					(slotBuilder, fluidStacks) -> slotBuilder.addIngredients(NeoForgeTypes.FLUID_STACK, fluidStacks).setFluidRenderer(1, false, 16, 16),
					fluidStacks -> !fluidStacks.isEmpty() ? Component.literal(TextHelper.getFormattedFluidAmount(fluidStacks.getFirst().getAmount())) : null
				);
			}
		}
	}

	private static <T> void makeRecipeSlotEntry(
		final IRecipeLayoutBuilder builder, final int xOffset, final int slotOffset, final IGuiWidget widget, final String widgetKey, final IO io, final List<Tuple<List<T>, RecipeElement>> mapping,
		final boolean perTick, final BiConsumer<IRecipeSlotBuilder, List<T>> ingredientSetter, @Nullable final Function<List<T>, @Nullable Component> bottomText
	) {
		final int slotIndex = Integer.parseInt(widgetKey.substring(widgetKey.lastIndexOf('_') + 1)) - slotOffset;
		if (slotIndex < 0 || slotIndex >= mapping.size()) {
			return;
		}
		final Tuple<List<T>, RecipeElement> data = mapping.get(slotIndex);
		final IRecipeSlotBuilder slotBuilder = switch (io) {
			case IN -> builder.addInputSlot(xOffset + widget.getX() + 1, widget.getY() + 1);
			case OUT -> builder.addOutputSlot(xOffset + widget.getX() + 1, widget.getY() + 1);
		};
		ingredientSetter.accept(slotBuilder, data.getA());
		slotBuilder.addRichTooltipCallback((recipeSlotView, tooltip) -> {
			if (perTick) {
				tooltip.add(Component.translatable("info.conductance.jei.%s.per_tick".formatted(io)));
			}
			if (data.getB().chance() == 0) {
				tooltip.add(Component.translatable("info.conductance.jei.%s.chance_0".formatted(io)));
			} else if (data.getB().chance() != 1.0) {
				tooltip.add(Component.translatable("info.conductance.jei.%s.chance".formatted(io), data.getB().chance() * 100));
			}
		});
		slotBuilder.setOverlay(new SlotTextOverlay(perTick ? switch (io) {
			case IN -> SlotTextOverlay.IN_PER_TICK;
			case OUT -> SlotTextOverlay.OUT_PER_TICK;
		} : null, switch (io) {
			case IN -> data.getB().chance() == 0 ? SlotTextOverlay.IN_CHANCE_0 : data.getB().chance() < 1 ? SlotTextOverlay.IN_CHANCE : null;
			case OUT -> data.getB().chance() == 0 ? SlotTextOverlay.OUT_CHANCE_0 : data.getB().chance() < 1 ? SlotTextOverlay.OUT_CHANCE : null;
		}, bottomText != null ? bottomText.apply(data.getA()) : null), 0, 0);
	}

	@Override
	public void draw(final MachineRecipe recipe, final IRecipeSlotsView recipeSlotsView, final GuiGraphics guiGraphics, final double mouseX, final double mouseY) {
		final int xOffset = (this.width - this.rootGroup.getWidth()) / 2;
		guiGraphics.pose().pushMatrix();
		guiGraphics.pose().translate(xOffset, 0);
		Optional.ofNullable(this.rootGroup.getWidgetById("in")).ifPresent(inputGroup -> {
			inputGroup.renderBackground(guiGraphics, (int) mouseX, (int) mouseY, 0);
		});
		Optional.ofNullable(this.rootGroup.getWidgetById("progress")).ifPresent(progress -> {
			progress.renderBackground(guiGraphics, (int) mouseX, (int) mouseY, 0);
			progress.renderForeground(guiGraphics, (int) mouseX, (int) mouseY, 0);
		});
		Optional.ofNullable(this.rootGroup.getWidgetById("out")).ifPresent(outputGroup -> {
			outputGroup.renderBackground(guiGraphics, (int) mouseX, (int) mouseY, 0);
		});
		guiGraphics.pose().popMatrix();
	}

	@SuppressWarnings("CheckStyle")
	@Override
	public void createRecipeExtras(final IRecipeExtrasBuilder builder, final MachineRecipe recipe, final IFocusGroup focuses) {
		int y = this.rootGroup.getHeight() + 3;
		builder.addText(Component.translatable("info.conductance.jei.duration", TextHelper.getFormattedRecipeDuration(recipe.getRecipeDuration())), this.getWidth(), 10).setPosition(0, y);
		if (recipe.getPerTickInputs().containsKey(NCRecipeElementTypes.ENERGY)) {
			final long energyPerTick = recipe.getPerTickInputs().get(NCRecipeElementTypes.ENERGY).stream().mapToLong(element -> (long) element.data()).sum();
			builder.addText(Component.translatable("info.conductance.jei.energy", TextHelper.getFormattedEnergy(energyPerTick), CAPI.tiers().getByVoltage(energyPerTick).getName()), this.getWidth(), 10)
				.setPosition(0, y += 10);
		} else if (recipe.getPerTickOutputs().containsKey(NCRecipeElementTypes.ENERGY)) {
			final long energyPerTick = recipe.getPerTickOutputs().get(NCRecipeElementTypes.ENERGY).stream().mapToLong(element -> (long) element.data()).sum();
			builder.addText(Component.translatable("info.conductance.jei.energy", TextHelper.getFormattedEnergy(energyPerTick), CAPI.tiers().getByVoltage(energyPerTick).getName()), this.getWidth(), 10)
				.setPosition(0, y += 10);
		}
	}

	@Override
	public IRecipeType<MachineRecipe> getRecipeType() {
		return this.jeiRecipeType;
	}

	@Override
	public Component getTitle() {
		return this.recipeType.getName();
	}

	@Override
	public @Nullable IDrawable getIcon() {
		return null;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}
}
