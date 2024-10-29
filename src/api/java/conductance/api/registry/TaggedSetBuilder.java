package conductance.api.registry;

public interface TaggedSetBuilder<TYPE> {

	// region Tags
	TaggedSetBuilder<TYPE> addTagLoader(String tagPathFactory);

	TaggedSetBuilder<TYPE> addTagCommon(String tagPathFactory);

	TaggedSetBuilder<TYPE> addTagMod(String tagPathFactory);

	TaggedSetBuilder<TYPE> addTagVanilla(String tagPathFactory);
	// endregion

	// region Unformatted Tags
	TaggedSetBuilder<TYPE> addTagLoaderUnformatted(String tagPathFactory);

	TaggedSetBuilder<TYPE> addTagCommonUnformatted(String tagPathFactory);

	TaggedSetBuilder<TYPE> addTagModUnformatted(String tagPathFactory);

	TaggedSetBuilder<TYPE> addTagVanillaUnformatted(String tagPathFactory);
	// endregion

	// region Generation
	TaggedSetBuilder<TYPE> generateItems(boolean generateItems);

	TaggedSetBuilder<TYPE> generateBlocks(boolean generateBlocks);

	TaggedSetBuilder<TYPE> generateFluids(boolean generateFluids);
	// endregion

	// region Properties
	TaggedSetBuilder<TYPE> unitValue(long unitValue);
	// endregion
}
