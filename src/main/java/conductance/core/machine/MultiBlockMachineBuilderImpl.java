package conductance.core.machine;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.machine.event.MachineBlockEntityFactory;
import conductance.api.machine.event.MultiBlockMachineBuilder;
import conductance.api.machine.multi.MultiBlockStructureBuilder;
import conductance.api.machine.multi.MultiMachineBlockEntity;
import conductance.api.machine.multi.MultiBlockControllerGuiSetup;

final class MultiBlockMachineBuilderImpl<T extends MultiMachineBlockEntity<T>> extends AbstractMachineBuilderImpl<T, MultiBlockMachineBuilder<T>> implements MultiBlockMachineBuilder<T> {

	private char controllerChar = 0;
	private @Nullable Consumer<MultiBlockStructureBuilder> structureFactory;
	private @Nullable Supplier<BlockState> casingAppearance;

	MultiBlockMachineBuilderImpl(final ResourceLocation registryKey, final MachineBlockEntityFactory<T> blockEntityFactory) {
		super(registryKey, blockEntityFactory);
		this.setGuiSetup(new MultiBlockControllerGuiSetup());
	}

	@Override
	public MultiBlockMachineBuilder<T> structure(final char controllerChar, final Consumer<MultiBlockStructureBuilder> builder) {
		this.controllerChar = controllerChar;
		this.structureFactory = builder;
		return this.self();
	}

	@Override
	public MultiBlockMachineBuilder<T> casingAppearance(final Supplier<BlockState> appearance) {
		this.casingAppearance = appearance;
		return this.self();
	}

	@Override
	protected MachineTypeImpl<T> makeMachineType(final ResourceLocation registryKey, final String descriptionId, final MutableComponent name) {
		Objects.requireNonNull(this.structureFactory, "No structure set");
		return new MultiMachineTypeImpl<>(descriptionId, name);
	}

	@SuppressWarnings("DataFlowIssue")
	@Override
	public MultiMachineTypeImpl<T> build(final ResourceLocation registryKey) {
		return CAPI.make((MultiMachineTypeImpl<T>) super.build(registryKey), type -> {
			type.setStructure(CAPI.make(new MultiBlockStructureBuilderImpl(this.controllerChar, () -> type.getBlock().get()), this.structureFactory).build());
			type.setCasingAppearance(this.casingAppearance);
		});
	}
}
