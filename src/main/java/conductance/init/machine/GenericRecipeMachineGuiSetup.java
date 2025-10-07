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
import conductance.api.machine.gui.IGuiWidget;
import conductance.api.machine.gui.MachineMenu;
import conductance.api.machine.gui.MachineScreen;
import conductance.api.machine.gui.ManagedInt;
import conductance.api.machine.gui.MarkerWidget;
import conductance.api.machine.gui.MutablePoint;
import conductance.api.machine.gui.MutableSize;
import conductance.api.machine.gui.ProgressProvider;
import conductance.api.machine.gui.ProgressWidget;
import conductance.api.machine.gui.Rectangle;
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
	public void addWidgets(final MachineMenu menu, final BiConsumer<String, IGuiWidget> adder) {
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
			root.setY(10);
			Util.make(new ShowRecipeViewerHandlers(menu.getMachine()), handler -> {
				final IGuiWidget widget = root.getWidgetById("progress");
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
			final IGuiWidget inputGroup = Util.make(groupFactory.apply(IO.IN), group -> root.addWidget("in", group));
			final IGuiWidget outputGroup = Util.make(groupFactory.apply(IO.OUT), group -> root.addWidget("out", group));
			final IGuiWidget progress = Util.make(new ProgressWidget(
				new GuiDrawableTexture(recipeType.getGuiArrow()),
				progressProvider,
				recipeType.getGuiArrowDirection(),
				0, 0, 20, 20
			), progressWidget -> root.addWidget("progress", progressWidget));
			root.setSize(MutableSize.of(
				new ManagedInt(null, () -> Math.max(inputGroup.getWidth(), outputGroup.getWidth()) * 2 + 20 + progress.getWidth()),
				new ManagedInt(null, () -> Math.max(Math.max(inputGroup.getHeight(), progress.getHeight()), outputGroup.getHeight()))
			));
			progress.setPosition(MutablePoint.of(
				new ManagedInt(null, () -> root.getX() + (root.getWidth() - progress.getWidth()) / 2),
				new ManagedInt(null, () -> (root.getHeight() - progress.getHeight()) / 2)
			));
			inputGroup.setPosition(MutablePoint.of(
				new ManagedInt(null, () -> progress.getX() - inputGroup.getWidth() - 10),
				new ManagedInt(null, () -> (root.getHeight() - inputGroup.getHeight()) / 2)
			));
			outputGroup.setPosition(MutablePoint.of(
				new ManagedInt(null, () -> progress.getBounds().maxX() + 10),
				new ManagedInt(null, () -> (root.getHeight() - outputGroup.getHeight()) / 2)
			));
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
		final GuiTheme theme, final IO io, final int itemCount, final int fluidCount, final TriFunction<Integer, Integer, Integer, IGuiWidget> itemWidgetFactory,
		final TriFunction<Integer, Integer, Integer, IGuiWidget> fluidWidgetFactory
	) {
		return Util.make(new WidgetGroup(0, 0, 0, 0), mainGroup -> {
			final WidgetGroup itemGroup = Util.make(new WidgetGroup(0, 0, 0, 0), group -> {
				final int cols = itemCount == 4 ? 2 : Math.min(itemCount, 3);
				final int rows = itemCount == 0 ? 0 : itemCount / cols + Math.min(1, itemCount % cols);
				group.setWidth(cols * 18);
				group.setHeight(rows * 18);
				for (int i = 0; i < itemCount; ++i) {
					final String slotName = "items_" + io + "_" + i;
					group.addWidget(slotName, itemWidgetFactory.apply(i, (i % cols) * 18, (i / cols) * 18));
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
			itemGroup.setX(mainGroup.getWidth() - itemGroup.getWidth());
			mainGroup.addWidget("items_" + io, itemGroup);
			fluidGroup.setX(mainGroup.getWidth() - fluidGroup.getWidth());
			fluidGroup.setY(itemGroup.getHeight());
			mainGroup.addWidget("fluids_" + io, fluidGroup);
		});
	}

	@Override
	public void init(final MachineScreen screen, final Rectangle rootBounds) {
		final IGuiWidget root = screen.getMenu().getWidgetById("root");
		root.setBounds(rootBounds);
	}
}
