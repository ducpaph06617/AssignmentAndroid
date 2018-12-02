package com.dev.ducpaph.assignmentandroid;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {
 private RecyclerView recyclerView;
 private LinearLayoutManager linearLayoutManager;
 private MyAdapter myAdapter;
 private ArrayList<Items> items=new ArrayList<>();
 private ProgressBar progressBar;
 private Button btLoadNews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        recyclerView=findViewById(R.id.recyclerview);
        btLoadNews = findViewById(R.id.btLoadNews);

        progressBar=findViewById(R.id.loading);
        progressBar.setVisibility(View.VISIBLE);
        getDataFromDocBao();
        btLoadNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btLoadNews.setVisibility(View.GONE);
                myAdapter=new MyAdapter(NewsActivity.this,items);
                linearLayoutManager=new LinearLayoutManager(NewsActivity.this,LinearLayoutManager.VERTICAL,false);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(myAdapter);
            }
        });



    }

    public void getDataFromDocBao(){
        GetDataTask getDataTask=new GetDataTask();
        getDataTask.execute();
        progressBar.setVisibility(View.GONE);

    }


    class  GetDataTask extends AsyncTask<String,Long,List<Items>> {

        @Override
        protected void onPostExecute(List<Items> items) {
            super.onPostExecute(items);



        }

        @Override
        protected List<Items> doInBackground(String... strings) {
            String urlx="https://www.epu.edu.vn/rss/tin-tuc-1120.rss";
            ArrayList<Items> newsArrayList = new ArrayList();

            try {
                URL url = new URL(urlx);

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(getInputStream(url), "UTF_8");


                int eventType = xpp.getEventType();
                String text = "";

                Items news = null;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String nameTag = xpp.getName();
                    switch (eventType) {
                        case XmlPullParser.START_TAG:

                            Log.e("Name", xpp.getName());
                            if (nameTag.equalsIgnoreCase("item")) {
                                news = new Items();
                                Log.e("CREATE","NEWS");
                            }
                            break;

                        case XmlPullParser.TEXT:
                            text = xpp.getText();
                            break;

                        case XmlPullParser.END_TAG:
                            if (nameTag.equals("item"))
                                newsArrayList.add(news);
                            else if (news!=null & nameTag.equalsIgnoreCase("title"))
                                news.title = text.trim();
                            else if (news!=null & nameTag.equalsIgnoreCase("description"))
                                news.description = text.trim();
                            else if (news!=null & nameTag.equalsIgnoreCase("pubDate"))
                                news.pubDate = text.trim();
                            else if (news!=null & nameTag.equalsIgnoreCase("link"))
                                news.linkx = text.trim();
                            else if (news!=null & nameTag.equalsIgnoreCase("image"))
                                news.link = text.trim();



                            Log.e("END_TAG " + nameTag, text + "");
                            break;

                        default:
                            break;

                    }
                    eventType = xpp.next(); //move to next element
                }

                Log.e("SIZE", newsArrayList.size() + "");
                items.clear();
                items=newsArrayList;
                Log.e("SIZE", items.size() + "");



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newsArrayList;
        }
    }

    private InputStream getInputStream(URL url) {
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            return null;
        }
    }

    public void webview(String url){
        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.web_layout);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height =WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);
        WebView mWebview ;
        ImageView exit;
        exit=dialog.findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        mWebview  = dialog.findViewById(R.id.webview);
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview .setWebViewClient(new WebViewController());
        mWebview.loadUrl(url);
        dialog.show();
    }

    public class WebViewController extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
