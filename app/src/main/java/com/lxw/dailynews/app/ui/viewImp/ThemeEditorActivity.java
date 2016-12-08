package com.lxw.dailynews.app.ui.viewImp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.lxw.dailynews.R;
import com.lxw.dailynews.app.bean.ThemeContentBean;
import com.lxw.dailynews.framework.base.BaseCommonAdapter;
import com.lxw.dailynews.framework.base.BaseMvpView;
import com.lxw.dailynews.framework.image.ImageManager;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThemeEditorActivity extends AppCompatActivity implements BaseMvpView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private List<ThemeContentBean.EditorsBean> editorsBean = new ArrayList();//主编的基本信息
    private BaseCommonAdapter<ThemeContentBean.EditorsBean> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_editor);
        ButterKnife.bind(this);
        initView();
        prepareData();
        rePrepareData();
    }

    @Override
    public void initView() {
        //返回键
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        recyclerview.setLayoutManager(new LinearLayoutManager(ThemeEditorActivity.this));
        adapter = new BaseCommonAdapter<ThemeContentBean.EditorsBean>(ThemeEditorActivity.this, R.layout.item_theme_editor, editorsBean) {
            @Override
            protected void convert(ViewHolder holder, ThemeContentBean.EditorsBean editorsBean, int position) {
                holder.setText(R.id.txt_name, editorsBean.getName());
                holder.setText(R.id.txt_introduction, editorsBean.getBio());
                ImageView imageView = holder.getView(R.id.img_avatar);
                ImageManager.getInstance().loadCircleImage(ThemeEditorActivity.this, imageView, editorsBean.getAvatar());
            }
        };
    }

    @Override
    public void prepareData() {
        editorsBean.clear();
        editorsBean.addAll((List<ThemeContentBean.EditorsBean>)getIntent().getSerializableExtra("editorsBean"));
    }

    @Override
    public void rePrepareData() {
        recyclerview.setAdapter(adapter);
    }
}
