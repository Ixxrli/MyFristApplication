package com.jnu.student.data;

import android.content.Context;

import com.jnu.student.content.MyTask;

import java.util.ArrayList;

public interface TaskStorage {
    ArrayList<MyTask> LoadTaskItems(Context context, String fileName);
    ArrayList<MyTask> LoadTaskItems(Context context, String fileName, String type);

    ArrayList<MyTask> LoadTaskItems(Context context,String fileName,String type,String tag);
    void SaveTaskItems(Context context, String fileName, ArrayList<MyTask> Data);
}
