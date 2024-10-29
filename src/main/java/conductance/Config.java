package conductance;

import net.neoforged.neoforge.common.ModConfigSpec;
import conductance.api.CAPI;

@SuppressWarnings("CheckStyle")
public final class Config {

	private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

	// region Debug
	// @formatter:off
	static { Config.BUILDER.push("debug"); }
	public static ModConfigSpec.BooleanValue debug_enableOreVeinDebugLogging = Config.BUILDER
			.comment("Enable debug logging for Conductance ore vein generation")
			.define("enableOreVeinDebugLogging", false);
	public static ModConfigSpec.BooleanValue debug_disableOreVeinValidityChecks = Config.BUILDER
			.comment("Disable validity checks for Conductance ore vein generation")
			.define("disableOreVeinValidityChecks", false);
	public static ModConfigSpec.IntValue debug_translationRegistryDebugLogging = Config.BUILDER
			.comment("Enable translation registry debug logging", "0 = none, 1=registrations only, 2=registrations and fallbacks")
			.defineInRange("translationRegistryDebugLogging", 0, 0, 2);
	public static ModConfigSpec.BooleanValue debug_textureSetDebugLogging = Config.BUILDER
			.comment("Enable texture set  debug logging")
			.define("textureSetDebugLogging", false);
	public static ModConfigSpec.BooleanValue debug_dumpRuntimeResourcePack = Config.BUILDER
			.comment("Dump the generated ResourcePack to disk", "Will be dumped to .minecraft/" + CAPI.MOD_ID)
			.define("dumpRuntimeResourcePack", false);
	public static ModConfigSpec.BooleanValue debug_dumpRuntimeDataPack = Config.BUILDER
			.comment("Dump the generated DataPack to disk", "Will be dumped to .minecraft/" + CAPI.MOD_ID)
			.define("dumpRuntimeDataPack", false);
	static { Config.BUILDER.pop(); }
	// @formatter:on
	// endregion

	static final ModConfigSpec SPEC = Config.BUILDER.build();

	private Config() {
	}
}
