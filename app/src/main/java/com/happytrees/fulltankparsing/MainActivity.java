package com.happytrees.fulltankparsing;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.happytrees.fulltankparsing.Adapter.MyAdapter;
import com.happytrees.fulltankparsing.Objects.Station;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


//HOVOT :
//onNullInstance

public class MainActivity extends AppCompatActivity {

    public String line;
    public BufferedReader br;
    public URL url;
    public InputStream is = null;
    public Element divEl;
    public ArrayList<Station> allStations = new ArrayList<>();
    public RecyclerView myRecycler;
    public String city;
    public EditText cityET;
    public String startString = " https://www.fulltank.co.il/?s= ";
    public String endString = "&latitude=undefined&longitude=undefined";
    public String fullUrl;
    public Button goBtn;
    public String cityImproved;
    public ProgressDialog progressDialog;
    public String name;
    public String price;
    public String urlImg;
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> prices = new ArrayList<>();
    ArrayList<String> urlImgs = new ArrayList<>();
    public String newLine;


    //creating object Station and assigning its string variable value as myElement.ownText()
    //    Station station = new Station(myElement.ownText(),1,2,3);
    /// /    allStations.add(station);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        goBtn = (Button) findViewById(R.id.GoButton);
        cityET = (EditText) findViewById(R.id.cityET);


//https://www.fulltank.co.il/?s=jerusalem&latitude=31.8055944&longitude=35.2298522&sort=cheapest


        myRecycler = (RecyclerView) findViewById(R.id.MyRecyclerView);
        myRecycler.setLayoutManager(new LinearLayoutManager(this));


        //PROGRESS BAR
        // Set up progress before call
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Its loading....");
        progressDialog.setTitle("ProgressDialog bar ");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cityET.length() != 0) {
                    //opening new thread for using network
                    progressDialog.show();//SHOW PROGRESS BAR BEFORE CALL
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            city = cityET.getText().toString();
                            cityImproved = city.replace(" ", "%20");
                            fullUrl = startString + cityImproved + endString;
                            //downloading html and keeping it under "line" variable
                            try {
                                url = new URL(fullUrl);
                                is = url.openStream();  // throws an IOException
                                br = new BufferedReader(new InputStreamReader(is));
                                String currentLine;
                                line = " ";
                                //while loop stops when currentLine becomes null ,so we keep whole growing String  under line variable
                                while ((currentLine = br.readLine()) != null) {
                                    line += currentLine;
                                }
                                //parsing html
                                Document parsedDocument = Jsoup.parse(line);
                                //selecting names from  html -->    <h2><a href="https://www.fulltank.co.il/station/296/דלק/זטלר">NAME</a></h2>
                                //NAMES
                                Elements myElements = parsedDocument.select("h2 > a");
                                for (Element myElement : myElements) {
                                    //getting station's name
                                    name = myElement.ownText();
                                    // Log.e("name","name :  " + name);
                                    //keeping all names under array of names
                                    names.add(name);
                                }
                                //PRICES
                                //<div class=""><span class="search-data-num">6.37</span> ₪</div>
                                //selecting prices from html
                                Elements pricesElements = parsedDocument.select("span.search-data-num");
                                for (Element priceElement : pricesElements) {
                                    price = priceElement.ownText();
                                    //keeping all prices under array of prices
                                    prices.add(price);
                                }
                                //IMAGES
                                /*
                                  <figure class="search-figure">
                                           <a href="https://www.fulltank.co.il/station/415/Ten/ירושלים%20תלפיות"><img src="https://www.fulltank.co.il/wp-content/uploads/2015/08/TenPT@2x.png" width="267" height="193"></a>
                                 </figure>

                                 */
                                Elements urlImages = parsedDocument.select("figure.search-figure>a>img");
                                for (Element urlElement : urlImages) {
                                    //getting value  --> ("src") <-- <img src="https://maps.googleapis.com/maps/api/streetview?size=260x150&location=31.749428,35.206287" >
                                    urlImg = urlElement.attr("src");
                                    urlImgs.add(urlImg);
                                }
                                Log.e("size", "size" + allStations.size());
                            } catch (MalformedURLException mue) {
                                mue.printStackTrace();
                            } catch (IOException ioe) {
                                ioe.printStackTrace();
                            } finally {
                                try {
                                    if (is != null) is.close();
                                } catch (IOException ioe) {
                                }

                            }
                            //post to UI (main thread) through post method .without post method there will be CalledFromWrongThreadException
                            myRecycler.post(new Runnable() {//alternatively use  runOnUiThread();
                                @Override
                                public void run() {
                                    if (allStations != null) {
                                        allStations.clear();
                                    }
                                    //LOOP OF CREATING OBJECTS
                                    for (int i = 0; i < names.size(); i++) {
                                        Station station = new Station(names.get(i), prices.get(i * 3), prices.get((i * 3) + 1), prices.get((i * 3) + 2), urlImgs.get(i));
                                        allStations.add(station);
                                    }
                                    MyAdapter myAdapter = new MyAdapter(allStations, MainActivity.this);
                                    myAdapter.notifyDataSetChanged();
                                    myRecycler.setAdapter(myAdapter);//In order to display items in the list, call setAdapter(ListAdapter) to associate an adapter with the list.
                                    progressDialog.dismiss();//dismiss progress bar after call was completed
                                    cityET.setText(" ");
                                }
                            });
                        }
                    }).start();

                } else {
                    Toast.makeText(MainActivity.this, "write something", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}


//Runnable ->  we create a new Thread to download the content of the website.
// connect() ->  connects the application to the website
// get() ->  method to download the content. These calls return a Document object instance.
//element.ownText() -> gets element's content
//Jsoup.parse -> when we are dealing with raw string html text
//Jsoup.connect -> when we are dealing with url HTML link


/*
     final List<Location> allLocations = Location.listAll(Location.class);

        RecyclerView recyclerView = v.findViewById(R.id.locationRecyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());//layout manager defines look of RecyclerView -- > grid,list
        recyclerView.setLayoutManager(layoutManager);

        //adapter
        final LocationAdapter locationAdapter = new LocationAdapter(allLocations, getActivity());

        recyclerView.setAdapter(locationAdapter);

 */