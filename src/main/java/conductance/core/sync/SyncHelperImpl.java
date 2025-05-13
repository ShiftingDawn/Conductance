package conductance.core.sync;

import java.lang.reflect.Type;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.sync.Checker;
import conductance.api.machine.sync.IManaged;
import conductance.api.machine.sync.ManagedDataMap;
import conductance.api.machine.sync.ReferenceHandler;
import conductance.api.machine.sync.Serializer;
import conductance.api.machine.sync.SyncHelper;

public final class SyncHelperImpl implements SyncHelper {

	public static final SyncHelperImpl INSTANCE = new SyncHelperImpl();

	private SyncHelperImpl() {
	}

	@Override
	public ManagedDataMap requestDataMap(final IManaged managed) {
		return new ManagedDataMapImpl(managed);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean isEqual(@Nullable final Object value1, @Nullable final Object value2) {
		if ((value1 == null) != (value2 == null)) {
			return false;
		}
		if (value1 == null) {
			return true;
		}
		if (!value1.getClass().isAssignableFrom(value2.getClass()) && !value2.getClass().isAssignableFrom(value1.getClass())) {
			return false;
		}
		final Checker checker1 = SyncFieldSerializerRegisterImpl.INSTANCE.getChecker(value1.getClass());
		final Checker checker2 = SyncFieldSerializerRegisterImpl.INSTANCE.getChecker(value2.getClass());
		if (checker1 != checker2) {
			return false;
		}
		return checker1 == null ? Objects.equals(value1, value2) : checker1.equals(value1, value2) && checker2.equals(value1, value2);
	}

	@Override
	public int getSerializerId(final Serializer<?> serializer) {
		return SyncFieldSerializerRegisterImpl.INSTANCE.getSerializerId(serializer);
	}

	@Override
	public Serializer<?> getSerializerById(final int sid) {
		return SyncFieldSerializerRegisterImpl.INSTANCE.getSerializerById(sid);
	}

	@Override
	@Nullable
	public ReferenceHandler getHandlerByType(final Type type) {
		return SyncFieldSerializerRegisterImpl.INSTANCE.getHandler(type);
	}

	@Override
	public Serializer<?> getSerializerByHandler(final ReferenceHandler handler) {
		return SyncFieldSerializerRegisterImpl.INSTANCE.getSerializerByHandler(handler);
	}
}
