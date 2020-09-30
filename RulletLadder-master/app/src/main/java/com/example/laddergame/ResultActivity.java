package com.example.laddergame;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jiwoung.laddergame.R;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    ListView mListResult = null;
    // ListView 에 표시할 데이터를 저장하는 ArayList 배열
    ArrayList<String> mArResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_result);

        // ListView 위젯의 핸들을 구해서 멤버변수에 저장
        mListResult = (ListView)findViewById(R.id.listResult);
        // ArrayList 배열 객체 생성
        mArResult = new ArrayList<String>();

        // ListView 위젯에 결과 항목을 추가
        initListView(getIntent());
    }

    // ListView 위젯에 결과 항목을 추가
    public void initListView(Intent intent) {

        int peopleMax = intent.getIntExtra("PeopleMax", 0);
        if( peopleMax < 2 )
            return;
        for(int i=0; i < peopleMax; i++) {
            String strResult = intent.getStringExtra("Result" + i);
            mArResult.add(strResult);
        }

        // 어댑터 객체를 생성하고 ArrayList 를 지정
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mArResult);

        // ListView 의 어댑터를 지정
        mListResult.setAdapter(adapter);
    }

    // 버튼 클릭 이벤트 함수
    public void onClick(View v) {
        finish();
    }
}
