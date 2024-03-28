package ca.event.solosphere.ui.fragment.organizer;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.skydoves.powerspinner.IconSpinnerAdapter;
import com.skydoves.powerspinner.IconSpinnerItem;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import ca.event.solosphere.R;
import ca.event.solosphere.core.constants.Constants;
import ca.event.solosphere.core.constants.RegexTemplate;
import ca.event.solosphere.core.model.Category;
import ca.event.solosphere.core.model.Event;
import ca.event.solosphere.databinding.FragmentOrgAddEventBinding;
import ca.event.solosphere.ui.fragment.BaseFragment;
import ca.event.solosphere.ui.utils.AppUtils;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class OrgAddEventFragment extends BaseFragment implements View.OnClickListener {


    private static final String TAG = "OrgAddEventFragment";
    private FragmentOrgAddEventBinding binding;
    private Context context;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mEventRef;
    private DatabaseReference mCategoryRef;
    private StorageReference storageReference;
    private Calendar calendarStart;
    private Calendar calendarEnd;
    private Calendar minDateCalendar;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private Uri filePath;
    private static ActivityResultLauncher<Intent> launcher;
    private ArrayList<IconSpinnerItem> categoryArrayList = new ArrayList<>();
    private String selectedCategory = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarTitle(baseActivity.getResources().getString(R.string.title_add_event));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrgAddEventBinding.inflate(inflater, container, false);

        context = getActivity();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    private void init() {
        calendarStart = Calendar.getInstance();
        calendarEnd = Calendar.getInstance();
        minDateCalendar = Calendar.getInstance();
        minDateCalendar.add(Calendar.DAY_OF_MONTH, 1);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mEventRef = mFirebaseInstance.getReference(Constants.TBL_EVENTS);
        mCategoryRef = mFirebaseInstance.getReference(Constants.TBL_CATEGORIES);
        storageReference = FirebaseStorage.getInstance().getReference();

        doCategoryList(true);

        binding.inputStartDate.setOnClickListener(this);
        binding.inputStartTime.setOnClickListener(this);
        binding.inputEndDate.setOnClickListener(this);
        binding.inputEndTime.setOnClickListener(this);
        binding.btnAdd.setOnClickListener(this);
        binding.btnSelectEventImage.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        launcher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (ActivityResult result) -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Uri uri = result.getData().getData();
                        binding.ivEventImage.setImageURI(uri);
                        filePath = uri;
                    } else if (result.getResultCode() == ImagePicker.RESULT_ERROR) {
                        Log.e(TAG, "onAttach: " + ImagePicker.Companion.getError(result.getData()));
                    }
                });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.inputStartDate:
                showDatePicker(calendarStart, true);
                break;

            case R.id.inputStartTime:
                showTimePicker(calendarEnd, true);
                break;
            case R.id.inputEndDate:
                showDatePicker(calendarStart, false);
                break;
            case R.id.inputEndTime:
                showTimePicker(calendarEnd, false);
                break;

            case R.id.btnSelectEventImage:
                openImagePicker();
                break;

            case R.id.btnAdd:
                if (validateData()) {
                    uploadData();
                }
                break;
        }

    }

    private void doCategoryList(boolean isShowProgress) {
        if (isShowProgress) {
            showProgress(binding.loadingView.getRoot());
        }

        mCategoryRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                categoryArrayList.clear();
                hideProgress(binding.loadingView.getRoot());

                categoryArrayList.add(new IconSpinnerItem("Please select category"));
                selectedCategory = "Please select category";

                if (dataSnapshot != null && dataSnapshot.exists()) {
                    for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                        Category category = categorySnapshot.getValue(Category.class);

                        if (category != null) {
                            categoryArrayList.add(new IconSpinnerItem(category.getName()));
                        }
                    }

                    IconSpinnerAdapter iconSpinnerAdapter = new IconSpinnerAdapter(binding.spinnerCategory);
                    binding.spinnerCategory.setSpinnerAdapter(iconSpinnerAdapter);
                    binding.spinnerCategory.setItems(categoryArrayList);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
                    binding.spinnerCategory.getSpinnerRecyclerView().setLayoutManager(gridLayoutManager);
                    binding.spinnerCategory.selectItemByIndex(0);
                    binding.spinnerCategory.setLifecycleOwner(OrgAddEventFragment.this);

                    binding.spinnerCategory.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<IconSpinnerItem>() {
                        @Override
                        public void onItemSelected(int i, @Nullable IconSpinnerItem iconSpinnerItem, int i1, IconSpinnerItem t1) {
                            selectedCategory = t1.getText().toString();
                        }

                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgress(binding.loadingView.getRoot());
                Log.e(TAG, "onCancelled: " + databaseError.getMessage().toString());
            }
        });


    }


    private void uploadData() {
        showProgress(binding.loadingView.getRoot());
        if (filePath != null) {
            String filename = UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("events/" + filename);
            ref.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        ref.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            addEvent(imageUrl,
                                    binding.inputEventName.getText().toString().trim(),
                                    binding.inputEventDesc.getText().toString().trim(),
                                    Double.parseDouble(binding.inputTicketPrice.getText().toString().trim()),
                                    Integer.parseInt(binding.inputTotalSpots.getText().toString().trim()),
                                    selectedCategory,
                                    binding.inputEventAddress.getText().toString().trim(),
                                    binding.inputEventCity.getText().toString().trim(),
                                    binding.inputEventState.getText().toString().trim(),
                                    startDate, startTime, endDate, endTime);
                        });
                    })
                    .addOnFailureListener(e -> {
                        hideProgress(binding.loadingView.getRoot());
                        Snackbar.make(binding.btnSelectEventImage, baseActivity.getResources().getString(R.string.profile_not_updated), Snackbar.LENGTH_LONG).show();
                    });
        } else {
            hideProgress(binding.loadingView.getRoot());
            Snackbar.make(context, binding.btnSelectEventImage, baseActivity.getResources().getString(R.string.err_event_image), Snackbar.LENGTH_LONG).show();
        }
    }

    private void addEvent(String eventImage, String name, String desc, double price, int totalSpots, String category, String location, String city, String state, String startDate, String startTime, String endDate, String endTime) {
        String orgID = mAuth.getCurrentUser().getUid();
        String eventID = "event_org_" + System.currentTimeMillis();
        Event event = new Event(orgID, eventID, eventImage, name, desc, price, totalSpots, category, location, city, state, startDate, startTime, endDate, endTime,0);

        mEventRef.child(eventID).setValue(event)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideProgress(binding.loadingView.getRoot());
                        Snackbar.make(context, binding.btnSelectEventImage, baseActivity.getResources().getString(R.string.event_added), Snackbar.LENGTH_LONG).show();
                        baseActivity.finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add event data to Firebase
                        hideProgress(binding.loadingView.getRoot());
                        Snackbar.make(context, binding.btnSelectEventImage, baseActivity.getResources().getString(R.string.err_event_not_added) + e.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    private void openImagePicker() {
        ImagePicker.Companion.with(baseActivity)
                .crop()
                .maxResultSize(512, 512, true)
                .provider(ImageProvider.BOTH)
                .createIntentFromDialog(new Function1() {
                    public Object invoke(Object var1) {
                        this.invoke((Intent) var1);
                        return Unit.INSTANCE;
                    }

                    public void invoke(@NotNull Intent it) {
                        Intrinsics.checkNotNullParameter(it, "it");
                        launcher.launch(it);
                    }
                });
    }


    private void showDatePicker(Calendar calendar, boolean isStart) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.CustomDatePickerTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Update calendar with selected date
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String selectedDate = DateFormat.getDateInstance().format(calendar.getTime());
                        if (isStart) {
                            startDate = selectedDate;
                            binding.inputStartDate.setText(startDate);
                        } else {
                            endDate = selectedDate;
                            binding.inputEndDate.setText(endDate);
                        }
                    }
                }, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(minDateCalendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void showTimePicker(Calendar calendar, boolean isStart) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, R.style.CustomDatePickerTheme,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
                        String selectedTime = timeFormat.format(calendar.getTime());
                        if (isStart) {
                            startTime = selectedTime;
                            binding.inputStartTime.setText(startTime);
                        } else {
                            endTime = selectedTime;
                            binding.inputEndTime.setText(endTime);
                        }
                    }
                }, hour, minute, true);

        timePickerDialog.show();
    }


    private boolean validateData() {
        if (!AppUtils.validateEditTextIp(binding.inputEventName, RegexTemplate.NOT_EMPTY, getString(R.string.err_name_length))) {
            return false;
        } else if (!AppUtils.validateMultiLineEditTextIp(binding.inputEventDesc, RegexTemplate.NAME_PATTERN, getString(R.string.err_name_length))) {
            return false;
        } else if (!AppUtils.validateEditTextIp(binding.inputTicketPrice, RegexTemplate.NOT_EMPTY, getString(R.string.err_event_price))) {
            return false;
        } else if (!AppUtils.validateEditTextIp(binding.inputTicketPrice, RegexTemplate.VALID_PRICE, getString(R.string.err_valid_event_price))) {
            return false;
        } else if (!AppUtils.validateEditTextIp(binding.inputTotalSpots, RegexTemplate.NOT_EMPTY, getString(R.string.err_total_spots))) {
            return false;
        } else if (!AppUtils.validateEditTextIp(binding.inputTotalSpots, RegexTemplate.ONLY_DIGITS, getString(R.string.err_valid_total_spots))) {
            return false;
        } else if (selectedCategory == null || selectedCategory.equals("Please select category")) {
            Snackbar.make(context, binding.btnAdd, baseActivity.getResources().getString(R.string.err_select_category), Snackbar.LENGTH_LONG).show();
            return false;
        } else if (!AppUtils.validateEditTextIp(binding.inputEventAddress, RegexTemplate.NAME_PATTERN, getString(R.string.err_name_length))) {
            return false;
        } else if (!AppUtils.validateEditTextIp(binding.inputEventCity, RegexTemplate.NAME_PATTERN, getString(R.string.err_name_length))) {
            return false;
        } else if (!AppUtils.validateEditTextIp(binding.inputEventState, RegexTemplate.NAME_PATTERN, getString(R.string.err_name_length))) {
            return false;
        } else if (startDate == null || startDate.isEmpty()) {
            Snackbar.make(context, binding.btnAdd, baseActivity.getResources().getString(R.string.err_start_date), Snackbar.LENGTH_LONG).show();
            return false;
        } else if (startTime == null || startTime.isEmpty()) {
            Snackbar.make(context, binding.btnAdd, baseActivity.getResources().getString(R.string.err_start_time), Snackbar.LENGTH_LONG).show();
            return false;
        } else if (endDate == null || endDate.isEmpty()) {
            Snackbar.make(context, binding.btnAdd, baseActivity.getResources().getString(R.string.err_end_date), Snackbar.LENGTH_LONG).show();
            return false;
        } else if (endTime == null || endTime.isEmpty()) {
            Snackbar.make(context, binding.btnAdd, baseActivity.getResources().getString(R.string.err_end_time), Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


}