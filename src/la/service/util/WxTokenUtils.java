package la.service.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by zhenjiaWang on 14-6-28.
 */
public class WxTokenUtils {
    public static final String laTokenKey = "la.service.common.wxToken";
    private static Map<String, String> userSessionMap = new ConcurrentHashMap<String, String>();
    private static ReadWriteLock readWriteLock = new ReentrantReadWriteLock(false);

    public static void put(String userId, String token) {
        readWriteLock.writeLock().lock();
        userSessionMap.put(userId, token);
        readWriteLock.writeLock().unlock();
    }

    public static String get(String userId) {
        readWriteLock.readLock().lock();
        String companyId = userSessionMap.get(userId);
        readWriteLock.readLock().unlock();
        return companyId;
    }

    public static void remove(String userId) {
        readWriteLock.writeLock().lock();
        userSessionMap.remove(userId);
        readWriteLock.writeLock().unlock();
    }
}
