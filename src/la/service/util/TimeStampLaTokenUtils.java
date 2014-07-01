package la.service.util;

import la.service.common.UserInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by zhenjiaWang on 14-6-19.
 */
public class TimeStampLaTokenUtils {
    private static Map<String, Long> tokenTimeStampMap = new ConcurrentHashMap<String, Long>();
    private static ReadWriteLock readWriteLock = new ReentrantReadWriteLock(false);

    public static void put(String laToken, Long timeStamp) {
        readWriteLock.writeLock().lock();
        tokenTimeStampMap.put(laToken, timeStamp);
        readWriteLock.writeLock().unlock();
    }

    public static Long get(String laToken) {
        readWriteLock.readLock().lock();
        Long timeStamp = tokenTimeStampMap.get(laToken);
        readWriteLock.readLock().unlock();
        return timeStamp;
    }

    public static void remove(String laToken) {
        readWriteLock.writeLock().lock();
        tokenTimeStampMap.remove(laToken);
        readWriteLock.writeLock().unlock();
    }
}
