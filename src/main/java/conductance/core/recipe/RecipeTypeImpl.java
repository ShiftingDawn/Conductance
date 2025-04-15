package conductance.core.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.DoubleSupplier;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.widget.ProgressWidget;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.TankWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.jei.IngredientIO;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import lombok.Getter;
import conductance.api.NCRecipeElementTypes;
import conductance.api.machine.gui.GuiTextures;
import conductance.api.machine.gui.MachineGuiTemplate;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.machine.recipe.NCRecipeSerializer;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.machine.recipe.RecipeHolder;
import conductance.api.registry.RegistryObject;
import conductance.api.util.IOMode;
import conductance.client.GuiHelper;
import static conductance.client.GuiHelper.GUI_HEIGHT;
import static conductance.client.GuiHelper.GUI_WIDTH;
import static conductance.client.GuiHelper.NAME_SLOT_REGEX;

public class RecipeTypeImpl extends RegistryObject<ResourceLocation> implements NCRecipeType {

	private final Object2IntMap<IRecipeElementType<?>> maxInputs;
	private final Object2IntMap<IRecipeElementType<?>> maxOutputs;
	@Getter
	private final ResourceTexture progressBar;
	@Getter
	private final ProgressTexture.FillDirection progressBarDirection;
	private final ResourceTexture recipeViewProgressBar;

	public RecipeTypeImpl(
			final ResourceLocation registryKey,
			final Object2IntMap<IRecipeElementType<?>> maxInputs,
			final Object2IntMap<IRecipeElementType<?>> maxOutputs,
			final ResourceLocation progressBar,
			final ResourceLocation recipeViewProgressBar,
			final ProgressTexture.FillDirection direction) {
		super(registryKey);
		this.maxInputs = Object2IntMaps.unmodifiable(maxInputs);
		this.maxOutputs = Object2IntMaps.unmodifiable(maxOutputs);
		this.progressBar = new ResourceTexture(progressBar.toString());
		this.recipeViewProgressBar = new ResourceTexture(recipeViewProgressBar.toString());
		this.progressBarDirection = direction;
	}

	@Override
	public NCRecipeSerializer getSerializer() {
		return (NCRecipeSerializer) BuiltInRegistries.RECIPE_SERIALIZER.get(this.getRegistryKey());
	}

	@Override
	public int getMaxInputs(final IRecipeElementType<?> elementType) {
		return this.maxInputs.getOrDefault(elementType, -1);
	}

	@Override
	public int getMaxOutputs(final IRecipeElementType<?> elementType) {
		return this.maxOutputs.getOrDefault(elementType, -1);
	}

	@Override
	public WidgetGroup createGuiTemplate(
			final DoubleSupplier progressSupplier, final IItemHandlerModifiable inputItems, final IItemHandlerModifiable outputItems, final IFluidHandler inputFluids, final IFluidHandler outputFluids
	) {
		final MachineGuiTemplate<WidgetGroup, RecipeHolder> template = this.createGuiTemplate();
		final WidgetGroup group = template.createDefault();
		template.setupGui(group, new RecipeHolder(progressSupplier, inputItems, outputItems, inputFluids, outputFluids), false);
		return group;
	}

