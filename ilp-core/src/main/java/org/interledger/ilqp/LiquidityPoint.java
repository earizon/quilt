package org.interledger.ilqp;

import java.math.BigInteger;
import java.util.Objects;

/**
 * Defines a <b>point</b> on the liquidity curve, a mapping between an input amount (X), and an
 * output amount (Y).
 */
public interface LiquidityPoint extends Comparable<LiquidityPoint> {

  /**
   * Returns the input amount associated with a point on the liquidity curve.
   * @return A {@link BigInteger} amount.
   */
  BigInteger getInputAmount();

  /**
   * Returns the output amount associated with a point on the liquidity curve.
   * @return A {@link BigInteger} amount.
   */
  BigInteger getOutputAmount();


  class Builder {

    final private BigInteger inputAmount;
    final private BigInteger outputAmount;
    Builder (BigInteger inputAmount, BigInteger outputAmount) {
      this.inputAmount  = inputAmount;
      this.outputAmount = outputAmount;
    }

    /**
     * The method that actually constructs a {@link LiquidityPoint} instance.
     *
     * @return An instance of {@link LiquidityPoint}.
     */
    public LiquidityPoint build() {
      return new Builder.Impl(this);
    }

    public static Builder builder(BigInteger inputAmount, BigInteger outputAmount) {
      return new Builder(inputAmount, outputAmount);
    }

    /**
     * A concrete implementation of {@link LiquidityPoint}.
     */
    private static class Impl implements LiquidityPoint {

      private final BigInteger inputAmount;
      private final BigInteger outputAmount;

      private Impl(final Builder builder) {
        Objects.requireNonNull(builder);
        this.inputAmount = builder.inputAmount;
        this.outputAmount = builder.outputAmount;
      }

      @Override
      public int compareTo(LiquidityPoint other) {
        /* ordering of liquidity points are based on the input amounts */
        return inputAmount.compareTo(other.getInputAmount());
      }

      @Override
      public BigInteger getInputAmount() {
        return this.inputAmount;
      }

      @Override
      public BigInteger getOutputAmount() {
        return this.outputAmount;
      }

      @Override
      public int hashCode() {
        return Objects.hash(inputAmount, outputAmount);
      }

      @Override
      public boolean equals(Object obj) {
        if (this == obj) {
          return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
          return false;
        }

        Impl impl = (Impl) obj;

        return Objects.equals(inputAmount, impl.inputAmount)
            && Objects.equals(outputAmount, impl.outputAmount);
      }

      @Override
      public String toString() {
        return "LiquidityPoint.Impl{inputAmount=" + inputAmount + ", outputAmount=" + outputAmount
            + "}";
      }
    }
  }
}
