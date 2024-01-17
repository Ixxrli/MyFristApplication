package com.jnu.student;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jnu.student.content.MyTask;
import com.jnu.student.data.TaskStorage;
import com.jnu.student.data.TaskStorageItem;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder>{
    private final String FILE_TASK_NAME = "tData";
    private TaskStorage taskStorage;
    private final Context context;
    private final ArrayList<MyTask> list ;
    private int menuid;

    public TaskAdapter(Context context, ArrayList<MyTask> list,int menuid){
        this.context = context;
        this.list=list;
        this.menuid=menuid;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.targetitem,parent,false);
        return new MyViewHolder(view,menuid);
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyTask myTask = list.get(position);
        holder.TarTitle.setText(myTask.getTitle());
        holder.taskNumFinish.setText(myTask.getTNumFinish()+"/"+ myTask.getTNum());
        holder.taskeditPoint.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // 改变数据
                if (myTask.getTNumFinish() < myTask.getTNum()) {
                    taskStorage = new TaskStorageItem();
                    ArrayList<MyTask> taskAllList = taskStorage.LoadTaskItems(context.getApplicationContext(), FILE_TASK_NAME);
                    for (MyTask task : taskAllList) {
                        if (task.getTime().equals(myTask.getTime())) {
                            task.setTNumFinish(task.getTNumFinish() + 1);
                            taskStorage.SaveTaskItems(context, FILE_TASK_NAME, taskAllList);
                        }
                    }
                    myTask.setTNumFinish(myTask.getTNumFinish() + 1);
                    holder.taskNumFinish.setText(myTask.getTNumFinish() + "/" + myTask.getTNum());
                    // 发送广播
                    Intent intent = new Intent("MY_CUSTOM_ACTION");
                    intent.putExtra("data", "invalidate");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        public int menuid;
        ImageView Image;
        TextView TarTitle;
        TextView taskNumFinish;
        TextView taskeditPoint;

        public MyViewHolder(@NonNull View itemView,int menuid) {
            super(itemView);
            Image = itemView.findViewById(R.id.icon);
            TarTitle = itemView.findViewById(R.id.target_item_title);
            taskNumFinish = itemView.findViewById(R.id.target_complete_num);
            taskeditPoint=itemView.findViewById(R.id.target_edit);
            this.menuid=menuid;
            // 设置监听者为自己
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(menuid,0,this.getAdapterPosition(),"修改");
            menu.add(menuid,1,this.getAdapterPosition(),"删除");
        }
    }
}
