package com.curry.bhk.bhk.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.curry.bhk.bhk.R;
import com.curry.bhk.bhk.activity.BaseActivity;
import com.curry.bhk.bhk.adapter.NewListitemAdapter;
import com.curry.bhk.bhk.bean.EventBean;
import com.curry.bhk.bhk.sqlite.EventdbOperator;

import java.util.List;

/**
 * Created by Curry on 2016/8/17.
 */
public class PendingFragment extends Fragment {
    private List<EventBean> mEventBeanList;
    private SwipeMenuListView mListView;

    private EventdbOperator eventdbOperator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.panding_fragment, null);
        mListView = (SwipeMenuListView) view.findViewById(R.id.pendingListView);
        dataInit();

        addSwipeMenu();

        menuClick();

        return view;
    }

    public void dataInit() {
        TextView tv = (TextView)getActivity().findViewById(R.id.title_bar_name);
        tv.setText("Pending");

        EventBean eventBean = new EventBean();
        eventBean.setResolvedby(BaseActivity.mUsername);

        eventdbOperator = new EventdbOperator(getActivity());
        mEventBeanList = eventdbOperator.queryEvent(4, eventBean);

        NewListitemAdapter newListitemAdapter = new NewListitemAdapter(getActivity(), mEventBeanList);
        mListView.setAdapter(newListitemAdapter);

        newListitemAdapter.notifyDataSetChanged();
    }

    public void addSwipeMenu() {

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {


                SwipeMenuItem item = new SwipeMenuItem(getActivity());

                item.setBackground(new ColorDrawable(Color.rgb(162, 162, 162)));

                item.setTitle("On Hold");

                item.setTitleColor(Color.WHITE);

                item.setTitleSize(18);

                item.setWidth(dpToPx(90));

                menu.addMenuItem(item);

                SwipeMenuItem item1 = new SwipeMenuItem(getActivity());

                item1.setBackground(new ColorDrawable(Color.rgb(190, 60, 58)));

//				item1.setIcon(R.drawable.ic_launcher);

                item1.setTitle("Resolved");

                item1.setTitleColor(Color.WHITE);

                item1.setTitleSize(18);

                item1.setWidth(dpToPx(90));

                menu.addMenuItem(item1);

            }
        };

        mListView.setMenuCreator(creator);
    }

    /**
     *  swipemenu on hold & resolved click event
     */
    private void menuClick() {
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (index) {
                    case 0://on hold
                        EventBean eventBeanHold = new EventBean();
                        eventBeanHold.setId(mEventBeanList.get(position).getId());
                        eventBeanHold.setStatus(2);

                        eventdbOperator.updateEvent(eventBeanHold, 1);

                        OnHoldFragment onHoldFragment = new OnHoldFragment();
                        fragmentTransaction.replace(R.id.fragment, onHoldFragment);

                        break;
                    case 1://resolved
                        EventBean eventBeanResolved = new EventBean();
                        eventBeanResolved.setId(mEventBeanList.get(position).getId());
                        eventBeanResolved.setStatus(3);

                        eventdbOperator.updateEvent(eventBeanResolved, 1);
                        ResolvedFragment resolvedFragment = new ResolvedFragment();
                        fragmentTransaction.replace(R.id.fragment, resolvedFragment);
                        break;
                    default:
                        break;
                }
                fragmentTransaction.commit();
                return false;
            }
        });

    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
