package conductance.api.cover;

public interface ICoverItem<COVER extends CoverEntity<COVER>> {

	CoverType<COVER> getCoverType();
}
