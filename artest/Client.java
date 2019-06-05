package artest;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Client implements Runnable {
    private static int idCounter = 0;
    private static Random random = new Random();

    // номер клиента
    private int id = idCounter++;

    // таймаут ожидания оператора (10-20 секунд)
    private int timeoutWaitOperatorSeconds;

    // операторы
    private OperatorPool operatorPool;

    public Client(OperatorPool operatorPool, int timeoutWaitOperatorSeconds) {
        this.operatorPool = operatorPool;
        this.timeoutWaitOperatorSeconds = timeoutWaitOperatorSeconds;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                '}';
    }

    /**
     * Сделать звонок в отдельном потоке
     */
    public void asyncCall() {
        Thread thread = new Thread(this);
        thread.start();
    }

    private void maybeCallYouBack() throws InterruptedException {
        // с шансом 40% перезвоним
        if (random.nextInt(100) >= 60) {

            int callBackThroughSeconds = random.nextInt(20) + 10;
            System.out.println("Решили " + this + " перезвонить через " + callBackThroughSeconds);
            TimeUnit.SECONDS.sleep(callBackThroughSeconds);

            System.out.println("Перезваниваем " + this);
            this.asyncCall();
        }
    }

    @Override
    public void run() {
        System.out.println("Звоним в call центер " + this);
        try {
            Operator operator = operatorPool.pollFreeOperator(timeoutWaitOperatorSeconds);
            if (operator == null) {
                System.out.println("Не дозвонились, бросили трубку " + this);
                this.maybeCallYouBack(); // думаем, будем ли перезванивать
            } else {
                int callDurationSeconds = random.nextInt(30) + 40;
                System.out.println("Дозвонились " + this + " оператору " + operator + ". Будем говорить " + callDurationSeconds + " секунд");
                TimeUnit.SECONDS.sleep(callDurationSeconds);
                System.out.println("Поговорили " + this + " с оператором " + operator + ", кладем трубку.");
                operatorPool.returnOperator(operator);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
