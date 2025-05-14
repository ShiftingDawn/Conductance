package conductance.init;

import java.util.UUID;
import conductance.api.machine.recipe.IRecipe;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.plugin.SyncFieldSerializerRegister;
import conductance.api.util.tier.Tier;
import conductance.core.sync.handlers.BooleanHandler;
import conductance.core.sync.handlers.ByteHandler;
import conductance.core.sync.handlers.CharHandler;
import conductance.core.sync.handlers.DoubleHandler;
import conductance.core.sync.handlers.EnumHandler;
import conductance.core.sync.handlers.FloatHandler;
import conductance.core.sync.handlers.IntHandler;
import conductance.core.sync.handlers.LongHandler;
import conductance.core.sync.handlers.ManagedHandler;
import conductance.core.sync.handlers.NBTSerializableHandler;
import conductance.core.sync.handlers.ShortHandler;
import conductance.core.sync.serializers.ArraySerializer;
import conductance.core.sync.serializers.ManagedSerializer;
import conductance.core.sync.serializers.PrimitiveCodecSerializer;
import conductance.core.sync.serializers.RecipeSerializer;
import conductance.core.sync.serializers.RecipeTypeSerializer;
import conductance.core.sync.serializers.TagSerializer;
import conductance.core.sync.serializers.TierSerializer;
import conductance.core.sync.serializers.UUIDSerializer;

public final class ConductanceSyncFieldSerializers {

	public static void init(final SyncFieldSerializerRegister register) {
		register.register(PrimitiveCodecSerializer.BooleanSerializer.class, PrimitiveCodecSerializer.BooleanSerializer::new, new BooleanHandler());
		register.register(PrimitiveCodecSerializer.ByteSerializer.class, PrimitiveCodecSerializer.ByteSerializer::new, new ByteHandler());
		register.register(PrimitiveCodecSerializer.ShortSerializer.class, PrimitiveCodecSerializer.ShortSerializer::new, new ShortHandler());
		register.register(PrimitiveCodecSerializer.IntSerializer.class, PrimitiveCodecSerializer.IntSerializer::new, new IntHandler());
		register.register(PrimitiveCodecSerializer.LongSerializer.class, PrimitiveCodecSerializer.LongSerializer::new, new LongHandler());
		register.register(PrimitiveCodecSerializer.FloatSerializer.class, PrimitiveCodecSerializer.FloatSerializer::new, new FloatHandler());
		register.register(PrimitiveCodecSerializer.DoubleSerializer.class, PrimitiveCodecSerializer.DoubleSerializer::new, new DoubleHandler());
		register.register(PrimitiveCodecSerializer.CharSerializer.class, PrimitiveCodecSerializer.CharSerializer::new, new CharHandler());
		register.register(PrimitiveCodecSerializer.StringSerializer.class, PrimitiveCodecSerializer.StringSerializer::new, new CharHandler());
		register.register(ArraySerializer.class, ArraySerializer::new);
		register.register(UUIDSerializer.class, UUIDSerializer::new, UUID.class, true);
		register.register(PrimitiveCodecSerializer.StringSerializer.class, PrimitiveCodecSerializer.StringSerializer::new, new EnumHandler());

		register.register(ManagedSerializer.class, ManagedSerializer::new, new ManagedHandler());
		register.register(TagSerializer.class, TagSerializer::new, new NBTSerializableHandler());

		register.register(TierSerializer.class, TierSerializer::new, Tier.class, false);
		register.register(RecipeTypeSerializer.class, RecipeTypeSerializer::new, NCRecipeType.class, false);
		register.register(RecipeSerializer.class, RecipeSerializer::new, IRecipe.class, false);
	}

	private ConductanceSyncFieldSerializers() {
	}
}
