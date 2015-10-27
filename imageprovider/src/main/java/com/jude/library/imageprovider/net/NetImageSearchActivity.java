package com.jude.library.imageprovider.net;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.library.R;
import com.jude.library.imageprovider.ImageProvider;
import com.jude.library.imageprovider.Utils;
import com.jude.library.imageprovider.net.searchers.SosoSearcher;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class NetImageSearchActivity extends AppCompatActivity {

    private RecyclerView recycleview;
    private ImageListAdapter adapter;
    private SearchView searchview;
    private String searchText;
    private GridView mGridView;

    private SearcherConstructor seacher = new SosoSearcher();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment);
        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_return_black);
        setSupportActionBar(toolbar);
        recycleview = (RecyclerView) findViewById(R.id.recyclerview);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recycleview.setLayoutManager(manager);
        adapter = new ImageListAdapter();
        recycleview.setAdapter(adapter);
        mGridView = (GridView) findViewById(R.id.grid);
        mGridView.setAdapter(new GridViewAdapter());
        mGridView.setFocusableInTouchMode(true);
        mGridView.setFocusable(true);
        mGridView.requestFocus();
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                searchview.setQuery(((TextView)view).getText().toString(), true);
                Utils.closeInputMethod(NetImageSearchActivity.this);
            }
        });
        searchview = (SearchView)findViewById(R.id.searchview);
        searchview.setIconified(false);
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchText = query;
                getImageList(searchText, 0);
                recycleview.scrollToPosition(0);
                adapter.clear();
                recycleview.setVisibility(View.VISIBLE);
                mGridView.setVisibility(View.INVISIBLE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
                    recycleview.setVisibility(View.INVISIBLE);
                    mGridView.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
	}


    private void getImageList(final String word, final int page){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String response = null;
                try {
                    response = Utils.sendGet(seacher.getUrl(word, page), "");
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NetImageSearchActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }

                final NetImage[] images = seacher.getImageList(response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addList(images);
                    }
                });
            }
        }).start();
    }


    class ImageListAdapter extends RecyclerView.Adapter<ImageViewHolder>{
        private int page = 0;
        private ArrayList<NetImage> arr = new ArrayList<>();

        public void addList(NetImage[] imgs){
            if (imgs!=null){
                arr.addAll(Arrays.asList(imgs));
                notifyDataSetChanged();
            }
            page++;
        }

        public void clear(){
            arr.clear();
            notifyDataSetChanged();
            page=0;
        }

        @Override
        public int getItemCount() {
            return arr.size();
        }

        @Override
        public void onBindViewHolder(final ImageViewHolder arg0, int arg1) {



            if (arr.get(arg1).getHeight()!=0 && arr.get(arg1).getWidth()!=0){
                int width = Utils.getScreenWidth()/2;
                int height = width*arr.get(arg1).getHeight()/arr.get(arg1).getWidth();
                arg0.img.setLayoutParams(new FrameLayout.LayoutParams(width,height));

                String url = arr.get(arg1).getSmallImage();
                Picasso.with(NetImageSearchActivity.this)
                        .load(url)
                        .error(R.drawable.default_error)
                        .resize(arr.get(arg1).getWidth(),arr.get(arg1).getHeight())
                        .into(arg0.img);
            }




            //加载下一页
            if(arg1==arr.size()-3){
                getImageList(searchText,page+1);
            }
            arg0.imageModel = arr.get(arg1);
        }

        @Override
        public ImageViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
            ImageView img = new ImageView(arg0.getContext());
            img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
            img.setPadding(Utils.dip2px(4), Utils.dip2px(4), Utils.dip2px(4), Utils.dip2px(4));
            img.setId(R.id.image);
            FrameLayout layout = new FrameLayout(arg0.getContext());
            layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layout.addView(img);
            return new ImageViewHolder(layout);
        }
    }

    class ImageViewHolder extends RecyclerView.ViewHolder{
        public NetImage imageModel;
        public ImageView img;

        public ImageViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.image);
            img.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    setResult(Activity.RESULT_OK, intent);
                    intent.putExtra("data", imageModel.getLargeImage());
                    finish();
                }
            });
        }

    }

    class GridViewAdapter extends BaseAdapter {



        @Override
        public int getCount() {
            return ImageProvider.mRecommendList.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = new TextView(NetImageSearchActivity.this);
            view.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_bright));
            view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dip2px(48)));
            view.setGravity(Gravity.CENTER);
            view.setTextColor(getResources().getColor(android.R.color.white));
            view.setText(ImageProvider.mRecommendList[position]);
            return view;
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}


