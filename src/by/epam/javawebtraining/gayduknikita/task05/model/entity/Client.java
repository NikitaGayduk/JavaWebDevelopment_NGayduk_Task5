package by.epam.javawebtraining.gayduknikita.task05.model.entity;


import org.apache.log4j.Logger;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author NikitaGayduk
 * @date 01.04.2019
 */
public class Client implements Runnable {
    private static final Logger LOGGER = Logger.getRootLogger();
    private static AtomicInteger clientsCount = new AtomicInteger(1);

    private final int clientId;
    private final int waitingTimeSeconds;
    private final int problemSolvingTimeSeconds;
    private final CallCenter callCenter;
    private final ReentrantLock lock = new ReentrantLock();

    private Thread thread = new Thread(this);
    private volatile State state = State.NO_AWAITING;

    public enum State{
        AWAITING, BEING_PROCESSED, NO_AWAITING
    }


    public Client(int waitingTimeSeconds, int problemSolvingTimeSeconds, CallCenter callCenter) {
        this.waitingTimeSeconds = waitingTimeSeconds;
        this.problemSolvingTimeSeconds = problemSolvingTimeSeconds;
        this.callCenter = callCenter;
        this.clientId = clientsCount.getAndIncrement();

        LOGGER.trace("Client " + clientId + " created, waiting time: " + waitingTimeSeconds
                + " sec, problem solving time: " + problemSolvingTimeSeconds + " sec");
        thread.start();
    }


    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public int getClientId() {
        return clientId;
    }

    public int getProblemSolvingTimeSeconds() {
        return problemSolvingTimeSeconds;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    @Override
    public void run() {
        Random random = new Random();
        boolean recall = true;

        while (recall) {
            lock.lock();
            callCenter.call(this);
            LOGGER.trace("Client " + clientId + " call and placed in queue");
            this.state = State.AWAITING;
            lock.unlock();



            try {
                this.state = State.AWAITING;
                TimeUnit.SECONDS.sleep(waitingTimeSeconds);
            } catch (InterruptedException e) {
                LOGGER.trace("Client " + clientId + " was interrupted while sleeping");
            }

            lock.lock();
            if (state == State.NO_AWAITING) {
                lock.unlock();
                break;
            } else {
                LOGGER.trace("Client " + clientId + " waited too long, he leaving the queue");
                state = State.NO_AWAITING;
                lock.unlock();
            }

            recall = random.nextBoolean();
            if(!recall){
                LOGGER.trace("Client " + clientId + " will no more call");
            }
        }
    }
}
