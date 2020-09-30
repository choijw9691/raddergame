// 사다리 게임 화면 Activity
package com.example.laddergame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.jiwoung.laddergame.R;

import java.util.ArrayList;

public class LadderGameActivity extends AppCompatActivity {
    int mPeopleMax = 2;
private AdView mAdView;
    ArrayList<PeopleInfo> mArPeople = null;         // 사람 정보 배열
    ArrayList<String> mArPresent = null;        // 벌칙 정보 배열
    ArrayList<Point> mArHBar = null;            // 수평 사다리 정보 배열
    MainView mainView;
    int mCanvasW = 100;         // 캔버스 넓이
    int mCanvasH = 100;         // 캔버스 높이
    int mMoveUnitH = 10;        // 수평 이동 단위
    int mMoveUnitV = 10;        // 수직 이동 단위
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ladder_game);

        MobileAds.initialize(this, "ca-app-pub-4692999327577755~7322161136");

mAdView=findViewById(R.id.adView);
        AdRequest adRequest=new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        // Intent 에서 데이터를 읽는다
        readIntent(getIntent());

        mainView = new MainView(this);
        RelativeLayout layoutCanvas = (RelativeLayout) findViewById(R.id.layoutCanvas);
        layoutCanvas.addView(mainView);


    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Back button pressed.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }


    // Intent 에서 데이터를 읽는다
    public void readIntent(Intent intent) {

        mArPeople = new ArrayList<PeopleInfo>();
        mArPresent = new ArrayList<String>();

        // 최대 인원수
        mPeopleMax = intent.getIntExtra("PeopleMax", 2);

        // '이름' 목록 추출
        for (int i = 0; i < mPeopleMax; i++) {
            String strName = intent.getStringExtra("Name" + i);
            PeopleInfo pi = new PeopleInfo(strName, i);
            mArPeople.add(pi);

        }

        // '벌칙' 목록 추출
        for (int i = 0; i < mPeopleMax; i++) {
            String strPresent = intent.getStringExtra("Present" + i);
            mArPresent.add(strPresent);
        }


    }

    // 버튼 클릭 이벤트 함수
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGameStart:
                // 게임 시작
                startLadder();
                break;
        }
    }

    // 캔버스용 뷰 클래스
    protected class MainView extends View {
        public MainView(Context context) {
            super(context);
        }

        public void onDraw(Canvas canvas) {
            // 게임이 생성되지 않았다면 게임 생성
            if (mArHBar == null) {
                createGame(canvas);
            }
          //  canvas.drawColor(Color.LTGRAY);

            // 배경 그리기
            drawBack(canvas);

            // 수평바 그리기
            drawHbar(canvas);

            // 벌칙 그리기
            drawPresent(canvas);

            // 사람 그리기
            drawPeople(canvas);


        }

        // 배경 그리기
        public void drawBack(Canvas canvas) {
            Paint pnt = new Paint();
            pnt.setStyle(Paint.Style.STROKE);
            pnt.setStrokeWidth(mCanvasW / 50);
            pnt.setColor(Color.rgb(0, 0, 0));

            for (int i = 0; i < mPeopleMax; i++) {
                Rect rtPart = getLadderPartArea(i);
                canvas.drawLine(rtPart.centerX(), rtPart.top, rtPart.centerX(), rtPart.bottom, pnt);
            }//사각형의 수평 중심 계산 된 값이 분수 인 경우이 방법은 계산 된 값보다 작은 가장 큰 정수를 반환합니다.

        }



        // 벌칙 개별 영역 좌표를 반환
        public Rect getPresentPartArea(int index) {
            // 사다리 부분 영역 좌표를 반환
            Rect rtLadderPart = getLadderPartArea(index);
            rtLadderPart.top = rtLadderPart.bottom;
            rtLadderPart.bottom = mCanvasH;
            return rtLadderPart;
        }

        // 벌칙 그리기
        public void drawPresent(Canvas canvas) {


            for (int i = 0; i < mPeopleMax; i++) {
                // 벌칙 개별 영역 좌표를 반환

                Rect rtPresentPart = getPresentPartArea(i);

                // 둥근 사각형 그리기
              drawRoundRect(canvas, rtPresentPart, Color.rgb(0, 0, 0));

                // 사각형 영역 안에 텍스트 출력
                drawText(canvas, rtPresentPart, mArPresent.get(i));
            }

        }

        // 사각형 영역 안에 텍스트 출력
        public void drawText(Canvas canvas, Rect rtArea, String strText) {
            int fontSize = rtArea.width() / 5;
            Paint pnt = new Paint();
            pnt.setTextAlign(Paint.Align.CENTER);
            // 텍스트 폰트 크기를 지정
            pnt.setTextSize(fontSize);
            // 텍스트 폰트 컬러를 지정
            pnt.setARGB(255, 0, 0, 64);
            // 캔버스에 텍스트를 출력
            canvas.drawText(strText, rtArea.centerX(), rtArea.centerY() + fontSize / 3, pnt);
        }

        // 둥근 사각형 그리기
        public void drawRoundRect(Canvas canvas, Rect rtArea, int colorBack) {
            Paint pnt = new Paint();
            pnt.setStyle(Paint.Style.STROKE);
            pnt.setColor(colorBack);
            pnt.setStrokeWidth(mCanvasW / 50);


            canvas.drawRect(rtArea.left , rtArea.top, rtArea.right, rtArea.bottom, pnt);
        }

        // 게임 정보를 생성
        public void createGame(Canvas canvas) {
            Log.d(TAG, "시작!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            mCanvasW = canvas.getWidth();           // 캔버스 넓이
            mCanvasH = canvas.getHeight();          // 캔버스 높이
            mMoveUnitH = (int) ((float) mCanvasW / 100.f);        // 수평 이동 단위
            mMoveUnitV = (int) ((float) mCanvasH / 100.f);        // 수직 이동 단위
            mArHBar = new ArrayList<Point>();            // 수평 사다리 정보 배열

            int check1 = 100;
            int check2 = 100;
            int check3 = 100;
            int check4 = 100;
            int check5 = 100;

            int check6 = 100;

            // 사다리 영역 좌표를 반환
            Rect rtLadder = getLadderArea();

            // 인원수에 비례해서 수평바를 생성
            for (int i = 0; i < mPeopleMax * 2; i++) {

                boolean check = false;

                Point ptHBar = new Point();
                // 수평바 왼쪽 위치를 랜덤으로 생성
                ptHBar.x = getRandomMath(mPeopleMax - 1, 0);
//                check1[i]=ptHBar.x;
                // 수평바 수직 위치를 랜덤으로 생성

                ////중복제거하자~!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                ptHBar.y = getRandomMath(rtLadder.height() - 10, rtLadder.top);
                if (i > 0) {

                    for (int j = 0; j < i; j++) {

                        if (ptHBar.y == mArHBar.get(j).y || Math.abs(ptHBar.y - mArHBar.get(j).y) < 50) {
                            Log.d(TAG, "걸렸다!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");


                            check = true;
                            i--;
                            break;

                        }

                    }
                    if (check == false) {

                        mArHBar.add(ptHBar);
                    }
                } else if (i == 0) {

                    mArHBar.add(ptHBar);
                }

            }
            if (mPeopleMax == 6) {
                Point ptHBar = new Point();
                for (int i = 0; i < mPeopleMax * 2; i++) {

                    switch (mArHBar.get(i).x) {

                        case 0:
                            check1 = 0;
                            break;
                        case 1:
                            check2 = 1;
                            break;
                        case 2:
                            check3 = 2;
                            break;
                        case 3:
                            check4 = 3;
                            break;
                        case 4:
                            check5 = 4;
                            break;


                    }

                }
                if (check1 == 100) {
                    ptHBar.x = 0;
                    ptHBar.y = rtLadder.height();
                    mArHBar.add(ptHBar);
                } else if (check2 == 100) {
                    ptHBar.x = 1;
                    ptHBar.y = rtLadder.height();
                    mArHBar.add(ptHBar);
                } else if (check3 == 100) {
                    ptHBar.x = 2;
                    ptHBar.y = rtLadder.height();
                    mArHBar.add(ptHBar);
                } else if (check4 == 100) {
                    ptHBar.x = 3;
                    ptHBar.y = rtLadder.height();
                    mArHBar.add(ptHBar);
                } else if (check5 == 100) {
                    ptHBar.x = 4;
                    ptHBar.y = rtLadder.height();
                    mArHBar.add(ptHBar);
                }


            }
            if (mPeopleMax == 5) {

                for (int i = 0; i < mPeopleMax * 2; i++) {

                    switch (mArHBar.get(i).x) {

                        case 0:
                            check1 = 0;
                            break;
                        case 1:
                            check2 = 1;
                            break;
                        case 2:
                            check3 = 2;
                            break;
                        case 3:
                            check4 = 3;
                            break;


                    }

                }
                Point ptHBar = new Point();
                if (check1 == 100) {
                    ptHBar.x = 0;
                    ptHBar.y = rtLadder.height();
                    mArHBar.add(ptHBar);
                } else if (check2 == 100) {
                    ptHBar.x = 1;
                    ptHBar.y = rtLadder.height();
                    mArHBar.add(ptHBar);
                } else if (check3 == 100) {
                    ptHBar.x = 2;
                    ptHBar.y = rtLadder.height();
                    mArHBar.add(ptHBar);
                } else if (check4 == 100) {
                    ptHBar.x = 3;
                    ptHBar.y = rtLadder.height();
                    mArHBar.add(ptHBar);
                }

            }

            if (mPeopleMax == 4) {
                for (int i = 0; i < mPeopleMax * 2; i++) {

                    switch (mArHBar.get(i).x) {

                        case 0:
                            check1 = 0;
                            break;
                        case 1:
                            check2 = 1;
                            break;
                        case 2:
                            check3 = 2;
                            break;


                    }

                }
                Point ptHBar = new Point();
                if (check1 == 100) {
                    ptHBar.x = 0;
                    ptHBar.y = rtLadder.height();
                    mArHBar.add(ptHBar);
                } else if (check2 == 100) {
                    ptHBar.x = 1;
                    ptHBar.y = rtLadder.height();
                    mArHBar.add(ptHBar);
                } else if (check3 == 100) {
                    ptHBar.x = 2;
                    ptHBar.y = rtLadder.height();
                    mArHBar.add(ptHBar);
                }

            }
            if (mPeopleMax == 3) {
                for (int i = 0; i < mPeopleMax * 2; i++) {

                    switch (mArHBar.get(i).x) {

                        case 0:
                            check1 = 0;
                            break;
                        case 1:
                            check2 = 1;
                            break;


                    }

                }
                Point ptHBar = new Point();
                if (check1 == 100) {
                    ptHBar.x = 0;
                    ptHBar.y = rtLadder.height();
                    mArHBar.add(ptHBar);
                } else if (check2 == 100) {
                    ptHBar.x = 1;
                    ptHBar.y = rtLadder.height();
                    mArHBar.add(ptHBar);
                }
            }
            if (mPeopleMax == 2) {
                for (int i = 0; i < mPeopleMax * 2; i++) {

                    switch (mArHBar.get(i).x) {

                        case 0:
                            check1 = 0;
                            break;


                    }

                }
                Point ptHBar = new Point();
                if (check1 == 100) {
                    ptHBar.x = 0;
                    ptHBar.y = rtLadder.height();
                    mArHBar.add(ptHBar);
                }
            }

        }

        //Math.random() 0-1
        // 수학함수로 난수를 생성해서 반환하는 함수
        public int getRandomMath(int max, int offset) {
            int nResult = (int) (Math.random() * max) + offset;
            return nResult;
        }

        // 수평바 그리기
        public void drawHbar(Canvas canvas) {
            if (mArHBar == null) return;
            Paint pnt = new Paint();
            pnt.setStyle(Paint.Style.STROKE);
            pnt.setStrokeWidth(mCanvasW / 50);
            pnt.setColor(Color.rgb(0, 0, 0));

            for (int i = 0; i < mArHBar.size(); i++) {

                Point ptHBar = mArHBar.get(i);
                // 사다리 부분 영역 좌표를 반환
                Rect ladderPart = getLadderPartArea(ptHBar.x);

                canvas.drawLine(ladderPart.centerX(), ptHBar.y, ladderPart.centerX() + ladderPart.width(), ptHBar.y, pnt);
            }
        }

        // 사람 개별 영역 좌표를 반환
        public Rect getPeoplePartArea(int index) {
            // 개별 사람 정보를 구한다
            PeopleInfo pi = mArPeople.get(index);

            // 사다리 부분 영역 좌표를 반환
            Rect rtLadderPart = getLadderPartArea(pi.mPosition.x);

            Rect rtPeople = new Rect(rtLadderPart);
            rtPeople.top = pi.mPosition.y;
            rtPeople.bottom = rtPeople.top + rtLadderPart.top;
            rtPeople.left += pi.mOffsetH;
            rtPeople.right += pi.mOffsetH;
            return rtPeople;
        }

        // 사람 그리기
        public void drawPeople(Canvas canvas) {
            for (int i = 0; i < mPeopleMax; i++) {
                // 사람 개별 영역 좌표를 반환
                Rect rtPresentPart = getPeoplePartArea(i);
                // 둥근 사각형 그리기
                drawRoundRect(canvas, rtPresentPart, Color.rgb(0, 0, 0));
                // 사각형 영역 안에 텍스트 출력
                drawText(canvas, rtPresentPart, mArPeople.get(i).mName);
            }
        }
    }


    // 게임 시작
    public void startLadder() {
        // 타이머 시작
        mTimer.sendEmptyMessageDelayed(0, 10);
    }


    // 게임 완료 여부를 구해서 반환
    public boolean isCompleted() {
        for (int i = 0; i < mArPeople.size(); i++) {
            PeopleInfo pi = mArPeople.get(i);
            if (pi.mFinished == false)
                return false;
        }
        return true;
    }


    // 사다리 영역 좌표를 반환
    public Rect getLadderArea() {
        Rect rtPart = new Rect(0, 0, mCanvasW, mCanvasH);
        rtPart.top = (int) (mCanvasH * 0.08);
        rtPart.bottom = (int) (mCanvasH - rtPart.top);
        return rtPart;
    }

    // 사다리 부분 영역 좌표를 반환
    public Rect getLadderPartArea(int index) {
        // 사다리 영역 좌표를 반환
        Rect rtLadder = getLadderArea();
        float partWidth = mCanvasW / mPeopleMax;

        rtLadder.left = (int) (partWidth * index);
        rtLadder.right = rtLadder.left + (int) partWidth;
        return rtLadder;
    }

    // 사람 정보 클래스
    public class PeopleInfo {
        String mName = null;        // 사람 이름
        Point mPosition = null;     // 위치 좌표
        int mOffsetH = 0;           // 수평 이동 거리
        boolean mFinished = false;

        public PeopleInfo(String name, int index) {
            mName = name;
            mPosition = new Point(index, 0);
        }

        // 사다리 이동
        public void move() {
            if (mFinished)
                return;
            // 사다리 영역 좌표를 반환

            Rect rtLadderPart = getLadderPartArea(mPosition.x);
            // 수평 이동 체크
            if (mOffsetH != 0) {
                // 오른쪽으로 이동중이라면
                if (mOffsetH > 0) {
                    mOffsetH += mMoveUnitH;
                    // 수평 이동이 끝났다면
                    if (mOffsetH > rtLadderPart.width()) {
                        mOffsetH = 0;
                        mPosition.x++;
                    }
                }
                // 왼쪽으로 이동중이라면
                else {
                    mOffsetH -= mMoveUnitH;
                    // 수평 이동이 끝났다면
                    if (Math.abs(mOffsetH) > rtLadderPart.width()) {
                        mOffsetH = 0;
                        mPosition.x--;
                    }
                }

                return;
            }

            // 수직 방향 이동
            mPosition.y += mMoveUnitV;
            // 사람 영역의 수직 중심점
            int peopleY = mPosition.y + (rtLadderPart.top / 2);

            for (int i = 0; i < mArHBar.size(); i++) {
                Point ptHBar = mArHBar.get(i);
                // 수평바를 만났다면
                if (ptHBar.y <= peopleY && ptHBar.y > (peopleY - mMoveUnitV)) {
                    // 수평 위치를 변경
                    if (mPosition.x == ptHBar.x) {
                        mOffsetH = mMoveUnitH;
                    } else if (mPosition.x == ptHBar.x + 1) {

                        mOffsetH = mMoveUnitH * -1;

                    }
                    // 좌우에 있는 수평바에 아니라면 무시
                    else {
                        continue;
                    }
                    // 수직 위치를 수평바에 맞춘다
                    mPosition.y = peopleY - (rtLadderPart.top / 2);

                    return;
                }
            }

            if (mPosition.y >= rtLadderPart.bottom) {
                mPosition.y = rtLadderPart.bottom;
                mFinished = true;
            }
        }
    }


    // 타이머 이벤트를 위한 핸들러 객체 생성 & 이벤트 함수 재정의
    Handler mTimer = new Handler() {
        public void handleMessage(Message msg) {
            for (int i = 0; i < mArPeople.size(); i++) {
                mArPeople.get(i).move();
            }

            // 캔버스 갱신
            mainView.invalidate();

            // 게임이 완료되지 않았다면 타이머 재시작
            if (isCompleted() == false) {
                // 타이머 재시작
                mTimer.sendEmptyMessageDelayed(0, 30);
            }
            // 게임이 완료되었다면
            else {
                Toast.makeText(getApplicationContext(), "Game Completed", Toast.LENGTH_SHORT).show();
                // 게임 완료 이벤트 함수
                onGameCompleted();
            }
        }
    };


    // 게임 완료 이벤트 함수
    public void onGameCompleted() {
        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
        // Intent 에 인원수를 입력
        intent.putExtra("PeopleMax", mPeopleMax);

        // Intent 에 사람-벌칙 매칭 정보를 입력
        for (int i = 0; i < mPeopleMax; i++) {
            intent.putExtra("Result" + i, mArPeople.get(i).mName
                    + " - " + mArPresent.get(mArPeople.get(i).mPosition.x));
        }

        startActivity(intent);
    }


}
