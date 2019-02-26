package parrot.mc.com.memegenerator.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import parrot.mc.com.memegenerator.R;
import parrot.mc.com.memegenerator.manager.ApiManager;
import parrot.mc.com.memegenerator.manager.Utils;
import parrot.mc.com.memegenerator.manager.VolleyManager;
import parrot.mc.com.memegenerator.model.IMemeCardModelObserver;
import parrot.mc.com.memegenerator.model.MemeCardModel;
import parrot.mc.com.memegenerator.model.data.MemeEntity;
import parrot.mc.com.memegenerator.ui.activity.EditMemeActivity;
import parrot.mc.com.memegenerator.ui.adapter.MemeCardAdapter;
import parrot.mc.com.memegenerator.ui.component.CardItemSpacing;
import parrot.mc.com.memegenerator.ui.component.CardOnClickListener;
import parrot.mc.com.memegenerator.ui.component.CardOnLoadMoreListener;
import parrot.mc.com.memegenerator.ui.component.CardOnLoadMoreScroll;
import parrot.mc.com.memegenerator.utils.Constant;

public class MemeCardFragment extends Fragment implements IMemeCardModelObserver {

    private String page;
    private RecyclerView recyclerView;
    private FrameLayout loadingLay;
    private FrameLayout reloadLay;
    private Button reloadBtn;
    private MemeCardAdapter memeAdapter;
    private List<MemeEntity> memeEntities;
    private MemeCardModel memeModel;
    boolean isLoading = false;
    boolean isLoadMore = false;
    boolean isReload = false;
    private CardOnLoadMoreScroll scrollListener;
    private GridLayoutManager gridLayoutManager;

    private OnFragmentInteractionListener mListener;

    public MemeCardFragment() {
        memeEntities = new ArrayList<>();
    }

    public static MemeCardFragment newInstance(String param1) {
        MemeCardFragment fragment = new MemeCardFragment();
        Bundle args = new Bundle();
        args.putSerializable("page_key", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            page = getArguments().getString("page_key");
            memeModel = new MemeCardModel(getContext());
            memeModel.addObserver(this);
            switch (page) {
                case "New":
                    memeModel.getImageByNew();
                    break;
                case "Trending":
                    memeModel.getImageByTrending();
                    break;
                case "Popular":
                    memeModel.getImageByPopular();
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memes_card, container, false);
        initView(view);
        setEvent();
        return view;
    }

    private void initView(View view){
        recyclerView =  view.findViewById(R.id.recycler_view);
        loadingLay = view.findViewById(R.id.loading_view);
        reloadLay = view.findViewById(R.id.reload_container);
        reloadBtn = view.findViewById(R.id.reload_btn);
        reloadLay.setVisibility(View.GONE);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch(memeAdapter.getItemViewType(position)) {
                    case Constant.VIEW_ITEM:
                        return 1;
                    case Constant.VIEW_LOADING:
                        return 2;
                    default:
                        return -1;
                }
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new CardItemSpacing(2, Utils.getInstance().dpToPx(getContext(),10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSuccess(List<MemeEntity> memeEntities) {
        this.memeEntities.addAll(memeEntities);
        loadingLay.setVisibility(View.GONE);
        memeAdapter.notifyDataSetChanged();
        if (isLoadMore || isReload){
            memeAdapter.removeLoadingView();
            scrollListener.setLoaded();
            isLoadMore = false;
            isReload = false;
        }
    }

    @Override
    public void onError(VolleyManager.ErrorState errorMessage) {
        loadingLay.setVisibility(View.GONE);
        reloadLay.setVisibility(View.VISIBLE);
        memeAdapter.removeLoadingView();
        if (errorMessage.equals(VolleyManager.ErrorState.API_KEY))
            Utils.getInstance().makeToast(getContext(),"Please get an API Key");
        else
            Utils.getInstance().makeToast(getContext(),"Please check your internet");
    }

    private void setEvent(){
        memeAdapter = new MemeCardAdapter(memeEntities, new CardOnClickListener() {
            @Override
            public void onClick(MemeEntity memeEntity) {
                Intent intent = new Intent(getActivity(), EditMemeActivity.class);
                intent.putExtra("image_url", memeEntity.getImageUrl());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(memeAdapter);
        scrollListener=new CardOnLoadMoreScroll(gridLayoutManager);
        scrollListener.setOnLoadMoreListener(new CardOnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                isLoadMore = true;
                loadMore();
            }
        });
        recyclerView.addOnScrollListener(scrollListener);
        reloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isReload = true;
                loadMore();
            }
        });
    }

    private void loadMore() {
        reloadLay.setVisibility(View.GONE);
        switch (page) {
            case "Trending":
                if (isReload){
                    memeAdapter.addLoadingView();
                    memeModel.getImageByTrending();
                }
                break;
            case "New":
                memeAdapter.addLoadingView();
                memeModel.getImageByNew();
                break;

            case "Popular":
                memeAdapter.addLoadingView();
                memeModel.getImageByPopular();
                break;

            default:
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
