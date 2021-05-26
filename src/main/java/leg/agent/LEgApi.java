package leg.agent;

import java.util.Properties;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LEgApi {
    private static boolean initialized = false;
    private static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();



    public static void init(Properties properties, StringBuilder errBuilder) {
        try {
            readWriteLock.writeLock().lock();
            if (initialized) {
                errBuilder.append("Already initialized.");
            } else {
                ActorInitProperties.initialize(properties);
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

    public static void mark(String domain, String event, byte data[]) {
        try {
            readWriteLock.readLock().lock();
            if (!initialized) {
                return;
            }
            Actor actor = Actor.getCreateCurrentActor();
            /**
             *   void mark(String  domain,
             *   String event,byte data[],
             *   String classFullName,
             *   String method,
             *   int lineNumber){
             *
             */
            int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String method = Thread.currentThread().getStackTrace()[2].getMethodName();
            Thread.currentThread().getStackTrace()[1].
            long time = System.nanoTime();
            EventComputedDetails eventComputedDetails = new EventComputedDetails(lineNumber, className, method, time);
            actor.mark(domain, event, data, eventComputedDetails);
        } catch (Throwable t) {

        } finally {
            readWriteLock.readLock().unlock();
        }
    }
}
