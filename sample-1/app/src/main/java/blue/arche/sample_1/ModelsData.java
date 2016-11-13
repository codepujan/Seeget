package blue.arche.sample_1;

import android.content.Context;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pujanpaudel on 11/13/16.
 */

public  class ModelsData {

    private static List<String>partners;

    private static List<StoreItem>addidasItem;
    private static List<StoreItem>nikeItem;
    private   static List<StoreItem>thenorthfaceItem;


    public  static List<StoreItem>getAdidasItem(){

        return addidasItem;

    }


    public static List<StoreItem>getNikeItem(){
        return nikeItem;
    }

    public  static List<StoreItem>getNorthFaceItems(){
        return thenorthfaceItem;
    }


    public  static void populateItemsData(Context ctx){

        //let's fill in the items one by one

        //ADDIDAS FIRST


       addidasItem=new ArrayList<StoreItem>();
        nikeItem=new ArrayList<StoreItem>();
       thenorthfaceItem=new ArrayList<StoreItem>();
        addidasItem.add(new StoreItem("White Woolen Shirt",28.50f,"White Shirts favoured by Sexy men above everything",
                BitmapFactory.decodeResource(ctx.getResources(), R.drawable.addswhit),
                BitmapFactory.decodeResource(ctx.getResources(),R.drawable.bonobos_zoom_hand_corduroy)));

        addidasItem.add(new StoreItem("Red  Jeans Shirt",30.0f,"Especially popular at the month of Halloween",
                BitmapFactory.decodeResource(ctx.getResources(),R.drawable.addsred),
                BitmapFactory.decodeResource(ctx.getResources(),R.drawable.bonobos_zoom_hand_jean)));

        addidasItem.add(new StoreItem("Black Cotton Shirt",24.0f,"Used by The Satans of The Hell",
                BitmapFactory.decodeResource(ctx.getResources(),R.drawable.addsblack),
                BitmapFactory.decodeResource(ctx.getResources(),R.drawable.bonobos_zoom_hand_wool)));

            //NIKE THEN

        nikeItem.add(new StoreItem("White Cotton Cuisine",39.0f,"White Cuisines Used by The Royal Families",
                BitmapFactory.decodeResource(ctx.getResources(),R.drawable.nikewhite),
                BitmapFactory.decodeResource(ctx.getResources(),R.drawable.tanvas_catalog_monochrome)));

        nikeItem.add(new StoreItem("Scary Black Customee",24.0f,"Scary as hell : Especially in Halloween",
                BitmapFactory.decodeResource(ctx.getResources(),R.drawable.nikeblack),
                BitmapFactory.decodeResource(ctx.getResources(),R.drawable.flannel_haptics)));

        nikeItem.add(new StoreItem("Royal Grey Leather",50.0f,"Grey Cuisine from the Royal Family",
                BitmapFactory.decodeResource(ctx.getResources(),R.drawable.nikegrey),
                BitmapFactory.decodeResource(ctx.getResources(),R.drawable.fabrictest_haptics)));

        nikeItem.add(new StoreItem("Red Riding Hood",45.0f,"The Hood Little Red got away with",
                BitmapFactory.decodeResource(ctx.getResources(),R.drawable.nikered),
                BitmapFactory.decodeResource(ctx.getResources(),R.drawable.zipper_haptics)));


        thenorthfaceItem.add(new StoreItem("Navy Blue Mountain",60.0f,"Suited for The Mountain Everest",
                BitmapFactory.decodeResource(ctx.getResources(),R.drawable.northfacejacketblack),
                BitmapFactory.decodeResource(ctx.getResources(),R.drawable.bonobos_zoom_hand_jean)));

        thenorthfaceItem.add(new StoreItem("Black Jacket",80.0f,"Black Leather Warmth",
                BitmapFactory.decodeResource(ctx.getResources(),R.drawable.northfacejacketnavyblue),
                BitmapFactory.decodeResource(ctx.getResources(),R.drawable.bonobos_zoom_hand_wool)));

    }
}
