package conductance.compat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.ProgressWidget;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.TankWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.CycleFluidTransfer;
import com.lowdragmc.lowdraglib.utils.CycleItemStackHandler;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;
import com.lowdragmc.lowdraglib.utils.Size;
import conductance.api.CAPI;
import conductance.api.NCRecipeElementTypes;
import conductance.api.machine.gui.GuiTheme;
import conductance.api.machine.recipe.IRecipe;
import conductance.api.machine.recipe.RecipeElement;
import conductance.api.util.IOMode;
import conductance.api.util.TextHelper;
import conductance.Conductance;
import conductance.client.GuiHelper;
import static conductance.client.GuiHelper.GUI_WIDTH;
import static conductance.client.GuiHelper.NAME_SLOT_REGEX;

public final class ConductanceRecipeWidget extends WidgetGroup {

	public ConductanceRecipeWidget(final RecipeHolder<? extends IRecipe> recipeHolder) {
		super();
		this.setClientSideWidget();
		this.setBackground(GuiTheme.THEME_DEFAULT.getBackground());
		final IRecipe recipe = recipeHolder.value();

		final List<RecipeElement> inputItemElements = new ArrayList<>();
		inputItemElements.addAll(recipe.getInputs(NCRecipeElementTypes.ITEM));
		inputItemElements.addAll(recipe.getInputsPerTick(NCRecipeElementTypes.ITEM));
		final List<List<ItemStack>> inputItems = inputItemElements.stream()
				.map(RecipeElement::data)
				.map(data -> (SizedIngredient) data)
				.map(SizedIngredient::getItems)
				.map(Arrays::stream)
				.map(Stream::toList)
				.collect(Collectors.toList());
		while (inputItems.size() < recipe.getType().getMaxInputs(NCRecipeElementTypes.ITEM)) {
			inputItems.add(null);
		}
		final List<RecipeElement> inputFluidElements = new ArrayList<>();
		inputFluidElements.addAll(recipe.getInputs(NCRecipeElementTypes.FLUID));
		inputFluidElements.addAll(recipe.getInputsPerTick(NCRecipeElementTypes.FLUID));
		final List<List<FluidStack>> inputFluids = inputFluidElements.stream()
				.map(RecipeElement::data)
				.map(data -> (SizedFluidIngredient) data)
				.map(SizedFluidIngredient::getFluids)
				.map(Arrays::stream)
				.map(Stream::toList)
				.collect(Collectors.toList());
		while (inputFluids.size() < recipe.getType().getMaxInputs(NCRecipeElementTypes.FLUID)) {
			inputFluids.add(null);
		}
		final List<RecipeElement> outputItemElements = new ArrayList<>();
		outputItemElements.addAll(recipe.getOutputs(NCRecipeElementTypes.ITEM));
		outputItemElements.addAll(recipe.getOutputsPerTick(NCRecipeElementTypes.ITEM));
		final List<List<ItemStack>> outputItems = outputItemElements.stream()
				.map(RecipeElement::data)
				.map(data -> (SizedIngredient) data)
				.map(SizedIngredient::getItems)
				.map(Arrays::stream)
				.map(Stream::toList)
				.collect(Collectors.toList());
		while (outputItems.size() < recipe.getType().getMaxOutputs(NCRecipeElementTypes.ITEM)) {
			outputItems.add(null);
		}
		final List<RecipeElement> outputFluidElements = new ArrayList<>();
		outputFluidElements.addAll(recipe.getOutputs(NCRecipeElementTypes.FLUID));
		outputFluidElements.addAll(recipe.getOutputsPerTick(NCRecipeElementTypes.FLUID));
		final List<List<FluidStack>> outputFluids = outputFluidElements.stream()
				.map(RecipeElement::data)
				.map(data -> (SizedFluidIngredient) data)
				.map(SizedFluidIngredient::getFluids)
				.map(Arrays::stream)
				.map(Stream::toList)
				.collect(Collectors.toList());
		while (outputFluids.size() < recipe.getType().getMaxOutputs(NCRecipeElementTypes.FLUID)) {
			outputFluids.add(null);
		}

		final WidgetGroup template = recipe.getType().createGuiTemplate(ProgressWidget.JEIProgress,
				new CycleItemStackHandler(inputItems), new CycleItemStackHandler(outputItems),
				new CycleFluidTransfer(inputFluids), new CycleFluidTransfer(outputFluids),
				GuiTheme.THEME_DEFAULT
		);
		GuiHelper.getWidgetByIdForEach(template, NAME_SLOT_REGEX.formatted(NCRecipeElementTypes.ITEM.getSlotName(IOMode.INPUT)), SlotWidget.class, slot -> {
			final int index = GuiHelper.getWidgetIndex(slot);
			if (index >= 0 && index < inputItemElements.size()) {
				final RecipeElement element = inputItemElements.get(index);
				final boolean perTick = recipe.getInputsPerTick(NCRecipeElementTypes.ITEM).contains(element);
				slot.setOverlay(GuiHelper.createRecipeSlotOverlay(perTick, element.chance()));
				slot.setOnAddedTooltips((ignored, tooltip) -> {
					if (element.chance() < 100) {
						if (element.chance() == 0) {
							tooltip.add(Conductance.tooltip("recipe.tooltip_chance_0"));
						} else {
							tooltip.add(Conductance.tooltip("recipe.tooltip_chance_1", element.chance()));
						}
						if (element.tieredChanceBoost() > 0) {
							tooltip.add(Conductance.tooltip("recipe.tooltip_tier_boost", element.tieredChanceBoost()));
						}
					}
					if (perTick) {
						tooltip.add(Conductance.tooltip("recipe.tooltip_per_tick_input"));
					}
				});
			}
		});
		GuiHelper.getWidgetByIdForEach(template, NAME_SLOT_REGEX.formatted(NCRecipeElementTypes.FLUID.getSlotName(IOMode.INPUT)), TankWidget.class, tank -> {
			final int index = GuiHelper.getWidgetIndex(tank);
			if (index >= 0 && index < inputFluidElements.size()) {
				final RecipeElement element = inputFluidElements.get(index);
				final boolean perTick = recipe.getInputsPerTick(NCRecipeElementTypes.FLUID).contains(element);
				tank.setOverlay(GuiHelper.createRecipeSlotOverlay(perTick, element.chance()));
				tank.setOnAddedTooltips((ignored, tooltip) -> {
					if (element.chance() < 100) {
						if (element.chance() == 0) {
							tooltip.add(Conductance.tooltip("recipe.tooltip_chance_0"));
						} else {
							tooltip.add(Conductance.tooltip("recipe.tooltip_chance_1", element.chance()));
						}
						if (element.tieredChanceBoost() > 0) {
							tooltip.add(Conductance.tooltip("recipe.tooltip_tier_boost", element.tieredChanceBoost()));
						}
					}
					if (perTick) {
						tooltip.add(Conductance.tooltip("recipe.tooltip_per_tick_input"));
					}
				});
			}
		});
		GuiHelper.getWidgetByIdForEach(template, NAME_SLOT_REGEX.formatted(NCRecipeElementTypes.ITEM.getSlotName(IOMode.OUTPUT)), SlotWidget.class, slot -> {
			final int index = GuiHelper.getWidgetIndex(slot);
			if (index >= 0 && index < outputItemElements.size()) {
				final RecipeElement element = outputItemElements.get(index);
				final boolean perTick = recipe.getOutputsPerTick(NCRecipeElementTypes.ITEM).contains(element);
				slot.setOverlay(GuiHelper.createRecipeSlotOverlay(perTick, element.chance()));
				slot.setOnAddedTooltips((ignored, tooltip) -> {
					if (element.chance() < 100) {
						if (element.chance() == 0) {
							tooltip.add(Conductance.tooltip("recipe.tooltip_chance_0"));
						} else {
							tooltip.add(Conductance.tooltip("recipe.tooltip_chance_1", element.chance()));
						}
						if (element.tieredChanceBoost() > 0) {
							tooltip.add(Conductance.tooltip("recipe.tooltip_tier_boost", element.tieredChanceBoost()));
						}
					}
					if (perTick) {
						tooltip.add(Conductance.tooltip("recipe.tooltip_per_tick_output"));
					}
				});
			}
		});
		GuiHelper.getWidgetByIdForEach(template, NAME_SLOT_REGEX.formatted(NCRecipeElementTypes.FLUID.getSlotName(IOMode.OUTPUT)), TankWidget.class, tank -> {
			final int index = GuiHelper.getWidgetIndex(tank);
			if (index >= 0 && index < outputFluidElements.size()) {
				final RecipeElement element = outputFluidElements.get(index);
				final boolean perTick = recipe.getOutputsPerTick(NCRecipeElementTypes.FLUID).contains(element);
				tank.setOverlay(GuiHelper.createRecipeSlotOverlay(perTick, element.chance()));
				tank.setOnAddedTooltips((ignored, tooltip) -> {
					if (element.chance() < 100) {
						if (element.chance() == 0) {
							tooltip.add(Conductance.tooltip("recipe.tooltip_chance_0"));
						} else {
							tooltip.add(Conductance.tooltip("recipe.tooltip_chance_1", element.chance()));
						}
						if (element.tieredChanceBoost() > 0) {
							tooltip.add(Conductance.tooltip("recipe.tooltip_tier_boost", element.tieredChanceBoost()));
						}
					}
					if (perTick) {
						tooltip.add(Conductance.tooltip("recipe.tooltip_per_tick_output"));
					}
				});
			}
		});

		this.addWidget(template);
		this.setSize(GUI_WIDTH, template.getSizeHeight() + 10);
		template.setSelfPosition((this.getSizeWidth() - template.getSizeWidth()) / 2, 5);
		final Size size = this.getSize();

		int yPos = this.getSizeHeight() + 1;

		if (recipe.getProgram() == 0) {
			this.addWidget(new LabelWidget(5, yPos, LocalizationUtils.format(Conductance.tooltipText("recipe.program_any"))));
			yPos += 10;
		} else {
			this.addWidget(new LabelWidget(5, yPos, LocalizationUtils.format(Conductance.tooltipText("recipe.program"), recipe.getProgram())));
			yPos += 10;
		}
		this.addWidget(new LabelWidget(5, yPos, LocalizationUtils.format(Conductance.tooltipText("recipe.process_time"), recipe.getProcessTime() / 20f, recipe.getProcessTime())));
		yPos += 10;
		long energyPerTick = recipe.getInputsPerTick(NCRecipeElementTypes.ENERGY).stream().mapToLong(content -> (long) content.data()).sum();
		boolean isOutput = false;
		if (energyPerTick == 0) {
			energyPerTick = recipe.getOutputsPerTick(NCRecipeElementTypes.ENERGY).stream().mapToLong(content -> (long) content.data()).sum();
			isOutput = true;
		}
		if (energyPerTick > 0) {
			this.addWidget(new LabelWidget(5, yPos, Conductance.tooltip("recipe.total_energy", energyPerTick * recipe.getProcessTime(), TextHelper.ENERGY_FORMAT)));
			yPos += 10;
			this.addWidget(new LabelWidget(5, yPos, Conductance.tooltip("recipe.energy_%s".formatted(isOutput ? "produce" : "consume"), energyPerTick, TextHelper.ENERGY_FORMAT_PER_TICK,
					CAPI.tiers().getTierByVoltage(energyPerTick).getLocalizedName())));
			yPos += 10;
		}

		this.setSize(size.getWidth(), size.getHeight() + yPos + 5);
	}
}
