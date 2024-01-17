package com.jnu.student.data;


import android.content.Context;

import com.jnu.student.content.MyTask;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class TaskStorageItem implements TaskStorage {

    @Override
    public ArrayList<MyTask> LoadTaskItems(Context context, String fileName) {
        ArrayList<MyTask> TData = new ArrayList<>();
        try{
            FileInputStream fileInputStream =context.openFileInput(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            TData =(ArrayList<MyTask>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TData;
    }

    @Override
    public ArrayList<MyTask> LoadTaskItems(Context context, String fileName, String type) {
        ArrayList<MyTask> targetData = LoadTaskItems(context,fileName);
        ArrayList<MyTask> targetDataFilter = new ArrayList<>();
        for(MyTask myTask : targetData){
            if(myTask.getTType().equals(type)){
                targetDataFilter.add(myTask);
            }
        }
        return targetDataFilter;
    }

    @Override
    public ArrayList<MyTask> LoadTaskItems(Context context, String fileName, String type, String tag) {
        ArrayList<MyTask> taskData = LoadTaskItems(context,fileName);
        ArrayList<MyTask> taskDataFilter = new ArrayList<>();
        for(MyTask task : taskData){
            if(task.getTType().equals(type) && task.getTTag().equals(tag)){
                taskDataFilter.add(task);
            }
        }
        return taskDataFilter;
    }


    @Override
    public void SaveTaskItems(Context context, String fileName, ArrayList<MyTask> taskData) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName,Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(taskData);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}