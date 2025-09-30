package conductance.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import conductance.Conductance;
import conductance.init.item.ProgramCircuitMenu;
import conductance.init.item.ProgramCircuitScreen;

public final class ConductanceMenuTypes {

	private static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, Conductance.MODID);
	public static final DeferredHolder<MenuType<?>, MenuType<ProgramCircuitMenu>> PROGRAM_CIRCUIT = ConductanceMenuTypes.REGISTRY.register("program_circuit", () -> IMenuTypeExtension.create((id, player, buf) -> {
		InteractionHand hand = buf.readEnum(InteractionHand.class);
		return new ProgramCircuitMenu(id, player.player, hand, DataSlot.standalone());
	}));

	public static void initialize(final IEventBus modEventBus) {
		ConductanceMenuTypes.REGISTRY.register(modEventBus);
		modEventBus.addListener(RegisterMenuScreensEvent.class, ConductanceMenuTypes::registerMenuScreens);
	}

	private static void registerMenuScreens(final RegisterMenuScreensEvent event) {
		event.register(ConductanceMenuTypes.PROGRAM_CIRCUIT.get(), ProgramCircuitScreen::new);
	}

	private ConductanceMenuTypes() {
	}
}
