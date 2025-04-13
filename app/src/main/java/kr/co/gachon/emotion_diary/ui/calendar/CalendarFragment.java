package kr.co.gachon.emotion_diary.ui.calendar;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;

import kr.co.gachon.emotion_diary.R;
import kr.co.gachon.emotion_diary.databinding.FragmentCalendarBinding;

public class CalendarFragment extends Fragment {

    private FragmentCalendarBinding binding;
    private TableLayout calendarTable;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CalendarViewModel calendarViewModel = new ViewModelProvider(this).get(CalendarViewModel.class);

        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        calendarTable = binding.calendar;

        // Set cell size dynamically
        calendarTable.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                calendarTable.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                updateCellSizes();
            }
        });

        createCalendar();

        return root;
    }


    private void createCalendar() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // cause Zero-Based Indexing

        // Get the first day of the month
        Calendar firstDayCalendar = Calendar.getInstance();
        firstDayCalendar.set(year, month - 1, 1);
        int dayOfWeekNumber = firstDayCalendar.get(Calendar.DAY_OF_WEEK); // sun(1) ~ sat(7)
        int daysInMonth = firstDayCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Add the row for the day names
        TableRow dayNamesRow = new TableRow(getContext());
        String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String dayName : dayNames) {
            TextView dayNameTextView = createEmotionTextView(dayName);
            dayNameTextView.setTextSize(Dimension.SP, 24);
            dayNamesRow.addView(dayNameTextView);
        }
        calendarTable.addView(dayNamesRow);

        TableRow currentRow = new TableRow(getContext());

        // Add empty cells for the days before the first day of the month
        for (int i = 1; i < dayOfWeekNumber; i++) {
            TextView emptyTextView = createEmotionTextView("");
            currentRow.addView(emptyTextView);
        }

        // Add the days of the month
        int currentDayOfMonth = 1;

        while (currentDayOfMonth <= daysInMonth) {
            TextView dayTextView = createEmotionTextView(String.valueOf(currentDayOfMonth));

            // for remove warning: variable used in lambda expression should be final
            int finalCurrentDayOfMonth = currentDayOfMonth;

            // TODO: Add click listener to show something...
            dayTextView.setOnClickListener(v -> Toast.makeText(getContext(), "Clicked on " + finalCurrentDayOfMonth, Toast.LENGTH_SHORT).show());
            currentRow.addView(dayTextView);

            int currentTableNumber = dayOfWeekNumber + currentDayOfMonth - 1;
            boolean isLastDayOfWeek = currentTableNumber % 7 == 0;

            if (isLastDayOfWeek) {
                calendarTable.addView(currentRow);
                currentRow = new TableRow(getContext());
            }

            // fill the rest of the row with empty cells
            if (!isLastDayOfWeek && currentDayOfMonth == daysInMonth) {
                if (currentRow.getChildCount() > 0) {
                    while (currentRow.getChildCount() < 7) {
                        TextView emptyTextView = createEmotionTextView("");
                        currentRow.addView(emptyTextView);
                    }

                    calendarTable.addView(currentRow);
                }

                break;
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