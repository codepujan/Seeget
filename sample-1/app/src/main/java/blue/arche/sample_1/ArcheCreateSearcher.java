package blue.arche.sample_1;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import jp.co.nec.gazirur.rtsearch.lib.clientapi.RTFeatureSearcher;
import jp.co.nec.gazirur.rtsearch.lib.clientapi.RTSearchApi;

public class ArcheCreateSearcher extends AsyncTask<Void, Void, RTFeatureSearcher>  {
    private Context context;
    private ArcheCreateSearcherCallbacks callback = null;

    // Constructor
    public ArcheCreateSearcher(Context context, ArcheCreateSearcherCallbacks callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected RTFeatureSearcher doInBackground(Void... params) {
        RTSearchApi rtSearchApi = new RTSearchApi(context);
        RTFeatureSearcher feSearcher = rtSearchApi.GetInstance(320, 240, null);
        return feSearcher;
    }

    @Override
    protected void onPostExecute(RTFeatureSearcher feSearcher) {
        if (feSearcher != null) {
            Log.d("ARCHE", "Instance_OK");
        } else {
            Log.e("ARCHE", "Fail to Create Search Instance");
        }
        callback.postExecute(feSearcher);
    }
}
