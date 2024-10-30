package conductance.core.data;

import conductance.api.plugin.ManagedFieldValueHandlerRegister;

public final class ConductanceFieldValueHandlers {

	public static void init(final ManagedFieldValueHandlerRegister register) {
		ConductanceFieldValueHandlers.registerPrimitives(register);

		register.register(new RegistryFieldValueHandlers.HandlerBlock());
		register.register(new RegistryFieldValueHandlers.HandlerItem());
		register.register(new RegistryFieldValueHandlers.HandlerFluid());

		register.register(new SimpleObjectFieldValueHandlers.HandlerString());
		register.register(new SimpleObjectDataSerializers.SerializerString());
		register.register(new SimpleObjectFieldValueHandlers.HandlerResourceLocation());
		register.register(new SimpleObjectDataSerializers.SerializerResourceLocation());
		register.register(new SimpleObjectFieldValueHandlers.HandlerUUID());
		register.register(new SimpleObjectDataSerializers.SerializerUUID());
		register.register(new SimpleObjectFieldValueHandlers.HandlerBlockState());
		register.register(new SimpleObjectDataSerializers.SerializerBlockState());
	}

	private static void registerPrimitives(final ManagedFieldValueHandlerRegister register) {
		register.register(new PrimitiveFieldValueHandler.HandlerBoolean());
		register.register(new PrimitiveDataSerializers.SerializerBoolean());

		register.register(new PrimitiveFieldValueHandler.HandlerByte());
		register.register(new PrimitiveDataSerializers.SerializerByte());

		register.register(new PrimitiveFieldValueHandler.HandlerShort());
		register.register(new PrimitiveDataSerializers.SerializerShort());

		register.register(new PrimitiveFieldValueHandler.HandlerInteger());
		register.register(new PrimitiveDataSerializers.SerializerInteger());

		register.register(new PrimitiveFieldValueHandler.HandlerLong());
		register.register(new PrimitiveDataSerializers.SerializerLong());

		register.register(new PrimitiveFieldValueHandler.HandlerFloat());
		register.register(new PrimitiveDataSerializers.SerializerFloat());

		register.register(new PrimitiveFieldValueHandler.HandlerDouble());
		register.register(new PrimitiveDataSerializers.SerializerDouble());

		register.register(new PrimitiveFieldValueHandler.HandlerCharacter());
		register.register(new PrimitiveDataSerializers.SerializerCharacter());

		register.register(new EnumFieldValueHandler());
		register.register(new EnumSerializer());
	}

	private ConductanceFieldValueHandlers() {
	}
}
