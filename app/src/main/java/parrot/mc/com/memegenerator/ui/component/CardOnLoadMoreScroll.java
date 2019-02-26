package parrot.mc.com.memegenerator.ui.component;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

public class CardOnLoadMoreScroll extends RecyclerView.OnScrollListener {

    private int visibleThreshold = 5;
    private CardOnLoadMoreListener cardOnLoadMoreListener;
    private boolean isLoading;
    private int lastVisibleItem, totalItemCount;
    private RecyclerView.LayoutManager layoutManager;

    public void setLoaded() {
        isLoading = false;
    }

    public boolean getLoaded() {
        return isLoading;
    }

    public void setOnLoadMoreListener(CardOnLoadMoreListener mOnLoadMoreListener) {
        this.cardOnLoadMoreListener = mOnLoadMoreListener;
    }

    public CardOnLoadMoreScroll(GridLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (dy <= 0) return;

        totalItemCount = layoutManager.getItemCount();

        if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null);
            lastVisibleItem = getLastVisibleItem(lastVisibleItemPositions);
        } else if (layoutManager instanceof GridLayoutManager) {
            lastVisibleItem = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof LinearLayoutManager) {
            lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        }

        if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
            if (cardOnLoadMoreListener != null) {
                cardOnLoadMoreListener.onLoadMore();
            }
            isLoading = true;
        }

    }

    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            }
            else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }
}
