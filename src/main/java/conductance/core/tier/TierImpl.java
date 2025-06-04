package conductance.core.tier;

import java.util.Objects;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.neoforge.common.util.Lazy;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import conductance.api.CAPI;
import conductance.api.registry.RegistryObject;
import conductance.api.tier.Tier;
import conductance.api.tier.TieredComponentMap;

final class TierImpl extends RegistryObject<String> implements Tier {

	@Getter
	private final String localizedNameUnformatted;
	@Getter
	private final MutableComponent localizedName;
	@Getter
	private final int color;
	private final Lazy<TieredComponentMap> tieredComponentMap;

	@Setter(AccessLevel.PACKAGE)
	@Nullable
	private Tier prevTier;
	@Setter(AccessLevel.PACKAGE)
	@Nullable
	private Tier nextTier;

	@Getter
	private int index;
	@Getter
	private long voltage;
	@Getter
	private long recipeVoltage;

	TierImpl(final String registryName, final String displayName, final int color, final Supplier<TieredComponentMap> componentMapFactory, @Nullable final Tier prevTier) {
		super(registryName);
		this.localizedNameUnformatted = ChatFormatting.stripFormatting(displayName);
		this.localizedName = Component.literal(displayName);
		this.color = color;
		this.tieredComponentMap = Lazy.of(componentMapFactory);
		this.prevTier = prevTier;
	}

	void recalculate() {
		this.index = TierRegistryImpl.INSTANCE.getIndex(this);
		this.voltage = 32L * (long) Math.pow(4, this.index);
		this.recipeVoltage = 30L * (long) Math.pow(4, this.index);
	}

	@Override
	public boolean isEmpty() {
		return this.getRegistryKey().equals(TierRegistryImpl.ID_EMPTY);
	}

	@Override
	public boolean isMax() {
		return this.getRegistryKey().equals(TierRegistryImpl.ID_MAX);
	}

	@Override
	public Tier getPrevTier() {
		return Objects.requireNonNullElseGet(this.prevTier, () -> CAPI.tiers().empty());
	}

	@Override
	public Tier getNextTier() {
		return Objects.requireNonNullElseGet(this.nextTier, () -> CAPI.tiers().max());
	}

	@Override
	public TieredComponentMap getComponentMap() {
		if (this.tieredComponentMap == null) {
			throw new AssertionError("Missing tiered component map for tier " + this.getRegistryKey());
		}
		return this.tieredComponentMap.get();
	}

	@Override
	public String toString() {
		return "Tier %s[index=%s, prev=%s, next=%s]".formatted(this.getRegistryKey(), this.getIndex(), this.getPrevTier().getRegistryKey(), this.getNextTier().getRegistryKey());
	}
}
