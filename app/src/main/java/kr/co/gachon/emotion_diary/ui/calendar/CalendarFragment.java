package kr.co.gachon.emotion_diary.ui.calendar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import kr.co.gachon.emotion_diary.R;
import kr.co.gachon.emotion_diary.databinding.FragmentCalendarBinding;

public class CalendarFragment extends Fragment {

    private FragmentCalendarBinding binding;
    private TableLayout calendarTable;
    private TextView monthYearText;
    private Button prevMonthButton;
    private Button nextMonthButton;

    private CalendarViewModel calendarViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        calendarViewModel = new ViewModelProvider(this).get(CalendarViewModel.class);

        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        calendarTable = binding.calendar;
        monthYearText = binding.monthYearText;
        prevMonthButton = binding.prevMonthButton;
        nextMonthButton = binding.nextMonthButton;

        monthYearText.setOnClickListener(v -> calendarViewModel.goToCurrentMonth());

        // Set cell size dynamically
        calendarTable.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                calendarTable.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                updateCellSizes();
            }
        });

        observeViewModel();
        setEventListeners();

        return root;
    }

    // Execute when data changed
    private void observeViewModel() {
        calendarViewModel.currentYear.observe(getViewLifecycleOwner(), year -> {
            updateMonthYearText(year, calendarViewModel.currentMonth.getValue());
            recreateCalendar(year, calendarViewModel.currentMonth.getValue());
        });

        calendarViewModel.currentMonth.observe(getViewLifecycleOwner(), month -> {
            updateMonthYearText(calendarViewModel.currentYear.getValue(), month);
            recreateCalendar(calendarViewModel.currentYear.getValue(), month);
        });
    }

    private void setEventListeners() {
        prevMonthButton.setOnClickListener(v -> calendarViewModel.goToPreviousMonth());
        nextMonthButton.setOnClickListener(v -> calendarViewModel.goToNextMonth());
    }

    private void updateMonthYearText(Integer year, Integer month) {
        if (year != null && month != null) {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month - 1, 1); // -1 cause zero-based month
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 M월", Locale.getDefault());
            monthYearText.setText(sdf.format(cal.getTime()));
        }
    }

    private void recreateCalendar(Integer year, Integer month) {
        if (year != null && month != null) {
            int childCount = calendarTable.getChildCount();

            // start: 1 => "day of the week" doesn't need to be removed
            if (childCount > 1) calendarTable.removeViews(1, childCount - 1);

            createCalendar(year, month);
            updateCellSizes();
        }
    }

    private void createCalendar(int year, int month) {
        // Get the first day of the month
        Calendar firstDayCalendar = Calendar.getInstance();
        firstDayCalendar.set(year, month - 1, 1); // -1 cause zero-based month
        int dayOfWeekNumber = firstDayCalendar.get(Calendar.DAY_OF_WEEK); // sun(1) ~ sat(7)
        int daysInMonth = firstDayCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // First time running
        if (calendarTable.getChildCount() == 0) {
            TableRow dayNamesRow = new TableRow(getContext());
            String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

            for (String dayName : dayNames) {
                TextView dayNameTextView = createEmotionTextView(dayName);
                dayNameTextView.setTextSize(Dimension.SP, 16);
                dayNameTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                dayNamesRow.addView(dayNameTextView);
            }

            calendarTable.addView(dayNamesRow);

            TableRow.LayoutParams dayOfTheWeekParams = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            dayOfTheWeekParams.setMargins(0, 0, 0, dpToPx(8));

            for (int j = 0; j < dayNamesRow.getChildCount(); j++) {
                View cell = dayNamesRow.getChildAt(j);
                cell.setLayoutParams(dayOfTheWeekParams);
            }
        }

        TableRow currentRow = new TableRow(getContext());

        // TODO: Instead of empty, fill prev month? -> but with emoji, calendar may change in the future
        // Before the first day of the month, add empty cells
        for (int i = 1; i < dayOfWeekNumber; i++) {
            TextView emptyTextView = createEmotionTextView("");
            currentRow.addView(emptyTextView);
        }

        // Add the days of the month
        int currentDayOfMonth = 1;

        while (currentDayOfMonth <= daysInMonth) {
            TextView dayTextView = createEmotionTextView(String.valueOf(currentDayOfMonth));

            int finalCurrentDayOfMonth = currentDayOfMonth; // For the lambda wtf
            dayTextView.setOnClickListener(v -> Toast.makeText(getContext(), year + "년 " + month + "월 " + finalCurrentDayOfMonth + "일 클릭", Toast.LENGTH_SHORT).show());
            currentRow.addView(dayTextView);

            int currentTableNumber = dayOfWeekNumber + currentDayOfMonth - 1;
            boolean isLastDayOfWeek = currentTableNumber % 7 == 0;


            // Last day of the month
            if (!isLastDayOfWeek && currentDayOfMonth == daysInMonth) {
                // Fill the rest of the row with empty cells
                while (currentRow.getChildCount() < 7) {
                    TextView emptyTextView = createEmotionTextView("");
                    currentRow.addView(emptyTextView);
                }

                calendarTable.addView(currentRow);
            }

            // Last day of the week
            if (isLastDayOfWeek) {
                calendarTable.addView(currentRow);
                currentRow = new TableRow(getContext());
            }

            currentDayOfMonth++;
        }
    }

    private void updateCellSizes() {
        int cellSize = (calendarTable.getWidth() - (calendarTable.getPaddingStart() + calendarTable.getPaddingEnd())) / 7;
        TableRow.LayoutParams params = new TableRow.LayoutParams(cellSize, cellSize);
        TableRow.LayoutParams dayOfTheWeekParams = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        dayOfTheWeekParams.setMargins(0, 0, 0, dpToPx(8));

        TableRow dayOfTheWeekRow = (TableRow) calendarTable.getChildAt(0);
        for (int j = 0; j < dayOfTheWeekRow.getChildCount(); j++) {
            View cell = dayOfTheWeekRow.getChildAt(j);
            cell.setLayoutParams(dayOfTheWeekParams);
        }

        for (int i = 1; i < calendarTable.getChildCount(); i++) {
            TableRow row = (TableRow) calendarTable.getChildAt(i);

            for (int j = 0; j < row.getChildCount(); j++) {
                View cell = row.getChildAt(j);
                cell.setLayoutParams(params);
            }
        }
    }

    private TextView createEmotionTextView(String text) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.calendar_text));
        textView.setGravity(android.view.Gravity.CENTER);

        return textView;
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;

        return Math.round(dp * density);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}