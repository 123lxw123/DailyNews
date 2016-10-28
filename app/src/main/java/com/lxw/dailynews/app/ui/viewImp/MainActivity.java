package com.lxw.dailynews.app.ui.viewImp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lxw.dailynews.R;
import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.app.presenter.MainPresenter;
import com.lxw.dailynews.app.ui.view.IMainView;
import com.lxw.dailynews.framework.base.BaseMvpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseMvpActivity<IMainView, MainPresenter> implements IMainView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private LatestNewsBean latestNewsBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        latestNewsBean = (LatestNewsBean) getIntent().getSerializableExtra("latestNewsBean");
        if (latestNewsBean == null) {
            getLatestNews();
        } else {
            initView();
        }
    }

    public void setLatestNewsBean(LatestNewsBean latestNewsBean) {
        this.latestNewsBean = latestNewsBean;
        if (this.latestNewsBean != null) {
            initView();
        }
    }

    //获取最新消息传给主页
    @Override
    public void getLatestNews() {
        getPresenter().getLatestNews();
    }

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public void initView() {
        toolbar.inflateMenu(R.menu.toolbar_main);//设置右上角的填充菜单
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
//                int menuItemId = item.getItemId();
//                if (menuItemId == R.id.action_search) {
//                    Toast.makeText(ToolBarActivity.this , R.string.menu_search , Toast.LENGTH_SHORT).show();
//
//                } else if (menuItemId == R.id.action_notification) {
//                    Toast.makeText(ToolBarActivity.this , R.string.menu_notifications , Toast.LENGTH_SHORT).show();
//
//                } else if (menuItemId == R.id.action_item1) {
//                    Toast.makeText(ToolBarActivity.this , R.string.item_01 , Toast.LENGTH_SHORT).show();
//
//                } else if (menuItemId == R.id.action_item2) {
//                    Toast.makeText(ToolBarActivity.this , R.string.item_02 , Toast.LENGTH_SHORT).show();
//
//                }
                return true;
            }
        });
    }
}
