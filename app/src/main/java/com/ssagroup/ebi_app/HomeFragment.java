package com.ssagroup.ebi_app;

/**
 * Created by User on 8/3/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ssagroup.ebi_app.models.Course;
import com.ssagroup.ebi_app.services.DownloadImageService;
import com.ssagroup.ebi_app.services.JSONServiceHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class HomeFragment extends Fragment {
    private List<Course> course;
    private Integer limit = 8;
    private Integer offset = 0;

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private final int[] pastVisibleItems = new int[1];
    private final int[] visibleItemCount = new int[1];
    private final int[] totalItemCount = new int[1];
    private boolean loading = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        course = new ArrayList<>();
        course = fillWithCourseData(offset);

        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(course, recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            //public interface LoadMoreItems { void LoadItems(); }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!loading && !recyclerView.canScrollVertically(1)) {
                    loading = true;
                    // Scrolled to bottom
                    offset += limit;
                    List<Course> moreCourses = fillWithCourseData(offset);
                    if (null != moreCourses) {
                        course.addAll(moreCourses);
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
//                    recyclerView.setAdapter(new RecyclerViewAdapter(course, recyclerView.getContext()));

                } else {
                    loading = false;
                }
//                else if (dy > 0) {
//                    // Scrolled down
//                    visibleItemCount[0] = mLayoutManager.getChildCount();
//                    totalItemCount[0] = recyclerView.getAdapter().getItemCount();
//                    pastVisibleItems[0] = mLayoutManager.findFirstVisibleItemPosition();
//                    //Log.v("StringTOTAL", String.valueOf(pastVisibleItems[0]));
//                    if (loading) {
//                        if ((visibleItemCount[0] + pastVisibleItems[0]) >= totalItemCount[0]) {
//                            loading = false;
//                            Log.v("String", "last VIew");
//                        }
//                    }
//                }
            }
        });

        // padding tiles
        int tilePadding = getResources().getDimensionPixelSize(R.dimen.tile_padding);
        recyclerView.setPadding(tilePadding, tilePadding, tilePadding, tilePadding);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        return recyclerView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView picture;
        public TextView name;
        private CardView cv;
        TextView id;
        TextView title;
        TextView description;
        ImageView thumbnail;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.home, parent, false));
            picture = (ImageView) itemView.findViewById(R.id.tile_picture);
            name = (TextView) itemView.findViewById(R.id.tile_title);
            id = (TextView) itemView.findViewById(R.id.hidden_title);
        }

        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)  itemView.findViewById(R.id.card_view);
            title = (TextView) itemView.findViewById(R.id.tile_title);
            description = (TextView) itemView.findViewById(R.id.tile_desc);
            thumbnail = (ImageView) itemView.findViewById(R.id.tile_picture);
            id = (TextView) itemView.findViewById(R.id.hidden_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(DetailActivity.EXTRA_POSITION, getAdapterPosition());
                    intent.putExtra("hidden_title", id.getText());
                    context.startActivity(intent);
                }
            });
        }
    }

    public static class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

        private static final int LENGTH = 18;
        private final String[] mPlaces;
        private final Drawable[] mPlacePictures;
        private List<Course> course = Collections.emptyList();
        Context context;

        public RecyclerViewAdapter(List<Course> course, Context context) {
            this.course = course;
            this.context = context;

            Resources resources = context.getResources();
            mPlaces = resources.getStringArray(R.array.course_title);
            TypedArray a = resources.obtainTypedArray(R.array.places_picture);
            mPlacePictures = new Drawable[a.length()];
            for (int i = 0; i < mPlacePictures.length; i++) {
                mPlacePictures[i] = a.getDrawable(i);
            }
            a.recycle();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home, parent, false);
            ViewHolder holder = new ViewHolder(v);
            return holder;
            //return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.id.setText(course.get(position).id);
            holder.title.setText(course.get(position).title);
            holder.description.setText(course.get(position).description);
            holder.thumbnail.setImageBitmap(course.get(position).thumbnail);
        }

        @Override
        public int getItemCount() {
            return course.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }
    }

    public List<Course> fillWithCourseData(Integer offset) {
        if (null == offset) offset = this.offset;
        List<Course> myCourse = new ArrayList<>();
        try {
            String results = new JSONServiceHandler(getActivity()).execute(
                    "http://training.ssagroup.com/_test/Yggdrasil/public/api/courses/all.json?limit="+limit+"&skip="+offset).get();
            if (null != results) {
                JSONArray reader = new JSONArray(results);
                for (int i = 0; i < reader.length(); i++) {
                    JSONObject jObject = reader.getJSONObject(i);
                    Bitmap bitmap = new DownloadImageService().execute(jObject.getString("thumbnail")).get();
                    Course c = new Course(
                            jObject.getString("id"),
                            jObject.getString("name"),
                            bitmap);

                    myCourse.add(c);
                }

            } else {
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return myCourse;
    }
}
