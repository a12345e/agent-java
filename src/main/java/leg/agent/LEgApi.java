package leg.agent;

import java.util.Properties;

public class LEgApi {
    private static boolean initialized = false;
    private static PropertyChainBox propertyChainBox =new PropertyChainBox(null);
    private static Object lockMark = new Object();
    public  static PropertyChainBox getProperties(){
        return propertyChainBox;
    }
    public static void init(Properties properties, StringBuilder errBuilder){

        synchronized(lockMark) {
            if(initialized){
                errBuilder.append("Already initialized.");
            }else {
                propertyChainBox =new PropertyChainBox(null,properties);
                initialized = true;
            }
        }
    }
    public static void dispose(StringBuilder errBuilder){
        synchronized(lockMark) {
            if(!initialized){
                errBuilder.append("Not initialized.");
            }else {
                initialized = false;
            }
        };
    }

    public  static  void mark(String  domain,String operation,byte propertyData[]){
        Actor actor = Actor.getCreateCurrentActor(propertyChainBox);
        /**
         *   void mark(String  domain,
         *   String operation,byte propertyData[],
         *   String classFullName,
         *   String method,
         *   int lineNumber){
         *
         */
        int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
        String fileName= Thread.currentThread().getStackTrace()[2].getFileName();
        String className = Thread.currentThread().getStackTrace()[2].getClassName();
        String method = Thread.currentThread().getStackTrace()[2].getMethodName();
        actor.mark(domain,operation,propertyData,className,method,lineNumber);
    }



}
