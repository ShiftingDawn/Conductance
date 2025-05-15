package conductance.api.machine.sync;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SpecialHandled {

	/**
	 * Method to call to test dirtiness. Must return a boolean and have exactly 1 parameter of this fields type.
	 *
	 * @return the test method name
	 */
	String testDirtyMethod();

	/**
	 * Method to call to serialize the field. Must return a {@link net.minecraft.nbt.CompoundTag} and have exactly 1 parameter of this fields type.
	 *
	 * @return the serialize method name
	 */
	String serializeMethod();

	/**
	 * Method to call to deserialize the field. Must return an object of this fields type (or null) and have exactly 1 parameter of type {@link net.minecraft.nbt.CompoundTag}.
	 *
	 * @return the deserialize method name
	 */
	String deserializeMethod();
}
