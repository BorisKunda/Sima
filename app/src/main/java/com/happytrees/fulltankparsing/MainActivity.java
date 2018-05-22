package com.happytrees.fulltankparsing;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


//HOVOT :
//onNullInstance
//remove keyboard after search
//one adapter for multiple adapters

public class MainActivity extends AppCompatActivity {

    private final static String START_STRING = " https://www.fulltank.co.il/?s= ";
    private final static String END_STRING = "&latitude=undefined&longitude=undefined&sort=cheapest";
    public ArrayList<Station> allStations = new ArrayList<>();
    public RecyclerView myRecycler;
    public EditText cityET;
    public Button goBtn;
    public ProgressDialog progressDialog;


    //creating object Station and assigning its string variable value as myElement.ownText()
    //    Station station = new Station(myElement.ownText(),1,2,3);
    /// /    allStations.add(station);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        goBtn = findViewById(R.id.GoButton);
        cityET = findViewById(R.id.cityET);


//https://www.fulltank.co.il/?s=jerusalem&latitude=31.8055944&longitude=35.2298522&sort=cheapest


        myRecycler = findViewById(R.id.MyRecyclerView);
        myRecycler.setLayoutManager(new LinearLayoutManager(this));


        //PROGRESS BAR
        // Set up progress before call
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Its loading....");
        progressDialog.setTitle("ProgressDialog bar ");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        final MyAdapter myAdapter = new MyAdapter(allStations, MainActivity.this);
        myRecycler.setAdapter(myAdapter);
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cityET.length() != 0) {
                    //opening new thread for using network
                    progressDialog.show();//SHOW PROGRESS BAR BEFORE CALL
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String city = cityET.getText().toString();
                            String cityImproved = city.replace(" ", "%20");
                            String fullUrl = START_STRING + cityImproved + END_STRING;

                            //downloading html and keeping it under "line" variable
                            InputStream is = null;
                            try {
                                URL url = new URL(fullUrl);
                                is = url.openStream();  // throws an IOException
                                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                                String currentLine;
                                String line = " ";
                                //while loop stops when currentLine becomes null ,so we keep whole growing String  under line variable
                                while ((currentLine = br.readLine()) != null) {
                                    line += currentLine;
                                }
                                //parsing html
                                Document parsedDocument = Jsoup.parse(line);
                                //selecting names from  html -->    <h2><a href="https://www.fulltank.co.il/station/296/דלק/זטלר">NAME</a></h2>
                                //NAMES
                                Elements myElements = parsedDocument.select("h2 > a");
                                ArrayList<String> names = new ArrayList<>();
                                for (Element myElement : myElements) {
                                    //getting station's name
                                    String name = myElement.ownText();
                                    // Log.e("name","name :  " + name);
                                    //keeping all names under array of names
                                    names.add(name);
                                }
                                //PRICES
                                //<div class=""><span class="search-data-num">6.37</span> ₪</div>
                                //selecting prices from html
                                Elements pricesElements = parsedDocument.select("span.search-data-num");
                                ArrayList<String> prices = new ArrayList<>();
                                for (Element priceElement : pricesElements) {
                                    String price = priceElement.ownText();
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
                                ArrayList<String> urlImgs = new ArrayList<>();
                                for (Element urlElement : urlImages) {
                                    //getting value  --> ("src") <-- <img src="https://maps.googleapis.com/maps/api/streetview?size=260x150&location=31.749428,35.206287" >
                                    String urlImg = urlElement.attr("src");
                                    urlImgs.add(urlImg);
                                }

                                //delete old results if exist
                                if (allStations != null) {
                                    allStations.clear();
                                }

                                //LOOP OF CREATING OBJECTS
                                for (int i = 0; i < names.size(); i++) {
                                    Station station = new Station(names.get(i), prices.get(i * 3), prices.get((i * 3) + 1), prices.get((i * 3) + 2), urlImgs.get(i));
                                    allStations.add(station);
                                }

                            } catch (MalformedURLException mue) {
                                mue.printStackTrace();
                            } catch (IOException ioe) {
                                ioe.printStackTrace();
                            } finally {
                                try {
                                    if (is != null)
                                        is.close();
                                } catch (IOException ioe) {
                                }

                            }

                            //post to UI (main thread) through post method .without post method there will be CalledFromWrongThreadException
                            myRecycler.post(new Runnable() {//alternatively use  runOnUiThread();
                                @Override
                                public void run() {
                                    myAdapter.notifyDataSetChanged();
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

