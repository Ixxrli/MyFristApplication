package com.jnu.student;


import android.os.Bundle;




import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;

import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jnu.student.Fragment.MyFragment;
import com.jnu.student.Fragment.RewardFragment;
import com.jnu.student.Fragment.TaskFragment;


public class MainActivity extends AppCompatActivity {
    private final String[] tabHeaderStrings = {"任务", "奖励", "统计","个人"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager2 viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        FragmentAdapter pagerAdapter = new FragmentAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(pagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(tabHeaderStrings[position])
                // 设置TabLayout的标题
        ).attach();
    }

    private static class FragmentAdapter extends FragmentStateAdapter {
        private static final int NUM_TABS = 4;

        public FragmentAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }
            @Override
            public Fragment createFragment (int position){
                switch (position) {
                    case 0:
                        return new TaskFragment();
                    case 1:
                        return new RewardFragment();
                    case 2:
                        return new WebViewFragment();
                    case 3:
                        return new MyFragment();
                    default:
                        return null;
                }
            }
            public int getItemCount() {
                return NUM_TABS;
            }
        }
}