	@Override
	public MachineGuiTemplate<WidgetGroup, RecipeHolder> createGuiTemplate() {
		return new MachineGuiTemplate.Default<>(() -> {
			final WidgetGroup inputs = this.makeIOGroup(true);
			final WidgetGroup outputs = this.makeIOGroup(false);

			final WidgetGroup group = new WidgetGroup(0, 0, Math.max(GUI_WIDTH, inputs.getSize().width + outputs.getSize().width + 40),
					Math.max(GUI_HEIGHT / 2, Math.max(inputs.getSize().height, outputs.getSize().height)));
			final Size size = group.getSize();

			inputs.setSelfPosition(new Position((size.width / 2 - inputs.getSize().width) / 2, (size.height - inputs.getSize().height) / 2));
			outputs.setSelfPosition(new Position(size.width / 2 + (size.width / 2 - outputs.getSize().width) / 2, (size.height - outputs.getSize().height) / 2));
			group.addWidget(inputs);
			group.addWidget(outputs);

			final ProgressWidget progressWidget = new ProgressWidget(ProgressWidget.JEIProgress, size.width / 2 - 10, size.height / 2 - 10, 20, 20, this.getProgressBar());
			progressWidget.setId(GuiHelper.NAME_PROGRESS);
			progressWidget.setSize(new Size(20, 20));
			progressWidget.setFillDirection(this.progressBarDirection);
			progressWidget.setProgressTexture(this.progressBar.getSubTexture(0, 0, this.progressBar.imageWidth, this.progressBar.imageHeight / 2),
					this.progressBar.getSubTexture(0, this.progressBar.imageHeight / 2, this.progressBar.imageWidth, this.progressBar.imageHeight / 2));
			group.addWidget(progressWidget);

			return group;
		}, (template, recipeHolder, autoCalc) -> {
			final boolean isRecipeView = recipeHolder.progressSupplier() == ProgressWidget.JEIProgress;
			final List<Widget> progressWidgets = new ArrayList<>();
			GuiHelper.getWidgetByIdForEach(template, GuiHelper.NAME_PROGRESS_REGEX, ProgressWidget.class, progressWidget -> {
				progressWidget.setProgressSupplier(recipeHolder.progressSupplier());
				if (isRecipeView) {
					progressWidget.setProgressTexture(this.recipeViewProgressBar.getSubTexture(0, 0, this.recipeViewProgressBar.imageWidth, this.recipeViewProgressBar.imageHeight / 2),
							this.recipeViewProgressBar.getSubTexture(0, this.recipeViewProgressBar.imageHeight / 2, this.recipeViewProgressBar.imageWidth, this.recipeViewProgressBar.imageHeight / 2));
				}
				progressWidgets.add(progressWidget);
			});
			//			// todo add recipe button
			//			if (!isRecipeView && (LDLib.isReiLoaded() || LDLib.isJeiLoaded() || LDLib.isEmiLoaded())) {
			//				for (final Widget widget : progressWidgets) {
			//					template.addWidget(new ButtonWidget(widget.getPosition().x, widget.getPosition().y, widget.getSize().width, widget.getSize().height, IGuiTexture.EMPTY, clickData -> {
			//						if (clickData.isRemote) {
			//							//TODO REI
			//							//							if (LDLib.isReiLoaded()) {
			//							//								ViewSearchBuilder.builder().addCategory(GTRecipeTypeDisplayCategory.CATEGORIES.apply(GTRecipeType.this)).open();
			//							//							} else
			//
			//							// TODO JEI
			//							//							if (LDLib.isJeiLoaded()) {
			//							//								JEIPlugin.jeiRuntime.getRecipesGui().showTypes(List.of(GenericRecipeTypeCategory.TYPES.apply(this)));
			//							//							} else
			//
			//							//TODO EMI
			//							//							if (LDLib.isEmiLoaded()) {
			//							//								EmiApi.displayRecipeCategory(conductance.compat.emi.GenericRecipeTypeCategory.CATEGORIES.apply(this));
			//							//						}
			//						}
			//					}).setHoverTooltips("tooltip.conductance.recipe.show_all"));
			//				}
			//			}

			final AtomicReference<WidgetGroup> inputGroup = new AtomicReference<>();
			GuiHelper.getWidgetByIdForEach(template, NCRecipeElementTypes.ITEM.getGroupName(IOMode.INPUT), WidgetGroup.class, itemGroup -> {
				if (!itemGroup.widgets.isEmpty()) {
					itemGroup.setBackground(GuiTextures.getItemSlots(recipeHolder.inputItems().getSlots(), false));
				}
			});
			GuiHelper.getWidgetByIdForEach(template, NAME_SLOT_REGEX.formatted(NCRecipeElementTypes.ITEM.getSlotName(IOMode.INPUT)), SlotWidget.class, slot -> {
				final int index = GuiHelper.getWidgetIndex(slot);
				if (index >= 0 && index < recipeHolder.inputItems().getSlots()) {
					slot.setBackgroundTexture(null);
					slot.setHoverTexture(GuiTextures.SLOT_HOVER);
					slot.setDrawHoverOverlay(false);
					slot.setHandlerSlot(recipeHolder.inputItems(), index);
					slot.setIngredientIO(IngredientIO.INPUT);
					slot.setCanTakeItems(!isRecipeView);
					slot.setCanPutItems(!isRecipeView);
				} else if (autoCalc) {
					slot.getParent().removeWidget(slot);
					inputGroup.set(slot.getParent());
				}
			});
			GuiHelper.getWidgetByIdForEach(template, NCRecipeElementTypes.FLUID.getGroupName(IOMode.INPUT), WidgetGroup.class, fluidGroup -> {
				if (!fluidGroup.widgets.isEmpty()) {
					fluidGroup.setBackground(GuiTextures.getFluidSlots(recipeHolder.inputFluids().getTanks(), false));
				}
			});
			GuiHelper.getWidgetByIdForEach(template, NAME_SLOT_REGEX.formatted(NCRecipeElementTypes.FLUID.getSlotName(IOMode.INPUT)), TankWidget.class, tank -> {
				final int index = GuiHelper.getWidgetIndex(tank);
				if (index >= 0 && index < recipeHolder.inputFluids().getTanks()) {
					tank.setBackground((IGuiTexture) null);
					tank.setHoverTexture(GuiTextures.SLOT_HOVER);
					tank.setDrawHoverOverlay(false);
					tank.setFluidTank(recipeHolder.inputFluids(), index);
					tank.setIngredientIO(IngredientIO.INPUT);
					tank.setAllowClickFilled(!isRecipeView);
					tank.setAllowClickDrained(!isRecipeView);
				} else if (autoCalc) {
					tank.getParent().removeWidget(tank);
					inputGroup.set(tank.getParent());
				}
			});
			if (inputGroup.get() != null) {
				this.fixGroupBounding(inputGroup.get());
			}
			final AtomicReference<WidgetGroup> outputGroup = new AtomicReference<>();
			GuiHelper.getWidgetByIdForEach(template, NCRecipeElementTypes.ITEM.getGroupName(IOMode.OUTPUT), WidgetGroup.class, itemGroup -> {
				if (!itemGroup.widgets.isEmpty()) {
					itemGroup.setBackground(GuiTextures.getItemSlots(recipeHolder.outputItems().getSlots(), true));
				}
			});
			GuiHelper.getWidgetByIdForEach(template, NCRecipeElementTypes.ITEM.getSlotName(IOMode.OUTPUT), SlotWidget.class, slot -> {
				final int index = GuiHelper.getWidgetIndex(slot);
				if (index >= 0 && index < recipeHolder.outputItems().getSlots()) {
					slot.setBackgroundTexture(null);
					slot.setHoverTexture(GuiTextures.SLOT_HOVER);
					slot.setDrawHoverOverlay(false);
					slot.setHandlerSlot(recipeHolder.outputItems(), index);
					slot.setIngredientIO(IngredientIO.OUTPUT);
					slot.setCanTakeItems(!isRecipeView);
					slot.setCanPutItems(false);
				} else if (autoCalc) {
					slot.getParent().removeWidget(slot);
					outputGroup.set(slot.getParent());
				}
			});
			GuiHelper.getWidgetByIdForEach(template, NCRecipeElementTypes.FLUID.getGroupName(IOMode.OUTPUT), WidgetGroup.class, fluidGroup -> {
				if (!fluidGroup.widgets.isEmpty()) {
					fluidGroup.setBackground(GuiTextures.getFluidSlots(recipeHolder.outputFluids().getTanks(), true));
				}
			});
			GuiHelper.getWidgetByIdForEach(template, NAME_SLOT_REGEX.formatted(NCRecipeElementTypes.FLUID.getSlotName(IOMode.OUTPUT)), TankWidget.class, tank -> {
				final int index = GuiHelper.getWidgetIndex(tank);
				if (index >= 0 && index < recipeHolder.outputFluids().getTanks()) {
					tank.setBackground((IGuiTexture) null);
					tank.setHoverTexture(GuiTextures.SLOT_HOVER);
					tank.setDrawHoverOverlay(false);
					tank.setFluidTank(recipeHolder.outputFluids(), index);
					tank.setIngredientIO(IngredientIO.OUTPUT);
					tank.setAllowClickFilled(!isRecipeView);
					tank.setAllowClickDrained(false);
				} else if (autoCalc) {
					tank.getParent().removeWidget(tank);
					outputGroup.set(tank.getParent());
				}
			});
			if (outputGroup.get() != null) {
				this.fixGroupBounding(outputGroup.get());
			}
		});
	}

