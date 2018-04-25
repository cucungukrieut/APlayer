package remix.myplayer.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.StaticLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import butterknife.BindView;
import remix.myplayer.App;
import remix.myplayer.R;
import remix.myplayer.adapter.holder.BaseViewHolder;
import remix.myplayer.lyric.bean.LrcRow;
import remix.myplayer.theme.ThemeStore;
import remix.myplayer.util.ColorUtil;
import remix.myplayer.util.DensityUtil;
import remix.myplayer.util.LogUtil;

/**
 * Created by Remix on 2018/3/1.
 */

public class LyricAdapter extends BaseAdapter<LrcRow,LyricAdapter.LrcViewHolder>{
    private RecyclerView mReclcyerView;
    private LinearLayoutManager mLinearLayoutManager;
    private final int OTHER_COLOR = ColorUtil.getColor(ThemeStore.isDay() ? R.color.lrc_normal_day : R.color.lrc_normal_night);
    private final int HIGHLIGHT_COLOR = ColorUtil.getColor(ThemeStore.isDay() ? R.color.lrc_highlight_day : R.color.lrc_highlight_night);

    public LyricAdapter(Context context, int layoutId, RecyclerView recyclerView) {
        super(context, layoutId);
        mReclcyerView = recyclerView;
        mLinearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
    }

    @Override
    protected void convert(LrcViewHolder holder, LrcRow lrcRow, int position) {
        holder.mContent.setText(String.format("%s%s", lrcRow.getContent(), lrcRow.hasTranslate() ? "\n" +  lrcRow.getTranslate() : ""));
//        holder.mTranslation.setText(lrcRow.hasTranslate() ? lrcRow.getTranslate() : "");
//        holder.mTranslation.setVisibility(lrcRow.hasTranslate() ? View.VISIBLE : View.GONE);
        int first = mLinearLayoutManager.findFirstVisibleItemPosition();
        int last = mLinearLayoutManager.findLastVisibleItemPosition();

        if(first > 0 && last > 0 && position == (first + last) / 2){
            holder.mContent.setTextColor(HIGHLIGHT_COLOR);
            holder.mContent.setTextSize(14);
        } else {
            holder.mContent.setTextColor(OTHER_COLOR);
            holder.mContent.setTextSize(14);
        }
        LogUtil.d("LyricAdapter","Position: " + position + " MeasuredHeight: " + getHeightForPosition(position));
        holder.mRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                holder.mRoot.getViewTreeObserver().removeOnPreDrawListener(this);
                LogUtil.d("LyricAdapter","Position: " + position + " ActualHeight: " + holder.mRoot.getHeight());
                return true;
            }
        });
    }

    private static final int PADDING = DensityUtil.dip2px(App.getContext(),16);
    private int getHeightForPosition(final int position){
        if(position >= mDatas.size() || position < 0)
            return 0;
        final LrcRow lrcRow = mDatas.get(position);
        final View itemRoot = LayoutInflater.from(mContext).inflate(R.layout.item_lyric,null);
        final int availWidth = mReclcyerView.getWidth();
        int height = 0;

        StaticLayout contentStaticLayout = new StaticLayout(String.format("%s%s", lrcRow.getContent(), lrcRow.hasTranslate() ? "\n" +  lrcRow.getTranslate() : ""),
                ((TextView)itemRoot.findViewById(R.id.item_content)).getPaint(),
                availWidth, Layout.Alignment.ALIGN_CENTER ,
                1f, 0, true);
        height += contentStaticLayout.getHeight();

//        if(lrcRow.hasTranslate()){
//            StaticLayout translateStaticLayout = new StaticLayout(lrcRow.getContent(), ((TextView)itemRoot.findViewById(R.id.item_translation)).getPaint(), availWidth, Layout.Alignment.ALIGN_CENTER ,
//                    1f, 0, true);
//            height += translateStaticLayout.getHeight();
//        }

//        itemRoot.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        int height = itemRoot.getMeasuredHeight();
//        int width = itemRoot.getMeasuredWidth();
        height += PADDING;
        return height;
    }

    static class LrcViewHolder extends BaseViewHolder{
        @BindView(R.id.item_content)
        TextView mContent;
        @BindView(R.id.item_translation)
        TextView mTranslation;

        public LrcViewHolder(View itemView) {
            super(itemView);
        }
    }
}
