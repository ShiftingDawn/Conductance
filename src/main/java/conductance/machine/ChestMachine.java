package conductance.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.jei.IngredientIO;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import conductance.api.NCRecipeElementTypes;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineType;
import conductance.api.machine.capability.MachineCapabilityInventory;
import conductance.api.machine.gui.GuiTextures;
import conductance.api.machine.gui.MachineGuiHolder;
import conductance.api.machine.gui.MachineGuiSupplier;
import conductance.api.util.IOMode;
import conductance.client.GuiHelper;
import conductance.client.MachineUIFactory;
import static conductance.client.GuiHelper.NAME_SLOT_REGEX;

public final class ChestMachine extends MachineBlockEntity<ChestMachine> implements MachineGuiHolder {

	protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(ChestMachine.class, MachineBlockEntity.MANAGED_FIELD_HOLDER);

	@Persisted
	@DescSynced
	private final MachineCapabilityInventory inventory;

	public ChestMachine(final MachineType<ChestMachine> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
		this.inventory = new MachineCapabilityInventory(this, 27);
	}

	@Override
	public ManagedFieldHolder getFieldHolder() {
		return ChestMachine.MANAGED_FIELD_HOLDER;
	}

	@Override
	public ModularUI createUI(final Player entityPlayer) {
		return MachineUIFactory.createGui(this, entityPlayer);
	}

	public static final MachineGuiSupplier GUI_SUPPLIER = new MachineGuiSupplier(() -> {
		WidgetGroup group = new WidgetGroup(0, 0, 18 * 9, 18 * 3);
		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < 9; ++col) {
				SlotWidget slot = new SlotWidget();
				slot.initTemplate();
				slot.setSelfPosition(col * 18, row * 18);
				slot.setId(NCRecipeElementTypes.ITEM.getSlotName(IOMode.INPUT_OUTPUT, col + row * 9));
				group.addWidget(slot);
			}
		}
		group.setSelfPosition(7, 20);
		group.setBackground(GuiTextures.PLAYER_INVENTORY);
		return group;
	}, (template, instance, autoCalc) -> {
		assert instance instanceof ChestMachine;
		ChestMachine chestMachine = (ChestMachine) instance;
		GuiHelper.getWidgetByIdForEach(template, NAME_SLOT_REGEX.formatted(NCRecipeElementTypes.ITEM.getSlotName(IOMode.INPUT_OUTPUT)), SlotWidget.class, slot -> {
			final int index = GuiHelper.getWidgetIndex(slot);
			assert index >= 0 && index < chestMachine.inventory.getSlots();
			slot.setBackgroundTexture(null);
			slot.setHandlerSlot(chestMachine.inventory, index);
			slot.setIngredientIO(IngredientIO.OUTPUT);
			slot.setCanTakeItems(true);
			slot.setCanPutItems(true);
		});
	});
}
