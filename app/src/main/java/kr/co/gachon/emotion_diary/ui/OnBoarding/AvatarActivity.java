package kr.co.gachon.emotion_diary.ui.OnBoarding;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
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
    private ImageView profileImage;
    private Uri selectedImageUri;
    int selectedYear;
    int selectedMonth;
    int selectedDay;

    // 이미지 선택런처
    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        profileImage.setImageURI(selectedImageUri);
                    }
                }
            }
    );

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
        profileImage = findViewById(R.id.profileImage);
        profileImage.setOnClickListener(v -> checkAndRequestImagePermission());

        // NumberPicker 설정 (범위 제한)
        yearPicker.setMinValue(1950);
        yearPicker.setMaxValue(2020);
        yearPicker.setValue(2000); // 초기 값 설정
        yearPicker.setWrapSelectorWheel(true); // 스크롤 순환 활성화
        yearPicker.setOnValueChangedListener((picker, oldVal, newVal) -> selectedYear = newVal);
        selectedYear = yearPicker.getValue();

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(1); // 초기 값 설정
        monthPicker.setWrapSelectorWheel(true); // 스크롤 순환 활성화
        monthPicker.setOnValueChangedListener((picker, oldVal, newVal) -> selectedMonth = newVal);
        selectedMonth = monthPicker.getValue();

        updateDaysInMonth(selectedYear, selectedMonth);
        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(31); // 최대 31로 설정, updateDaysInMonth에서 실제 최대 일수로 조정
        dayPicker.setValue(1); // 초기 값 설정
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

        button.setOnClickListener(view -> {
            final String input = editText.getText().toString().trim();
            final int genderid = radGender.getCheckedRadioButtonId();

            final String gender;
            if (TextUtils.isEmpty(input)) {
                Toast.makeText(context, "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            if (genderM.getId() == genderid) {
                gender = "남";
            } else if (genderF.getId() == genderid) {
                gender = "여";
            } else {
                Toast.makeText(context, "성별을 선택해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            final String birthDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth, selectedDay);

            AlertDialog dialogBuilder = new AlertDialog.Builder(AvatarActivity.this)
                    .setTitle("입력 확인")
                    .setMessage("닉네임: " + input + "\n성별: " + gender + "\n생년월일: " + birthDate + "\n\n입력하신 내용이 맞습니까?")
                    .setPositiveButton("확인", (dialog, which) -> {
                        SharedPreferences prefs = getSharedPreferences("avatar_pref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("isAvatarCompleted", true);
                        editor.putString("nickname", input);
                        editor.putString("gender", gender);
                        editor.putString("birthDate", birthDate);
                        if (selectedImageUri != null) {
                            String savedPath = copyUriToInternalStorage(selectedImageUri);
                            if (savedPath != null) {
                                editor.putString("profileImage", savedPath);
                            }
                        }
                        editor.apply();

                        Intent intent = new Intent(AvatarActivity.this, MainActivity.class);
                        intent.putExtra("nickname", input);
                        intent.putExtra("gender", gender);
                        intent.putExtra("birthDate", birthDate);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("취소", null)
                    .show();

            dialogBuilder.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(ContextCompat.getColor(this, R.color.colorSecondary));
            dialogBuilder.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat.getColor(this, R.color.colorSecondary));
        });
    }

    // 해당 년도와 월의 마지막 날짜 계산
    private int getDaysInMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    // 일(day) NumberPicker 범위를 업데이트
    private void updateDaysInMonth(int year, int month) {
        int daysInMonth = getDaysInMonth(year, month);
        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(daysInMonth);
    }

    // 갤러리 권한 체크
    private void checkAndRequestImagePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES)
                    != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, 100);
            } else {
                launchGallery();
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
            } else {
                launchGallery();
            }
        }
    }

    // 권한 응답 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if ((requestCode == 100 || requestCode == 101) &&
                grantResults.length > 0 && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            launchGallery();
        } else {
            Toast.makeText(this, "갤러리 접근 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
        }
    }

    // 갤러리 열기
    private void launchGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    // 내부 저장소로 복사
    private String copyUriToInternalStorage(Uri sourceUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(sourceUri);
            File file = new File(getFilesDir(), "profile.jpg");
            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();

            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}