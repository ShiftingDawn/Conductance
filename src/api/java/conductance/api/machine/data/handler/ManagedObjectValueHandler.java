package conductance.api.machine.data.handler;

public abstract class ManagedObjectValueHandler implements ManagedFieldValueHandler {

	private final Class<?> typeClass;
	private final boolean shallowHandlerCheck;

	protected ManagedObjectValueHandler(final Class<?> typeClass, final boolean shallowHandlerCheck) {
		this.typeClass = typeClass;
		this.shallowHandlerCheck = shallowHandlerCheck;
	}

	@Override
	public boolean canHandle(final Class<?> type) {
		return this.shallowHandlerCheck ? type == this.typeClass : this.typeClass.isAssignableFrom(type);
	}
}
