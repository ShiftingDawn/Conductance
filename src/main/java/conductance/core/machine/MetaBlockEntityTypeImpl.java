package conductance.core.machine;

import java.util.Objects;
import net.minecraft.world.level.block.entity.BlockEntityType;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import conductance.api.machine.MetaBlockEntity;
import conductance.api.machine.MetaBlockEntityBlock;
import conductance.api.machine.MetaBlockEntityType;
import conductance.api.registry.RegistryObject;
import conductance.api.resource.RuntimeModelProvider;

public class MetaBlockEntityTypeImpl<T extends MetaBlockEntity<T>> extends RegistryObject<String> implements MetaBlockEntityType<T> {

	@Setter(AccessLevel.PACKAGE)
	private BlockEntry<? extends MetaBlockEntityBlock<T>> block;
	@Setter(AccessLevel.PACKAGE)
	private BlockEntityEntry<T> blockEntityType;
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private RuntimeModelProvider modelProvider;


	public MetaBlockEntityTypeImpl(final String registryKey) {
		super(registryKey);
	}

	/**
	 * Override to add validation after creation. DO NOT FORGET TO CALL SUPER
	 */
	protected void validate() {
		Objects.requireNonNull(this.block, "No block");
		Objects.requireNonNull(this.blockEntityType, "No block entity type");
		Objects.requireNonNull(this.modelProvider, "No model provider");
	}

	@Override
	public NonNullSupplier<? extends MetaBlockEntityBlock<T>> getBlock() {
		return this.block;
	}

	@Override
	public NonNullSupplier<BlockEntityType<T>> getBlockEntityType() {
		return this.blockEntityType;
	}
}
