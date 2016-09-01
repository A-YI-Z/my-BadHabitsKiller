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

import com.curry.bhk.bhk.R;
import com.curry.bhk.bhk.activity.BaseActivity;
import com.curry.bhk.bhk.activity.MainActivity;
import com.curry.bhk.bhk.adapter.MenuAdapter;
import com.curry.bhk.bhk.adapter.NewListitemAdapter;
import com.curry.bhk.bhk.bean.EventBean;
import com.curry.bhk.bhk.sqlite.EventdbOperator;
import com.curry.bhk.bhk.swipemenu.SwipeMenu;
import com.curry.bhk.bhk.swipemenu.SwipeMenuCreator;
import com.curry.bhk.bhk.swipemenu.SwipeMenuItem;
import com.curry.bhk.bhk.swipemenu.SwipeMenuListView;

import java.util.List;

/**
 * Created by Curry on 2016/8/17.
 */
public class PendingFragment extends Fragment {
    private List<EventBean> mEventBeanList;
    private SwipeMenuListView mListView;
    private TextView mNullTextView;
    private EventdbOperator eventdbOperator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.panding_fragment, null);
        mListView = (SwipeMenuListView) view.findViewById(R.id.pendingListView);
        mNullTextView = (TextView) view.findViewById(R.id.pending_null_textview);
        dataInit();

        addSwipeMenu();

        menuClick();

        return view;
    }

    public void dataInit() {
        //notify the menu refresh if replace this fragment
        MenuAdapter.defItem=1;
        MainActivity.myMenuAdapter.notifyDataSetChanged();

        EventBean eventBean = new EventBean();
        eventBean.setResolvedby(BaseActivity.mEmail);
        eventBean.setStatus(1);

        eventdbOperator = new EventdbOperator(getActivity());
        mEventBeanList = eventdbOperator.queryEvent(4, eventBean);
        if (mEventBeanList.size() == 0) {
            mNullTextView.setVisibility(View.VISIBLE);
        } else {
            NewListitemAdapter newListitemAdapter = new NewListitemAdapter(getActivity(), mEventBeanList);
            mListView.setAdapter(newListitemAdapter);
            newListitemAdapter.notifyDataSetChanged();
        }


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
     * swipemenu on hold & resolved click event
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
