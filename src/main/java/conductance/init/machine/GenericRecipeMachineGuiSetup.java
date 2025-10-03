package conductance.init.machine;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.Util;
import net.minecraft.world.inventory.Slot;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.CapIO;
import conductance.api.machine.gui.GuiDrawableTexture;
import conductance.api.machine.gui.GuiSetup;
import conductance.api.machine.gui.GuiTheme;
import conductance.api.machine.gui.GuiWidget;
import conductance.api.machine.gui.MachineMenu;
import conductance.api.machine.gui.MachineScreen;
import conductance.api.machine.gui.MarkerWidget;
import conductance.api.machine.gui.ProgressProvider;
import conductance.api.machine.gui.ProgressWidget;
import conductance.api.machine.gui.RepositionableSlotItemHandler;
import conductance.api.machine.gui.SlotWidget;
import conductance.api.machine.gui.TankWidget;
import conductance.api.machine.gui.WidgetGroup;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.util.IO;

public class GenericRecipeMachineGuiSetup extends GuiSetup {

	@Override
	public void addSlots(final MachineMenu menu, final Consumer<Slot> adder) {
		final GenericRecipeMachine machine = (GenericRecipeMachine) menu.getMachine();
		if (machine.getInputItems() != null) {
			for (int i = 0; i < machine.getInputItems().getSlots(); ++i) {
				adder.accept(new RepositionableSlotItemHandler(machine.getInputItems().getInventory(), i, i * 18, 0, CapIO.BOTH));
			}
		}
		if (machine.getOutputItems() != null) {
			for (int i = 0; i < machine.getOutputItems().getSlots(); ++i) {
				adder.accept(new RepositionableSlotItemHandler(machine.getOutputItems().getInventory(), i, i * 18, 0, CapIO.OUT));
			}
		}
	}

	@Override
	public void addWidgets(final MachineMenu menu, final BiConsumer<String, GuiWidget> adder) {
		final GenericRecipeMachine machine = (GenericRecipeMachine) menu.getMachine();
		final IItemHandler inputItems = machine.getInputItems() != null ? machine.getInputItems().getInventory() : null;
		final IItemHandler outputItems = machine.getOutputItems() != null ? machine.getOutputItems().getInventory() : null;
		final IFluidHandler inputFluids = machine.getInputFluids() != null ? machine.getInputFluids().getRealFluidHandler() : null;
		final IFluidHandler outputFluids = machine.getOutputFluids() != null ? machine.getOutputFluids().getRealFluidHandler() : null;
		adder.accept("root", Util.make(GenericRecipeMachineGuiSetup.makeRootGroup(
			this.getTheme(),
			inputItems, outputItems, inputFluids, outputFluids,
			menu, machine.getRecipeType(), new RecipeHandlerProgressProvider(machine.getRecipeHandler())
		), root -> {
			root.setInitialY(10);
			Util.make(new ShowRecipeViewerHandlers(menu.getMachine()), handler -> {
				final GuiWidget widget = root.getWidgetById("progress");
				widget.addTooltipCallback(handler);
				widget.addMouseListener(handler);
			});
		}));
	}

	public static WidgetGroup makeRootGroup(
		final GuiTheme theme,
		@Nullable final IItemHandler inputItems, @Nullable final IItemHandler outputItems, @Nullable final IFluidHandler inputFluids, @Nullable final IFluidHandler outputFluids,
		final MachineMenu menu, final MachineRecipeType recipeType, final ProgressProvider progressProvider
	) {
		return GenericRecipeMachineGuiSetup.makeRootGroup(recipeType, progressProvider, io -> switch (io) {
			case IN -> GenericRecipeMachineGuiSetup.makeGroup(theme, IO.IN, inputItems, inputFluids, menu);
			case OUT -> GenericRecipeMachineGuiSetup.makeGroup(theme, IO.OUT, outputItems, outputFluids, menu);
		});
	}

	public static WidgetGroup makeDummyRootGroup(
		final GuiTheme theme,
		final int itemInputCount, final int itemOutputCount,
		final int fluidInputCount, final int fluidOutputCount,
		final MachineRecipeType recipeType, final ProgressProvider progressProvider
	) {
		return GenericRecipeMachineGuiSetup.makeRootGroup(recipeType, progressProvider, io -> switch (io) {
			case IN -> GenericRecipeMachineGuiSetup.makeDummyGroup(theme, IO.IN, itemInputCount, fluidInputCount);
			case OUT -> GenericRecipeMachineGuiSetup.makeDummyGroup(theme, IO.OUT, itemOutputCount, fluidOutputCount);
		});
	}

