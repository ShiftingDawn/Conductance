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
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import conductance.api.NCRecipeElementTypes;
import conductance.api.machine.MachineType;
import conductance.api.machine.gui.GuiTheme;
import conductance.api.machine.gui.GuiWidget;
import conductance.api.machine.gui.WidgetGroup;
import conductance.api.recipe.MachineRecipe;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.util.IO;
import conductance.init.machine.GenericRecipeMachineGuiSetup;

final class GenericRecipeMachineCategory implements IRecipeCategory<MachineRecipe> {

	static final Function<MachineRecipeType, IRecipeType<MachineRecipe>> RECIPE_TYPES = Util.memoize(machineRecipeType ->
		new IRecipeType.JeiRecipeType<>(machineRecipeType.getId(), MachineRecipe.class)
	);
	private final MachineType<?> machineType;
	private final MachineRecipeType recipeType;
	private final IRecipeType<MachineRecipe> jeiRecipeType;
	private final Component title;
	private final IDrawable icon;
	private final WidgetGroup rootGroup;
	private final int width;
	private final int height;

	GenericRecipeMachineCategory(final MachineType<?> machineType, final IRecipeType<MachineRecipe> jeiRecipeType) {
		this.machineType = machineType;
		this.recipeType = machineType.getRecipeTypes()[0];
		this.jeiRecipeType = jeiRecipeType;
		this.title = machineType.getName();
		this.icon = null;
		this.rootGroup = GenericRecipeMachineGuiSetup.makeDummyRootGroup(
			machineType.getGuiSetup() != null ? machineType.getGuiSetup().getTheme() : GuiTheme.THEME_DEFAULT,
			this.recipeType.getLimit(IO.IN, NCRecipeElementTypes.ITEM), this.recipeType.getLimit(IO.OUT, NCRecipeElementTypes.ITEM),
			this.recipeType, new JeiProgressProvider()
		);
		this.width = this.rootGroup.getWidth();
		this.height = this.rootGroup.getHeight();
	}

	@Override
	public void setRecipe(final IRecipeLayoutBuilder builder, final MachineRecipe recipe, final IFocusGroup focuses) {
		final SimulatedRecipeCapabilityHolder holder = new SimulatedRecipeCapabilityHolder(recipe);
		final Map<String, GuiWidget> allWidgets = this.rootGroup.getWidgetsFlattened();
		for (final Map.Entry<String, GuiWidget> entry : allWidgets.entrySet()) {
			final String key = entry.getKey();
			final GuiWidget widget = entry.getValue();
			if (key.startsWith("items_in_")) {
				final int slotIndex = Integer.parseInt(key.substring("items_in_".length()));
				if (slotIndex >= 0 && slotIndex < holder.getInputItems().getStacks().size()) {
					final List<ItemStack> items = holder.getInputItems().getStacks().get(slotIndex);
					builder.addInputSlot(widget.getX(), widget.getY())
						.addIngredients(VanillaTypes.ITEM_STACK, items);
				}
			}
			if (key.startsWith("items_out_")) {
				final int slotIndex = Integer.parseInt(key.substring("items_out_".length()));
				if (slotIndex >= 0 && slotIndex < holder.getOutputItems().getStacks().size()) {
					final List<ItemStack> items = holder.getOutputItems().getStacks().get(slotIndex);
					builder.addOutputSlot(widget.getX(), widget.getY())
						.addIngredients(VanillaTypes.ITEM_STACK, items);
				}
			}
		}
	}

	@Override
	public void draw(final MachineRecipe recipe, final IRecipeSlotsView recipeSlotsView, final GuiGraphics guiGraphics, final double mouseX, final double mouseY) {
		Optional.ofNullable(this.rootGroup.getWidgetById("items_in")).ifPresent(inputGroup -> {
			inputGroup.renderBackground(guiGraphics, (int) mouseX, (int) mouseY, 0);
		});
		Optional.ofNullable(this.rootGroup.getWidgetById("progress")).ifPresent(progress -> {
			progress.render(guiGraphics, (int) mouseX, (int) mouseY, 0);
		});
		Optional.ofNullable(this.rootGroup.getWidgetById("items_out")).ifPresent(outputGroup -> {
			outputGroup.renderBackground(guiGraphics, (int) mouseX, (int) mouseY, 0);
		});
	}

	@Override
	public IRecipeType<MachineRecipe> getRecipeType() {
		return this.jeiRecipeType;
	}

	@Override
	public Component getTitle() {
		return this.title;
	}

	@Override
	public IDrawable getIcon() {
		return this.icon;
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
