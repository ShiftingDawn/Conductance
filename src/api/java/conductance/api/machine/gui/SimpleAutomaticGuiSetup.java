package conductance.api.machine.gui;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.world.inventory.Slot;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.machine.CapIO;
import conductance.api.machine.IFluidHandlerModifiable;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.RecipeHandler;
import conductance.api.machine.energy.IEnergyHandler;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.util.IO;

public abstract class SimpleAutomaticGuiSetup extends GuiSetup {

	protected abstract @Nullable IItemHandlerModifiable getInputItems(MachineBlockEntity<?> machine);

	protected abstract @Nullable IItemHandlerModifiable getOutputItems(MachineBlockEntity<?> machine);

	protected abstract @Nullable IFluidHandlerModifiable getInputFluids(MachineBlockEntity<?> machine);

	protected abstract @Nullable IFluidHandlerModifiable getOutputFluids(MachineBlockEntity<?> machine);

	protected abstract @Nullable IEnergyHandler getEnergyHandler(MachineBlockEntity<?> machine);

	protected abstract @Nullable RecipeHandler getRecipeHandler(MachineBlockEntity<?> machine);

	@Override
	public void addSlots(final MachineMenu menu, final Consumer<Slot> adder) {
		CAPI.make(this.getInputItems(menu.getMachine()), handler -> {
			for (int i = 0; i < handler.getSlots(); ++i) {
				adder.accept(new RepositionableSlotItemHandler(handler, i, i * 18, 0, CapIO.BOTH));
			}
		});
		CAPI.make(this.getOutputItems(menu.getMachine()), handler -> {
			for (int i = 0; i < handler.getSlots(); ++i) {
				adder.accept(new RepositionableSlotItemHandler(handler, i, i * 18, 0, CapIO.OUT));
			}
		});
	}

	@Override
	public void addWidgets(final MachineMenu menu, final BiConsumer<String, IGuiWidget> adder) {
		final MachineBlockEntity<?> machine = menu.getMachine();
		adder.accept("root", CAPI.make(SimpleAutomaticGuiSetup.makeRootGroup(
			this.getTheme(),
			this.getInputItems(machine), this.getOutputItems(machine), this.getInputFluids(machine), this.getOutputFluids(machine),
			menu,
			Optional.ofNullable(this.getRecipeHandler(machine)).map(recipeHandler -> recipeHandler.getHolder().getRecipeType()).orElse(null),
			Optional.ofNullable(this.getRecipeHandler(machine)).map(RecipeHandlerProgressProvider::new).orElse(null)
		), root -> root.setY(10)));
		Optional.ofNullable(this.getEnergyHandler(machine)).ifPresent(handler ->
			adder.accept("energy", new EnergyBarWidget(0, 0, this.getTheme(), handler))
		);
	}

	public static WidgetGroup makeRootGroup(
		final GuiTheme theme,
		@Nullable final IItemHandler inputItems, @Nullable final IItemHandler outputItems, @Nullable final IFluidHandler inputFluids, @Nullable final IFluidHandler outputFluids,
		final MachineMenu menu, @Nullable final MachineRecipeType recipeType, @Nullable final ProgressProvider progressProvider
	) {
		return SimpleAutomaticGuiSetup.makeRootGroup(recipeType, progressProvider, io -> switch (io) {
			case IN -> SimpleAutomaticGuiSetup.makeGroup(theme, IO.IN, inputItems, inputFluids, menu);
			case OUT -> SimpleAutomaticGuiSetup.makeGroup(theme, IO.OUT, outputItems, outputFluids, menu);
		});
	}

	public static WidgetGroup makeDummyRootGroup(
		final GuiTheme theme,
		final int itemInputCount, final int itemOutputCount,
		final int fluidInputCount, final int fluidOutputCount,
		@Nullable final MachineRecipeType recipeType, @Nullable final ProgressProvider progressProvider
	) {
		return SimpleAutomaticGuiSetup.makeRootGroup(recipeType, progressProvider, io -> switch (io) {
			case IN -> SimpleAutomaticGuiSetup.makeDummyGroup(theme, IO.IN, itemInputCount, fluidInputCount);
			case OUT -> SimpleAutomaticGuiSetup.makeDummyGroup(theme, IO.OUT, itemOutputCount, fluidOutputCount);
		});
	}

	private static WidgetGroup makeRootGroup(final @Nullable MachineRecipeType recipeType, final @Nullable ProgressProvider progressProvider, final Function<IO, WidgetGroup> groupFactory) {
		return CAPI.make(new WidgetGroup(0, 0, 0, 0), root -> {
			final IGuiWidget inputGroup = CAPI.make(groupFactory.apply(IO.IN), group -> root.addWidget("in", group));
			final IGuiWidget outputGroup = CAPI.make(groupFactory.apply(IO.OUT), group -> root.addWidget("out", group));
			final IGuiWidget progress;
			if (recipeType != null && progressProvider != null) {
				progress = CAPI.make(new ProgressWidget(
					0, 0, 20, 20,
					new GuiDrawableTexture(recipeType.getGuiArrow()),
					progressProvider,
					recipeType.getGuiArrowDirection()
				), progressWidget -> root.addWidget("progress", progressWidget));
			} else {
				progress = CAPI.make(new MarkerWidget(0, 0, 20, 20),
					progressWidget -> root.addWidget("progress", progressWidget));
			}
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
		return SimpleAutomaticGuiSetup.makeGroup(theme, io, itemSlots, fluidSlots, (slotIndex, x, y) -> {
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
		return SimpleAutomaticGuiSetup.makeGroup(
			theme, io,
			itemSlots, fluidSlots,
			(slotIndex, x, y) -> new MarkerWidget(x, y, 18, 18), (slotIndex, x, y) -> new MarkerWidget(x, y, 18, 18)
		);
	}

	private static WidgetGroup makeGroup(
		final GuiTheme theme, final IO io, final int itemCount, final int fluidCount, final TriFunction<Integer, Integer, Integer, IGuiWidget> itemWidgetFactory,
		final TriFunction<Integer, Integer, Integer, IGuiWidget> fluidWidgetFactory
	) {
		return CAPI.make(new WidgetGroup(0, 0, 0, 0), mainGroup -> {
			final WidgetGroup itemGroup = CAPI.make(new WidgetGroup(0, 0, 0, 0), group -> {
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
			final WidgetGroup fluidGroup = CAPI.make(new WidgetGroup(0, 0, 0, 0), group -> {
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
		final IGuiWidget energyBar = screen.getMenu().getWidgetById("energy");
		if (energyBar != null) {
			energyBar.setPosition(Point.of(rootBounds.x(), rootBounds.maxY() - 6));
		}
		final IGuiWidget root = screen.getMenu().getWidgetById("root");
		if (energyBar == null) {
			root.setBounds(rootBounds);
		} else {
			root.setBounds(MutableRectangle.of(rootBounds.position(), MutableSize.of(
				new ManagedInt(null, rootBounds::width),
				new ManagedInt(null, () -> rootBounds.height() - 8)
			)));
		}
	}
}
