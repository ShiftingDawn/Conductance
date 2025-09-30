package conductance.compat.jei;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import org.jetbrains.annotations.Nullable;
import conductance.api.NCRecipeElementTypes;
import conductance.api.machine.gui.GuiTheme;
import conductance.api.machine.gui.GuiWidget;
import conductance.api.machine.gui.WidgetGroup;
import conductance.api.recipe.MachineRecipe;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.util.IO;
import conductance.init.item.ProgramCircuitItem;
import conductance.init.machine.GenericRecipeMachineGuiSetup;

final class GenericRecipeMachineCategory implements IRecipeCategory<MachineRecipe> {

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
			this.recipeType, new JeiProgressProvider()
		);
		this.width = Math.max(140, this.rootGroup.getWidth());
		this.height = this.rootGroup.getHeight();
	}

	@Override
	public void setRecipe(final IRecipeLayoutBuilder builder, final MachineRecipe recipe, final IFocusGroup focuses) {
		final int xOffset = (this.width - this.rootGroup.getWidth()) / 2;
		if (recipe.getProgram() >= 0) {
			builder.addSlot(RecipeIngredientRole.RENDER_ONLY, xOffset - 20, this.rootGroup.getHeight() / 2 - 10)
				.addItemStacks(List.of(ProgramCircuitItem.makeStack(recipe.getProgram())))
				.addRichTooltipCallback((recipeSlotView, tooltip) -> {
					tooltip.clear();
					tooltip.add(Component.translatable("info.conductance.jei.requires_program_circuit", recipe.getProgram()));
				});
		}
		final SimulatedRecipeCapabilityHolder holder = new SimulatedRecipeCapabilityHolder(recipe);
		final Map<String, GuiWidget> allWidgets = this.rootGroup.getWidgetsFlattened();
		for (final Map.Entry<String, GuiWidget> entry : allWidgets.entrySet()) {
			final String key = entry.getKey();
			final GuiWidget widget = entry.getValue();
			if (key.startsWith("items_in_")) {
				final int slotIndex = Integer.parseInt(key.substring("items_in_".length()));
				if (slotIndex >= 0 && slotIndex < holder.getInputItems().getStacks().size()) {
					final List<ItemStack> items = holder.getInputItems().getStacks().get(slotIndex);
					builder.addInputSlot(xOffset + widget.getX(), widget.getY()).addIngredients(VanillaTypes.ITEM_STACK, items);
				}
			}
			if (key.startsWith("items_out_")) {
				final int slotIndex = Integer.parseInt(key.substring("items_out_".length()));
				if (slotIndex >= 0 && slotIndex < holder.getOutputItems().getStacks().size()) {
					final List<ItemStack> items = holder.getOutputItems().getStacks().get(slotIndex);
					builder.addOutputSlot(xOffset + widget.getX(), widget.getY()).addIngredients(VanillaTypes.ITEM_STACK, items);
				}
			}
		}
	}

	@Override
	public void draw(final MachineRecipe recipe, final IRecipeSlotsView recipeSlotsView, final GuiGraphics guiGraphics, final double mouseX, final double mouseY) {
		final int xOffset = (this.width - this.rootGroup.getWidth()) / 2;
		guiGraphics.pose().pushMatrix();
		guiGraphics.pose().translate(xOffset, 0);
		Optional.ofNullable(this.rootGroup.getWidgetById("items_in")).ifPresent(inputGroup -> {
			inputGroup.renderBackground(guiGraphics, (int) mouseX, (int) mouseY, 0);
		});
		Optional.ofNullable(this.rootGroup.getWidgetById("progress")).ifPresent(progress -> {
			progress.render(guiGraphics, (int) mouseX, (int) mouseY, 0);
		});
		Optional.ofNullable(this.rootGroup.getWidgetById("items_out")).ifPresent(outputGroup -> {
			outputGroup.renderBackground(guiGraphics, (int) mouseX, (int) mouseY, 0);
		});
		guiGraphics.pose().popMatrix();
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
