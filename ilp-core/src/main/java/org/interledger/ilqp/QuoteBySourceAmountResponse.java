package org.interledger.ilqp;

import java.math.BigInteger;
import java.time.Duration;
import java.util.Objects;

/**
 * A quote sent in response to a request of type {@link QuoteBySourceAmountRequest}.
 */
public interface QuoteBySourceAmountResponse extends QuoteResponse {

  @Override
  Duration getSourceHoldDuration();

  /**
   * Returns the amount that will arrive at the receiver.
   * @return A {@link BigInteger} amount
   */
  BigInteger getDestinationAmount();

  /**
   * A builder for constructing instances of {@link QuoteBySourceAmountResponse}.
   */
  class Builder {

    final private BigInteger destinationAmount;
    final private Duration sourceHoldDuration;

    Builder (BigInteger destinationAmount, Duration sourceHoldDuration) {
      this.destinationAmount  = destinationAmount;
      this.sourceHoldDuration = sourceHoldDuration;
    }
    /**
     * Constructs a new builder.
     * @return A new {@link Builder} instance.
     */
    public static Builder builder(BigInteger destinationAmount, Duration sourceHoldDuration) {
      return new Builder(destinationAmount, sourceHoldDuration);
    }

    /**
     * The method that actually constructs a QuoteBySourceAmountResponse instance.
     *
     * @return An instance of {@link QuoteBySourceAmountResponse}.
     */
    public QuoteBySourceAmountResponse build() {
      return new Builder.Impl(this);
    }

    /**
     * A private, immutable implementation of {@link QuoteBySourceAmountResponse}.
     */
    private static class Impl implements QuoteBySourceAmountResponse {

      private final BigInteger destinationAmount;
      private final Duration sourceHoldDuration;

      private Impl(final Builder builder) {
        Objects.requireNonNull(builder);

        this.destinationAmount = Objects
            .requireNonNull(builder.destinationAmount, "destinationAmount must not be null!");
        if (this.destinationAmount.compareTo(BigInteger.ZERO) < 0) {
          throw new IllegalArgumentException("destinationAmount must be at least 0!");
        }

        this.sourceHoldDuration = Objects.requireNonNull(builder.sourceHoldDuration,
            "sourceHoldDuration must not be null!");

      }

      @Override
      public Duration getSourceHoldDuration() {
        return this.sourceHoldDuration;
      }

      @Override
      public BigInteger getDestinationAmount() {
        return this.destinationAmount;
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

        if (!destinationAmount.equals(impl.destinationAmount)) {
          return false;
        }
        return sourceHoldDuration.equals(impl.sourceHoldDuration);
      }

      @Override
      public int hashCode() {
        int result = destinationAmount.hashCode();
        result = 31 * result + sourceHoldDuration.hashCode();
        return result;
      }

      @Override
      public String toString() {
        return "QuoteBySourceAmountResponse.Impl{"
            + "destinationAmount=" + destinationAmount
            + ", sourceHoldDuration=" + sourceHoldDuration
            + '}';
      }
    }
  }
}
