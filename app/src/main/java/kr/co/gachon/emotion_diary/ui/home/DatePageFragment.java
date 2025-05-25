package kr.co.gachon.emotion_diary.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import kr.co.gachon.emotion_diary.R;

public class DatePageFragment extends Fragment {

    private static final String ARG_DATE = "arg_date";

    // 수정: LocalDate → String
    public static DatePageFragment newInstance(String dateText) {
        DatePageFragment fragment = new DatePageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DATE, dateText); // 문자열로 저장
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_date_page, container, false);
        TextView dateTextView = view.findViewById(R.id.dateTextView);

        if (getArguments() != null) {
            String dateStr = getArguments().getString(ARG_DATE);
            dateTextView.setText(dateStr);
        }

        return view;
    }
}
