package conductance.api.util;

import java.util.Locale;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.tier.Tier;

public enum TieredItemType {

	CIRCUIT,
	ADVANCED_CIRCUIT("advanced_%s_circuit", "Advanced %s Circuit"),
	ELECTRIC_MOTOR,
	ELECTRIC_PISTON,
	CONVEYOR_MODULE,
	ELECTRIC_PUMP,
	ROBOT_ARM;

	private final String name = super.toString().toLowerCase(Locale.ROOT);
	@Getter
	private final String unlocalizedNameFactory;
	@Getter
	private final String localizedNameFactory;

	TieredItemType(@Nullable final String unlocalizedNameFactory, @Nullable final String localizedNameFactory) {
		this.unlocalizedNameFactory = unlocalizedNameFactory == null ? "%s_" + this.name : unlocalizedNameFactory;
		this.localizedNameFactory = localizedNameFactory == null ? "%s " + TextHelper.lowerUnderscoreToEnglish(this.name) : localizedNameFactory;
	}

	TieredItemType() {
		this(null, null);
	}

	public String makeUnlocalizedName(final Tier tier) {
		return this.getUnlocalizedNameFactory().formatted(tier.getRegistryKey());
	}

	@Override
	public String toString() {
		return this.name;
	}
}
