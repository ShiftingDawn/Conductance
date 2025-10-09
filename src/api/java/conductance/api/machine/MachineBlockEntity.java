package conductance.api.machine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import com.mojang.logging.LogUtils;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import conductance.api.CAPI;
import conductance.api.NCBlockStateProperties;
import conductance.api.util.Internal;

public class MachineBlockEntity<T extends MachineBlockEntity<T>> extends BlockEntity {

	public static final Logger LOGGER = LogUtils.getLogger();
	private final int timerOffset = CAPI.RANDOM.nextInt(20);
	private final @Getter Map<String, MachineCapability> capabilities = new ConcurrentHashMap<>();
	private final @Getter MachineType<T> machineType;
	private final List<MachineTick> ticksActive = new ArrayList<>();
	private final List<MachineTick> ticksPending = new ArrayList<>();
	private @Getter boolean currentlyWorking = false;

	public MachineBlockEntity(final MachineType<T> type, final BlockPos pos, final BlockState blockState) {
		super(type.getBlockEntityType().get(), pos, blockState);
		this.machineType = type;
	}

	//region Capabilities
	public final void registerCapability(final String key, final MachineCapability capability) {
		if (this.capabilities.containsKey(key) || this.capabilities.containsValue(capability)) {
			return;
		}
		this.capabilities.put(key, capability);
	}

	public final <C extends MachineCapability> Optional<C> getCapability(final Class<C> type, final boolean exact) {
		for (final MachineCapability capability : this.capabilities.values()) {
			if (exact) {
				if (type.equals(capability.getClass())) {
					return Optional.of(type.cast(capability));
				}
			} else if (type.isAssignableFrom(capability.getClass())) {
				return Optional.of(type.cast(capability));
			}
		}
		return Optional.empty();
	}

	public final <C extends MachineCapability> Optional<C> getCapability(final Class<C> type) {
		return this.getCapability(type, false);
	}

	public Optional<IItemHandler> getItemTransferCapability(@Nullable final Direction side, final CapabilityMode mode) {
		final List<IItemHandlerModifiable> itemHandlers = new ArrayList<>();
		for (final MachineCapability capability : this.capabilities.values()) {
			if (capability instanceof final IItemHandlerModifiable itemHandler && capability.isValid(side)) {
				if (mode == CapabilityMode.INTERNAL) {
					IItemHandlerModifiable handler = itemHandler;
					if (itemHandler instanceof final MachineRecipeCapabilityItems recipeCapabilityItems) {
						handler = recipeCapabilityItems.getInventory();
					}
					itemHandlers.add(handler);
				} else {
					itemHandlers.add(itemHandler);
				}
			}
		}
		if (itemHandlers.isEmpty()) {
			return Optional.empty();
		}
		final CapIO io = CapIO.BOTH;
		final IOItemHandlerList handlerList = new IOItemHandlerList(itemHandlers, io);
		return Optional.of(handlerList);
	}

	public Optional<IFluidHandler> getFluidTransferCapability(@Nullable final Direction side, final CapabilityMode mode) {
		final List<IFluidHandler> fluidHandlers = new ArrayList<>();
		for (final MachineCapability capability : this.capabilities.values()) {
			if (capability instanceof final IFluidHandler fluidHandler && capability.isValid(side)) {
				//TODO handle internal mode
				fluidHandlers.add(fluidHandler);
			}
		}
		if (fluidHandlers.isEmpty()) {
			return Optional.empty();
		}
		final CapIO io = CapIO.BOTH;
		final IOFluidHandlerList handlerList = new IOFluidHandlerList(fluidHandlers, io);
		return Optional.of(handlerList);
	}
	//endregion

	//region Event
	public final MachineTick addTick(final Runnable action) {
		if (this.level instanceof final ServerLevel serverLevel) {
			return Util.make(new MachineTick(action), result -> {
				this.ticksPending.add(result);
				if (!this.getBlockState().getValue(MachineBlock.TICKING)) {
					final BlockState newState = this.getBlockState().setValue(MachineBlock.TICKING, true);
					serverLevel.setBlockAndUpdate(this.getBlockPos(), newState);
				}
			});
		}
		throw new IllegalStateException("addTick can only be called on the server!");
	}

	public final MachineTick addTick(final Runnable action, @Nullable final MachineTick previous) {
		if (previous == null || !previous.isValid()) {
			return this.addTick(action);
		}
		return previous;
	}

