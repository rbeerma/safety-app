package com.plusmobileapps.safetyapp.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.plusmobileapps.safetyapp.FragmentFactory;
import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.actionitems.landing.ActionItemPresenter;
import com.plusmobileapps.safetyapp.main.MainSwipeAdapter;
import com.plusmobileapps.safetyapp.summary.landing.SummaryPresenter;
import com.plusmobileapps.safetyapp.surveys.landing.SurveyLandingPresenter;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View {

    private TextView mTextMessage;

    private ViewPager viewPager;
    private BottomNavigationView navigation;
    private String surveyFragmentTitle = "";
    private MainActivityPresenter presenter;
    private SurveyLandingPresenter surveyLandingPresenter;
    private ActionItemPresenter actionItemPresenter;
    private SummaryPresenter summaryPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewsById();
        setUpPresenters();
        presenter = new MainActivityPresenter(this);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        final MainSwipeAdapter swipeAdapter = new MainSwipeAdapter(getSupportFragmentManager());
        viewPager.setAdapter(swipeAdapter);
        viewPager.addOnPageChangeListener(pageChangeListener);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        setAppBarTitle(getString(R.string.walk_throughs));
    }

    private void setUpPresenters() {
        surveyLandingPresenter = new SurveyLandingPresenter(FragmentFactory.getInstance().getSurveyLandingFragment());
        actionItemPresenter = new ActionItemPresenter(FragmentFactory.getInstance().getActionItemsFragment());
        summaryPresenter = new SummaryPresenter(FragmentFactory.getInstance().getSummaryFragment());
    }

    private void findViewsById() {
        mTextMessage = findViewById(R.id.message);
        navigation = findViewById(R.id.navigation);
        viewPager = findViewById(R.id.view_pager);
    }

    @Override
    public void changePage(int position) {
        viewPager.setCurrentItem(position, true);
    }

    @Override
    public void changeNavHighlight(int position) {
        navigation.setSelectedItemId(position);
    }

    private void setAppBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        findViewById(R.id.floatingActionButton).setVisibility(View.VISIBLE);
    }

    /**
     * Handle clicks of the bottom navigation
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_survey:
                    presenter.navButtonClicked(0);
                    return true;
                case R.id.navigation_dashboard:
                    presenter.navButtonClicked(1);
                    return true;
                case R.id.navigation_history:
                    presenter.navButtonClicked(2);
                    return true;
            }
            return false;
        }

    };


    /**
     * Handle ViewPager page change events
     */
    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            presenter.pageSwipedTo(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}