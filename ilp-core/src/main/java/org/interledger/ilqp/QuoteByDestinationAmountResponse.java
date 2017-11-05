package org.interledger.ilqp;

import java.math.BigInteger;
import java.time.Duration;
import java.util.Objects;

/**
 * A quote sent in response to a request of type {@link QuoteByDestinationAmountRequest}.
 */
public interface QuoteByDestinationAmountResponse extends QuoteResponse {

  @Override
  Duration getSourceHoldDuration();

  /**
   * The amount the sender needs to send based on the requested destination amount.
   *
   * @return The amount the sender needs to send.
   */
  BigInteger getSourceAmount();

  /**
   * A builder for instances of {@link QuoteByDestinationAmountRequest}.
   */
  class Builder {

    private final BigInteger sourceAmount;
    private final Duration sourceHoldDuration;

    private Builder(BigInteger sourceAmount, Duration sourceHoldDuration) {
      this.sourceAmount       = sourceAmount       ;
      this.sourceHoldDuration = sourceHoldDuration ;
    }

    /**
     * Constructs a new builder.
     * @return A new {@link Builder} instance.
     */

    public static Builder builder(BigInteger sourceAmount, Duration sourceHoldDuration) {
      return new Builder(sourceAmount, sourceHoldDuration);
    }

    /**
     * The method that actually constructs a QuoteByDestinationAmountResponse instance.
     *
     * @return An instance of {@link QuoteByDestinationAmountResponse}.
     */
    public QuoteByDestinationAmountResponse build() {
      return new Builder.Impl(this);
    }

    /**
     * A private, immutable implementation of {@link QuoteByDestinationAmountResponse}.
     */
    public static class Impl implements QuoteByDestinationAmountResponse {

      private final BigInteger sourceAmount;
      private final Duration sourceHoldDuration;

      private Impl(final Builder builder) {
        Objects.requireNonNull(builder);

        this.sourceAmount = Objects
            .requireNonNull(builder.sourceAmount, "sourceAmount must not be null!");
        if (this.sourceAmount.compareTo(BigInteger.ZERO) < 0) {
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
      public BigInteger getSourceAmount() {
        return this.sourceAmount;
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

        if (!sourceAmount.equals(impl.sourceAmount)) {
          return false;
        }
        return sourceHoldDuration.equals(impl.sourceHoldDuration);
      }

      @Override
      public int hashCode() {
        int result = sourceAmount.hashCode();
        result = 31 * result + sourceHoldDuration.hashCode();
        return result;
      }

      @Override
      public String toString() {
        return "QuoteByDestinationAmountResponse.Impl{"
            + "sourceAmount=" + sourceAmount
            + ", sourceHoldDuration=" + sourceHoldDuration
            + '}';
      }
    }
  }
}
