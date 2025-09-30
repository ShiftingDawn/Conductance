package conductance.init.machine;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.Util;
import net.minecraft.world.inventory.Slot;
import net.neoforged.neoforge.items.IItemHandler;
import org.apache.commons.lang3.function.TriFunction;
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
import conductance.api.machine.gui.WidgetGroup;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.util.IO;

public class GenericRecipeMachineGuiSetup extends GuiSetup {

	@Override
	public void addSlots(final MachineMenu menu, final Consumer<Slot> adder) {
		final GenericRecipeMachine machine = (GenericRecipeMachine) menu.getMachine();
		for (int i = 0; i < machine.getInputItems().getSlots(); ++i) {
			adder.accept(new RepositionableSlotItemHandler(machine.getInputItems().getInventory(), i, i * 18, 0));
		}
		for (int i = 0; i < machine.getOutputItems().getSlots(); ++i) {
			adder.accept(new RepositionableSlotItemHandler(machine.getOutputItems().getInventory(), i, i * 18, 0));
		}
	}

	@Override
	public void addWidgets(final MachineScreen screen, final BiConsumer<String, GuiWidget> adder) {
		final GenericRecipeMachine machine = (GenericRecipeMachine) screen.getMachine();
		adder.accept("root", Util.make(GenericRecipeMachineGuiSetup.makeRootGroup(
			this.getTheme(), machine.getInputItems().getInventory(), machine.getOutputItems().getInventory(),
			screen.getMenu(), machine.getRecipeType(), new RecipeHandlerProgressProvider(machine.getRecipeHandler())
		), root -> root.setInitialY(10)));
	}

	public static WidgetGroup makeRootGroup(
		final GuiTheme theme, final IItemHandler inputItems, final IItemHandler outputItems, final MachineMenu menu, final MachineRecipeType recipeType, final ProgressProvider progressProvider
	) {
		return GenericRecipeMachineGuiSetup.makeRootGroup(recipeType, progressProvider, io -> switch (io) {
			case IN -> GenericRecipeMachineGuiSetup.makeGroup(theme, IO.IN, inputItems, menu);
			case OUT -> GenericRecipeMachineGuiSetup.makeGroup(theme, IO.OUT, outputItems, menu);
		});
	}

	public static WidgetGroup makeDummyRootGroup(final GuiTheme theme, final int inputCount, final int outputCount, final MachineRecipeType recipeType, final ProgressProvider progressProvider) {
		return GenericRecipeMachineGuiSetup.makeRootGroup(recipeType, progressProvider, io -> switch (io) {
			case IN -> GenericRecipeMachineGuiSetup.makeDummyGroup(theme, IO.IN, inputCount);
			case OUT -> GenericRecipeMachineGuiSetup.makeDummyGroup(theme, IO.OUT, outputCount);
		});
	}

	private static WidgetGroup makeRootGroup(final MachineRecipeType recipeType, final ProgressProvider progressProvider, final Function<IO, WidgetGroup> groupFactory) {
		return Util.make(new WidgetGroup(0, 0, 0, 0), root -> {
			final GuiWidget groupItemsIn = Util.make(groupFactory.apply(IO.IN), group -> root.addWidget("items_in", group));
			final GuiWidget groupItemsOut = Util.make(groupFactory.apply(IO.OUT), group -> root.addWidget("items_out", group));
			final GuiWidget progress = Util.make(new ProgressWidget(
				new GuiDrawableTexture(recipeType.getGuiArrow()),
				progressProvider,
				recipeType.getGuiArrowDirection(),
				5, 0, 20, 20
			), progressWidget -> root.addWidget("progress", progressWidget));
			final int totalWidth = groupItemsIn.getWidth() + 5 + progress.getWidth() + 5 + groupItemsOut.getWidth();
			final int totalHeight = Math.max(Math.max(groupItemsIn.getHeight(), progress.getHeight()), groupItemsOut.getHeight());
			root.setWidth(totalWidth);
			root.setHeight(totalHeight);
			progress.setInitialX(totalWidth / 2 - 10);
			progress.setInitialY(totalHeight / 2 - 10);
			groupItemsOut.setInitialX(totalWidth - groupItemsOut.getWidth());
		});
	}

	public static WidgetGroup makeGroup(final GuiTheme theme, final IO io, final IItemHandler inv, final MachineMenu menu) {
		return GenericRecipeMachineGuiSetup.makeGroup(theme, io, inv.getSlots(), (slotIndex, x, y) -> {
			final RepositionableSlotItemHandler slot = (RepositionableSlotItemHandler) menu.getSlot(inv, slotIndex);
			slot.setX(x);
			slot.setY(y);
			return new SlotWidget(slot);
		});
	}

	public static WidgetGroup makeDummyGroup(final GuiTheme theme, final IO io, final int slotCount) {
		return GenericRecipeMachineGuiSetup.makeGroup(theme, io, slotCount, (slotIndex, x, y) -> new MarkerWidget(x, y, 18, 18));
	}

	private static WidgetGroup makeGroup(final GuiTheme theme, final IO io, final int slotCount, final TriFunction<Integer, Integer, Integer, GuiWidget> slotWidgetFactory) {
		return Util.make(new WidgetGroup(0, 0, 0, 0), group -> {
			final int cols = slotCount == 4 ? 2 : Math.min(slotCount, 3);
			final int rows = slotCount == 0 ? 0 : slotCount / cols + Math.min(1, slotCount % cols);
			group.setWidth(cols * 18);
			group.setHeight(rows * 18);
			for (int i = 0; i < slotCount; ++i) {
				final String slotName = "items_" + io + "_" + i;
				group.addWidget(slotName, slotWidgetFactory.apply(i, (i % cols) * 18 + 1, (i / cols) * 18 + 1));
			}
			group.setBackground(theme.getItemSlots(slotCount, io == IO.OUT));
		});
	}

	@Override
	public void init(final MachineScreen screen) {
		final GuiWidget root = screen.getWidgetById("root");
		root.setX((screen.getXSize() - root.getWidth()) / 2);
	}
}
