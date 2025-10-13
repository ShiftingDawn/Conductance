package conductance.api.tier;

import java.util.Locale;
import java.util.Objects;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;

public enum TieredItemType {

	CIRCUIT,
	ADVANCED_CIRCUIT("advanced_%s_circuit"),
	ELECTRIC_MOTOR,
	ELECTRIC_PISTON,
	CONVEYOR_MODULE,
	ELECTRIC_PUMP,
	ROBOT_ARM,
	MACHINE_CASING;

	private final String name = super.toString().toLowerCase(Locale.ROOT);
	private final String unlocalizedNameFactory;
	private final @Getter String descriptionId;

	TieredItemType(@Nullable final String unlocalizedNameFactory) {
		this.unlocalizedNameFactory = Objects.requireNonNullElseGet(unlocalizedNameFactory, () -> "%s_" + this.name);
		this.descriptionId = Util.makeDescriptionId("tieredItemType", ResourceLocation.fromNamespaceAndPath(CAPI.MOD_ID, this.name));
	}

	TieredItemType() {
		this(null);
	}

	public boolean isItem() {
		return this != TieredItemType.MACHINE_CASING;
	}

	public boolean isBlock() {
		return this == TieredItemType.MACHINE_CASING;
	}

	public String getUnlocalizedName(final Tier tier) {
		return this.unlocalizedNameFactory.formatted(tier.getId().getPath());
	}

	@Override
	public String toString() {
		return this.name;
	}
}
