package com.lxw.dailynews.app.ui.viewImp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.lxw.dailynews.R;
import com.lxw.dailynews.framework.base.BaseMvpView;
import com.lxw.dailynews.framework.image.ImageManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PhotoPreviewActivity extends AppCompatActivity implements BaseMvpView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_preview)
    ImageView imgPreview;

    private String imgUrl;//预览图片的地址
    private PhotoViewAttacher attacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);
        ButterKnife.bind(this);
        imgUrl = getIntent().getStringExtra("imgUrl");
        initView();
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
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.toolbar_photo_preview);//设置右上角的填充菜单
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //保存图片
                return true;
            }
        });

        ImageManager.getInstance().loadImage(this, imgPreview, imgUrl, true);
        attacher = new PhotoViewAttacher(imgPreview);
    }

    @Override
    public void prepareData() {

    }

    @Override
    public void rePrepareData() {

    }
}
