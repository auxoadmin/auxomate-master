package com.auxomate.mynewself.mynewself.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;

import com.auxomate.mynewself.mynewself.R;


import java.util.ArrayList;

public class MenuPage extends AppCompatActivity {
    RecyclerView recyclerView;
    MenuScrollAdapter adapter;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<Integer> imagesList = new ArrayList<>();

    boolean clickable = true;
    boolean firstTime = true;

    public static void startActivity(@NonNull Activity activity,
                                     ArrayList<String> list,
                                     ArrayList<Integer> imagesList) {
        Intent intent = new Intent(activity, MenuPage.class);
        intent.putStringArrayListExtra("list", list);
        intent.putIntegerArrayListExtra("imagesList", imagesList);
        activity.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_page);

        recyclerView = findViewById(R.id.recycler_view);

        clickable = true;

        if (recyclerView != null && firstTime) {
            firstTime = false;
            list = getIntent().getStringArrayListExtra("list");
            imagesList = getIntent().getIntegerArrayListExtra("imagesList");
            adapter = new MenuScrollAdapter(this, list, imagesList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
            recyclerView.smoothScrollToPosition(list.size() - 1);
            recyclerView.setHasFixedSize(true);
        }

        adapter.setOnClickListener(new MenuScrollAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position, Pair[] pairs) {

                if (!clickable) {
                    return;
                }

                clickable = false;

                System.gc();

                Intent intent = new Intent(MenuPage.this, MenuScroll.class);
                intent.putExtra("viewPagerInitialPosition", position);
                intent.putStringArrayListExtra("list", list);
                intent.putIntegerArrayListExtra("imagesList", imagesList);


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && (list.size() <= 8)) {

                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            MenuPage.this, pairs);
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }
            }

            @Override
            public void scrollCallback(double pos) {
                recyclerView.smoothScrollToPosition(0);
            }

        });
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {

        clickable = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
        }

        final int position = MenuScroll.getPosition(resultCode, data);

        if (position != -1) {
            recyclerView.scrollToPosition(position);
        }

        recyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                recyclerView.getViewTreeObserver().removeOnPreDrawListener(this);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startPostponedEnterTransition();
                }
                return true;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        clickable = true;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            onActivityReenter(resultCode, data);

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
