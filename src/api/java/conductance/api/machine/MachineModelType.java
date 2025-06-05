package conductance.api.machine;

import net.minecraft.resources.ResourceLocation;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import conductance.api.tier.Tier;

@SuppressWarnings({"InstantiationOfUtilityClass", "checkstyle:HideUtilityClassConstructor"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MachineModelType<T> {

	public static final MachineModelType<Void> DEFAULT = new MachineModelType<>();
	public static final MachineModelType<ResourceLocation> DEFAULT_WORKABLE = new MachineModelType<>();
	public static final MachineModelType<TypeAndTier> TIERED = new MachineModelType<>();
	public static final MachineModelType<TypeAndTier> TIERED_WORKABLE = new MachineModelType<>();

	public record TypeAndTier(String type, Tier tier) {

	}
}
