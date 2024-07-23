package com.ytdd9527.networks.expansion.util.databases;

public interface QueuedTask {

    /**
     * The code will be invoked
     * @return true if do callback, else false.
     */
    boolean execute();

    /**
     * Invoke after execute()
     * @return true if abort the processor thread, else false.
     */
    default boolean callback() {
        return false;
    }

}
