package me.zsj.moment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import me.zsj.moment.model.Picture;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends RxAppCompatActivity {

    private SwipeRefreshLayout refresh;
    private RecyclerView list;

    private PictureAdapter adapter;

    private List<Picture> pictures = new ArrayList<>();
    private int page = 1;
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        applyFontsToTitle(toolbar);

        list = (RecyclerView) findViewById(R.id.list);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);

        toolbar.setOnClickListener(v -> list.scrollToPosition(0));

        url = getString(R.string.pic_category_url, 21, page);
        setupRecyclerView();

        toolbar.setOnClickListener(v -> list.scrollToPosition(0));

        loadData(true);
    }

    private void applyFontsToTitle(Toolbar toolbar) {
        int childCount = toolbar.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = toolbar.getChildAt(i);
            if (child instanceof TextView) {
                TextView tv = (TextView) child;
                tv.setTextSize(getResources().getDimensionPixelSize(R.dimen.font_text_size));
                Typeface titleFont = Typeface.
                        createFromAsset(getAssets(), "fonts/AlexBrush-Regular.ttf");
                if(tv.getText().equals(toolbar.getTitle())){
                    tv.setTypeface(titleFont);
                    break;
                }
            }
        }
    }

    private void setupRecyclerView() {
        adapter = new PictureAdapter(pictures);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = adapter.getItemViewType(position);
                if (viewType == R.layout.item_footer) {
                    return layoutManager.getSpanCount();
                } else {
                    return 1;
                }
            }
        });
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);

        list.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && pictures.size() > 0 &&
                        layoutManager.findFirstCompletelyVisibleItemPosition() >=
                                adapter.getItemCount() - 10) {
                    page += 1;
                    setUrl();
                    loadData();
                }
            }

        });

        list.setOnTouchListener((v, event) -> refresh.isRefreshing());

        refresh.setOnRefreshListener(() -> {
            page = 1;
            setUrl();
            loadData(true);
        });
    }

    private void setUrl() {
        url = getString(R.string.pic_category_url, 21, page);
    }

    private void loadData() {
        loadData(false /* load more data */);
    }

    private void loadData(boolean clean) {
        Worker.getInstance().start(url)
                .compose(bindToLifecycle())
                .filter(text -> text != null)
                .doOnNext(pictures -> {
                    if (clean) this.pictures.clear();
                })
                .flatMap(Parser.loadData())
                .filter(pictures -> pictures != null)
                .doOnNext(pictures -> this.pictures.addAll(pictures))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(this::setRefreshing)
                .subscribe(pictures -> {
                    adapter.notifyDataSetChanged();
                }, Throwable::printStackTrace);
    }

    private void setRefreshing() {
        refresh.setRefreshing(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.about){
            startActivity(new Intent(this, AboutActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
}
