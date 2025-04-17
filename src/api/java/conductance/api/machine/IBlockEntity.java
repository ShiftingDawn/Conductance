package conductance.api.machine;

public interface IBlockEntity extends RunnableContainer, EnvironmentProvider {

	boolean isInvalid();

	default boolean isValid() {
		return !this.isInvalid();
	}
}