	private void fixGroupBounding(final WidgetGroup group) {
		final int totalCount = group.getContainedWidgets(false).size();
		final int groupWidth = totalCount == 4 ? 2 : 3; //Looks better

		final Size oldSize = group.getSize();
		group.setSize(new Size(Math.min(totalCount, groupWidth) * 18 + 8, (totalCount / groupWidth + (totalCount % groupWidth == 0 ? 0 : 1)) * 18 + 8));

		final int dX = oldSize.width - group.getSize().width;
		final int dY = oldSize.height - group.getSize().height;
		final int newX = group.getPosition().x + dX / 2;
		final int newY = group.getPosition().y + dY / 2;
		group.setSelfPosition(new Position(newX, newY));
	}

	private WidgetGroup makeIOGroup(final boolean isInput) {
		final int itemCount = isInput ? this.getMaxInputs(NCRecipeElementTypes.ITEM) : this.getMaxOutputs(NCRecipeElementTypes.ITEM);
		final int fluidCount = isInput ? this.getMaxInputs(NCRecipeElementTypes.FLUID) : this.getMaxOutputs(NCRecipeElementTypes.FLUID);

		final int itemWidth = itemCount == 4 ? 2 : Math.min(itemCount, 3);
		final int fluidWidth = fluidCount == 4 ? 2 : Math.min(fluidCount, 3);
		final int itemHeight = itemCount == 0 ? 0 : itemCount / itemWidth + Math.min(1, itemCount % itemWidth);
		final int fluidHeight = fluidCount == 0 ? 0 : fluidCount / fluidWidth + Math.min(1, fluidCount % fluidWidth);

		final WidgetGroup group = new WidgetGroup(0, 0, Math.min(Math.max(itemCount, fluidCount), Math.max(itemWidth, fluidWidth)) * 18, (itemHeight + fluidHeight) * 18);
		group.addWidget(Util.make(new WidgetGroup(0, 0, itemWidth * 18, itemHeight * 18), itemGroup -> {
			itemGroup.setId(NCRecipeElementTypes.ITEM.getGroupName(isInput ? IOMode.INPUT_OUTPUT : IOMode.OUTPUT));
			for (int slotIndex = 0; slotIndex < itemCount; ++slotIndex) {
				final SlotWidget slot = new SlotWidget();
				slot.initTemplate();
				slot.setSelfPosition(new Position((slotIndex % itemWidth) * 18, (slotIndex / itemWidth) * 18));
				slot.setId(NCRecipeElementTypes.ITEM.getSlotName(isInput ? IOMode.INPUT : IOMode.OUTPUT, slotIndex));
				itemGroup.addWidget(slot);
			}
		}));
		group.addWidget(Util.make(new WidgetGroup(0, itemHeight * 18, fluidWidth * 18, fluidHeight * 18), fluidGroup -> {
			fluidGroup.setId(NCRecipeElementTypes.FLUID.getGroupName(isInput ? IOMode.INPUT_OUTPUT : IOMode.OUTPUT));
			for (int tankIndex = 0; tankIndex < fluidCount; ++tankIndex) {
				final TankWidget tank = new TankWidget();
				tank.initTemplate();
				tank.setFillDirection(ProgressTexture.FillDirection.ALWAYS_FULL);
				tank.setSelfPosition(new Position((tankIndex % fluidWidth) * 18, (itemHeight + (tankIndex / fluidWidth)) * 18));
				tank.setId(NCRecipeElementTypes.FLUID.getSlotName(isInput ? IOMode.INPUT : IOMode.OUTPUT, tankIndex));
				fluidGroup.addWidget(tank);
			}
		}));
		return group;
	}
}
