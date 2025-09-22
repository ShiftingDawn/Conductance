package conductance.api.machine;

import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import conductance.api.CAPI;

public final class MachineGuiHelper {

	public static final Supplier<MenuType<?>> MENU_TYPE = () -> BuiltInRegistries.MENU.getValue(ResourceLocation.fromNamespaceAndPath(CAPI.MOD_ID, "machine"));

	private MachineGuiHelper() {
	}
}
