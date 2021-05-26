package leg.agent;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

public class Node {
    final String domain;
    final String operation;
    final String className;
    final String method;
    final int lineNumber;


    Map<String, History> maptTarget2History = new HashMap<>();
    public Node(String domain,
            String operation,
            String className,
            String method,
            int lineNumber)
    {
            this.domain = domain;
            this.operation = operation;
            this.className = className;
            this.method = method;
            this.lineNumber = lineNumber;

    }


    final public String getKey(){
        return getKey(this.domain,this.className,this.method,this.lineNumber,this.operation);
    }
    final static public String getKey(
            String domain,
            String className,
            String method,
            int lineNumber,
            String operation)
    {
        StringBuilder buf = new StringBuilder();
        buf.append(domain).append('#');
        buf.append(className).append('#');
        buf.append(method).append('#');
        buf.append(lineNumber).append('#');
        buf.append(operation);
        String key = buf.toString();
        return sha1(key);
    }

    private static String sha1(String value)
    {
        String sha1 = "";
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(value.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return sha1;
    }
    private static String byteToHex(final byte[] hash)
    {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }


}
