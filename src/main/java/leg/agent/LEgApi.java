package leg.agent;

import java.util.Properties;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LEgApi {
    private static boolean initialized = false;
    private static PropertyChainBox propertyChainBox = new PropertyChainBox(null);
    private static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public static PropertyChainBox getProperties() {
        return propertyChainBox;
    }

    public static void init(Properties properties, StringBuilder errBuilder) {
        try {
            readWriteLock.writeLock().lock();
            if (initialized) {
                errBuilder.append("Already initialized.");
            } else {
                propertyChainBox = new PropertyChainBox(null, properties);
                initialized = true;
            }
        } catch (Throwable t) {

        } finally {
            readWriteLock.writeLock().unlock();
        }

    }

    public static void dispose(StringBuilder errBuilder) {
        try {
            readWriteLock.writeLock().lock();
            if (!initialized) {
                errBuilder.append("Not initialized.");
            } else {
                initialized = false;
                Actor.reset();
            }
        } catch (Throwable t) {

        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public static void mark(String domain, String event, byte propertyData[]) {
        try {
            readWriteLock.readLock().lock();
            if (!initialized) {
                return;
            }
            Actor actor = Actor.getCreateCurrentActor(propertyChainBox);
            /**
             *   void mark(String  domain,
             *   String event,byte propertyData[],
             *   String classFullName,
             *   String method,
             *   int lineNumber){
             *
             */
            int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String method = Thread.currentThread().getStackTrace()[2].getMethodName();
            long time = System.nanoTime();
            EventComputedDetails eventComputedDetails = new EventComputedDetails(lineNumber, className, method, time);
            actor.mark(domain, event, propertyData, eventComputedDetails);
        } catch (Throwable t) {

        } finally {
            readWriteLock.readLock().unlock();
        }
    }
}
