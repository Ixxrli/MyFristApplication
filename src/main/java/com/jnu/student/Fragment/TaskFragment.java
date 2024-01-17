package com.jnu.student.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jnu.student.R;
import com.jnu.student.TaskItemDetailActivity;
import com.jnu.student.content.MyTask;
import com.jnu.student.data.TaskStorage;
import com.jnu.student.data.TaskStorageItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

public class TaskFragment extends Fragment {
    private final static  String[] TType = {"每日任务", "每周任务", "普通任务"};
    private final String File_Task_Name ="TData";

    private  ArrayList<MyTask> TList_all;

    private TaskStorage taskStorage;
    private int PointSum;

    private ActivityResultLauncher<Intent> AddTaskLauncher;
    private TextView PointSumView;
    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction()!=null&&intent.getAction().equals("MY_CUSTOM_ACTION")){
                TList_all =taskStorage.LoadTaskItems(requireActivity(),File_Task_Name);
                PointSum=getPointSum(TList_all);
                if(PointSum<0){
                    PointSumView.setTextColor(Color.RED);
                }else{
                    PointSumView.setTextColor(Color.GREEN);
                }
                PointSumView.setText("  "+PointSum);
            }
        }
    };



    public TaskFragment() {
        // Required empty public constructor
    }

    public static TaskFragment newInstance() {
        TaskFragment fragment = new TaskFragment();
        Bundle T = new Bundle();
        fragment.setArguments(T);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(requireContext())
                .registerReceiver(receiver, new IntentFilter("MY_CUSTOM_ACTION"));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_task, container, false);
        // 数据读取
        taskStorage = new TaskStorageItem();
        TList_all = taskStorage.LoadTaskItems(requireActivity(), File_Task_Name);
        // 设置左下角点数
        PointSum=getPointSum(TList_all);
        PointSumView=rootview.findViewById(R.id.point_sum);
        if(PointSum<0){
            PointSumView.setTextColor(Color.RED);
        }else{
            PointSumView.setTextColor(Color.GREEN);
        }
        // Fragment + ViewPager2 + TableLayout
        ViewPager2 viewPager2 = rootview.findViewById(R.id.view_pager);
        TabLayout tabLayout = rootview.findViewById(R.id.tab_layout);

        PagerAdapter pagerAdapter = new PagerAdapter(this);
        viewPager2.setAdapter(pagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> tab.setText("Tab " + (position + 1))
        ).attach();
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> tab.setText(TType[position])).attach();

        AddTaskLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // 当数据准备好时
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        String title = null;
                        if (intent != null) {
                            title = intent.getStringExtra("title");
                        }
                        int points = 0;
                        if (intent != null) {
                            points = Integer.parseInt(Objects.requireNonNull(intent.getStringExtra("points")));
                        }
                        int numbers = 0;
                        if (intent != null) {
                            numbers = Integer.parseInt(Objects.requireNonNull(intent.getStringExtra("numbers")));
                        }
                        String type = null;
                        if (intent != null) {
                            type = intent.getStringExtra("type");
                        }
                        String tag = null;
                        if (intent != null) {
                            tag = intent.getStringExtra("tag");
                        }
                        // 创建 Date 对象表示当前时间
                        Date currentDate = new Date();
                        // 创建 SimpleDateFormat 对象，指定日期格式和时区
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai")); // 设置时区为北京时间
                        // 格式化输出
                        String beijingTime = sdf.format(currentDate);
                        MyTask myTask = new MyTask(beijingTime, title, points, numbers, 0, type, tag,"正常");
                        // 存储数据
                        TList_all.add(myTask);
                        List<Fragment> fragments = getChildFragmentManager().getFragments();
                        for(Fragment fragment:fragments){
                            if(fragment instanceof TaskListFragment){
                                TaskListFragment phaseTaskFragment = (TaskListFragment)fragment;
                                if(phaseTaskFragment.Type.equals(myTask.getTType())) {
                                    phaseTaskFragment.list_type.add(myTask);
                                    phaseTaskFragment.TAdapter.notifyItemInserted(phaseTaskFragment.list_type.size());
                                    break;
                                }
                            }
                        }

                        taskStorage.SaveTaskItems(requireActivity().getApplicationContext(), File_Task_Name, TList_all);

                    }
                    else if (result.getResultCode() == requireActivity().RESULT_CANCELED) {

                    }
                });

        ImageView addButton = rootview.findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), TaskItemDetailActivity.class);
            AddTaskLauncher.launch(intent);
        });
        return rootview;
    }
    private static class PagerAdapter extends FragmentStateAdapter {

        private static final int NUM_TABS = 3;

        public PagerAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            // 根据位置返回不同的子 Fragment
            switch (position) {
                case 0:
                    return new TaskListFragment(TType[0],0);
                case 1:
                    return new TaskListFragment(TType[1],1);
                case 2:
                    return new TaskListFragment(TType[2],2);
                default:
                    throw new IllegalArgumentException("Invalid position");
            }
        }
        @Override
        public int getItemCount() {
            // 返回子 Fragment 的数量
            return NUM_TABS;
        }
    }
    private int getPointSum(ArrayList<MyTask>TList){
        int sum=0;
        for(MyTask myTask:TList){
            sum+=myTask.getPoint()*myTask.getTNumFinish();
        }
        return sum;
    }
    @Override
    public void onDestroy() {
        // 注销广播接收器
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver);
        super.onDestroy();
    }
}