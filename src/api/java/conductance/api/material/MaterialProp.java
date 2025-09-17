package conductance.api.material;

public class MaterialProp<T> {

	public static class Int extends MaterialProp<Integer> {
	}

	public static class Enum<E extends java.lang.Enum<E>> extends MaterialProp<E> {
	}
}
