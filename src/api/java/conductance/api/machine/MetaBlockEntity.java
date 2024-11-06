package conductance.api.machine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import nl.appelgebakje22.xdata.ManagedDataMap;
import nl.appelgebakje22.xdata.api.IManaged;
import nl.appelgebakje22.xdata.api.Persisted;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.machine.trait.MetaCapability;
import conductance.api.machine.trait.MetaRecipeCapability;

public abstract class MetaBlockEntity<T extends MetaBlockEntity<T>> extends BlockEntity implements IManaged {

	private final List<MetaTick> ticks = new ArrayList<>();
	private final List<MetaTick> pending = new ArrayList<>();
	private final ManagedDataMap managedDataMap = new ManagedDataMap(this);
	@Persisted(key = "capabilities")
	private final List<MetaCapability> capabilities;

	public MetaBlockEntity(final MetaBlockEntityType<T> type, final BlockPos pos, final BlockState blockState) {
		super(type.getBlockEntityType().get(), pos, blockState);
		this.capabilities = Collections.unmodifiableList(Util.make(new ArrayList<>(), list -> this.registerCapabilities(list::add)));
	}

	@Override
	public final ManagedDataMap getDataMap() {
		return this.managedDataMap;
	}

	//region Capability
	protected abstract void registerCapabilities(Consumer<MetaCapability> register);

	@Nullable
	public final <C extends MetaCapability> C getCapability(final Class<C> type, final boolean exact) {
		for (final MetaCapability capability : this.capabilities) {
			if (exact) {
				if (type.equals(capability.getClass())) {
					return type.cast(capability);
				}
			} else if (type.isAssignableFrom(capability.getClass())) {
				return type.cast(capability);
			}
		}
		return null;
	}

	@Nullable
	public final <C extends MetaCapability> C getCapability(final Class<C> type) {
		return this.getCapability(type, false);
	}

	@SuppressWarnings("unchecked")
	@Nullable
	public final <C> MetaRecipeCapability<C> getRecipeCapability(final IRecipeElementType<C> type) {
		for (final MetaCapability capability : this.capabilities) {
			if (capability instanceof final MetaRecipeCapability<?> recipeCapability && recipeCapability.getElementType() == type) {
				return (MetaRecipeCapability<C>) capability;
			}
		}
		return null;
	}
	//endregion

	//region Update

	@Override
	public void onLoad() {
		super.onLoad();
		this.capabilities.forEach(MetaCapability::onLoad);
	}

	public void onUnload() {
		this.ticks.forEach(MetaTick::invalidate);
		this.ticks.clear();
		this.capabilities.forEach(MetaCapability::onUnload);
	}

	@Nullable
	public final MetaTick addTick(final Runnable action) {
		if (!this.isRemote()) {
			return Util.make(new MetaTick(action), result -> {
				this.pending.add(result);
				if (!this.getBlockState().getValue(MetaBlockEntityBlock.ACTIVE) && this.getLevel() instanceof final ServerLevel serverLevel) {
					final var newState = this.getBlockState().setValue(MetaBlockEntityBlock.ACTIVE, true);
					serverLevel.setBlockAndUpdate(this.getBlockPos(), newState);
				}
			});
		}
		return null;
	}

	@OnlyIn(Dist.CLIENT)
	protected void onClientTick() {
	}

	protected void handleServerTick() {
		if (!this.pending.isEmpty()) {
			this.ticks.addAll(this.pending);
			this.pending.clear();
		}
		final Iterator<MetaTick> iterator = this.ticks.iterator();
		while (iterator.hasNext()) {
			final MetaTick tick = iterator.next();
			tick.tick();
			if (this.isInvalid()) {
				break;
			}
			if (!tick.isValid()) {
				iterator.remove();
			}
		}
		if (this.isValid() && this.ticks.isEmpty() && this.pending.isEmpty()) {
			this.level.setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(MetaBlockEntityBlock.ACTIVE, false));
		}
	}
	//endregion

	//region Event
	protected void onNeighborChanged(final BlockPos neighborPos, final BlockState neighborState, final Direction neighborSide) {
	}

	protected boolean canConnectRedstone(@Nullable final Direction direction) {
		return false;
	}

	protected int getRedstone(final Direction side) {
		return 0;
	}

	protected int getRedstoneDirect(final Direction side) {
		return 0;
	}

	protected int getRedstoneAnalog() {
		return 0;
	}
	//endregion

	//region Misc

	/**
	 * @return <code>true</code> if the currently executing code is running on a client or <code>false</code> if it's a server
	 */
	public boolean isRemote() {
		return this.getLevel() == null ? CAPI.isClient() : this.getLevel().isClientSide();
	}

	/**
	 * @return <code>true</code> if the BlockEntity should be considered valid, <code>false</code> otherwise
	 * @see #isInvalid()
	 */
	public boolean isValid() {
		return !this.isRemoved();
	}

	/**
	 * @return <code>true</code> if the BlockEntity should be considered invalid, <code>false</code> otherwise
	 * @see #isValid()
	 */
	public boolean isInvalid() {
		return this.isRemoved();
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		this.onUnload();
	}

	//endregion
}
