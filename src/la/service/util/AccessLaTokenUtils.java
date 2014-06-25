package la.service.util;

import la.service.common.UserInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by zhenjiaWang on 14-6-19.
 */
public class AccessLaTokenUtils {
    public static final String laTokenKey="la.service.common.UserInfo";
    private static Map<String, UserInfo> userSessionMap = new ConcurrentHashMap<String, UserInfo>();
    private static ReadWriteLock readWriteLock = new ReentrantReadWriteLock(false);

    public static void put(String laToken, UserInfo userInfo) {
        readWriteLock.writeLock().lock();
        userSessionMap.put(laToken, userInfo);
        readWriteLock.writeLock().unlock();
    }

    public static UserInfo get(String laToken) {
        readWriteLock.readLock().lock();
        UserInfo userInfo = userSessionMap.get(laToken);
        readWriteLock.readLock().unlock();
        return userInfo;
    }
    public static void remove(String laToken) {
        readWriteLock.writeLock().lock();
        userSessionMap.remove(laToken);
        readWriteLock.writeLock().unlock();
    }
}
