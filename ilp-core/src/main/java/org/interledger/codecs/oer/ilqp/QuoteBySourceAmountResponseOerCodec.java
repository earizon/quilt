package org.interledger.codecs.oer.ilqp;

import org.interledger.codecs.Codec;
import org.interledger.codecs.CodecContext;
import org.interledger.codecs.QuoteBySourceAmountResponseCodec;
import org.interledger.codecs.oer.OerUint32Codec.OerUint32;
import org.interledger.codecs.oer.OerUint64Codec.OerUint64;
import org.interledger.codecs.packettypes.InterledgerPacketType;
import org.interledger.ilqp.QuoteBySourceAmountResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * An implementation of {@link Codec} that reads and writes instances of {@link
 * QuoteBySourceAmountResponse}. in OER format.
 *
 * @see "https://github.com/interledger/rfcs/blob/master/asn1/InterledgerQuotingProtocol.asn"
 */
public class QuoteBySourceAmountResponseOerCodec implements QuoteBySourceAmountResponseCodec {

  @Override
  public QuoteBySourceAmountResponse read(CodecContext context, InputStream inputStream)
      throws IOException {

    Objects.requireNonNull(context);
    Objects.requireNonNull(inputStream);

    /* read the destination amount, which is a uint64 */
    final BigInteger destinationAmount = context.read(OerUint64.class, inputStream).getValue();

    /* read the source hold duration which is a unit32 */
    long sourceHoldDuration = context.read(OerUint32.class, inputStream).getValue();

    return QuoteBySourceAmountResponse.Builder.builder(
            destinationAmount,Duration.of(sourceHoldDuration, ChronoUnit.MILLIS))
        .build();
  }

  @Override
  public void write(CodecContext context, QuoteBySourceAmountResponse instance,
      OutputStream outputStream) throws IOException {

    Objects.requireNonNull(context);
    Objects.requireNonNull(instance);
    Objects.requireNonNull(outputStream);

    /* Write the packet type. */
    context.write(InterledgerPacketType.class, this.getTypeId(), outputStream);

    /* destination amount */
    context.write(OerUint64.class, new OerUint64(instance.getDestinationAmount()), outputStream);

    /* source hold duration */
    context.write(OerUint32.class, new OerUint32(instance.getSourceHoldDuration().toMillis()),
        outputStream);
  }
}
