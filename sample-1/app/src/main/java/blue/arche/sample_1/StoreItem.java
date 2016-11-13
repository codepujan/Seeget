package blue.arche.sample_1;

import android.graphics.Bitmap;

/**
 * Created by pujanpaudel on 11/13/16.
 */

public class StoreItem {

    public String name;
    public Bitmap productImage;
    public Bitmap tanvasImage;

    public StoreItem(String name, float price, String description,Bitmap productImage,Bitmap tanvasImage) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.productImage=productImage;
        this.tanvasImage=tanvasImage;
    }

    public float price;
    public String description;
    //Camera Button
}
