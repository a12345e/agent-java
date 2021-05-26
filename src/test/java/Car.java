import java.util.HashMap;
import java.util.Map;

public class Car {
    public Car(String type, Owner owner) {
        this.type = type;
        this.localOwner = owner;
    }

    String type;
    Owner localOwner;

    Map<String,Owner> mapKeyToOwner = new HashMap<>();
    public void addOwner(Owner owner){
        mapKeyToOwner.put(owner.id,owner);
    }





}
