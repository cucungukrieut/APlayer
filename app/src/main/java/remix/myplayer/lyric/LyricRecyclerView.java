package remix.myplayer.lyric;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import remix.myplayer.adapter.LyricAdapter;
import remix.myplayer.lyric.bean.LrcRow;
import remix.myplayer.util.LogUtil;

public class LyricRecyclerView extends RecyclerView {
    private LyricAdapter mLyricAdapter;
    private List<LrcRow> mLrcRows;
    private int mCurRow;
    private int mLastRow;

    public LyricRecyclerView(Context context) {
        super(context);
        init();
    }

    public LyricRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LyricRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                LogUtil.d("LyricRecyclerView","newState: " + newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                LogUtil.d("LyricRecyclerView","dx: " + dx + " dy: " + dy);
            }
        });
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        mLyricAdapter = (LyricAdapter) adapter;
    }

    public void setLrcRows(List<LrcRow> lrcRows){
        mLrcRows = lrcRows;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {


        return super.onTouchEvent(e);
    }

    public void seekTo(long progress){
        mLrcRows = mLyricAdapter.getDatas();
        if(mLrcRows == null || mLrcRows.size() == 0)
            return;
        List<LrcRow> tempList = new ArrayList<>(mLrcRows.subList(4, mLrcRows.size() - 4));
        //忽略前后四个
        for (int i = tempList.size() - 1; i >= 0; i--) {
            if (progress >= tempList.get(i).getTime()) {
                if (mCurRow != i) {
                    mLastRow = mCurRow;
                    mCurRow = i;
                    smoothScrollBy(0,mCurRow);
                    LogUtil.d("LyricRecyclerView","scrollTo: " + mCurRow);
                }
                break;
            }
        }
    }
}
