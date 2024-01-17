package com.jnu.student;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TaskItemDetailActivity extends AppCompatActivity {
    private String ST;
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        // 获取标题、成就点数、数量、类型和标签
        EditText editTitle = findViewById(R.id.editTitle);
        EditText editPoint = findViewById(R.id.editAchievementPoints);
        EditText editNum   = findViewById(R.id.editQuantity);
        EditText editTag   = findViewById(R.id.editTag);
        Intent intent =getIntent();
        if(intent != null){
            if(intent.getStringExtra("title") != null) {
                TextView textView = findViewById(R.id.titleText);
                textView.setText("修改任务");

            }
            editTitle.setText(intent.getStringExtra("title"));
            editPoint.setText(intent.getStringExtra("points"));
            editTag.setText(intent.getStringExtra("tag"));
            position = intent.getIntExtra("position",-1);
        }
        ImageView backimage =findViewById(R.id.goBackButton);
        backimage.setOnClickListener(v -> TaskItemDetailActivity.this.finish());

        Spinner editor =findViewById(R.id.spinnerTaskType);
        editor.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ST = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ST = "每日任务";
            }
        });

        Button ok =findViewById(R.id.btnSubmit);
        ok.setOnClickListener(v -> {

            if(editTitle.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(),"请输入名称", Toast.LENGTH_SHORT).show();
                return;
            }
            if(editPoint.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(),"请输入完成得分", Toast.LENGTH_SHORT).show();
                return;
            }
            intent.putExtra("title",editTitle.getText().toString());
            intent.putExtra("points",editPoint.getText().toString());
            intent.putExtra("type",ST);
            if(editNum.getText().toString().equals("")){
                intent.putExtra("numbers","1");
            }
            else{
                intent.putExtra("numbers",editNum.getText().toString());
            }
            if(editTag.getText().toString().equals("")){
                intent.putExtra("tag","全部");
            }
            else{
                intent.putExtra("tag",editTag.getText().toString());
            }
            setResult(Activity.RESULT_OK,intent);
            TaskItemDetailActivity.this.finish();
        });
    }
}