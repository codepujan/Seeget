package blue.arche.sample_1;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import jp.co.nec.gazirur.rtsearch.lib.bean.SearchResult;
import jp.co.nec.gazirur.rtsearch.lib.clientapi.RTFeatureSearcher;

public class ArcheSearch extends AsyncTask<Void, Void, List<SearchResult>> {
    private ArcheSearchCallbacks callback = null;
    private RTFeatureSearcher feSearcher;
    private byte[] frame;

    // Constructor
    public ArcheSearch(RTFeatureSearcher feSearcher, byte[] frame, ArcheSearchCallbacks callback) {
        this.feSearcher = feSearcher;
        this.frame = frame;
        this.callback = callback;
    }

    @Override
    protected List<SearchResult> doInBackground(Void... params) {
        //List<SearchResult> searchResults = feSearcher.ExecuteFeatureSearch(frame, RTFeatureSearcher.SERVER_STAGING_SEARCH);
        List<SearchResult> searchResults = feSearcher.ExecuteFeatureSearch(frame, RTFeatureSearcher.SERVER_SERVICE_SEARCH);
        return searchResults;
    }

    @Override
    protected void onPostExecute(List<SearchResult> searchResults) {
        if (searchResults == null) {
            Log.d("ARCHE", "searchResults == null");
        } else {
            int hitCount = searchResults.size();
            Log.d("ARCHE", "Hit Count = " + hitCount);
            //select best match
            double maxScore = 0.;
            SearchResult bestResult = null;
            for (SearchResult result: searchResults) {
                double score = result.getScore();
                if (score > maxScore) {
                    maxScore = score;
                    bestResult = result;
                }
            }
            callback.postExecute(bestResult);
        }
    }
}