	private static WidgetGroup makeRootGroup(final MachineRecipeType recipeType, final ProgressProvider progressProvider, final Function<IO, WidgetGroup> groupFactory) {
		return Util.make(new WidgetGroup(0, 0, 0, 0), root -> {
			final GuiWidget inputGroup = Util.make(groupFactory.apply(IO.IN), group -> root.addWidget("in", group));
			final GuiWidget outputGroup = Util.make(groupFactory.apply(IO.OUT), group -> root.addWidget("out", group));
			final GuiWidget progress = Util.make(new ProgressWidget(
				new GuiDrawableTexture(recipeType.getGuiArrow()),
				progressProvider,
				recipeType.getGuiArrowDirection(),
				5, 0, 20, 20
			), progressWidget -> root.addWidget("progress", progressWidget));
			final int totalWidth = Math.max(inputGroup.getWidth(), outputGroup.getWidth()) * 2 + 20 + progress.getWidth();
			final int totalHeight = Math.max(Math.max(inputGroup.getHeight(), progress.getHeight()), outputGroup.getHeight());
			root.setWidth(totalWidth);
			root.setHeight(totalHeight);
			progress.setInitialX(totalWidth / 2 - 10);
			progress.setInitialY(totalHeight / 2 - 10);
			inputGroup.setInitialX(totalWidth / 2 - 10 - progress.getWidth() / 2 - inputGroup.getWidth());
			inputGroup.setInitialY((totalHeight - inputGroup.getHeight()) / 2);
			outputGroup.setInitialX(totalWidth / 2 + progress.getWidth() / 2 + 10);
			outputGroup.setInitialY((totalHeight - outputGroup.getHeight()) / 2);
		});
	}

	public static WidgetGroup makeGroup(final GuiTheme theme, final IO io, @Nullable final IItemHandler itemHandler, @Nullable final IFluidHandler fluidHandler, final MachineMenu menu) {
		final int itemSlots = itemHandler != null ? itemHandler.getSlots() : 0;
		final int fluidSlots = fluidHandler != null ? fluidHandler.getTanks() : 0;
		return GenericRecipeMachineGuiSetup.makeGroup(theme, io, itemSlots, fluidSlots, (slotIndex, x, y) -> {
			assert itemHandler != null;
			final RepositionableSlotItemHandler slot = (RepositionableSlotItemHandler) menu.getSlot(itemHandler, slotIndex);
			slot.setX(x);
			slot.setY(y);
			return new SlotWidget(slot);
		}, (tankIndex, x, y) -> {
			assert fluidHandler != null;
			return new TankWidget(x, y, fluidHandler, tankIndex, io == IO.IN ? CapIO.BOTH : CapIO.OUT);
		});
	}

	public static WidgetGroup makeDummyGroup(final GuiTheme theme, final IO io, final int itemSlots, final int fluidSlots) {
		return GenericRecipeMachineGuiSetup.makeGroup(
			theme, io,
			itemSlots, fluidSlots,
			(slotIndex, x, y) -> new MarkerWidget(x, y, 18, 18), (slotIndex, x, y) -> new MarkerWidget(x, y, 18, 18)
		);
	}

	private static WidgetGroup makeGroup(
		final GuiTheme theme, final IO io, final int itemCount, final int fluidCount, final TriFunction<Integer, Integer, Integer, GuiWidget> itemWidgetFactory,
		final TriFunction<Integer, Integer, Integer, GuiWidget> fluidWidgetFactory
	) {
		return Util.make(new WidgetGroup(0, 0, 0, 0), mainGroup -> {
			final WidgetGroup itemGroup = Util.make(new WidgetGroup(0, 0, 0, 0), group -> {
				final int cols = itemCount == 4 ? 2 : Math.min(itemCount, 3);
				final int rows = itemCount == 0 ? 0 : itemCount / cols + Math.min(1, itemCount % cols);
				group.setWidth(cols * 18);
				group.setHeight(rows * 18);
				for (int i = 0; i < itemCount; ++i) {
					final String slotName = "items_" + io + "_" + i;
					group.addWidget(slotName, itemWidgetFactory.apply(i, (i % cols) * 18 + 1, (i / cols) * 18 + 1));
				}
				group.setBackground(theme.getItemSlots(itemCount, io == IO.OUT));
			});
			final WidgetGroup fluidGroup = Util.make(new WidgetGroup(0, 0, 0, 0), group -> {
				final int cols = fluidCount == 4 ? 2 : Math.min(fluidCount, 3);
				final int rows = fluidCount == 0 ? 0 : fluidCount / cols + Math.min(1, fluidCount % cols);
				group.setWidth(cols * 18);
				group.setHeight(rows * 18);
				for (int i = 0; i < fluidCount; ++i) {
					final String slotName = "fluids_" + io + "_" + i;
					group.addWidget(slotName, fluidWidgetFactory.apply(i, (i % cols) * 18, (i / cols) * 18));
				}
				group.setBackground(theme.getFluidSlots(fluidCount, io == IO.OUT));
			});
			mainGroup.setWidth(Math.max(itemGroup.getWidth(), fluidGroup.getWidth()));
			mainGroup.setHeight(itemGroup.getHeight() + fluidGroup.getHeight());
			itemGroup.setInitialX(mainGroup.getWidth() - itemGroup.getWidth());
			mainGroup.addWidget("items_" + io, itemGroup);
			fluidGroup.setInitialX(mainGroup.getWidth() - fluidGroup.getWidth());
			fluidGroup.setInitialY(itemGroup.getHeight());
			mainGroup.addWidget("fluids_" + io, fluidGroup);
		});
	}

	@Override
	public void init(final MachineScreen screen) {
		final GuiWidget root = screen.getMenu().getWidgetById("root");
		root.setX((screen.getXSize() - root.getWidth()) / 2);
		root.setY((74 - root.getHeight()) / 2);
	}
}
