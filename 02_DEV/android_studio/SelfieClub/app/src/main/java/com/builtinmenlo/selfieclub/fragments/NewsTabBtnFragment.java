/**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*._
 /**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~,
 *
 * @class NewsTabBtnFragment
 * @project SelfieClub
 * @package com.builtinmenlo.selfieclub.fragments
 *
 * @author Matt.H <matt@builtinmenlo.com>
 * @created 16-Apr-2014 @ 16:11
 * @copyright (c) 2014 Built in Menlo, LLC. All rights reserved.
 *
/**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~·'
/**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~**/


package com.builtinmenlo.selfieclub.fragments;


//] includes [!]>
//]=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~.

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.builtinmenlo.selfieclub.R;
import com.builtinmenlo.selfieclub.dataSources.NewsItem;
import com.builtinmenlo.selfieclub.models.ClubManager;
import com.builtinmenlo.selfieclub.models.NewsFeedProtocol;
import com.builtinmenlo.selfieclub.util.ImageDownloader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

;
//]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[


// <[!] class delaration [¡]>
public class  NewsTabBtnFragment extends Fragment implements NewsFeedProtocol {
//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._

    //] class properties ]>
    ImageDownloader downloader;
    ArrayList<NewsItem> news;
    ImageView[] listImages;
    public ListView lv;
    private MyCustomAdapter adapter;
    private ProgressBar loadingIcon;
    //]=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~.
    //]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[

    // <*] class constructor [*>
    public NewsTabBtnFragment() {
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_tab, container, false);
        lv = (ListView) view.findViewById(android.R.id.list);
        downloader = new ImageDownloader(NewsTabBtnFragment.this.getActivity());
        news = new ArrayList<NewsItem>();
        populate();

        ClubManager clubManager = new ClubManager();
        clubManager.requestNews(this, "131820");

        return view;
    }//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯


    public void onCreate(Bundle savedInstanceState) {
        //]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
        super.onCreate(savedInstanceState);
    }//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯

    public void onAttach(Activity activity) {
        //]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
        super.onAttach(activity);
    }//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯

    public void onDetach() {
        //]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
        super.onDetach();
    }

    private static class ViewHolder {
        TextView lblStatus;
        TextView lblTime;
        TextView lblClubName;
        //ImageView imgNews;
        ImageView imgAvatar;
        //Button btnUpVote;
        //Button btnReply;
    }

    public void populate() {
        if (lv != null) {
            adapter = new MyCustomAdapter(getActivity().getBaseContext(), news);
            lv.post(new Runnable() {
                public void run() {
                    lv.setAdapter(adapter);
                }
            });

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    //arg1.setBackgroundColor(Color.TRANSPARENT);
                }
            });
        }
    }

    private class MyCustomAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;
        private ArrayList<NewsItem> listData;

        public MyCustomAdapter(Context context, ArrayList<NewsItem> listData) {
            this.listData = listData;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            if (convertView == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.news_feed_item, parent, false);

                viewHolder = new ViewHolder();
                viewHolder.lblClubName = (TextView) convertView.findViewById(R.id.lblClubName);
                viewHolder.imgAvatar = (ImageView) convertView.findViewById(R.id.imgAvatar);
                viewHolder.lblStatus = (TextView) convertView.findViewById(R.id.lblStatus);
                //viewHolder.imgNews = (ImageView) convertView.findViewById(R.id.imgNews);
                viewHolder.lblTime = (TextView) convertView.findViewById(R.id.lblTime);
                //viewHolder.btnReply = (Button) convertView.findViewById(R.id.btnReply);
                //viewHolder.btnUpVote = (Button) convertView.findViewById(R.id.btnUpVote);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            NewsItem newsItem = news.get(position);

            // viewHolder.lblFollowers.setText(String.valueOf(friend.getFollowers()));
            String status = "<strong>" + newsItem.getUserName() + "</strong>";
            /*String status = "<strong>" + newsItem.getUserName() + "</strong> is feeling <strong>";
            for (int i = 0; i < newsItem.getStatus().length(); i++) {
                try {
                    if (newsItem.getStatus().length() > 1 && i + 1 == newsItem.getStatus().length())
                        status = status.substring(0, status.length() - 2) + " & ";
                    status += newsItem.getStatus().getString(i);
                    if (i + 1 < newsItem.getStatus().length())
                        status += ", ";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            status += "...</strong>";*/
            viewHolder.lblStatus.setText(Html.fromHtml(status));
            SimpleDateFormat from_format = new SimpleDateFormat("yyyy-MM-d HH:mm:ss");
            Date rowDate = null;
            try {
                rowDate = from_format.parse(newsItem.getTimestamp());
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(rowDate);

            long time = (((Calendar.getInstance().getTimeInMillis() - calendar.getTimeInMillis()) / 1000) % 3600) / 60;


            viewHolder.lblTime.setText(String.valueOf(time) + " m");
            viewHolder.lblClubName.setText(" in "+newsItem.getClubName());

            /*if (viewHolder.imgAvatar != null) {
                new ImageDownloaderTask(viewHolder.imgAvatar).execute(newsItem.getAvatarUrl());
            }*/

            if (viewHolder.imgAvatar != null) {
                //viewHolder.imgNews.setImageBitmap(((BitmapDrawable)listImages[position].getDrawable()).getBitmap());
                //new ImageDownloaderTask(viewHolder.imgNews).execute(newsItem.getImageUrl()+"Tab_640x960.jpg");
                downloader.DisplayImage(newsItem.getImageUrl() + "Tab_640x960.jpg", String.valueOf(position), getActivity(), viewHolder.imgAvatar);

            }

            return convertView;
        }
    }

    class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

        public ImageDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        // Actual download method, run in the task thread
        protected Bitmap doInBackground(String... params) {
            // params comes from the execute() call: params[0] is the url.
            return downloadBitmap(params[0]);
        }

        @Override
        // Once the image is downloaded, associates it to the imageView
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {

                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        imageView.setImageDrawable(imageView.getContext().getResources()
                                .getDrawable(R.drawable.friends_item_image_mask));
                    }
                }

            }
        }


    }

    Bitmap downloadBitmap(String url) {
        final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
        final HttpGet getRequest = new HttpGet(url);
        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownloader", "Error " + statusCode
                        + " while retrieving bitmap from " + url);
                return null;
            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    inputStream = entity.getContent();
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            // Could provide a more explicit error message for IOException or
            // IllegalStateException
            getRequest.abort();
            Log.w("ImageDownloader", "Error while retrieving bitmap from " + url);
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return null;
    }

    public void didReceiveNewsFeed(ArrayList<NewsItem> newsItemArrayList) {
        news = newsItemArrayList;
        listImages = new ImageView[newsItemArrayList.size()];
        for (int i = 0; i < newsItemArrayList.size(); i++) {
            //new ImageDownloaderTask(new ImageView(this.getActivity())).execute(newsItemArrayList.get(i).getImageUrl()+"Tab_640x960.jpg");
            //listImages[i] = downloadBitmap(newsItemArrayList.get(i).getImageUrl()+"Tab_640x960.jpg");
        }
        adapter.notifyDataSetChanged();

    }

    public void didReceiveNewsFeedError(String errorMessage) {

    }
}
