package conductance.init;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import conductance.block.CableBlockEntity;
import conductance.core.apiimpl.ApiBridge;
import conductance.core.pipenet.CableRegistry;

public final class ConductanceBlockEntities {

	public static final BlockEntityEntry<CableBlockEntity> CABLE = ApiBridge.getRegistrate().blockEntity("cable", CableBlockEntity::new)
			.validBlocks(CableRegistry.getAllBlocks())
			.register();

	public static void init() {
		//NO-OP
	}

	private ConductanceBlockEntities() {
	}
}
