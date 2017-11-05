package org.interledger.ilqp;

import org.interledger.InterledgerAddress;

import java.time.Duration;
import java.util.Objects;

/**
 * A request to receive liquidity information between the current ledger and the destination
 * account. This information is sufficient to locally quote any amount until the curve expires.
 */
public interface QuoteLiquidityRequest extends QuoteRequest {

  @Override
  InterledgerAddress getDestinationAccount();

  @Override
  Duration getDestinationHoldDuration();

  /**
   * A builder for instances of {@link QuoteLiquidityRequest}.
   */
  class Builder {
    final private InterledgerAddress destinationAccount;
    final private Duration destinationHoldDuration;

    Builder(InterledgerAddress destinationAccount, Duration destinationHoldDuration) {
      this.destinationAccount =  destinationAccount;
      this.destinationHoldDuration = destinationHoldDuration;
    }

    public static Builder builder(InterledgerAddress destinationAccount, Duration destinationHoldDuration) {
      Objects.requireNonNull(destinationAccount);
      Objects.requireNonNull(destinationHoldDuration);
      return new Builder(destinationAccount, destinationHoldDuration);
    }

    /**
     * The method that actually constructs a QuoteByLiquidityRequest.
     * 
     * @return An instance of {@link QuoteLiquidityRequest}
     */
    public QuoteLiquidityRequest build() {
      return new Builder.Impl(this);
    }


    private static class Impl implements QuoteLiquidityRequest {

      private final InterledgerAddress destinationAccount;
      private final Duration destinationHoldDuration;

      /**
       * Constructs an instance from the values held in the builder.
       * 
       * @param builder A Builder used to construct {@link QuoteLiquidityRequest} instances.
       */
      private Impl(final Builder builder) {
        this.destinationAccount = builder.destinationAccount;

        this.destinationHoldDuration = builder.destinationHoldDuration;
      }

      @Override
      public InterledgerAddress getDestinationAccount() {
        return this.destinationAccount;
      }

      @Override
      public Duration getDestinationHoldDuration() {
        return this.destinationHoldDuration;
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

        return Objects.equals(destinationAccount, impl.destinationAccount)
            && Objects.equals(destinationHoldDuration, impl.destinationHoldDuration);
      }

      @Override
      public int hashCode() {
        return Objects.hash(destinationAccount, destinationHoldDuration);
      }

      @Override
      public String toString() {
        return "QuoteLiquidityRequest.Impl{" + "destinationAccount=" + destinationAccount
            + ", destinationHoldDuration=" + destinationHoldDuration + "}";
      }
    }
  }
}
