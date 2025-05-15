package conductance.api.capability.cover;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.machine.EnvironmentProvider;
import conductance.api.machine.IAppearance;
import conductance.api.machine.IBlockEntity;
import conductance.api.machine.MachineRunnable;
import conductance.api.machine.RunnableContainer;
import conductance.api.machine.sync.IManaged;
import conductance.api.machine.sync.ManagedDataMap;
import conductance.api.machine.sync.OnSynchronized;
import conductance.api.machine.sync.Persisted;
import conductance.api.machine.sync.SpecialHandled;
import conductance.api.machine.sync.Synchronized;

public class CoverManager implements IManaged, EnvironmentProvider, RunnableContainer, IAppearance {

	private final ManagedDataMap dataMap = CAPI.syncHelper().requestDataMap(this);
	private final IBlockEntity blockEntity;

	@Nullable
	@Synchronized
	@Persisted
	@OnSynchronized(method = "onCoverChanged")
	@SpecialHandled(testDirtyMethod = "testCoverDirty", serializeMethod = "serializeCover", deserializeMethod = "deserializeCover")
	private CoverEntity<?> coverUp, coverDown, coverNorth, coverSouth, coverEast, coverWest;

	public CoverManager(final IBlockEntity blockEntity) {
		this.blockEntity = blockEntity;
	}

	public double getCoverBackplateThickness() {
		return 1.0 / 16.0;
	}

	public VoxelShape[] getCoverCollisionShapes() {
		final double thickness = this.getCoverBackplateThickness();
		final List<VoxelShape> result = new ArrayList<>();
		if (thickness > 0.0) {
			for (final Direction side : Direction.values()) {
				if (this.getCover(side).isPresent()) {
					final VoxelShape coverBox = ICoverable.getCoverBackplateShape(side, thickness);
					result.add(coverBox);
				}
			}
		}
		return result.toArray(VoxelShape[]::new);
	}

	@Override
	public IBlockEntity getBlockEntity() {
		return this.blockEntity;
	}

	public boolean attachCover(final CoverType<?> cover, final Direction side, final ItemStack itemStack, final ServerPlayer player) {
		if (!this.canAcceptCover(cover, side)) {
			return false;
		}
		final CoverEntity<?> coverEntity = cover.instantiate(this, side);
		if (!coverEntity.canAttachTo(this, this.blockEntity)) {
			return false;
		}
		coverEntity.setAttachItem(itemStack.copyWithCount(1));
		coverEntity.onCoverAttached(itemStack, player);
		coverEntity.onCoverLoaded();
		this.setCover(coverEntity, side);
		this.notifyBlockUpdate();
		this.setChanged();
		this.scheduleNeighborShapeUpdate();
		return true;
	}

	@Nullable
	public CoverType<?> removeCover(final Direction side) {
		final CoverEntity<?> cover = this.getCover(side).orElse(null);
		if (cover == null) {
			return null;
		}
		cover.onCoverRemoved();
		this.setCover(null, side);
		this.notifyBlockUpdate();
		this.setChanged();
		this.scheduleNeighborShapeUpdate();
		this.scheduleRenderUpdate();
		return cover.getCoverType();
	}

	private void setCover(@Nullable final CoverEntity<?> coverEntity, final Direction side) {
		switch (side) {
			case DOWN -> this.coverDown = coverEntity;
			case UP -> this.coverUp = coverEntity;
			case NORTH -> this.coverNorth = coverEntity;
			case SOUTH -> this.coverSouth = coverEntity;
			case WEST -> this.coverWest = coverEntity;
			case EAST -> this.coverEast = coverEntity;
		}
		if (coverEntity != null) {
			coverEntity.getDataMap().markDirty();
		}
	}

	public boolean canAcceptCover(final CoverType<?> cover, @Nullable final Direction side) {
		return side == null || this.getCover(side).isEmpty();
	}

