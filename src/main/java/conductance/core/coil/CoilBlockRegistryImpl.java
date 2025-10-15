package conductance.core.coil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.jetbrains.annotations.Nullable;
import conductance.api.coil.CoilBlockRegistry;
import conductance.api.coil.CoilBlockType;

public final class CoilBlockRegistryImpl implements CoilBlockRegistry {

	private static final Cache<Integer, CoilBlockType> COIL_BY_TEMPERATURE_CACHE = CacheBuilder.newBuilder().maximumSize(512).build();
	private final LinkedList<CoilBlockTypeImpl> coils = new LinkedList<>();
	private final List<CoilBlockType> unmodifiableList = Collections.unmodifiableList(this.coils);

	void insertCoil(final CoilBlockTypeImpl coil) {
		if (this.coils.isEmpty()) {
			coil.setPrevCoil(null);
			coil.setNextCoil(null);
			this.coils.add(coil);
		} else if (coil.prevCoil() == null) {
			coil.setNextCoil(this.coils.getFirst());
			this.coils.getFirst().setPrevCoil(coil);
			this.coils.offerFirst(coil);
		} else {
			final Optional<CoilBlockTypeImpl> existingCoil = this.coils.stream().filter(existing -> existing.prevCoil() == coil.prevCoil()).findFirst();
			if (existingCoil.isPresent()) {
				final CoilBlockTypeImpl oldCoil = existingCoil.get();
				final int index = this.coils.indexOf(oldCoil);
				if (oldCoil.prevCoil() != null) {
					oldCoil.prevCoil().setNextCoil(coil);
				}
				oldCoil.setPrevCoil(coil);
				coil.setNextCoil(oldCoil);
				this.coils.add(index, coil);
			} else {
				this.coils.getLast().setNextCoil(coil);
				coil.setPrevCoil(this.coils.getLast());
				this.coils.add(coil);
			}
		}
		this.coils.forEach(CoilBlockTypeImpl::recalculate);
	}

	int getIndex(final CoilBlockTypeImpl coil) {
		return this.coils.indexOf(coil);
	}

	@Override
	public CoilBlockType first() {
		return this.coils.getFirst();
	}

	@Override
	public CoilBlockType last() {
		return this.coils.getLast();
	}

	@Override
	public CoilBlockType getByTemperature(final int temperature) {
		try {
			return CoilBlockRegistryImpl.COIL_BY_TEMPERATURE_CACHE.get(temperature, () -> {
				for (final CoilBlockType coil : this.coils) {
					if (temperature <= coil.getTemperature()) {
						return coil;
					}
				}
				return this.getLastCoil();
			});
		} catch (final ExecutionException e) {
			//Should not happen, famous last words
			throw new AssertionError(e);
		}
	}

	@Override
	public List<CoilBlockType> getByTemperatureOrHigher(final int temperature) {
		final List<CoilBlockType> result = new ArrayList<>();
		final CoilBlockType coil = this.getByTemperature(temperature);
		result.add(coil);
		for (int i = coil.getIndex(); i < this.coils.size(); ++i) {
			result.add(this.coils.get(i));
		}
		return result;
	}

	@Nullable
	CoilBlockTypeImpl getLastCoil() {
		if (this.coils.isEmpty()) {
			return null;
		}
		return this.coils.getLast();
	}

	@Override
	public List<CoilBlockType> getCoils() {
		return this.unmodifiableList;
	}
}
