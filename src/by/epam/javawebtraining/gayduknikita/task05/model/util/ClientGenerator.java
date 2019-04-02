package by.epam.javawebtraining.gayduknikita.task05.model.util;

import by.epam.javawebtraining.gayduknikita.task05.model.entity.CallCenter;
import by.epam.javawebtraining.gayduknikita.task05.model.entity.Client;
import org.apache.log4j.Logger;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author NikitaGayduk
 * @date 02.04.2019
 */
public class ClientGenerator implements Runnable {
    private static final Logger LOGGER = Logger.getRootLogger();

    private static final int MAX_GENERATION_PAUSE_SECONDS = 10;
    private static final int MIN_GENERATION_PAUSE_SECONDS = 3;
    private static final int MAX_WAITING_SECONDS = 20;
    private static final int MIN_WAITING_SECONDS = 5;
    private static final int MAX_SOLVING_TIME_SECONDS = 5;
    private static final int MIN_SOLVING_TIME_SECONDS = 3;


    private static final Random random = new Random();


    private Thread thread = new Thread(this);
    private CallCenter callCenter;

    public ClientGenerator(CallCenter callCenter){
        this.callCenter = callCenter;
        thread.start();
    }

    @Override
    public void run() {
        while (!thread.isInterrupted()){
            callCenter.call(new Client(getWaitingTime(),getSolvingTime()));

            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(getPauseTime()));
            } catch (InterruptedException e){
                LOGGER.trace("ClientGenerator was interrupted in pause time");
            }
        }
    }

    private int getRandomTime(int min, int max){
        return random.nextInt(max - min) + min;
    }

    private int getPauseTime(){
        return getRandomTime(MIN_GENERATION_PAUSE_SECONDS,MAX_GENERATION_PAUSE_SECONDS);
    }

    private int getWaitingTime(){
        return getRandomTime(MIN_WAITING_SECONDS,MAX_WAITING_SECONDS);
    }

    private int getSolvingTime(){
        return getRandomTime(MIN_SOLVING_TIME_SECONDS,MAX_SOLVING_TIME_SECONDS);
    }
}
