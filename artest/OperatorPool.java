package artest;

import java.util.List;

public class OperatorPool {
    private List<Operator> operators;

    public OperatorPool(List<Operator> operators) {
        this.operators = operators;
    }

    /**
     * Получить свободного оператора
     * @param timeoutSeconds таймаут ожидания клиента в секундах
     * @return оператор или null, если таймаут ожидания
     */
    public synchronized Operator pollFreeOperator(int timeoutSeconds) throws InterruptedException {
        long startWait = System.currentTimeMillis();
        while (operators.isEmpty()) { // ждем, пока появится оператор свободный
            if (System.currentTimeMillis() - startWait > timeoutSeconds) {
                return null;
            }
            Thread.sleep(100);
        }
        return operators.remove(0);
    }

    /**
     * Вернуть оператора в пул
     */
    public synchronized void returnOperator(Operator operator) {
        operators.add(operator);
    }
}
