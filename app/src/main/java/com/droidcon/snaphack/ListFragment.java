package com.droidcon.snaphack;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ListFragment extends Fragment {

    private MainActivity mainActivity;
    private PhotoAdapter adapter;
    private DropboxManager dropBoxManager;
    private DropboxManagerListener dropboxManagerListener = new DropboxManagerListener() {
        @Override
        public void onFileSystemChanged() {
            refresh();
        }
    };

    private void refresh() {
        adapter.setItems(dropBoxManager.getPhotos());
    }

    @InjectView(R.id.grid)
    public GridView gridView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dropBoxManager = ShApplication.getInstance().getDropboxManager();
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
                dropBoxManager.deleteAll();
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        dropBoxManager.addListener(dropboxManagerListener);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
        dropBoxManager.removeListener(dropboxManagerListener);
    }
}
