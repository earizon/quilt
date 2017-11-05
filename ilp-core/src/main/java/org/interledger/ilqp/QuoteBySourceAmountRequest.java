package org.interledger.ilqp;

import org.interledger.InterledgerAddress;

import java.math.BigInteger;
import java.time.Duration;
import java.util.Objects;

/**
 * A request for a quote with a fixed source amount to determine the destination amount.
 */
public interface QuoteBySourceAmountRequest extends QuoteRequest {

  @Override
  InterledgerAddress getDestinationAccount();

  /**
   * Returns the amount the sender wishes to send, denominated in the asset of the source ledger.
   * @return A {@link BigInteger} amount.
   */
  BigInteger getSourceAmount();

  @Override
  Duration getDestinationHoldDuration();

  /**
   * A builder for instances of {@link QuoteBySourceAmountRequest}.
   */
  class Builder {

    final private InterledgerAddress destinationAccount;
    final private BigInteger sourceAmount;
    final private Duration destinationHoldDuration;

    Builder(final InterledgerAddress destinationAccount, final BigInteger sourceAmount, final Duration destinationHoldDuration) {
       this.destinationAccount      = destinationAccount ;
       if (sourceAmount.compareTo(BigInteger.ZERO) < 0) {
           throw new RuntimeException("destinationAmount must be at least 0!");
       }
       this.sourceAmount            = sourceAmount ;
       this.destinationHoldDuration = destinationHoldDuration ;
    }

    // TODO: Create with default destinationHoldDuration
    public static Builder builder(
       final InterledgerAddress destinationAccount, final BigInteger sourceAmount, final Duration destinationHoldDuration) {
      return new Builder(destinationAccount, sourceAmount, destinationHoldDuration);
    }

    /**
     * The method that actually constructs a QuoteBySourceAmountRequest.
     *
     * @return An instance of {@link QuoteBySourceAmountRequest}.
     */
    public QuoteBySourceAmountRequest build() {
      return new Builder.Impl(this);
    }

    /**
     * A private, immutable implementation of {@link QuoteBySourceAmountRequest}.
     */
    private static class Impl implements QuoteBySourceAmountRequest {

      private final InterledgerAddress destinationAccount;
      private final BigInteger sourceAmount;
      private final Duration destinationHoldDuration;

      /**
       * No-args Constructor.
       */
      private Impl(final Builder builder) {
        Objects.requireNonNull(builder);
        this.destinationAccount = builder.destinationAccount;
        this.sourceAmount = builder.sourceAmount;
        this.destinationHoldDuration = builder.destinationHoldDuration;
      }

      @Override
      public InterledgerAddress getDestinationAccount() {
        return this.destinationAccount;
      }

      @Override
      public BigInteger getSourceAmount() {
        return this.sourceAmount;
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

        if (!destinationAccount.equals(impl.destinationAccount)) {
          return false;
        }
        if (!sourceAmount.equals(impl.sourceAmount)) {
          return false;
        }
        return destinationHoldDuration.equals(impl.destinationHoldDuration);
      }

      @Override
      public int hashCode() {
        int result = destinationAccount.hashCode();
        result = 31 * result + sourceAmount.hashCode();
        result = 31 * result + destinationHoldDuration.hashCode();
        return result;
      }

      @Override
      public String toString() {
        return "QuoteBySourceAmountRequest.Impl{"
            + "destinationAccount=" + destinationAccount
            + ", sourceAmount=" + sourceAmount
            + ", destinationHoldDuration=" + destinationHoldDuration
            + '}';
      }
    }
  }

}