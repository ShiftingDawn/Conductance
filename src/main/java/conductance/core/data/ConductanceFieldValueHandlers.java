package conductance.core.data;

import conductance.api.plugin.ManagedFieldValueHandlerRegister;

public final class ConductanceFieldValueHandlers {

	public static void init(final ManagedFieldValueHandlerRegister register) {
		register.register(new PrimitiveFieldValueHandler.HandlerBoolean());
		register.register(new PrimitiveFieldValueHandler.HandlerByte());
		register.register(new PrimitiveFieldValueHandler.HandlerShort());
		register.register(new PrimitiveFieldValueHandler.HandlerInteger());
		register.register(new PrimitiveFieldValueHandler.HandlerLong());
		register.register(new PrimitiveFieldValueHandler.HandlerFloat());
		register.register(new PrimitiveFieldValueHandler.HandlerDouble());
		register.register(new PrimitiveFieldValueHandler.HandlerCharacter());

		register.register(new SimpleObjectFieldValueHandlers.HandlerString());
		register.register(new SimpleObjectFieldValueHandlers.HandlerResourceLocation());
		register.register(new SimpleObjectFieldValueHandlers.HandlerUUID());

	}

	private ConductanceFieldValueHandlers() {
	}
}
