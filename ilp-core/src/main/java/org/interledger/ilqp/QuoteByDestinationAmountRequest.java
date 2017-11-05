package org.interledger.ilqp;

import org.interledger.InterledgerAddress;

import java.math.BigInteger;
import java.time.Duration;
import java.util.Objects;

/**
 * A request for a quote that specifies the amount to deliver at the destination address.
 */
public interface QuoteByDestinationAmountRequest extends QuoteRequest {

  @Override
  InterledgerAddress getDestinationAccount();

  /**
   * Returns fixed the amount that will arrive at the receiver.
   * @return A {@link BigInteger} amount
   */
  BigInteger getDestinationAmount();

  @Override
  Duration getDestinationHoldDuration();

  /**
   * A builder for instances of {@link QuoteByDestinationAmountRequest}.
   */
  class Builder {

    final private InterledgerAddress destinationAccount;
    final private BigInteger destinationAmount;
    final private Duration destinationHoldDuration;

    Builder(InterledgerAddress destinationAccount, BigInteger destinationAmount, Duration destinationHoldDuration) {

      if (destinationAmount.compareTo(BigInteger.ZERO) < 0) {
        throw new IllegalArgumentException("destinationAmount must be at least 0!");
      }
      this.destinationAccount      = destinationAccount     ;
      this.destinationAmount       = destinationAmount      ;
      this.destinationHoldDuration = destinationHoldDuration;
    }
    public static Builder builder(InterledgerAddress destinationAccount, BigInteger destinationAmount, Duration destinationHoldDuration) {
      return new Builder( destinationAccount,  destinationAmount, destinationHoldDuration);
    }

    /**
     * The method that actually constructs a QuoteByDestinationAmountRequest.
     *
     * @return An instance of {@link QuoteByDestinationAmountRequest}
     */
    public QuoteByDestinationAmountRequest build() {
      return new Builder.Impl(this);
    }

    /**
     * A private, immutable implementation of {@link QuoteByDestinationAmountRequest}.
     */
    private static class Impl implements QuoteByDestinationAmountRequest {

      private final InterledgerAddress destinationAccount;
      private final BigInteger destinationAmount;
      private final Duration destinationHoldDuration;

      /**
       * Constructs an instance from the values held in the builder.
       *
       * @param builder A Builder used to construct {@link QuoteByDestinationAmountRequest}
       *                instances.
       */
      private Impl(final Builder builder) {
        this.destinationAccount      =  builder.destinationAccount;
        this.destinationAmount       =  builder.destinationAmount;
        this.destinationHoldDuration =  builder.destinationHoldDuration;
      }

      @Override
      public InterledgerAddress getDestinationAccount() {
        return this.destinationAccount;
      }

      @Override
      public BigInteger getDestinationAmount() {
        return this.destinationAmount;
      }

      @Override
      public Duration getDestinationHoldDuration() {
        return this.destinationHoldDuration;
      }

      @Override
      public boolean equals(Object object) {
        if (this == object) {
          return true;
        }
        if (object == null || getClass() != object.getClass()) {
          return false;
        }

        Impl impl = (Impl) object;

        if (!destinationAccount.equals(impl.destinationAccount)) {
          return false;
        }
        if (!destinationAmount.equals(impl.destinationAmount)) {
          return false;
        }
        return destinationHoldDuration.equals(impl.destinationHoldDuration);
      }

      @Override
      public int hashCode() {
        int result = destinationAccount.hashCode();
        result = 31 * result + destinationAmount.hashCode();
        result = 31 * result + destinationHoldDuration.hashCode();
        return result;
      }

      @Override
      public String toString() {
        return "QuoteByDestinationAmountRequest.Impl{"
            + "destinationAccount=" + destinationAccount
            + ", destinationAmount=" + destinationAmount
            + ", destinationHoldDuration=" + destinationHoldDuration
            + '}';
      }
    }
  }
}