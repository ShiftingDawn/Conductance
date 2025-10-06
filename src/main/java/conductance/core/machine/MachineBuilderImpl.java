package conductance.core.machine;

import java.util.Objects;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.event.MachineBlockEntityFactory;
import conductance.api.machine.event.MachineBuilder;
import conductance.api.tier.Tier;

final class MachineBuilderImpl<T extends MachineBlockEntity<T>> extends AbstractMachineBuilderImpl<T, MachineBuilder<T>> implements MachineBuilder<T> {

	MachineBuilderImpl(final ResourceLocation registryKey, final MachineBlockEntityFactory<T> blockEntityFactory) {
		super(registryKey, blockEntityFactory);
	}

	@Override
	public MachineBuilder<T> tieredModel(final String machineModelKey, final Tier tier) {
		this.setModelType(ModelType.TIERED);
		this.setModelTypeData(new Object[] {
			Objects.requireNonNull(machineModelKey, "Machine key cannot be null"),
			Objects.requireNonNull(tier, "Tier cannot be null"),
		});
		return this.self();
	}

	@Override
	protected MachineTypeImpl<T> makeMachineType(final ResourceLocation registryKey, final String descriptionId, final MutableComponent name) {
		return new MachineTypeImpl<>(descriptionId, name);
	}
}