	public void setWorkingState(final boolean working) {
		if (this.currentlyWorking == working) {
			return;
		}
		this.currentlyWorking = working;
		if (this.isServerSide() && this.getBlockState().hasProperty(NCBlockStateProperties.WORKING)) {
			final boolean current = this.getBlockState().getValue(NCBlockStateProperties.WORKING);
			if (working != current) {
				assert this.level != null; //Handled by isServerSide()
				this.level.setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(NCBlockStateProperties.WORKING, working));
			}
		}
	}

	final void handleServerTick() {
		if (!this.ticksPending.isEmpty()) {
			this.ticksActive.addAll(this.ticksPending);
			this.ticksPending.clear();
		}
		final Iterator<MachineTick> iterator = this.ticksActive.iterator();
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
		if (this.isValid() && this.ticksActive.isEmpty() && this.ticksPending.isEmpty()) {
			assert this.level != null;
			this.level.setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(MachineBlock.TICKING, false));
		}
	}

	public void onClientTick() {
	}

	@Override
	public void onLoad() {
		super.onLoad();
		this.currentlyWorking = this.getBlockState().getValueOrElse(NCBlockStateProperties.WORKING, false);
		for (final MachineCapability capability : this.capabilities.values()) {
			capability.onLoad();
		}
	}

	public void onUnload() {
		this.ticksActive.forEach(MachineTick::invalidate);
		this.ticksActive.clear();
		for (final MachineCapability capability : this.capabilities.values()) {
			capability.onUnload();
		}
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		this.onUnload();
	}
	//endregion

	//region Data
	@Override
	protected void saveAdditional(final ValueOutput output) {
		super.saveAdditional(output);
		this.capabilities.forEach((key, cap) -> cap.serialize(output.child(key)));
	}

	@Override
	protected void loadAdditional(final ValueInput input) {
		super.loadAdditional(input);
		this.capabilities.forEach((key, cap) -> input.child(key).ifPresent(cap::deserialize));
	}

	@Override
	public CompoundTag getUpdateTag(final HolderLookup.Provider registries) {
		return super.saveWithoutMetadata(registries);
	}

	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this, (blockEntity, registryAccess) -> {
			try (final ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(this.problemPath(), MachineBlockEntity.LOGGER)) {
				final TagValueOutput output = TagValueOutput.createWithContext(scopedCollector, registryAccess);
				this.capabilities.forEach((key, capability) -> {
					if (capability.hasChanged()) {
						capability.serialize(output.child(key));
						capability.clearChanged();
					}
				});
				return output.buildResult();
			}
		});
	}

	@Override
	public void onDataPacket(final Connection net, final ValueInput valueInput) {
		this.capabilities.forEach((key, capability) -> {
			valueInput.child(key).ifPresent(capability::deserialize);
		});
	}

	public final void syncToClient() {
		if (this.getLevel() == null) {
			return;
		}
		final BlockState state = this.getBlockState();
		this.getLevel().sendBlockUpdated(this.getBlockPos(), state, state, MachineBlock.UPDATE_ALL);
	}

	public final void sendToClient(final int requestId, final @Nullable Consumer<ValueOutput> packetFiller) {
		if (this.level instanceof final ServerLevel serverLevel) {
			Internal.MACHINE_RPC_PACKET_SENDER.accept(this, serverLevel, payload -> {
				payload.putInt("r", requestId);
				if (packetFiller != null) {
					packetFiller.accept(payload.child("d"));
				}
			});
		}
	}

	public final void sendToServer(final int requestId, final @Nullable Consumer<ValueOutput> packetFiller) {
		if (this.isClientSide()) {
			Internal.MACHINE_RPC_PACKET_SENDER.accept(this, null, payload -> {
				payload.putInt("r", requestId);
				if (packetFiller != null) {
					packetFiller.accept(payload.child("d"));
				}
			});
		}
	}

	protected void handleServerRequest(final int requestId, final ValueInput input) {
	}

	protected void handleClientRequest(final int requestId, final ValueInput input) {
	}

	private void handleServerRequestPacket(final ValueInput input) {
		this.handleRpcPacket(false, input);
	}

	private void handleClientRequestPacket(final ValueInput input) {
		this.handleRpcPacket(true, input);
	}

	private void handleRpcPacket(final boolean isServer, final ValueInput input) {
		final int req = input.getInt("r").orElseThrow(() -> new IllegalStateException("Missing request id"));
		final ValueInput data = input.childOrEmpty("d");
		if (isServer) {
			this.handleClientRequest(req, data);
		} else {
			this.handleServerRequest(req, data);
		}
	}
	//endregion

	//region Helpers
	public final long getTimerOffset() {
		return this.level != null ? this.level.getGameTime() + this.timerOffset : this.timerOffset;
	}

	public final boolean haveTicksPassed(final int tickAmount) {
		return this.getTimerOffset() % tickAmount == 0;
	}

	public final boolean isValid() {
		return !this.isRemoved();
	}

	public final boolean isInvalid() {
		return this.isRemoved();
	}

	public final boolean isClientSide() {
		final Level level = this.getLevel();
		return level != null && level.isClientSide;
	}

	public final void onClient(final Consumer<Level> callback) {
		if (this.isClientSide()) {
			callback.accept(this.level);
		}
	}

	public final boolean isServerSide() {
		final Level level = this.getLevel();
		return level != null && !level.isClientSide;
	}

	public final void onServer(final Consumer<ServerLevel> callback) {
		if (this.level instanceof final ServerLevel serverLevel) {
			callback.accept(serverLevel);
		}
	}
	//endregion

	static {
		Internal.MACHINE_RPC_PACKET_RECEIVER_SERVER = machine -> machine::handleClientRequestPacket;
		Internal.MACHINE_RPC_PACKET_RECEIVER_CLIENT = machine -> machine::handleServerRequestPacket;
	}
}
