package com.example.laddergame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jiwoung.laddergame.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView mTextMax = null;
    int mPeopleMax = 2;
    // ListView 에 표시할 데이터를 저장하는 ArayList 배열
    ArrayList<MyItem> mArMember;
    ListView mListMember;
    MyListAdapter MyAdapter = null;
    GestureDetector mGesture = null;
    Boolean alltwo = false;
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);








        mTextMax = (TextView) findViewById(R.id.textMax);

        // ListView 위젯에 10개의 텍스트 항목을 추가
        initListView();

        // 최대 인원수를 화면에 표시
        showPeopleMax();
button();


    }

public void button(){



    Button btn=(Button)findViewById(R.id.sss);

               // final int finalI = i;
btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {


       for(int i=0;i<mListMember.getAdapter().getCount();i++) {

            final int ii=i;
         final   String aa=mArMember.get(i).textView1.getText().toString();



         mArMember.get(i).textView1.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {


                   count++;

                   if (count>2){

                       Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                       startActivity(intent);
                   }

                   if (mArMember.get(ii).mIsTitle==true){


                       Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                       startActivity(intent);

                   }

                   mArMember.remove(ii);
                        MyItem mi = new MyItem(aa, false, true);
                        mArMember.add(ii, mi);

                /*   Toast myToast = Toast.makeText(getApplicationContext(), Integer.toString(ii), Toast.LENGTH_SHORT);
                   myToast.show();*/

                 refreshList();
               }
           });




       }
    }
});








