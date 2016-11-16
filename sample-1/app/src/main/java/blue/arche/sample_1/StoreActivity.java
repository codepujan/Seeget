package blue.arche.sample_1;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import java.util.List;

public class StoreActivity extends FragmentActivity {

    private  int NUM_PAGES;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private List<StoreItem> activeArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ModelsData.populateItemsData(getApplication());

        setContentView(R.layout.activity_store);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setActionBar(toolbar);
       // getSupportActionBar().setTitle(getIntent().getExtras().getString("BRAND_NAME"));
        if(getIntent().getExtras().getString("BRAND_NAME").equals("Adidas"))
        {
         activeArrayList=ModelsData.getAdidasItem();
        }else if(getIntent().getExtras().getString("BRAND_NAME").equals("Nike")){
            activeArrayList=ModelsData.getNikeItem();
        }else if(getIntent().getExtras().getString("BRAND_NAME").equals("North Face Jacket")){
            activeArrayList=ModelsData.getNorthFaceItems();
        }else if(getIntent().getExtras().getString("BRAND_NAME").equals("UnderArmor")){
            activeArrayList=ModelsData.getUnderArmorItems();
        }else if(getIntent().getExtras().getString("BRAND_NAME").equals("NikeCap")){
            activeArrayList=ModelsData.getNikeCaps();
        }

        NUM_PAGES=activeArrayList.size();
// Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);



    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }



    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new StoreProductFragment(activeArrayList.get(position));
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }




}
