package kr.co.gachon.emotion_diary.ui.calendar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Calendar;

public class CalendarViewModel extends ViewModel {
    private final MutableLiveData<Integer> _currentYear = new MutableLiveData<>();
    private final MutableLiveData<Integer> _currentMonth = new MutableLiveData<>();

    public final LiveData<Integer> currentYear = _currentYear;
    public final LiveData<Integer> currentMonth = _currentMonth;

    public CalendarViewModel() {
        Calendar calendar = Calendar.getInstance();

        _currentYear.setValue(calendar.get(Calendar.YEAR));
        _currentMonth.setValue(calendar.get(Calendar.MONTH) + 1); // It's 0-based
    }

    public void goToNextMonth() {
        Integer year = _currentYear.getValue();
        Integer month = _currentMonth.getValue();

        if (year != null && month != null) {
            if (month == 12) {
                // In the last month of the year
                _currentYear.setValue(year + 1);
                _currentMonth.setValue(1);
            } else {
                _currentMonth.setValue(month + 1);
            }
        }
    }

    public void goToPreviousMonth() {
        Integer year = _currentYear.getValue();
        Integer month = _currentMonth.getValue();

        if (year != null && month != null) {
            if (month == 1) {
                // In the first month of the year
                _currentYear.setValue(year - 1);
                _currentMonth.setValue(12);
            } else {
                _currentMonth.setValue(month - 1);
            }
        }
    }

    public void goToMonth(int year, int month) {
        _currentYear.setValue(year);
        _currentMonth.setValue(month);
    }

    public void goToCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;

        goToMonth(currentYear, currentMonth);
    }
}