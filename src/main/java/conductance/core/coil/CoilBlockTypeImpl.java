package conductance.core.coil;

import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import conductance.api.coil.CoilBlockType;
import conductance.api.util.Lazy;
import conductance.Conductance;

final class CoilBlockTypeImpl implements CoilBlockType {

	private final Lazy<String> descriptionId = Lazy.of(() -> Util.makeDescriptionId("coilBlockType", this.getId()));
	private final Lazy<Component> description = Lazy.of(() -> Component.translatable(this.getDescriptionId()));
	private final @Getter int color;
	private @Getter int index;
	private @Getter int temperature;

	CoilBlockTypeImpl(final int color) {
		this.color = color;
	}

	void recalculate() {
		this.index = Conductance.COILS.getIndex(this);
		this.temperature = 500 * (this.index + 1);
	}

	@Setter(AccessLevel.PACKAGE)
	private @Nullable CoilBlockTypeImpl prevCoil;
	@Setter(AccessLevel.PACKAGE)
	private @Nullable CoilBlockTypeImpl nextCoil;

	@Nullable
	CoilBlockTypeImpl prevCoil() {
		return this.prevCoil;
	}

	@Override
	public boolean isFirst() {
		return this == Conductance.COILS.first();
	}

	@Override
	public boolean isLast() {
		return this == Conductance.COILS.last();
	}

	@Override
	public CoilBlockType getPreviousCoil() {
		return Objects.requireNonNullElse(this.prevCoil, Conductance.COILS.first());
	}

	@Override
	public CoilBlockType getNextCoil() {
		return Objects.requireNonNullElse(this.nextCoil, Conductance.COILS.last());
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
	public String toString() {
		return "Coil[" + this.getId() + "]";
	}
}
