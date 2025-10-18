package conductance.api.machine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import com.mojang.logging.LogUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import conductance.api.CAPI;
import conductance.api.machine.api.IEventListener;
import conductance.api.machine.api.IFeatureBase;
import conductance.api.machine.api.IMachineCapabilityHolder;
import conductance.api.machine.api.IPlacerAware;

public class BaseBlockEntity extends BlockEntity implements IFeatureBase {

	public static final Logger LOGGER = LogUtils.getLogger();
	private final int timerOffset = CAPI.RANDOM.nextInt(20);
	private final List<MachineTick> activeTicks = new ArrayList<>();
	private final List<MachineTick> pendingTicks = new ArrayList<>();

	public BaseBlockEntity(final BlockEntityType<?> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
	}

	//region Runtime
	public final long getTimer() {
		return this.level != null ? this.level.getGameTime() + this.timerOffset : this.timerOffset;
	}

	public final boolean haveTicksPassed(final int tickAmount) {
		return this.getTimer() % tickAmount == 0;
	}

	public final MachineTick addTick(final Runnable action) {
		if (this.getLevel() instanceof final ServerLevel serverLevel) {
			return CAPI.make(new MachineTick(action), tick -> {
				this.pendingTicks.add(tick);
				if (!this.getBlockState().getValue(MachineBlock.TICKING)) {
					final BlockState newState = this.getBlockState().setValue(MachineBlock.TICKING, true);
					serverLevel.setBlockAndUpdate(this.getBlockPos(), newState);
				}
			});
		}
		throw new IllegalStateException("addTick can only be called on the server!");
	}

	public final MachineTick addTick(final Runnable action, @Nullable final MachineTick previousTickInstance) {
		if (previousTickInstance != null && previousTickInstance.isValid()) {
			return previousTickInstance;
		}
		return this.addTick(action);
	}

	final void handleServerTick() {
		if (!this.pendingTicks.isEmpty()) {
			this.activeTicks.addAll(this.pendingTicks);
			this.pendingTicks.clear();
		}
		final Iterator<MachineTick> iterator = this.activeTicks.iterator();
		while (iterator.hasNext()) {
			final MachineTick tick = iterator.next();
			tick.tick();
			if (this.isInvalid()) {
				break;
			}
			if (!tick.isValid()) {
				iterator.remove();
			}
		}
		if (this.isValid() && this.activeTicks.isEmpty() && this.pendingTicks.isEmpty()) {
			this.onServer(level -> level.setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(MachineBlock.TICKING, false)));
		}
	}
	//endregion

	//region Data
	public final void syncToClient() {
		final Level level = this.getLevel();
		if (level != null) {
			final BlockState blockState = this.getBlockState();
			level.sendBlockUpdated(this.getBlockPos(), blockState, blockState, MachineBlock.UPDATE_ALL);
		}
	}
	//endregion

	//region Event
	@Override
	public void onLoad() {
		super.onLoad();
		if (this instanceof final IMachineCapabilityHolder capabilityHolder) {
			for (final MachineCapability capability : capabilityHolder.getCapabilities().values()) {
				capability.onLoad();
			}
		}
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		if (this instanceof final IEventListener eventListener) {
			eventListener.onUnload();
		}
		this.activeTicks.forEach(MachineTick::invalidate);
		this.activeTicks.clear();
		this.pendingTicks.clear();
		if (this instanceof final IMachineCapabilityHolder capabilityHolder) {
			for (final MachineCapability capability : capabilityHolder.getCapabilities().values()) {
				capability.onUnload();
			}
		}
	}
	//endregion

	//region Data
	protected void addAdditionalSyncData(final ValueOutput output) {
	}

	protected void loadAdditionalSyncData(final ValueInput input) {
	}

	@Override
	protected void saveAdditional(final ValueOutput output) {
		super.saveAdditional(output);
		if (this instanceof final IPlacerAware placerAware) {
			final UUID placerUuid = placerAware.getPlacer();
			if (placerUuid != null) {
				output.putString("placer", placerUuid.toString());
			}
		}
		if (this instanceof final IMachineCapabilityHolder capabilityHolder) {
			capabilityHolder.getCapabilities().forEach((key, capability) -> {
				final ValueOutput capOutput = output.child(key);
				capability.serialize(capOutput);
			});
		}
	}

	@Override
	protected void loadAdditional(final ValueInput input) {
		super.loadAdditional(input);
		if (this instanceof final IPlacerAware placerAware) {
			input.getString("placer").ifPresent(placerUuidString -> {
				try {
					final UUID placer = UUID.fromString(placerUuidString);
					placerAware.setPlacer(placer);
				} catch (final IllegalArgumentException e) {
					BaseBlockEntity.LOGGER.error("Could not set placer machine {} at {}: invalid uuid: {}", this.getClass().getSimpleName(), this.getBlockPos(), placerUuidString);
				}
			});
		}
		if (this instanceof final IMachineCapabilityHolder capabilityHolder) {
			capabilityHolder.getCapabilities().forEach((key, capability) -> {
				final ValueInput capInput = input.childOrEmpty(key);
				capability.deserialize(capInput);
			});
		}
	}

	@Override
	public final CompoundTag getUpdateTag(final HolderLookup.Provider registries) {
		return super.saveWithoutMetadata(registries);
	}

	@Override
	public final Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this, (blockEntity, registryAccess) -> {
			//noinspection CheckStyle
			try (final ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(this.problemPath(), BaseBlockEntity.LOGGER)) {
				final TagValueOutput output = TagValueOutput.createWithContext(scopedCollector, registryAccess);
				if (this instanceof final IMachineCapabilityHolder capabilityHolder) {
					capabilityHolder.getCapabilities().forEach((key, capability) -> {
						if (capability.hasChanged()) {
							capability.serialize(output.child(key));
							capability.clearChanged();
						}
					});
				}
				this.addAdditionalSyncData(output);
				return output.buildResult();
			}
		});
	}

	@Override
	public void onDataPacket(final Connection net, final ValueInput valueInput) {
		if (this instanceof final IMachineCapabilityHolder capabilityHolder) {
			capabilityHolder.getCapabilities().forEach((key, capability) -> {
				valueInput.child(key).ifPresent(capability::deserialize);
			});
		}
		this.loadAdditionalSyncData(valueInput);
	}
	//endregion
}
