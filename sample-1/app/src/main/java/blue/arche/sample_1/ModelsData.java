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
    private static List<StoreItem>underarmourItem;
    private static List<StoreItem>nikeCaps;


    public  static List<StoreItem>getAdidasItem(){

        return addidasItem;

    }


    public static List<StoreItem>getNikeItem(){
        return nikeItem;
    }

    public  static List<StoreItem>getNorthFaceItems(){
        return thenorthfaceItem;
    }


    public static List<StoreItem>getUnderArmorItems(){
        return underarmourItem;
    }

    public static List<StoreItem>getNikeCaps(){return nikeCaps;}



    public  static void populateItemsData(Context ctx){

        //let's fill in the items one by one

        //ADDIDAS FIRST


       addidasItem=new ArrayList<StoreItem>();
        nikeItem=new ArrayList<StoreItem>();
       thenorthfaceItem=new ArrayList<StoreItem>();
        underarmourItem=new ArrayList<StoreItem>();
        nikeCaps=new ArrayList<StoreItem>();

        nikeCaps.add(new StoreItem("Sun Protecting Hat",15.0f,"Best from the Nike Store",
                BitmapFactory.decodeResource(ctx.getResources(),R.drawable.nikehat),null,0));
        addidasItem.add(new StoreItem("White Woolen Shirt",28.50f,"White Shirts favoured by Sexy men above everything",
                BitmapFactory.decodeResource(ctx.getResources(), R.drawable.addswhit),
                null,1));

        addidasItem.add(new StoreItem("Red  Jeans Shirt",30.0f,"Especially popular at the month of Halloween",
                BitmapFactory.decodeResource(ctx.getResources(),R.drawable.addsred),
                null,1));

        addidasItem.add(new StoreItem("Black Cotton Shirt",24.0f,"Used by The Satans of The Hell",
                BitmapFactory.decodeResource(ctx.getResources(),R.drawable.addsblack),
                null,1));

            //NIKE THEN

        nikeItem.add(new StoreItem("White Cotton Cuisine",39.0f,"White Cuisines Used by The Royal Families",
                BitmapFactory.decodeResource(ctx.getResources(),R.drawable.nikewhite),
                null,1));

        nikeItem.add(new StoreItem("Scary Black Customee",24.0f,"Scary as hell : Especially in Halloween",
                BitmapFactory.decodeResource(ctx.getResources(),R.drawable.nikeblack),
                null,1));

        nikeItem.add(new StoreItem("Royal Grey Leather",50.0f,"Grey Cuisine from the Royal Family",
                BitmapFactory.decodeResource(ctx.getResources(),R.drawable.nikegrey),
            null,1));

        nikeItem.add(new StoreItem("Red Riding Hood",45.0f,"The Hood Little Red got away with",
                BitmapFactory.decodeResource(ctx.getResources(),R.drawable.nikered),
               null,1));


        thenorthfaceItem.add(new StoreItem("Navy Blue Mountain",60.0f,"Suited for The Mountain Everest",
                BitmapFactory.decodeResource(ctx.getResources(),R.drawable.northfacejacketblack),
                null,1));

        thenorthfaceItem.add(new StoreItem("Black Jacket",80.0f,"Black Leather Warmth",
                BitmapFactory.decodeResource(ctx.getResources(),R.drawable.northfacejacketnavyblue),
                null,1));


        //3 items for the underarmor stuff
        underarmourItem.add(new StoreItem("Red Sweatshirt",40.0f,"Suited for The Hot Guys in Summer",
                BitmapFactory.decodeResource(ctx.getResources(),R.drawable.ua_red),null,1));

        underarmourItem.add(new StoreItem("Black Sweatshirt",35.0f,"Black thing which absorbs every sun Out There",
                BitmapFactory.decodeResource(ctx.getResources(),R.drawable.ua_black),null,1));

        underarmourItem.add(new StoreItem("Blue Sweatshirt",58.0f,"Not Anything you find in every Bluemoon",
                BitmapFactory.decodeResource(ctx.getResources(),R.drawable.ua_blue),null,1));


    }
}
