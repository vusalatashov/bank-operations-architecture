package az.xalqbank.mscustomers.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransactionEventUtils {

    public static String generateEventTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }

    public static String generateTransactionId() {
        return "TXN-" + System.currentTimeMillis();
    }
}
