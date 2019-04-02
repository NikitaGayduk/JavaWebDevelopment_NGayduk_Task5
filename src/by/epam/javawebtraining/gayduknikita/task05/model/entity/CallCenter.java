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
        private Client client;

        public Operator(int operatorId){
            this.operatorId = operatorId;
            operatorThread.start();
        }

        public int getOperatorId() {
            return operatorId;
        }

        @Override
        public void run() {
            while(true){
                try {
                    client = clientsQueue.take();
                    client.getLock().lock();

                    if(client.getState() == Client.State.AWAITING){
                        LOGGER.trace("Operator " + operatorId + " get client " + client.getClientId());
                        client.setState(Client.State.BEING_PROCESSED);
                        TimeUnit.SECONDS.sleep(client.getProblemSolvingTimeSeconds());
                        LOGGER.trace("Operator " + operatorId + " finish work with client " + client.getClientId());
                        client.setState(Client.State.NO_AWAITING);
                    } else {
                        LOGGER.trace("Operator " + operatorId + " cant process client " + client.getClientId()
                                + ", this client already leave the queue");
                    }

                    client.getLock().unlock();
                } catch (InterruptedException e){
                    LOGGER.trace("Operator " + operatorId + " was interrupted while waiting client");
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
            client.setState(Client.State.AWAITING);
        } catch (InterruptedException e){
            LOGGER.trace("Client's " + client.getClientId() + "call was interrupted");
        }
    }
}
