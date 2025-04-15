package conductance.api.machine.recipe;

import java.math.BigDecimal;
import java.math.BigInteger;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class RecipeModifier {

	private final double multiplier;
	private final double addition;

	public Number apply(final Number num) {
		if (num instanceof final BigDecimal decimal) {
			return decimal.multiply(BigDecimal.valueOf(this.multiplier)).add(BigDecimal.valueOf(this.addition));
		}
		if (num instanceof final BigInteger bigInteger) {
			return bigInteger.multiply(BigInteger.valueOf((long) this.multiplier)).add(BigInteger.valueOf((long) this.addition));
		}
		return num.doubleValue() * this.multiplier + this.addition;
	}

	public static RecipeModifier copy() {
		return new RecipeModifier(1, 0);
	}

	public static RecipeModifier multiply(final double multiplier) {
		return new RecipeModifier(multiplier, 0);
	}

	public static RecipeModifier add(final double addition) {
		return new RecipeModifier(1, addition);
	}

	public static RecipeModifier subtract(final double addition) {
		return new RecipeModifier(1, -addition);
	}
}
