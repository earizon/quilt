package org.interledger.ipr;

import org.interledger.cryptoconditions.Condition;
import org.interledger.ilp.InterledgerPayment;

import java.util.Objects;

/**
 * An Interledger Payment Request as defined in ILP RFC 11.
 *
 * @see "https://github.com/interledger/rfcs/blob/master/0011-interledger-payment-request/0011
 * -interledger-payment-request.md"
 */

public interface InterledgerPaymentRequest {

  /**
   * Get the version of this IPR (this interface represents Version 2).
   *
   * @return The version of the IPR (currently 2)
   */
  default int getVersion() {
    return 2;
  }

  /**
   * The Interledger Payment being requested.
   *
   * @return an Interledger Payment.
   */
  InterledgerPayment getInterledgerPayment();

  /**
   * The {@link Condition} to use when sending the payment.
   *
   * @return a Condition
   */
  Condition getCondition();

  class Builder {

    final private InterledgerPayment interledgerPayment;
    final private Condition condition;

    Builder(InterledgerPayment interledgerPayment, Condition condition) {
      this.interledgerPayment = interledgerPayment;
      this.condition          = condition;
    }

    public static Builder builder(InterledgerPayment interledgerPayment, Condition condition) {
      return new Builder(interledgerPayment, condition);
    }

    /**
     * Get the IPR.
     *
     * <p>Calling this will result in the internal PSK message being built (and encrypted unless
     * encryption is disabled).
     *
     * <p>After the PSK message is built the ILP Packet is built and OER encoded before the
     * Condition is generated.
     *
     * @return an Interledger Payment Request.
     */
    public InterledgerPaymentRequest build() {
      return new Impl(interledgerPayment, condition);
    }

    private static final class Impl implements InterledgerPaymentRequest {

      private static final int VERSION = 2;

      private final InterledgerPayment packet;
      private final Condition condition;

      public Impl(InterledgerPayment packet, Condition condition) {
        this.packet = packet;
        this.condition = condition;
      }

      public int getVersion() {
        return VERSION;
      }

      public InterledgerPayment getInterledgerPayment() {
        return packet;
      }

      public Condition getCondition() {
        return condition;
      }

      @Override
      public boolean equals(Object other) {
        if (this == other) {
          return true;
        }
        if (other == null || getClass() != other.getClass()) {
          return false;
        }

        Impl impl = (Impl) other;

        return packet.equals(impl.packet)
            && condition.equals(impl.condition);
      }

      @Override
      public int hashCode() {
        int result = packet.hashCode();
        result = 31 * result + condition.hashCode();
        return result;
      }

      @Override

      public String toString() {
        return "InterledgerPaymentRequest.Impl{"
            + "packet=" + packet
            + ", condition=" + condition
            + '}';
      }
    }

  }
}

