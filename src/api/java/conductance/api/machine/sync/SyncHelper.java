package conductance.api.machine.sync;

import java.lang.reflect.Type;
import org.jetbrains.annotations.Nullable;

public interface SyncHelper {

	ManagedDataMap requestDataMap(IManaged managed);

	boolean isEqual(@Nullable Object value1, @Nullable Object value2);

	int getSerializerId(Serializer<?> serializer);

	Serializer<?> getSerializerById(int sid);

	@Nullable
	ReferenceHandler getHandlerByType(Type type);

	Serializer<?> getSerializerByHandler(ReferenceHandler handler);
}
