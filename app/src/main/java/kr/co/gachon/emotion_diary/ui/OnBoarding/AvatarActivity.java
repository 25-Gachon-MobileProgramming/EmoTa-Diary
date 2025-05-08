package kr.co.gachon.emotion_diary.ui.OnBoarding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

import kr.co.gachon.emotion_diary.MainActivity;
import kr.co.gachon.emotion_diary.R;

public class AvatarActivity extends AppCompatActivity {
    Button button;
    EditText editText;
    private Context context;
    RadioGroup radGender;
    RadioButton genderM;
    RadioButton genderF;
    NumberPicker yearPicker;
    NumberPicker monthPicker;
    NumberPicker dayPicker;
    int selectedYear;
    int selectedMonth;
    int selectedDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_avatar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        context = getApplicationContext();
        button = findViewById(R.id.button);
        editText = findViewById(R.id.nickname);
        radGender = findViewById(R.id.radGender);
        genderM = findViewById(R.id.genderM);
        genderF = findViewById(R.id.genderF);
        yearPicker = findViewById(R.id.yearPicker);
        monthPicker = findViewById(R.id.monthPicker);
        dayPicker = findViewById(R.id.dayPicker);

        // NumberPicker 설정 (범위 제한)
        yearPicker.setMinValue(1950);
        yearPicker.setMaxValue(2020);
        yearPicker.setValue(2000); // 초기 값 설정 (선택 사항)
        yearPicker.setWrapSelectorWheel(true); // 스크롤 순환 활성화
        yearPicker.setOnValueChangedListener((picker, oldVal, newVal) -> selectedYear = newVal);
        selectedYear = yearPicker.getValue();

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(1); // 초기 값 설정 (선택 사항)
        monthPicker.setWrapSelectorWheel(true); // 스크롤 순환 활성화
        monthPicker.setOnValueChangedListener((picker, oldVal, newVal) -> selectedMonth = newVal);
        selectedMonth = monthPicker.getValue();

        updateDaysInMonth(selectedYear, selectedMonth);
        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(31); // 최대 31로 설정, updateDaysInMonth에서 실제 최대 일수로 조정
        dayPicker.setValue(1); // 초기 값 설정 (선택 사항)
        dayPicker.setWrapSelectorWheel(true); // 스크롤 순환 활성화
        dayPicker.setOnValueChangedListener((picker, oldVal, newVal) -> selectedDay = newVal);
        selectedDay = dayPicker.getValue();

        // 월이 변경될 때마다 일(day) NumberPicker 업데이트
        monthPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            selectedMonth = newVal;
            updateDaysInMonth(selectedYear, selectedMonth);
            // 변경된 월에 맞춰 일 NumberPicker의 값을 재설정 (안전하게 범위 내의 값으로)
            int maxDay = getDaysInMonth(selectedYear, selectedMonth);
            if (dayPicker.getValue() > maxDay) {
                dayPicker.setValue(maxDay);
                selectedDay = maxDay;
            }
        });

        // 년도가 변경될 때마다 일(day) NumberPicker 업데이트
        yearPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            selectedYear = newVal;
            updateDaysInMonth(selectedYear, selectedMonth);
            // 변경된 년도에 맞춰 일 NumberPicker의 값을 재설정 (안전하게 범위 내의 값으로)
            int maxDay = getDaysInMonth(selectedYear, selectedMonth);
            if (dayPicker.getValue() > maxDay) {
                dayPicker.setValue(maxDay);
                selectedDay = maxDay;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = editText.getText().toString().trim();
                int genderid = radGender.getCheckedRadioButtonId();
                String gender = "";
                boolean genderSelected = false;

                if (TextUtils.isEmpty(input)) {
                    Toast.makeText(context, "Please enter your nickname", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (genderM.getId() == genderid) {
                    gender = "Male";
                    genderSelected = true;
                } else if (genderF.getId() == genderid) {
                    gender = "Female";
                    genderSelected = true;
                } else {
                    Toast.makeText(context, "Please select your gender", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 생년월일이 모두 선택되었는지 확인 (초기값으로 설정한 1도 선택된 것으로 간주하므로,
                // 실제 사용자가 스크롤했는지 여부를 추가적으로 확인하는 것이 더 좋음.)
                // 간단하게 초기값과 다르면 선택된 것으로 간주
                // 수정필요 !!!
                if (selectedYear == 2000 && selectedMonth == 1 && selectedDay == 1) {
                    Toast.makeText(context, "Please select your birth date", Toast.LENGTH_SHORT).show();
                    return;
                }

                String birthDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth, selectedDay);

                // 데이터가 잘 입력되었는지 확인하는 용도
                Toast.makeText(context, "Welcome, " + input + "! Birth Date: " + birthDate + ", Gender: " + gender, Toast.LENGTH_SHORT).show();

                // Avatar 중복 설정 방지
                SharedPreferences prefs = getSharedPreferences("avatar_pref", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isAvatarCompleted", true);  // Avatar 설정 완료 여부
                editor.putString("nickname", input);
                editor.putString("gender", gender);
                editor.putString("birthDate", birthDate);
                editor.apply();  // 반드시 apply() 또는 commit() 호출

                Intent intent = new Intent(AvatarActivity.this, MainActivity.class);
                intent.putExtra("nickname", input); // 닉네임 데이터 전달
                intent.putExtra("gender", gender); // 성별 데이터 전달
                intent.putExtra("birthDate", birthDate); // 생년월일 데이터 전달

                startActivity(intent);
            }
        });
    }

    // 해당 년도와 월의 마지막 날짜를 계산하는 함수
    private int getDaysInMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1); // Calendar는 월을 0부터 시작하므로 month - 1
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    // 일(day) NumberPicker의 범위를 업데이트하는 함수
    private void updateDaysInMonth(int year, int month) {
        int daysInMonth = getDaysInMonth(year, month);
        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(daysInMonth);
    }
}