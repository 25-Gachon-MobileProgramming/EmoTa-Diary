package kr.co.gachon.emotion_diary.ui.Remind.timeGraph;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.List;

public class TimeGraph extends View {
    private Paint paint;
    private Path path;
    private List<Point> timePoints;
    private float animatedProgress = 0f;
    private ValueAnimator animator;

    private int viewWidth, viewHeight;

    public TimeGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8f);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(Color.CYAN);  // 선 색상
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
    }

    public void setTimePoints(List<Point> points) {
        this.timePoints = points;
        buildPath();

        animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(2000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(a -> {
            animatedProgress = (float) a.getAnimatedValue();
            invalidate();
        });
        animator.start();
    }

    private void buildPath() {
        path = new Path();
        if (timePoints == null || timePoints.size() < 2) return;

        float maxX = 1440f;
        float usableWidth = viewWidth;
        float usableHeight = viewHeight;

        float maxY = 1f;
        for (Point p : timePoints) {
            if (p.y > maxY) maxY = p.y;
        }
        if (maxY < 5f) maxY = 5f;

        Point first = timePoints.get(0);
        float prevX = (first.x / maxX) * usableWidth;
        float prevY = usableHeight - (first.y / maxY) * usableHeight;
        path.moveTo(prevX, prevY);

        for (int i = 1; i < timePoints.size(); i++) {
            Point curr = timePoints.get(i);
            float x = (curr.x / maxX) * usableWidth;
            float y = usableHeight - (curr.y / maxY) * usableHeight;

            float midX = (prevX + x) / 2;
            float midY = (prevY + y) / 2;

            path.quadTo(prevX, prevY, midX, midY);  // 곡선으로 중간까지 연결
            prevX = x;
            prevY = y;
        }

        // 마지막 점까지 직선으로 연결
        path.lineTo(prevX, prevY);
    }




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (path == null) return;

        Path animPath = new Path();
        PathMeasure pm = new PathMeasure(path, false);
        float stop = pm.getLength() * animatedProgress;
        pm.getSegment(0, stop, animPath, true);

        canvas.drawPath(animPath, paint);

        // X축 라벨용 Paint 설정
        Paint labelPaint = new Paint();
        labelPaint.setColor(Color.LTGRAY);
        labelPaint.setTextSize(26f);
        labelPaint.setAntiAlias(true);
        labelPaint.setTextAlign(Paint.Align.CENTER);

        //X축 라벨 그리기 (예: 00:00 ~ 24:00까지 6시간 간격)
        float maxX = 1440f; // 24시간
        int[] hourMarks = {0, 360, 720, 1080, 1439}; // 분 단위: 00:00, 06:00, 12:00, 18:00, 23:59

        for (int mark : hourMarks) {
            float x = (mark / maxX) * viewWidth;
            String label = String.format("%02d:%02d", mark / 60, mark % 60);

            //  짤림 방지: 양끝 보정
            if (mark == 0) {
                labelPaint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(label, x + 20f, viewHeight - 30f, labelPaint);
            } else if (mark == 1439) {
                labelPaint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(label, x - 20f, viewHeight - 30f, labelPaint);
            } else {
                labelPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(label, x, viewHeight - 30f, labelPaint);
            }
        }
    }
}
