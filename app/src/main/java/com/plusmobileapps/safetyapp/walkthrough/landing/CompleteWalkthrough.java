package com.plusmobileapps.safetyapp.walkthrough.landing;

import android.os.AsyncTask;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.WalkthroughDao;
import com.plusmobileapps.safetyapp.data.entity.Walkthrough;
import com.plusmobileapps.safetyapp.sync.UploadTask;
import com.plusmobileapps.safetyapp.util.DateTimeUtil;

import java.util.Date;

/**
 * Created by aaronmusengo on 4/18/18.
 */

public class CompleteWalkthrough extends AsyncTask<Void, Void, Walkthrough> {
    private AppDatabase db;
    private Walkthrough walkthrough;
    private int walkthroughId;
    private WalkthroughLandingPresenter presenter;

    public CompleteWalkthrough(Walkthrough walkthrough, WalkthroughLandingPresenter presenter) {
        this.walkthrough = walkthrough;
        this.presenter = presenter;
        walkthrough.setPercentComplete(100);
        walkthrough.setLastUpdatedDate(DateTimeUtil.getDateTimeString());
    }

    @Override
    protected Walkthrough doInBackground(Void... voids) {
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        WalkthroughDao dao = db.walkthroughDao();
        dao.insert(walkthrough);
        return walkthrough;
    }

    @Override
    protected void onPostExecute(Walkthrough walkthrough) {
        new LoadWalkthroughs(presenter.listener).execute();
        UploadTask uploadTask = new UploadTask(MyApplication.getAppContext());
        uploadTask.uploadData();
    }

}

