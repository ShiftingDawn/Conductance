package conductance.core.apiimpl;

import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import conductance.api.registry.RegistryObject;
import conductance.api.util.tier.Tier;

public final class TierImpl extends RegistryObject<String> implements Tier {

	@Getter
	private final String localizedNameUnformatted;
	@Getter
	private final MutableComponent localizedName;

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
	@Getter
	private int color = -1;

	TierImpl(final String registryName, final String displayName, @Nullable final Tier prevTier) {
		super(registryName);
		this.localizedNameUnformatted = ChatFormatting.stripFormatting(displayName);
		this.localizedName = Component.literal(displayName);
		this.prevTier = prevTier;
		if (!registryName.equals(TierRegistryImpl.ID_EMPTY) && !registryName.equals(TierRegistryImpl.ID_MAX)) {
			ApiBridge.getRegs().tiers().register(this);
		}
	}

	void recalculate() {
		this.index = TierRegistryImpl.getIndex(this);
		this.voltage = 32L * (long) Math.pow(4, this.index);
		this.recipeVoltage = 30L * (long) Math.pow(4, this.index);
	}

	@Override
	public boolean isEmpty() {
		return this == TierRegistryImpl.EMPTY;
	}

	@Override
	public boolean isMax() {
		return this == TierRegistryImpl.MAX;
	}

	@Override
	public Tier getPrevTier() {
		return this.prevTier == null ? TierRegistryImpl.EMPTY : this.prevTier;
	}

	@Override
	public Tier getNextTier() {
		return this.nextTier == null ? TierRegistryImpl.MAX : this.nextTier;
	}

	@Override
	public String toString() {
		return "Tier %s[index=%s, prev=%s,next=%s]".formatted(this.getRegistryKey(), this.getIndex(), this.getPrevTier().getRegistryKey(), this.getNextTier().getRegistryKey());
	}

	public static class Builder implements Tier.Builder {

		private final String registryName;
		private final String displayName;
		private final int color;
		private Tier prevTier = TierRegistryImpl.getLastTier();

		public Builder(final String registryName, final String displayName, final int color) {
			this.registryName = registryName;
			this.displayName = displayName;
			this.color = color;
		}

		@Override
		public Builder previous(final Tier tier) {
			this.prevTier = tier;
			return this;
		}

		@Override
		public Tier build() {
			final TierImpl result = new TierImpl(this.registryName, this.displayName, this.prevTier);
			result.color = this.color | 0xff000000;
			return result;
		}
	}
}