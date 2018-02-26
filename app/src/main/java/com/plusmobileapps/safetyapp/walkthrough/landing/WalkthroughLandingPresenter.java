package com.plusmobileapps.safetyapp.walkthrough.landing;

import java.util.ArrayList;

import com.plusmobileapps.safetyapp.data.entity.Walkthrough;
import java.util.Date;

public class WalkthroughLandingPresenter implements WalkthroughLandingContract.Presenter {

    private WalkthroughLandingContract.View view;
    private ArrayList<Walkthrough> walkthroughs;
    private Walkthrough walkthrough;

    public WalkthroughLandingPresenter(WalkthroughLandingContract.View view) {
        this.view = view;
        view.setPresenter(this);
        walkthroughs = new ArrayList<>(0);
    }

    @Override
    public void start() {
        new LoadWalkthroughs(listener).execute();
    }

    @Override
    public void walkthroughClicked(int position) {
        final Walkthrough walkthrough = walkthroughs.get(position);
        view.openWalkthrough(walkthrough.getWalkthroughId(), walkthrough.getName());
    }

    @Override
    public void deleteInProgressWalkthroughConfirmed() {
        new DeleteWalkthrough(walkthroughs.get(0), view).execute();
        createNewWalkthrough();
    }

    @Override
    public void createNewWalkthroughIconClicked() {
        if(walkthroughs.size() > 0) {
            // Walkthrough in db doesn't align to how I set these up. This corrects it until we
            // add walkthroughs that conform. Changed it to 80% complete to test logic.
            Date d = new Date();
            walkthroughs.get(0).setCreatedDate(d.toString());
            walkthroughs.get(0).setLastUpdatedDate(d.toString());
            walkthroughs.get(0).setPercentComplete(80.0);
            if(walkthroughs.get(0).isInProgress()) {
                view.showInProcessConfirmationDialog();
            } else {
                createNewWalkthrough();
            }
        } else {
            createNewWalkthrough();
        }
    }

    private void createNewWalkthrough() {
        view.showCreateWalkthroughDialog();
    }


    public void setupLandingUi(ArrayList walkthroughs) {
        view.showWalkthroughs(walkthroughs);
    }

    @Override
    public void confirmCreateWalkthroughClicked(String title) {
        walkthrough = new Walkthrough(title);
        new SaveNewWalkthrough(walkthrough, view);
    }

    @Override
    public ArrayList getWalkthoughs() {
        return walkthroughs;
    }

    @Override
    public void firstAppLaunch() {
        view.showTutorial();
    }

    private WalkthroughListLoadingListener listener = new WalkthroughListLoadingListener() {
        @Override
        public void onWalkthroughListLoaded(ArrayList<Walkthrough> allWalkthroughs) {
            walkthroughs = allWalkthroughs;
            setupLandingUi(walkthroughs);

        }
    };

    interface WalkthroughListLoadingListener {
        void onWalkthroughListLoaded(ArrayList<Walkthrough> allWalkthroughs);
    }
}
