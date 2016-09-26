package audio.rabid.artemis;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import audio.rabid.artemis.lib.DisposeBag;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by  charles  on 9/25/16.
 */
public abstract class ArtemisActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Nullable
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    private DisposeBag disposeBag = new DisposeBag();

    public abstract void prepareContentView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareContentView();
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(collapsingToolbarLayout != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                collapsingToolbarLayout.setContentScrim(getDrawable(R.drawable.ylt));
            } else {
                collapsingToolbarLayout.setContentScrim(getResources().getDrawable(R.drawable.ylt));
            }
        }
    }

    @OnClick(R.id.fab)
    public abstract void onFabClick(FloatingActionButton fab);

    public DisposeBag getDisposeBag() {
        return disposeBag;
    }

    public FloatingActionButton getFab() {
        return fab;
    }

    @Override
    protected void onDestroy() {
        disposeBag.disposeAll();
        super.onDestroy();
    }
}
