package blue.arche.sample_1;

/**
 * Created by pujanpaudel on 11/13/16.
 */

public class StaticHolder {
    public static  StoreItem store;
    public static void setCurrentStore(StoreItem currentStore){
        store=currentStore;
    }


    public  static StoreItem getCurrentStore(){
        return store;
    }
}
