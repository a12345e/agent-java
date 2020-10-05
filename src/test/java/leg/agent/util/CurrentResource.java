package leg.agent.util;

public class CurrentResource {
    public static String getTestExpectedJsonFile(int pop){
        String className = Thread.currentThread().getStackTrace()[2+pop].getClassName();
        String method = Thread.currentThread().getStackTrace()[2+pop].getMethodName();
        String prefix = "src/test/resources/";
        String value = prefix + className.replaceAll("\\.","/")+'/'+ method+".json";

        return value;
    }


}
