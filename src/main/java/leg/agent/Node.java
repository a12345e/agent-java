package leg.agent;

import com.google.gson.annotations.SerializedName;

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

    final transient PropertyChainBox propertyChainBox;
    Map<String,Edge> maptTarget2Edge = new HashMap<String,Edge>();

    public Node(String domain,
            String operation,
            String className,
            String method,
            int lineNumber,
            PropertyChainBox parentProperties)
    {
            this.domain = domain;
            this.operation = operation;
            this.className = className;
            this.method = method;
            this.lineNumber = lineNumber;
            this.propertyChainBox = new PropertyChainBox(parentProperties);

    }

    public void visit(String target,long step,long time,byte data[]){
        Edge edge = maptTarget2Edge.get(target);
        if(edge == null) {
            edge = new Edge(target,propertyChainBox);
            maptTarget2Edge.put(target,edge);
        }
        edge.getHistory().visit(step,time,data);
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

    final public String getKey(){
        return getKey(this.domain,this.operation,this.className,this.method,this.lineNumber);
    }
    final static public String getKey(
            String domain,
            String operation,
            String className,
            String method,
            int lineNumber)
    {
        StringBuilder buf = new StringBuilder();
        buf.append(domain).append('#');
        buf.append(operation).append('#');
        buf.append(className).append('#');
        buf.append(method).append('#');
        buf.append(lineNumber);
        String key = buf.toString();
        return sha1(key);
    }

}
