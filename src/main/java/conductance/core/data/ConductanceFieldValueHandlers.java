package conductance.core.data;

import conductance.api.machine.data.serializer.PrimitiveDataSerializer;
import conductance.api.plugin.ManagedFieldValueHandlerRegister;

public final class ConductanceFieldValueHandlers {

	public static void init(final ManagedFieldValueHandlerRegister register) {
		ConductanceFieldValueHandlers.registerPrimitives(register);
	}

	private static void registerPrimitives(final ManagedFieldValueHandlerRegister register) {
		register.register(PrimitiveDataSerializer.BooleanSerializer.class, PrimitiveDataSerializer.BooleanSerializer::new, new PrimitiveValueHandler.BooleanHandler());
		register.register(PrimitiveDataSerializer.ByteSerializer.class, PrimitiveDataSerializer.ByteSerializer::new, new PrimitiveValueHandler.ByteHandler());
		register.register(PrimitiveDataSerializer.ShortSerializer.class, PrimitiveDataSerializer.ShortSerializer::new, new PrimitiveValueHandler.ShortHandler());
		register.register(PrimitiveDataSerializer.IntegerSerializer.class, PrimitiveDataSerializer.IntegerSerializer::new, new PrimitiveValueHandler.IntegerHandler());
		register.register(PrimitiveDataSerializer.LongSerializer.class, PrimitiveDataSerializer.LongSerializer::new, new PrimitiveValueHandler.LongHandler());
		register.register(PrimitiveDataSerializer.FloatSerializer.class, PrimitiveDataSerializer.FloatSerializer::new, new PrimitiveValueHandler.FloatHandler());
		register.register(PrimitiveDataSerializer.DoubleSerializer.class, PrimitiveDataSerializer.DoubleSerializer::new, new PrimitiveValueHandler.DoubleHandler());
		register.register(PrimitiveDataSerializer.CharacterSerializer.class, PrimitiveDataSerializer.CharacterSerializer::new, new PrimitiveValueHandler.CharacterHandler());

//		register.register(new EnumFieldValueHandler());
//		register.register(new EnumSerializer());
	}

	private ConductanceFieldValueHandlers() {
	}
}
