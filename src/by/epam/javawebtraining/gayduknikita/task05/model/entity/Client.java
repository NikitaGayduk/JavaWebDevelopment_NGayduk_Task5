package by.epam.javawebtraining.gayduknikita.task05.model.entity;


import org.apache.log4j.Logger;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author NikitaGayduk
 * @date 01.04.2019
 */
public class Client implements Runnable{
    private static final Logger LOGGER = Logger.getRootLogger();
    private static AtomicInteger clientsCount = new AtomicInteger(1);

    private final int clientId;
    private final int waitingTimeSeconds;
    private final int problemSolvingTimeSeconds;

    private AtomicBoolean awaiting = new AtomicBoolean(false);


    public Client(int waitingTimeSeconds,int problemSolvingTimeSeconds) {
        this.waitingTimeSeconds = waitingTimeSeconds;
        this.problemSolvingTimeSeconds = problemSolvingTimeSeconds;
        this.clientId = clientsCount.getAndIncrement();
        LOGGER.trace("Client " + clientId + " was created");
    }

    public boolean isAwaiting(){
        return awaiting.get();
    }

    public void setAwaiting(boolean awaiting){
        this.awaiting.set(awaiting);
    }

    public int getClientId() {
        return clientId;
    }

    @Override
    public void run() {

    }
}
