package conductance.api.registry;

public interface TaggedSetBuilder<TYPE, SET extends TaggedSet<TYPE>, BUILDER extends TaggedSetBuilder<TYPE, SET, BUILDER>> {

	// region Tags
	BUILDER addTagLoader(String tagPathFactory);

	BUILDER addTagCommon(String tagPathFactory);

	BUILDER addTagMod(String tagPathFactory);

	BUILDER addTagVanilla(String tagPathFactory);
	// endregion

	// region Unformatted Tags
	BUILDER addTagLoaderUnformatted(String tagPathFactory);

	BUILDER addTagCommonUnformatted(String tagPathFactory);

	BUILDER addTagModUnformatted(String tagPathFactory);

	BUILDER addTagVanillaUnformatted(String tagPathFactory);
	// endregion

	// region Generation
	BUILDER generateItems(boolean generateItems);

	BUILDER generateBlocks(boolean generateBlocks);

	BUILDER generateFluids(boolean generateFluids);
	// endregion

	// region Properties
	BUILDER unitValue(long unitValue);
	// endregion

	SET build();
}
