package blue.arche.sample_1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by pujanpaudel on 11/13/16.
 */

public class StoreProductFragment extends Fragment {

    private StoreItem storeInstance;


        public StoreProductFragment(){
        }

    @SuppressLint("ValidFragment")
    public  StoreProductFragment(StoreItem item){
        storeInstance=item;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.storeproductdetails, container, false);

        TextView productTitle=(TextView)rootView.findViewById(R.id.product_name);
        TextView description=(TextView)rootView.findViewById(R.id.product_description);
        TextView product_price=(TextView)rootView.findViewById(R.id.product_price);
        ImageView targetImage=(ImageView)rootView.findViewById(R.id.jersey);


        productTitle.setText(storeInstance.name);
        description.setText(storeInstance.description);
        product_price.setText("Price  $"+String.valueOf(storeInstance.price));
        targetImage.setImageBitmap(storeInstance.productImage);

        //the list of buttons which we could interact with
        Button tryout=(Button)rootView.findViewById(R.id.start_preview);
        Button shopNearMe=(Button)rootView.findViewById(R.id.shop_near_me);
        Button shop=(Button) rootView.findViewById(R.id.shop);



        tryout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Make the big leap to the Camera APIs
                Toast.makeText(getActivity(),"There . We're Almost There !! ",Toast.LENGTH_LONG).show();
                StaticHolder.setCurrentStore(storeInstance);

                //Now do an API CAll to get to the SurfaceView Activity

                Intent preview=new Intent(getActivity(), SurfaceViewActivity.class);
                startActivity(preview);
            }
        });

        shopNearMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Shop Near me is coming to your cities", Toast.LENGTH_SHORT).show();
            }
        });

        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "I am still machine learning Search Capacities", Toast.LENGTH_SHORT).show();

            }
        });
        return rootView;
    }



}
