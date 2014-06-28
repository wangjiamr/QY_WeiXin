package la.service.util;

import la.service.common.UserInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by zhenjiaWang on 14-6-28.
 */
public class CompanyChoiceUtils {
    public static final String laTokenKey = "la.service.common.companyID";
    private static Map<String, String> userSessionMap = new ConcurrentHashMap<String, String>();
    private static ReadWriteLock readWriteLock = new ReentrantReadWriteLock(false);

    public static void put(String laToken, String companyId) {
        readWriteLock.writeLock().lock();
        userSessionMap.put(laToken, companyId);
        readWriteLock.writeLock().unlock();
    }

    public static String get(String laToken) {
        readWriteLock.readLock().lock();
        String companyId = userSessionMap.get(laToken);
        readWriteLock.readLock().unlock();
        return companyId;
    }

    public static void remove(String laToken) {
        readWriteLock.writeLock().lock();
        userSessionMap.remove(laToken);
        readWriteLock.writeLock().unlock();
    }
}
