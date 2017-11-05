package org.interledger.ilqp;

import org.interledger.InterledgerAddress;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * A response to a quote request with liquidity information regarding transfers between the current
 * ledger and the destination account. This information is sufficient to locally quote any amount
 * until the curve expires.
 */
public interface QuoteLiquidityResponse extends QuoteResponse {

  @Override
  Duration getSourceHoldDuration();

  /**
   * <p>A series of exchange rates that can be plotted to assemble a "curve" of liquidity
   * representing the amount that one currency can be exchanged for another.</p> <p>For example, if
   * a liquidity curve contains the rate [0,0] and [10,20], then there is a linear path of rates for
   * which one currency can be exchange for another. To illustrate, it can be assumed that [5,10]
   * exists on this curve.</p>
   *
   * @return A {@link List} of type {link ExchangeRate}.
   */
  LiquidityCurve getLiquidityCurve();

  /**
   * <p>A common address prefix of all addresses for which the above liquidity curve applies. If the
   * curve only applies to the destination account (see {@link QuoteLiquidityRequest
   * #getDestinationAccount()}) of the corresponding quote request, then this value will be equal to
   * that address. If the curve applies to other accounts with a certain prefix, then this value
   * will be set to that prefix.</p>
   *
   * <p>For more on ILP Address Prefixes, see {@link InterledgerAddress}.</p>
   *
   * @return An instance of {@link InterledgerAddress}.
   */
  InterledgerAddress getAppliesToPrefix();

  /**
   * Maximum time where the connector (and any connectors after it) expects to be able to honor this
   * liquidity curve. Note that a quote in ILP is non-committal, meaning that the liquidity is only
   * likely to be available -- but not reserved -- and therefore not guaranteed.
   *
   * @return An instance of {@link Instant}.
   */
  Instant getExpiresAt();

  /**
   * A builder for instances of {@link QuoteLiquidityResponse}.
   */
  class Builder {

    final private LiquidityCurve liquidityCurve;
    final private InterledgerAddress appliesTo;
    final private Duration sourceHoldDuration;
    final private Instant expiresAt;

    public Builder (LiquidityCurve liquidityCurve, InterledgerAddress appliesTo, Duration sourceHoldDuration, Instant expiresAt) {
      this.liquidityCurve     = liquidityCurve;
      this.appliesTo          = appliesTo;
      this.sourceHoldDuration = sourceHoldDuration;
      this.expiresAt          = expiresAt;
    }

    /**
     * The method that actually constructs a QuoteByLiquidityResponse instance.
     *
     * @return An instance of {@link QuoteLiquidityResponse}.
     */
    public QuoteLiquidityResponse build() {
      return new Builder.Impl(this);
    }

    /**
     * Constructs a new builder.
     * @return A new instance of {@link Builder}
     */
    public static Builder builder(LiquidityCurve liquidityCurve, InterledgerAddress appliesTo, Duration sourceHoldDuration, Instant expiresAt) {
      return new Builder(liquidityCurve, appliesTo, sourceHoldDuration, expiresAt);
    }


    private static class Impl implements QuoteLiquidityResponse {

      private final LiquidityCurve liquidityCurve;
      private final InterledgerAddress appliesTo;
      private final Duration sourceHoldDuration;
      private final Instant expiresAt;

      private Impl(Builder builder) {
        Objects.requireNonNull(builder);

        this.liquidityCurve = builder.liquidityCurve;
        this.appliesTo = builder.appliesTo;
        this.sourceHoldDuration = builder.sourceHoldDuration;
        this.expiresAt = builder.expiresAt;
      }

      @Override
      public LiquidityCurve getLiquidityCurve() {
        return this.liquidityCurve;
      }

      @Override
      public InterledgerAddress getAppliesToPrefix() {
        return this.appliesTo;
      }

      @Override
      public Duration getSourceHoldDuration() {
        return this.sourceHoldDuration;
      }

      @Override
      public Instant getExpiresAt() {
        return this.expiresAt;
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

        /*
         * compare the quote responses, taking care that the expiration date is compared with
         * timezone information -> .equals != .isEquals for Instant :(
         */
        return Objects.equals(liquidityCurve, impl.liquidityCurve)
            && Objects.equals(appliesTo, impl.appliesTo)
            && Objects.equals(sourceHoldDuration, impl.sourceHoldDuration)
            && (expiresAt.equals(impl.expiresAt));
      }

      @Override
      public int hashCode() {
        return Objects.hash(liquidityCurve.hashCode(), appliesTo.hashCode(),
            sourceHoldDuration.hashCode(), expiresAt.hashCode());
      }

      @Override
      public String toString() {
        return "QuoteLiquidityResponse.Impl{liquidityCurve=" + liquidityCurve + ", appliesTo="
            + appliesTo + ", sourceHoldDuration=" + sourceHoldDuration + ", expiresAt=" + expiresAt
            + "}";
      }
    }
  }
}
