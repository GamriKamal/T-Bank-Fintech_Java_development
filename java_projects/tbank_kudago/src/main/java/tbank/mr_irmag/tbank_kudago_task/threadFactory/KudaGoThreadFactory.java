package tbank.mr_irmag.tbank_kudago_task.threadFactory;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.ThreadFactory;

@RequiredArgsConstructor
public class KudaGoThreadFactory implements ThreadFactory {
    private int threadCount = 0;
    private final String poolName;

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, poolName + " Thread " + (++threadCount));
    }
}
