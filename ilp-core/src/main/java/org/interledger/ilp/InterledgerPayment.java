package org.interledger.ilp;

import org.interledger.InterledgerAddress;
import org.interledger.InterledgerPacket;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;

/**
 * <p>Interledger Payments moves assets of one party to another that consists of one or more ledger
 * transfers, potentially across multiple ledgers.</p>
 *
 * <p>Interledger Payments have three major consumers:</p>
 *   <ul>
 *     <li>Connectors utilize the Interledger Address contained in the payment to route the
 * payment.</li>
 *     <li>The receiver of a payment uses it to identify the recipient and which condition to
 * fulfill.</li>
 *     <li>Interledger sub-protocols utilize custom data encoded in a payment to facilitate
 * sub-protocol operations.</li>
 *   </ul>
 *
 * <p>When a sender prepares a transfer to start a payment, the sender attaches an ILP Payment to
 * the transfer, in the memo field if possible. If a ledger does not support attaching the entire
 * ILP Payment to a transfer as a memo, users of that ledger can transmit the ILP Payment using
 * another authenticated messaging channel, but MUST be able to correlate transfers and ILP
 * Payments.</p>
 *
 * <p>When a connector sees an incoming prepared transfer with an ILP Payment, the receiver reads
 * the ILP Payment to confirm the details of the packet. For example, the connector reads the
 * InterledgerAddress of the payment's receiver, and if the connector has a route to the receiver's
 * account, the connector prepares a transfer to continue the payment, and attaches the same ILP
 * Payment to the new transfer. Likewise, the receiver confirms that the amount of the ILP Payment
 * Packet matches the amount actually delivered by the transfer. And finally, the receiver decodes
 * the data portion of the Payment and matches the condition to the payment.</p>
 *
 * <p>The receiver MUST confirm the integrity of the ILP Payment, for example with a hash-based
 * message authentication code (HMAC). If the receiver finds the transfer acceptable, the receiver
 * releases the fulfillment for the transfer, which can be used to execute all prepared transfers
 * that were established prior to the receiver accepting the payment.</p>
 */
public interface InterledgerPayment extends InterledgerPacket {

  /**
   * The Interledger address of the account where the receiver should ultimately receive the
   * payment.
   *
   * @return An instance of {@link InterledgerAddress}.
   */
  InterledgerAddress getDestinationAccount();

  /**
   * The amount to deliver, in discrete units of the destination ledger's asset type. The scale of
   * the units is determined by the destination ledger's smallest indivisible unit.
   *
   * @return An instance of {@link BigInteger}.
   */
  BigInteger getDestinationAmount();

  /**
   * Arbitrary data for the receiver that is set by the transport layer of a payment (for example,
   * this may contain PSK data).
   *
   * @return A byte array.
   */
  byte[] getData();

  /**
   * A builder for instances of {@link InterledgerPayment}.
   */
  class Builder {

    final private InterledgerAddress destinationAccount;
    final private BigInteger destinationAmount;
    final private byte[] data;

    public Builder(final InterledgerAddress destinationAccount, final BigInteger destinationAmount, final byte[] data) {
      this.destinationAccount = destinationAccount;
      this.destinationAmount  = destinationAmount ;
      this.data               = data              ;
    }


    public static Builder builder(final InterledgerAddress destinationAccount, final BigInteger destinationAmount, final byte[] data) {
      return new Builder(destinationAccount, destinationAmount, data);
    }

    /**
     * The method that actually constructs a payment.
     *
     * @return An instance of {@link InterledgerPayment}.
     */
    public InterledgerPayment build() {
      return new Impl(this);
    }

    /**
     * A private, immutable implementation of {@link InterledgerPayment}.
     */
    private static final class Impl implements InterledgerPayment {

      private final InterledgerAddress destinationAccount;
      private final BigInteger destinationAmount;
      private final byte[] data;

      /**
       * No-args Constructor.
       */
      private Impl(final Builder builder) {
        Objects.requireNonNull(builder);
        this.destinationAccount = builder.destinationAccount;
        this.destinationAmount  = builder.destinationAmount;
        this.data               = builder.data;
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
      public byte[] getData() {
        return Arrays.copyOf(this.data, this.data.length);
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

        return destinationAccount.equals(impl.destinationAccount)
            && destinationAmount.equals(impl.destinationAmount)
            && Arrays.equals(data, impl.data);
      }

      @Override
      public int hashCode() {
        int result = destinationAccount.hashCode();
        result = 31 * result + destinationAmount.hashCode();
        result = 31 * result + Arrays.hashCode(data);
        return result;
      }

      @Override
      public String toString() {
        return "InterledgerPayment.Impl{"
            + "destinationAccount=" + destinationAccount
            + ", destinationAmount=" + destinationAmount
            + ", data=" + Arrays.toString(data)
            + '}';
      }
    }
  }
}