/*

            mListMember.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 //   resetArrayList();
                    Toast myToast = Toast.makeText(getApplicationContext(), Integer.toString(1), Toast.LENGTH_SHORT);
                    myToast.show();

                }
            });
*/



}




    // ListView 를 초기화
    public void initListView() {
        if (mArMember != null) {
            mArMember.clear();
            mArMember = null;
        }
        // ArrayList 배열 객체를 생성
        mArMember = new ArrayList<MyItem>();

        if (MyAdapter != null) MyAdapter = null;
        // 어댑터 객체를 생성해서 ListView 에 지정
        MyAdapter = new MyListAdapter(this, R.layout.custom_list_item,
                mArMember);

        // ListView 위젯의 핸들을 구해서 멤버변수에 저장
        mListMember = (ListView) findViewById(R.id.listMember);
        // ListView 의 어댑터를 지정
        mListMember.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        mListMember.setAdapter(MyAdapter);



    }

    // 최대 인원수를 화면에 표시
    public void showPeopleMax() {
        mTextMax.setText(Integer.toString(mPeopleMax));

        // ArrayList 배열 데이터를 초기화
        resetArrayList();
    }

    // ListView 의 항목 데이터 클래스 정의
    public class MyItem {
        String mTitle;
        EditText mEditName = null;
        boolean mIsTitle = false;
        View mLayoutItem = null;
        boolean twotouch = false;
        TextView textView1 = null;


        // 생성자 함수에서 멤버변수 초기화
        MyItem(String str1, boolean isTitle, boolean two) {
            mTitle = str1;
            mIsTitle = isTitle;
            twotouch = two;
        }
    }

    // ListView 와 데이터 배열을 연결해주는 커스텀 어댑터 클래스를 정의
    public class MyListAdapter extends BaseAdapter {
        Context mMaincon;
        LayoutInflater mInflater;
        ArrayList<MyItem> mArSrc;
        int layout;

        // 생성자 함수에서 멤버변수 초기화
        MyListAdapter(Context context, int alayout, ArrayList<MyItem> aarSrc) {
            mMaincon = context;
            mInflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            mArSrc = aarSrc;
            layout = alayout;

        }

        // 항목 개수를 반환
        public int getCount() {
            return mArSrc.size();
        }

        // 특정 항목의 텍스트 데이터를 반환
        public String getItem(int position) {
            return mArSrc.get(position).mTitle;

        }

        // 특정 항목의 ID 를 반환
        public long getItemId(int position) {
            return position;
        }

        // ListView 아이템 내부 각각의 엘리먼트에 데이터를 입력
        public View getView(final int position, View convertView, ViewGroup parent) {



            // 데이터 배열에서 해당 항목을 구한다
            MyItem mi = mArSrc.get(position);
            // 항목 Layout 이 아직 생성되지 않았다면 생성한다
            if (mi.mLayoutItem == null)
                mi.mLayoutItem = mInflater.inflate(layout, null);

            // 문자열을 구한다
            String strText = mi.mTitle;

            // 1번째 TextView 에 데이터 입력
            mi.textView1 = (TextView) mi.mLayoutItem.findViewById(R.id.text1);

            mi.textView1.setText(strText);
            mi.mLayoutItem.setTag(position);

            // EditText 의 핸들을 ArrayList 에 저장
            mi.mEditName = (EditText) mi.mLayoutItem.findViewById(R.id.editName);

            // EditText 에 임시 데이터 입력
            if (mi.mEditName.getText().toString().length() < 1) {
                // 사람 이름 일때
                if (position <= mPeopleMax)
                    mi.mEditName.setText("People-" + position);
                    // 벌칙 이름 일때
                else {
                    int index = position - mPeopleMax - 1;
                    mi.mEditName.setText("Present-" + index);
                }
            }

            // 타이틀이면 EditText 를 감춘다
            if (mArSrc.get(position).mIsTitle) {
                mi.textView1.setTextColor(Color.rgb(0, 0, 192));
                mi.mEditName.setVisibility(View.INVISIBLE);
            } else {
                mi.textView1.setTextColor(Color.rgb(0, 0, 0));
                mi.mEditName.setVisibility(View.VISIBLE);
            }


            return mi.mLayoutItem;


        }

    }

    ;

    // ArrayList 배열 데이터를 초기화
    public void resetArrayList() {
        // ListView 를 초기화
        initListView();

        // '이름' 입력 제목
        MyItem mi = new MyItem("이름", true, false);
        mArMember.add(mi);

        // 이름 개수 만큼 항목 추가
        for (int i = 0; i < mPeopleMax; i++) {
            mi = new MyItem("이름-" + (i + 1), false, false);
            mArMember.add(mi);
        }

        // '벌칙' 입력 제목
        mi = new MyItem("벌칙", true, false);
        mArMember.add(mi);

        // 벌칙 개수 만큼 항목 추가
        for (int i = 0; i < mPeopleMax; i++) {
            mi = new MyItem("벌칙-" + (i + 1), false, false);
            mArMember.add(mi);
        }

        // 목록 갱신
        refreshList();
    }

    // 목록 갱신
    public void refreshList() {
        MyListAdapter MyAdapter = (MyListAdapter) mListMember.getAdapter();
        MyAdapter.notifyDataSetChanged();
    }

    public void scrollMyListViewToBottom() {
       mListMember.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                mListMember.setSelection(MyAdapter.getCount() - 1);
            }
        });
    }


    // Button 클릭 이벤트 함수
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnMinus:       // 인원수 감소
                mPeopleMax--;
                if (mPeopleMax < 2)
                    mPeopleMax = 2;
                // 최대 인원수를 화면에 표시
                showPeopleMax();
                break;
            case R.id.btnPlus:         // 인원수 증가
                mPeopleMax++;
                if (mPeopleMax > 6)
                    mPeopleMax = 6;
                // 최대 인원수르 화면에 표시


                showPeopleMax();
             //   mListMember.setTranscriptMode( ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL );
               scrollMyListViewToBottom();
                break;
            case R.id.btnStart:
                // 사다리 게임 시작
                startLadderGame();
                break;


        }
    }

    // 사다리 게임 시작
    public void startLadderGame() {


        Intent intent = new Intent(getApplicationContext(), LadderGameActivity.class);
        Intent intent1 = new Intent(getApplicationContext(), ScamLadder.class);
        boolean isPresent = false;
        boolean isPresentaa = false;
        int itemNumber = 0;
        int itemNumber1 = 0;
        int aa = 0;
//int touchnum=0;
        // Intent 에 인원수를 입력
        intent.putExtra("PeopleMax", mPeopleMax);
        intent1.putExtra("PeopleMax", mPeopleMax);

        //두번클릭했을때
        //두번클릭안했을때

        for (int i = 1; i < mArMember.size(); i++) {

            MyItem mi = mArMember.get(i);
            // '벌칙 입력' 제목을 만나면 무시
            if (mi.mIsTitle) {
                isPresentaa = true;
                aa = 0;
                continue;
            }


            if (mArMember.get(i).twotouch == true) {
                alltwo = true;

                if (isPresentaa == false) {

                    intent1.putExtra("twoname" + (aa + 1), 1);
                } else {

                    intent1.putExtra("twonpresent" + (aa + 1), 1);
                }
            } else {
                if (isPresentaa == false) {
                    intent1.putExtra("twoname" + (aa + 1), 0);
                } else {
                    intent1.putExtra("twonpresent" + (aa + 1), 0);
                }

            }
aa++;
        }

        for (int i = 1; i < mArMember.size(); i++) {

            if (alltwo == true) {


                MyItem mi = mArMember.get(i);
                // '벌칙 입력' 제목을 만나면 무시
                if (mi.mIsTitle) {
                    isPresent = true;
                    itemNumber1 = 0;
                    continue;
                }

                String strEdit = "aaaa";
                // '이름' 항목일때
                if (isPresent == false) {
                    if (mi.mEditName == null)
                        strEdit = "Name" + (itemNumber1 + 1);
                    else
                        strEdit = mi.mEditName.getText().toString();


                    intent1.putExtra("Name" + itemNumber1, strEdit);
                }
                // '벌칙' 항목일때
                else {
                    if (mi.mEditName == null)
                        strEdit = "Present" + (itemNumber1 + 1);
                    else
                        strEdit = mi.mEditName.getText().toString();


                    intent1.putExtra("Present" + itemNumber1, strEdit);
                }
                itemNumber1++;

            } else {
                MyItem mi = mArMember.get(i);
                // '벌칙 입력' 제목을 만나면 무시
                if (mi.mIsTitle) {
                    isPresent = true;
                    itemNumber = 0;
                    continue;
                }

                String strEdit = "aaaa";
                // '이름' 항목일때
                if (isPresent == false) {
                    if (mi.mEditName == null)
                        strEdit = "Name" + (itemNumber + 1);
                    else
                        strEdit = mi.mEditName.getText().toString();

                    intent.putExtra("Name" + itemNumber, strEdit);

                }
                // '벌칙' 항목일때
                else {
                    if (mi.mEditName == null)
                        strEdit = "Present" + (itemNumber + 1);
                    else
                        strEdit = mi.mEditName.getText().toString();

                    intent.putExtra("Present" + itemNumber, strEdit);


                }
                itemNumber++;
            }
        }
        if (alltwo == true) {
            startActivity(intent1);
        } else {
            startActivity(intent);
        }

    }
}

