package conductance;

import net.neoforged.neoforge.common.ModConfigSpec;
import conductance.api.CAPI;

public class Config {

	private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

	// region Debug
	// @formatter:off
	static { BUILDER.push("debug"); }
	public static ModConfigSpec.BooleanValue debug_enableOreVeinDebugLogging = BUILDER
			.comment("Enable debug logging for Conductance ore vein generation")
			.define("enableOreVeinDebugLogging", false);
	public static ModConfigSpec.BooleanValue debug_disableOreVeinValidityChecks = BUILDER
			.comment("Disable validity checks for Conductance ore vein generation")
			.define("disableOreVeinValidityChecks", false);
	public static ModConfigSpec.IntValue debug_translationRegistryDebugLogging = BUILDER
			.comment("Disable validity checks for Conductance ore vein generation", "0 = none, 1=custom only, 2=all")
			.defineInRange("translationRegistryDebugLogging", 0, 0, 2);
	public static ModConfigSpec.BooleanValue debug_dumpRuntimeResourcePack = BUILDER
			.comment("Dump the generated ResourcePack to disk", "Will be dumped to .minecraft/" + CAPI.MOD_ID)
			.define("dumpRuntimeResourcePack", false);
	public static ModConfigSpec.BooleanValue debug_dumpRuntimeDataPack = BUILDER
			.comment("Dump the generated DataPack to disk", "Will be dumped to .minecraft/" + CAPI.MOD_ID)
			.define("dumpRuntimeDataPack", false);
	static { BUILDER.pop(); }
	// @formatter:on
	// endregion

	static final ModConfigSpec SPEC = Config.BUILDER.build();
}
