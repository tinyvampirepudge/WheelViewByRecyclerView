package com.tinytongtong.wheelviewbyrecyclerview;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private List<String> mList = new ArrayList<>();
    private Button btnTest1;
    private Button btnTest2;
    private Button btnTest3;
    private Button btnTest4;
    private ProjSelectDialog projSelectDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mockData();
        initView();
    }

    private void mockData() {
        for (int i = 0; i < 10; i++) {
            for (int j = i * 10; j < (i + 1) * 10; j++) {
                StringBuilder sb = new StringBuilder("测试数据啊");
                for (int m = 0; m < i; m++) {
                    sb.append("测试数据啊");
                }
                mList.add(sb.toString() + j);
            }
        }
    }

    private void initView() {
        btnTest1 = findViewById(R.id.btn_test1);
        btnTest2 = findViewById(R.id.btn_test2);
        btnTest3 = findViewById(R.id.btn_test3);
        btnTest4 = findViewById(R.id.btn_test4);
        btnTest1.setOnClickListener(this);
        btnTest2.setOnClickListener(this);
        btnTest3.setOnClickListener(this);
        btnTest4.setOnClickListener(this);
    }

    private void createShowProjectDialog(int defaultValue) {

        if (mList == null || mList.isEmpty()) {
            return;
        }

        final List<String> mOptionsItems = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            mOptionsItems.add(mList.get(i));
        }

        if (projSelectDialog == null) {
            projSelectDialog = new ProjSelectDialog(this, mOptionsItems);
            projSelectDialog.setOnResultListener(new ProjSelectDialog.OnResultListener() {
                @Override
                public void onResult(int selectedIndex, String selectedItem) {
                    Toast.makeText(MainActivity.this, "selectedIndex:" + selectedIndex + ", selectedItem:" + selectedItem, Toast.LENGTH_SHORT).show();
                    Log.e(MainActivity.class.getSimpleName(), "selectedIndex:" + selectedIndex + ", selectedItem:" + selectedItem);
                }
            });
        }

        projSelectDialog.setSelectedPosition(defaultValue);

        projSelectDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_test1:
                createShowProjectDialog(0);
                break;
            case R.id.btn_test2:
                createShowProjectDialog(mList.size() - 1);
                break;
            case R.id.btn_test3:
                createShowProjectDialog(mList.size() / 2);
                break;
            case R.id.btn_test4:
                createShowProjectDialog(-1);
                break;
            default:
                break;
        }
    }
}
