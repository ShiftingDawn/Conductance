package conductance.core.tier;

import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import conductance.api.CAPI;
import conductance.api.tier.Tier;
import conductance.api.tier.TieredComponentMap;
import conductance.api.util.Lazy;
import conductance.Conductance;

@RequiredArgsConstructor
final class TierImpl implements Tier {

	private final Lazy<String> descriptionId = Lazy.of(() -> Util.makeDescriptionId("tier", this.getId()));
	private final Lazy<Component> description = Lazy.of(() -> Component.translatable(this.getDescriptionId()));
	private final @Getter int color;
	private final Lazy<TieredComponentMap> componentMap;
	private @Getter int index;
	private @Getter long voltage;

	@Setter(AccessLevel.PACKAGE)
	private @Nullable Tier prevTier;
	@Setter(AccessLevel.PACKAGE)
	private @Nullable Tier nextTier;

	void recalculate() {
		this.index = Conductance.TIERS.getIndex(this);
		this.voltage = 32L * (long) Math.pow(4, this.index);
	}

	@Override
	public boolean isEmpty() {
		return this == CAPI.tiers().empty();
	}

	@Override
	public boolean isMax() {
		return this == CAPI.tiers().max();
	}

	@Override
	public Tier getPreviousTier() {
		return Objects.requireNonNullElseGet(this.prevTier, CAPI.tiers()::empty);
	}

	@Override
	public Tier getNextTier() {
		return Objects.requireNonNullElseGet(this.nextTier, CAPI.tiers()::max);
	}

	@Override
	public TieredComponentMap getComponentMap() {
		return this.componentMap.get();
	}

	@Override
	public String getDescriptionId() {
		return this.descriptionId.get();
	}

	@Override
	public Component getName() {
		return this.description.get();
	}

	@Override
	public ResourceLocation getId() {
		if (this.isEmpty()) {
			return Conductance.id("empty");
		}
		if (this.isMax()) {
			return Conductance.id("max");
		}
		return Tier.super.getId();
	}

	@Override
	public String toString() {
		return "Tier %s[index=%s, prev=%s, next=%s]".formatted(this.getId(), this.getIndex(), this.getPreviousTier().getId(), this.getNextTier().getNextTier());
	}
}
