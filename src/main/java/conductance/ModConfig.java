package conductance;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class ModConfig {

	private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

	//region Difficulty
	static {
		ModConfig.BUILDER.push("difficulty");
	}

	public static ModConfigSpec.BooleanValue difficulty_enableOverVoltExplosions = ModConfig.BUILDER
		.comment("Machines will explode if given more volts than they can handle. Disabling will replace them with fire instead.")
		.define("enableOverVoltExplosions", true);

	static {
		ModConfig.BUILDER.pop();
	}
	//endregion

	// region Debug
	// @formatter:off
	static { ModConfig.BUILDER.push("debug"); }
	public static ModConfigSpec.BooleanValue debug_dumpRuntimeResourcePack = ModConfig.BUILDER
			.comment("Dump the generated ResourcePack to disk", "Will be dumped to .minecraft/" + Conductance.MODID)
			.define("dumpRuntimeResourcePack", false);
	public static ModConfigSpec.BooleanValue debug_dumpRuntimeDataPack = ModConfig.BUILDER
			.comment("Dump the generated DataPack to disk", "Will be dumped to .minecraft/" + Conductance.MODID)
			.define("dumpRuntimeDataPack", false);
	static { ModConfig.BUILDER.pop(); }
	// @formatter:on
	// endregion

	static final ModConfigSpec SPEC = ModConfig.BUILDER.build();

	private ModConfig() {
	}
}
