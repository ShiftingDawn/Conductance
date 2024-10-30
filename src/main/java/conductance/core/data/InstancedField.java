package conductance.core.data;

public final class InstancedField<T> {

	private final ManagedFieldWrapper field;

	private InstancedField(final ManagedFieldWrapper field) {
		this.field = field;
	}

	static <T> InstancedField<T> of(ManagedFieldWrapper wrapper) {

	}
}
