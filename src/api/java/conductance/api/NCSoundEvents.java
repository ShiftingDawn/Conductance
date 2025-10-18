package conductance.api;

import java.util.function.Supplier;
import net.minecraft.sounds.SoundEvent;

@SuppressWarnings("NotNullFieldNotInitialized")
public final class NCSoundEvents {

	public static Supplier<SoundEvent> TOOL_WRENCH;
	public static Supplier<SoundEvent> TOOL_WIRE_CUTTERS;
	public static Supplier<SoundEvent> TOOL_HAMMER;
	public static Supplier<SoundEvent> TOOL_CROWBAR;

	private NCSoundEvents() {
	}
}
