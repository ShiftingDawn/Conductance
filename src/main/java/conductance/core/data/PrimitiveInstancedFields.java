package conductance.core.data;

import conductance.api.machine.data.handler.ManagedFieldValueHandler;

final class PrimitiveInstancedFields {

	public static class InstancedBoolean extends BaseInstancedField {

		InstancedBoolean(final ManagedFieldWrapper field, final ManagedFieldValueHandler handler, final Object instance) {
			super(field, handler, instance);
		}

		@Override
		public void set(final Object data) {
			try {
				this.getField().getField().setBoolean(this.getInstance(), (Boolean) data);
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Object get() {
			try {
				return this.getField().getField().getBoolean(this.getInstance());
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static class InstancedByte extends BaseInstancedField {

		InstancedByte(final ManagedFieldWrapper field, final ManagedFieldValueHandler handler, final Object instance) {
			super(field, handler, instance);
		}

		@Override
		public void set(final Object data) {
			try {
				this.getField().getField().setByte(this.getInstance(), (Byte) data);
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Object get() {
			try {
				return this.getField().getField().getByte(this.getInstance());
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static class InstancedShort extends BaseInstancedField {

		InstancedShort(final ManagedFieldWrapper field, final ManagedFieldValueHandler handler, final Object instance) {
			super(field, handler, instance);
		}

		@Override
		public void set(final Object data) {
			try {
				this.getField().getField().setShort(this.getInstance(), (Short) data);
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Object get() {
			try {
				return this.getField().getField().getShort(this.getInstance());
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static class InstancedInteger extends BaseInstancedField {

		InstancedInteger(final ManagedFieldWrapper field, final ManagedFieldValueHandler handler, final Object instance) {
			super(field, handler, instance);
		}

		@Override
		public void set(final Object data) {
			try {
				this.getField().getField().setInt(this.getInstance(), (Integer) data);
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Object get() {
			try {
				return this.getField().getField().getInt(this.getInstance());
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static class InstancedLong extends BaseInstancedField {

		InstancedLong(final ManagedFieldWrapper field, final ManagedFieldValueHandler handler, final Object instance) {
			super(field, handler, instance);
		}

		@Override
		public void set(final Object data) {
			try {
				this.getField().getField().setLong(this.getInstance(), (Long) data);
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Object get() {
			try {
				return this.getField().getField().getLong(this.getInstance());
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static class InstancedFloat extends BaseInstancedField {

		InstancedFloat(final ManagedFieldWrapper field, final ManagedFieldValueHandler handler, final Object instance) {
			super(field, handler, instance);
		}

		@Override
		public void set(final Object data) {
			try {
				this.getField().getField().setFloat(this.getInstance(), (Float) data);
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Object get() {
			try {
				return this.getField().getField().getFloat(this.getInstance());
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static class InstancedDouble extends BaseInstancedField {

		InstancedDouble(final ManagedFieldWrapper field, final ManagedFieldValueHandler handler, final Object instance) {
			super(field, handler, instance);
		}

		@Override
		public void set(final Object data) {
			try {
				this.getField().getField().setDouble(this.getInstance(), (Double) data);
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Object get() {
			try {
				return this.getField().getField().getDouble(this.getInstance());
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static class InstancedCharacter extends BaseInstancedField {

		InstancedCharacter(final ManagedFieldWrapper field, final ManagedFieldValueHandler handler, final Object instance) {
			super(field, handler, instance);
		}

		@Override
		public void set(final Object data) {
			try {
				this.getField().getField().setChar(this.getInstance(), (Character) data);
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Object get() {
			try {
				return this.getField().getField().getChar(this.getInstance());
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private PrimitiveInstancedFields() {
	}
}
