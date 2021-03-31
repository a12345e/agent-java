package leg.common;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class TestJsonSerialization {

    @Test
    public void testSerialization(){
        CanonizedTransformableElement e = new CanonizedTransformableElement();
        e.getBooleanMap().put("bool1",true);
        e.getBooleanMap().put("bool2",false);

        e.getStringMap().put("s1","value-s1");
        e.getStringMap().put("s2","value-s2");

        e.getLongMap().put("l1",10L);
        e.getLongMap().put("l2",11L);

        e.getStringListMap().put("sl1",Arrays.asList("s1","s2"));


        CanonizedTransformableElement eSon1 = new CanonizedTransformableElement();
        eSon1.getBooleanMap().put("boolS1",true);
        eSon1.getBooleanMap().put("boolS2",false);

        eSon1.getStringMap().put("sS1","value-s1");
        eSon1.getStringMap().put("sS2","value-s2");

        eSon1.getLongMap().put("ls1",10L);
        eSon1.getLongMap().put("ls2",11L);

        e.getTransformableMap().put("t1",eSon1);

        CanonizedTransformableElement eSon11 = new CanonizedTransformableElement();
        eSon11.getBooleanMap().put("boolS11",true);
        eSon11.getBooleanMap().put("boolS21",false);

        eSon11.getStringMap().put("sS11","value-s1");
        eSon11.getStringMap().put("sS21","value-s2");

        eSon11.getLongMap().put("ls11",10L);
        eSon11.getLongMap().put("ls21",11L);

        CanonizedTransformableElement eSon12 = new CanonizedTransformableElement();
        eSon12.getBooleanMap().put("boolS12",true);
        eSon12.getBooleanMap().put("boolS22",false);

        eSon12.getStringMap().put("sS12","value-s1");
        eSon12.getStringMap().put("sS22","value-s2");

        eSon12.getLongMap().put("ls12",10L);
        eSon12.getLongMap().put("ls22",11L);

        eSon1.getTransformableListMap().put("kukus", Arrays.asList(eSon11,eSon12));

        ObjectNode node = new JsonTransfomer().serialize(e);
        String val = node.toPrettyString();
        System.out.println(val);

        CanonizedTransformableElement deser = (CanonizedTransformableElement) new JsonTransfomer().deserialize(node);
        ObjectNode node2 = new JsonTransfomer().serialize(deser);
        String val2 = node2.toPrettyString();
        System.out.println(val2);

        Assert.assertTrue(e.equalTo(deser));
        Assert.assertEquals(val,val2);



    }
}
