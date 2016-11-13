package blue.arche.sample_1;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import jp.co.nec.gazirur.rtsearch.lib.clientapi.RTSearchApi;

public class ArcheAuth extends AsyncTask<Void, Void, String> {
    private Context context;

    // Constructor
    public ArcheAuth(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... params) {
        RTSearchApi rtSearchApi = new RTSearchApi(context);
        String result = rtSearchApi.RTSearchAuth();
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.equals("0000")) { // result OK
            Log.d("ARCHE", "AUTH_OK");
        } else if (result.equals("0101")) { // service non-contract
            Log.e("ARCHE", "AUTH_NG");
        } else { // Otherwise
            Log.e("ARCHE", "AUTH_OPE_NG");
        }
    }
}