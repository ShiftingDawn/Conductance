package conductance.api.machine.gui;

import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import conductance.api.CAPI;

public final class MachineGuiHelper {

	public static final Supplier<MenuType<?>> MENU_TYPE = () -> BuiltInRegistries.MENU.getValue(ResourceLocation.fromNamespaceAndPath(CAPI.MOD_ID, "machine"));

	public static final Size DEFAULT_CONTAINER_SIZE = new Size(176, 166);
	public static final Coordinate DEFAULT_INVENTORY_POS = new Coordinate(8, 87);
	public static final Coordinate DEFAULT_HOTBAR_POS = new Coordinate(8, 143);

	private MachineGuiHelper() {
	}
}
