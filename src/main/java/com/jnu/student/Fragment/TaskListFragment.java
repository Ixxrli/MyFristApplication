package com.jnu.student.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.MenuItem;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jnu.student.R;
import com.jnu.student.TaskAdapter;
import com.jnu.student.TaskItemDetailActivity;
import com.jnu.student.data.TaskStorage;
import com.jnu.student.data.TaskStorageItem;
import com.jnu.student.content.MyTask;

import java.util.ArrayList;

public class TaskListFragment extends Fragment {
    private int menuid;
    public String Type;
    private RecyclerView targetR;
    private TaskStorage taskStorage;
    private ActivityResultLauncher<Intent> updateTaskLauncher;
    public TaskAdapter TAdapter;
    private ArrayList<MyTask> list_all;
    public ArrayList<MyTask> list_type;

    private String File_Target_Name = "TData";

    public TaskListFragment() {
        // Required empty public constructor
    }

    public TaskListFragment(String Type, int menuid) {
        this.Type = Type;
        this.menuid = menuid;
    }

    public static TaskListFragment newInstance() {
        TaskListFragment fragment = new TaskListFragment();
        Bundle T = new Bundle();
        fragment.setArguments(T);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_stage_task, container, false);
        taskStorage = new TaskStorageItem();
        list_all = taskStorage.LoadTaskItems(requireActivity().getApplicationContext(), File_Target_Name);
        list_type = filterType(list_all, Type);

        targetR = rootView.findViewById(R.id.task_recyclerView);
        TAdapter = new TaskAdapter(requireActivity(), list_type, this.menuid);
        targetR.setAdapter(TAdapter);
        targetR.setLayoutManager(new LinearLayoutManager(requireActivity()));
        registerForContextMenu(targetR);
        updateTaskLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // 当数据准备好时
                    if (result.getResultCode() == requireActivity().RESULT_OK) {
                        Intent intent = result.getData();
                        int position = intent.getIntExtra("position", 0);
                        String title = intent.getStringExtra("title");
                        int points = Integer.parseInt(intent.getStringExtra("points"));
                        int numbers = Integer.parseInt(intent.getStringExtra("numbers"));
                        String tag = intent.getStringExtra("tag");
                        MyTask task = list_type.get(position);
                        task.setTitle(title);
                        task.setTPoint(points);
                        task.setTNum(numbers);

                        task.setTTag(tag);
                        TAdapter.notifyItemChanged(position);
                        taskStorage.SaveTaskItems(requireActivity().getApplicationContext(), File_Target_Name, list_type);
                    }
                    // 当数据还没准备好时
                    else if (result.getResultCode() == requireActivity().RESULT_CANCELED) {

                    }
                });
        return rootView;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // 删除判断位置
        if (item.getGroupId() != menuid) {
            return false;
        }
        switch (item.getItemId()) {
            case 0:
                Intent intentUpdate = new Intent(requireActivity(), TaskItemDetailActivity.class);
                MyTask selectTask = list_type.get(item.getOrder());
                intentUpdate.putExtra("title", selectTask.getTitle());
                intentUpdate.putExtra("points", String.valueOf(selectTask.getPoint()));
                intentUpdate.putExtra("numbers", String.valueOf(selectTask.getTNum()));
                intentUpdate.putExtra("tag", selectTask.getTTag());
                intentUpdate.putExtra("position", item.getOrder());
                updateTaskLauncher.launch(intentUpdate);
                break;
            case 1:
                // 删除数据
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("Delete Data");
                builder.setMessage("你确定删除这个任务？");
                // 如果按下确定
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 更新数据
                        list_all = taskStorage.LoadTaskItems(requireActivity().getApplicationContext(), File_Target_Name);
                        MyTask myTask = list_type.get(item.getOrder());
                        for (MyTask task : list_all) {
                            if (task.getTime().equals(myTask.getTime())) {
                                list_all.remove("已删除");
                                break;
                            }
                        }
                        taskStorage.SaveTaskItems(requireActivity().getApplicationContext(), File_Target_Name, list_all);
                        // 刷新界面
                        list_type.remove(item.getOrder());
                        TAdapter.notifyItemRemoved(item.getOrder());
                    }
                });
                // 如果按下否定，什么都不做
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    private ArrayList<MyTask> filterType(ArrayList<MyTask> taskListAll, String taskType) {
        ArrayList<MyTask> taskFilterType = new ArrayList<>();
        for (MyTask task : taskListAll) {
            if (task.getTType() != null&&task.getTState() != null&&task.getTType().equals(taskType)&&task.getTState().equals("正常")) {
                taskFilterType.add(task);
            }
        }
        return taskFilterType;
    }

    private ArrayList<MyTask> filterTypeComplete(ArrayList<com.jnu.student.content.MyTask> taskListAll, String taskType) {
        ArrayList<com.jnu.student.content.MyTask> taskFilterType = new ArrayList<>();
        for (com.jnu.student.content.MyTask task : taskListAll) {
            if (task.getTType() != null&&task.getTState() != null&&task.getTType().equals(taskType)&&task.getTState().equals("完成")) {
                taskFilterType.add(task);
            }
        }
        return taskFilterType;
    }

}

