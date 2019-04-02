package by.epam.javawebtraining.gayduknikita.task05.model.entity;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author NikitaGayduk
 * @date 02.04.2019
 */
public class CallCenter {
    private static final Logger LOGGER = Logger.getRootLogger();

    private LinkedBlockingQueue<Client> clientsQueue = new LinkedBlockingQueue<>();
    private List<Operator> operatorsList = new ArrayList<>();

    //------------------------------------------------------------------------------------------------------------------
    private class Operator implements Runnable {
        private Thread operatorThread = new Thread(this);
        private int operatorId;

        public Operator(int operatorId){
            this.operatorId = operatorId;
            operatorThread.start();
        }

        public void solveProblem(int solvingTimeSeconds){
            try{
                TimeUnit.SECONDS.sleep(solvingTimeSeconds);
            } catch (InterruptedException e){
                LOGGER.trace("Operator " + operatorId + " work was interrupted");
            }
        }

        public int getOperatorId() {
            return operatorId;
        }

        @Override
        public void run() {
            while(true){
                try {
                    Thread.sleep(1000);
                    //LOGGER.trace("Operator " + operatorId +" awaiting for client");
                } catch (InterruptedException e){
                    LOGGER.trace("Operator work interrupted");
                }
            }
        }
    }
    //------------------------------------------------------------------------------------------------------------------


    public CallCenter(int initialOperatorsCount){
        for(int ptr = 0; ptr < initialOperatorsCount; ptr++){
            operatorsList.add(new Operator(ptr));
        }
        LOGGER.trace("Call-center created, operators: " + initialOperatorsCount);
    }

    public void call(Client client){
        try {
            clientsQueue.put(client);
            client.setAwaiting(true);
        } catch (InterruptedException e){
            LOGGER.trace("Client's " + client.getClientId() + "call was interrupted");
        }
    }
}
