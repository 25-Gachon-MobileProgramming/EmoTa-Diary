package kr.co.gachon.emotion_diary.ui.OnBoarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import kr.co.gachon.emotion_diary.R;

public class OnBoardingPageFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_DESC = "desc";
    private static final String ARG_IMAGE_RES = "image_res";

    public static OnBoardingPageFragment newInstance(String title, String desc, int imageRes) {
        OnBoardingPageFragment fragment = new OnBoardingPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DESC, desc);
        args.putInt(ARG_IMAGE_RES, imageRes);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onboarding, container, false);

        TextView title = view.findViewById(R.id.title);
        TextView description = view.findViewById(R.id.description);
        ImageView image = view.findViewById(R.id.image);
        Button startButton = view.findViewById(R.id.startButton);

        if (getArguments() != null) {
            title.setText(getArguments().getString(ARG_TITLE));
            description.setText(getArguments().getString(ARG_DESC));
            image.setImageResource(getArguments().getInt(ARG_IMAGE_RES));

            // 마지막 페이지일 때만 버튼 보이기
            String titleText = getArguments().getString(ARG_TITLE);
            if ("타로".equals(titleText)) {
                startButton.setVisibility(View.VISIBLE);
                startButton.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), AvatarActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                });
            }
        }
        return view;
    }
}