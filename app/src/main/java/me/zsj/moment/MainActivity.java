package me.zsj.moment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import me.zsj.moment.model.Picture;
import me.zsj.moment.utils.FilterUtils;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends RxAppCompatActivity {

    private DrawerLayout drawer;
    private SwipeRefreshLayout refresh;
    private RecyclerView filterList;
    private RecyclerView list;

    private PictureAdapter adapter;

    private List<Picture> pictures = new ArrayList<>();
    private int page = 1;
    private String url;
    private int categoryId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        filterList = (RecyclerView) findViewById(R.id.filters);
        list = (RecyclerView) findViewById(R.id.list);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);

        toolbar.setOnClickListener(v -> list.scrollToPosition(0));

        setupFilters();

        url = getString(R.string.new_picture_url, page);
        setupRecyclerView();

        toolbar.setOnClickListener(v -> list.scrollToPosition(0));

        loadData(true);
    }

    private void setupRecyclerView() {
        adapter = new PictureAdapter(pictures);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);

        list.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && pictures.size() > 0 &&
                        layoutManager.findFirstCompletelyVisibleItemPosition() >=
                                adapter.getItemCount() - 4) {
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

    private void setupFilters() {
        FilterAdapter filterAdapter = new FilterAdapter(this);
        filterList.setAdapter(filterAdapter);

        filterAdapter.setOnFilterListener(filterText -> {
            drawer.closeDrawer(GravityCompat.END);
            if (filterText.equals(getString(R.string.refresh))) {
                loadNewData();
            } else {
                loadCategoryData(filterText);
            }
        });
    }

    @SuppressWarnings("ConstantConditions")
    private void setUrl() {
        assert getSupportActionBar() != null;
        String title = getSupportActionBar().getTitle().toString();
        if (title.equals(getString(R.string.app_name))) {
            url = getString(R.string.new_picture_url, page);
        } else {
            url = getString(R.string.pic_category_url, categoryId, page);
        }
    }

    private void loadNewData() {
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(getString(R.string.app_name));
        page = 1;
        url = getString(R.string.new_picture_url, page);
        list.scrollToPosition(0);
        refresh.postDelayed(() -> {
            refresh.setRefreshing(true);
            loadData(true);
        }, 300);
    }

    private void loadCategoryData(String filterText) {
        page = 1;
        categoryId = FilterUtils.filterId(filterText);
        url = getString(R.string.pic_category_url, categoryId, page);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(filterText);
        list.scrollToPosition(0);
        refresh.postDelayed(() -> {
            refresh.setRefreshing(true);
            loadData(true);
        }, 300);
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
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.filters) {
            drawer.openDrawer(GravityCompat.END);
        } else if (item.getItemId() == R.id.about){
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
