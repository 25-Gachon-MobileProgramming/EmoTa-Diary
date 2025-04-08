package kr.co.gachon.emotion_diary.write;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Locale;

import kr.co.gachon.emotion_diary.databinding.FragmentDiaryWriteBinding;

public class DiaryWriteFirstFragment extends Fragment {

    // 이쪽은 biding을 위한 코드를 구성
    private FragmentDiaryWriteBinding binding;
    private DiaryWriteFirstViewModel viewModel;


    // findView 방식으로 넘길려고 했는데 왠지 모르게 오류가 발생해서 binding을 사용했습니다.
    // onCreate View는 XML 을 fragment에 붙이는 역할
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        // ViewBinding 연결 코드를 사영
        binding = FragmentDiaryWriteBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(DiaryWriteFirstViewModel.class);
        // 날짜 출력 테스트
        viewModel.getSelectedDate().observe(getViewLifecycleOwner(), date -> {
            if (date != null) {
                binding.dateTextView.setText(date.toString()); //휘발성 저장처럼하고 emtion버튼이 활성화 될때
                                                                // 이 데이터를 room에 저장할? Converters에 있는
                                                                // data변환하고 저장할 예정
            }
        });

        return binding.getRoot();
    }

    //onViewCreate는 text에 날짜 표시하게 만듬
    @Override
    public void onViewCreated( View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }



    // 이 코드 작동이 view가 만약 파괴가 될 때? 메모리 누수를 막아주는 코드
    // 빼셔도 됩니다. 그냥 있어서 가져와봤는데..
    // https://developer.android.com/topic/libraries/view-binding?hl=ko#java
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
