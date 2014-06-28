package la.service.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by zhenjiaWang on 14-6-28.
 */
public class WxParamsUtils {
    public static final String laTokenKey = "la.service.common.wxParams";
    private static Map<String, Map<String,String>> userSessionMap = new ConcurrentHashMap<String, Map<String,String>>();
    private static ReadWriteLock readWriteLock = new ReentrantReadWriteLock(false);

    public static void put(String userId, Map<String,String> pair) {
        readWriteLock.writeLock().lock();
        userSessionMap.put(userId, pair);
        readWriteLock.writeLock().unlock();
    }

    public static Map<String,String> get(String userId) {
        readWriteLock.readLock().lock();
        Map<String,String> companyId = userSessionMap.get(userId);
        readWriteLock.readLock().unlock();
        return companyId;
    }

    public static void remove(String userId) {
        readWriteLock.writeLock().lock();
        userSessionMap.remove(userId);
        readWriteLock.writeLock().unlock();
    }
}
