package leg.agent.execution;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LEgWorkerPool {

    private final int corePoolSize;
    private final int maximumPulSize;
    private final int keepAliveTime;
    private final int taskQueueSize;
    Map<Runnable,Integer> mapTasks;
    public void addRunnables(Runnable runnable,int count){
        mapTasks.put(runnable,count);
    }
    public LEgWorkerPool(int corePoolSize,int maximumPulSize,int keepAliveTime,int taskQueueSize){
        this.corePoolSize = corePoolSize;
        this.maximumPulSize = maximumPulSize;
        this.keepAliveTime = keepAliveTime;
        this.taskQueueSize = taskQueueSize;
        mapTasks = new HashMap<>();
    }
    public void run() throws InterruptedException{
        //RejectedExecutionHandler implementation
        LEgTestRejectedExecutionHandler rejectionHandler = new LEgTestRejectedExecutionHandler();
        //Get the ThreadFactory implementation to use
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        //creating the ThreadPoolExecutor
        ThreadPoolExecutor executorPool = new ThreadPoolExecutor(corePoolSize, maximumPulSize,keepAliveTime, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(taskQueueSize), threadFactory, rejectionHandler);
        //start the monitoring thread
        LEgTestMonitorThread monitor = new LEgTestMonitorThread(executorPool, 3);
        Thread monitorThread = new Thread(monitor);
        monitorThread.start();
        //submit work to the thread pool
        for( Map.Entry<Runnable,Integer> e: mapTasks.entrySet()){
            for(int i=0; i < e.getValue() ; i++){
                executorPool.execute(new Thread(e.getKey()));
            }

        }
        //shut down the pool
        executorPool.shutdown();
        //shut down the monitor thread
        while(true){
            if(!executorPool.awaitTermination(10,TimeUnit.SECONDS))
                break;
        }
        monitor.shutdown();

    }
    public static void main(String args[]) {

    }
}

