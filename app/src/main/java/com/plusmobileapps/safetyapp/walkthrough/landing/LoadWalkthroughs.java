package com.plusmobileapps.safetyapp.walkthrough.landing;

import android.os.AsyncTask;
import android.util.Log;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.WalkthroughDao;
import com.plusmobileapps.safetyapp.data.entity.Response;
import com.plusmobileapps.safetyapp.data.entity.Walkthrough;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kneil on 2/21/2018.
 */

public class LoadWalkthroughs extends AsyncTask<Void, Void, List<Walkthrough>> {
    private static final String TAG = "LoadWalkthroughs";
    private AppDatabase db;
    private List<Walkthrough> walkthroughs;
    private WalkthroughLandingContract.View view;
    private WalkthroughLandingPresenter.WalkthroughListLoadingListener listener;

    public LoadWalkthroughs(WalkthroughLandingPresenter.WalkthroughListLoadingListener listener) {
        this.listener = listener;
    }

    @Override
    protected List<Walkthrough> doInBackground(Void... voids) {
        Log.d(TAG, "Loading walkthroughs...");
        db = AppDatabase.getAppDatabase((MyApplication.getAppContext()));
        WalkthroughDao dao = db.walkthroughDao();
        walkthroughs = dao.getAll();

        return walkthroughs;
    }

    protected void onPostExecute(List<Walkthrough> walkthroughs) {
        Log.d(TAG, "Done loading walkthroughs");
        listener.onWalkthroughListLoaded(walkthroughs);
    }
}
