package conductance.core.pipenet;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.tterrag.registrate.util.entry.BlockEntry;
import conductance.api.material.Material;
import conductance.Conductance;
import conductance.block.CableBlock;

public final class CableRegistry {

	private static final Table<CableType, Material, BlockEntry<CableBlock>> REGISTRY = HashBasedTable.create();
	private static boolean frozen = false;

	public static void register(final CableType type, final Material material, final BlockEntry<CableBlock> cable) {
		if (CableRegistry.frozen) {
			throw new IllegalStateException("Trying to register cable in frozen CableRegistry!");
		}
		CableRegistry.REGISTRY.put(type, material, cable);
	}

	public static BlockEntry<CableBlock> getCable(final CableType type, final Material material) {
		return CableRegistry.REGISTRY.get(type, material);
	}

	@SuppressWarnings("unchecked")
	public static BlockEntry<CableBlock>[] getAllBlocks() {
		return CableRegistry.REGISTRY.values().toArray(BlockEntry[]::new);
	}

	public static void freeze() {
		Conductance.LOGGER.info("CableRegistry has been frozen!");
		CableRegistry.frozen = true;
	}

	private CableRegistry() {
	}
}
