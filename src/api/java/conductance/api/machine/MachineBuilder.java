package conductance.api.machine;

public interface MachineBuilder<T extends MetaBlockEntity<T>> {

	MetaBlockEntityType<T> build();
}
