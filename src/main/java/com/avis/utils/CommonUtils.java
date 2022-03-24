package com.avis.utils;

import lombok.extern.log4j.Log4j2;

import java.util.Random;
@Log4j2
public class CommonUtils {

    // Sleep time
    public static final long ONE_SECOND = 1000;
    public static final long TWO_SECONDS = 2000;

    /**
     * Returns the random number between the min and max number
     *
     * @param min minimum range 0
     * @param max maximum int range
     * @return random value
     */
    public static String getRandomNumber(int min, int max) {
        Random random = new Random();
        int number = random.nextInt((max - min) + 1) + min;
        return String.valueOf(number);
    }

    /**
     * This method will pause the thread execution for specified time
     *
     * @param time Time(in milliseconds) till when the thread execution will be paused
     */
    public static void threadSleep(long time) {
        try {
            log.info("Wait time for " + time + " milliseconds");
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.info("There was an error while wait for " + time + " milliseconds. Please try again.");
        }
    }

}

