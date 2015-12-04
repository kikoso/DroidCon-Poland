package com.droidcon.snaphack.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.droidcon.snaphack.MainActivity;
import com.droidcon.snaphack.R;
import com.droidcon.snaphack.manager.FileManager;
import com.droidcon.snaphack.model.PhotoItem;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class PhotoListFragment extends Fragment {

    private MainActivity mainActivity;
    private PhotoAdapter adapter;

    private void refresh() {
        List<PhotoItem> items = new FileManager(getActivity()).getAll();
        boolean containsItems = (items.size() > 0);
        emptyText.setVisibility(!containsItems ? View.VISIBLE : View.GONE);
        adapter.setItems(items);
    }

    @InjectView(R.id.grid)
    public GridView gridView;

    @InjectView(R.id.emptyText)
    public TextView emptyText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                new FileManager(getActivity()).deleteAll();
                refresh();
                return true;
            case R.id.action_refresh:
                refresh();
                return true;
            default:
                break;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new PhotoAdapter(getActivity());
        refresh();
        gridView.setAdapter(adapter);
    }

    @OnClick(R.id.photo)
    public void onTakePhoto() {
        mainActivity.takePhoto();
    }

    @OnClick(R.id.photo_encrypted)
    public void onTakePhotoEncrypted() {
        mainActivity.takePhotoEncrypted();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }

}