	public Optional<? extends CoverEntity<?>> getCover(final Direction side) {
		return Optional.ofNullable(switch (side) {
			case DOWN -> this.coverDown;
			case UP -> this.coverUp;
			case NORTH -> this.coverNorth;
			case SOUTH -> this.coverSouth;
			case WEST -> this.coverWest;
			case EAST -> this.coverEast;
		});
	}

	@Override
	public ManagedDataMap getDataMap() {
		return this.dataMap;
	}

	private List<? extends CoverEntity<?>> getCovers() {
		return Arrays.stream(Direction.values()).map(this::getCover).filter(Optional::isPresent).map(Optional::get).toList();
	}

	public void onLoaded() {
		this.getCovers().forEach(CoverEntity::onCoverLoaded);
	}

	public void onUnloaded() {
		this.getCovers().forEach(CoverEntity::onCoverUnloaded);
	}

	@Override
	public void onNeighborChanged(final BlockPos neighborPos, final BlockState neighborState, final Direction neighborSide) {
		this.getCovers().forEach(cover -> cover.onNeighborChanged(neighborPos, neighborState, neighborSide));
	}

	//	@Override
	//	public void onChanged() {
	//		final var level = this.blockEntity.getLevel();
	//		if (level != null && !level.isClientSide && level.getServer() != null) {
	//			level.getServer().execute(this::setChanged);
	//		}
	//	}

	@Override
	public void setChanged() {
		EnvironmentProvider.super.setChanged();
		this.getCovers().forEach(CoverEntity::onCoverManagerChanged);
	}

	public boolean isValid() {
		return this.blockEntity.isValid();
	}

	@Override
	public void scheduleRenderUpdate() {
		this.getBlockEntity().scheduleRenderUpdate();
	}

	@Nullable
	@Override
	public MachineRunnable addTick(final Runnable runnable) {
		return this.getBlockEntity().addTick(runnable);
	}

	@SuppressWarnings("unused") //Used by CoverEntities as sync listener
	private void onCoverChanged(@Nullable final CoverEntity<?> newCover, @Nullable final CoverEntity<?> oldCover) {
		if (newCover != oldCover && (newCover == null || oldCover == null)) {
			this.scheduleRenderUpdate();
		}
	}

	@SuppressWarnings("unused") //Used by CoverEntities as special handler
	private boolean testCoverDirty(@Nullable final CoverEntity<?> coverEntity) {
		if (coverEntity != null) {
			coverEntity.getDataMap().tick();
			return coverEntity.getDataMap().isDirty();
		}
		return false;
	}

	@SuppressWarnings("unused") //Used by CoverEntities as special handler
	private CompoundTag serializeCover(final CoverEntity<?> cover) {
		return Util.make(new CompoundTag(), nbt -> {
			nbt.putInt("side", cover.getSide().ordinal());
			nbt.putString("cover_id", cover.getCoverType().getRegistryKey().toString());
		});
	}

	@SuppressWarnings("unused") //Used by CoverEntities as special handler
	private CoverEntity<?> deserializeCover(final CompoundTag nbt) {
		final ResourceLocation coverId = ResourceLocation.parse(nbt.getString("cover_id"));
		final Direction side = Direction.values()[nbt.getInt("side")];
		if (!CAPI.regs().covers().containsKey(coverId)) {
			throw new IllegalStateException("Could not instantiate cover with unregistered id " + coverId);
		}
		final CoverType<?> coverType = CAPI.regs().covers().get(coverId);
		assert coverType != null;
		return coverType.instantiate(this, side);
	}

	@Override
	public BlockState getAppearance(final BlockState state, final BlockAndTintGetter level, final BlockPos pos, final Direction side, @Nullable final BlockState queryState, @Nullable final BlockPos queryPos) {
		return this.getCover(side).filter(cover -> cover instanceof IAppearance)
				.map(cover -> ((IAppearance) cover).getAppearance(state, level, pos, side, queryState, queryPos))
				.orElse(state);
	}
}
