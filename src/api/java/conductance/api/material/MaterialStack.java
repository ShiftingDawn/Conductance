package conductance.api.material;

import java.util.Objects;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
public final class MaterialStack {

	public static final MaterialStack EMPTY = new MaterialStack(null);
	private final Material material;
	private final long count;

	public MaterialStack(final Material material, final long count) {
		this.material = Objects.requireNonNull(material);
		this.count = count;
	}

	private MaterialStack(final Void dummy) {
		this.material = null;
		this.count = 0;
	}

	public MaterialStack copy(final long newCount) {
		return new MaterialStack(this.material, newCount);
	}

	public MaterialStack copy() {
		return this.copy(this.count);
	}

	public boolean isEmpty() {
		return this == MaterialStack.EMPTY || this.material == null || this.count < 1;
	}

	@Override
	public int hashCode() {
		return this.isEmpty() ? 0 : (int) (this.material.getRegistryKey().hashCode() * 31 + this.count);
	}

	@Override
	public boolean equals(final Object o) {
		if (!(o instanceof final MaterialStack stack)) {
			return false;
		}
		if (this.isEmpty()) {
			return stack.isEmpty();
		}
		return !stack.isEmpty() && this.material == stack.material && this.count == stack.count;
	}
}
