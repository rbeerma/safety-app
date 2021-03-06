package com.plusmobileapps.safetyapp.actionitems.detail;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.data.entity.Response;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by rbeerma
 * This activity displays the details of a specific action item.
 */
public class ActionItemDetailActivity extends AppCompatActivity
        implements EditPriorityDialogFragment.PriorityDialogListener,
                    ActionItemDetailContract.View {

    public static final String EXTRA_ACTION_ITEM_ID = "ACTION_ITEM_ID";

    private ActionItemDetailPresenter presenter;

    private Button editPriorityBtn;
    private View statusDot;
    private Button saveButton;
    private EditText actionPlanText;
    private TextView detailRatingTextView;
    private TextView locationTextView;
    private TextView detailTimeStampTextView;
    private TextView titleTextView;
    private ImageView imageView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_item_detail);

        presenter = new ActionItemDetailPresenter(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.action_item_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_action_item_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        statusDot = findViewById(R.id.action_item_status);
        editPriorityBtn = findViewById(R.id.edit_priority_btn);
        editPriorityBtn.setOnClickListener(editPriorityClickListener);
        saveButton = findViewById(R.id.save_action_item_detail);
        actionPlanText = findViewById(R.id.editTextActionPlan);
        saveButton.setOnClickListener(saveListener);
        detailRatingTextView = findViewById(R.id.detail_rating);
        locationTextView = findViewById(R.id.locationTextView);
        detailTimeStampTextView = findViewById(R.id.detail_timestamp);
        titleTextView = findViewById(R.id.actionItemDetailTitle);
        imageView = findViewById(R.id.imageView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String id = bundle.getString(EXTRA_ACTION_ITEM_ID);
            presenter.start(id);
        } else {
            presenter.startError();
        }
    }

    @Override
    public void showConfirmationExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(getString(R.string.exit_confirmation_no_save))
                .setTitle(getString(R.string.confirmation))
                .setPositiveButton(getString(R.string.yes), confirmationListener)
                .setNegativeButton(getString(R.string.cancel), confirmationListener);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                presenter.backButtonClicked();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(DialogFragment dialog, CharSequence selectedItem) {
        EditPriorityDialogFragment priorityDialog = (EditPriorityDialogFragment) dialog;
        String selectedPriority = priorityDialog.getSelectedItem().toString();
        presenter.editPriorityPicked(selectedPriority);
    }

    @Override
    public void showActionItem(Response response) {
        actionPlanText.setText(response.getActionPlan());
        detailRatingTextView.setText("Rating: " + response.getRatingText());
        locationTextView.setText(response.getLocationName());
        titleTextView.setText(response.getTitle());

        if(response.getImagePath() != null) {
            File file = new File(response.getImagePath());
            Picasso.get().load(file).into(imageView);
        }

        String priority = Integer.toString(response.getPriority());
        int drawable = presenter.getStatusColorDrawable(priority);
        changeStatusDot(drawable);

        String timeStamp = response.getTimeStamp();
        if (timeStamp != null) {
            detailTimeStampTextView.setText("Date: " + timeStamp);
        } else {
            detailTimeStampTextView.setText("");
        }

    }

    @Override
    public void showPriorityDialog() {
        DialogFragment editPriorityDialog = new EditPriorityDialogFragment();
        editPriorityDialog.show(getSupportFragmentManager(), "EditPriorityDialogFragment");
    }

    @Override
    public void changeStatusDot(int drawableId) {
        statusDot.setBackgroundResource(drawableId);
    }

    @Override
    public void onBackPressed() {
        presenter.backButtonClicked();
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public String getActionItemPlan() {
        return actionPlanText.getText().toString();
    }

    private View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            presenter.saveButtonClicked();
        }
    };

    private View.OnClickListener editPriorityClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            presenter.editPriorityButtonClicked();
        }
    };

    private DialogInterface.OnClickListener confirmationListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    finishActivity();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };
}
