package artest;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        final int phoneOperator = 4;

        List<Operator> operatorsList = Collections.synchronizedList(new LinkedList<>());
        for (int i = 0; i < phoneOperator; i++) {
            operatorsList.add(new Operator());
        }
        OperatorPool operatorPool = new OperatorPool(operatorsList);

        Random random = new Random();

        while (true) {
            // таймаут ожидания оператора (10-20 секунд) этим клиентом
            int timeoutWaitOperatorSeconds = random.nextInt(20) + 10;

            Client client = new Client(operatorPool, timeoutWaitOperatorSeconds);
            client.asyncCall();

            TimeUnit.SECONDS.sleep(10); // каждые 10 сек новый звонок
        }
    }
}
