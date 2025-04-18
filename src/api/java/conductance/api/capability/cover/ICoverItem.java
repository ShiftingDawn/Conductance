package conductance.api.capability.cover;

public interface ICoverItem<COVER extends CoverEntity<COVER>> {

	CoverType<COVER> getCoverType();
}
