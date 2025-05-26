package kr.co.gachon.emotion_diary.notification;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.time.LocalDate;

import kr.co.gachon.emotion_diary.R;
import kr.co.gachon.emotion_diary.data.DiaryDao;
import kr.co.gachon.emotion_diary.data.AppDatabase;
import kr.co.gachon.emotion_diary.utils.SharedPreferencesUtils;


public class DiaryAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("알람", "onReceive called. Alarm triggered.");
        AppExecutors.getInstance().diskIO().execute(() -> {
            // 오늘 일기 작성 여부 확인
            DiaryDao diaryDao = AppDatabase.getInstance(context).diaryDao();
            String today = LocalDate.now().toString(); // 예: "2025-05-20"
            boolean isWritten = diaryDao.isDiaryWritten(today);

            if (!isWritten) {
                sendNotification(context);

                int hour = SharedPreferencesUtils.getHour(context);
                int minute = SharedPreferencesUtils.getMinute(context);
                AlarmScheduler.scheduleDiaryReminder(context, hour, minute);
            }
        });
    }

    private void sendNotification(Context context) {

        SharedPreferences prefs = context.getSharedPreferences("avatar_pref", Context.MODE_PRIVATE);
        String nickname = prefs.getString("nickname", "사용자"); // 없으면 "사용자"로 대체

        String channelId = "diary_channel";
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Android 8.0 이상 채널 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId, "Diary Reminder", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.d("알람", "알림 권한 없음");

                return;
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(nickname + "님, 오늘의 감정을 남겨보세요")
                .setContentText("아직 일기 작성이 완료되지 않았습니다")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        Log.d("알람", "Notifying...");
        notificationManager.notify(1002, builder.build());
    }
}
