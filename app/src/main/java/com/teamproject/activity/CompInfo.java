package com.teamproject.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.teamproject.conn.TurningOnGPS;
import com.teamproject.functions.DialogCommunications;
import com.teamproject.functions.RestController;
import com.teamproject.functions.TimeValidation;
import com.teamproject.models.competitionDTO;
import com.teamproject.models.userDTO;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.ArrayList;


public class CompInfo extends Activity {
	private Button button2, button1;
	final Context context = this;
	private TextView datarozTV, nazwaTV, miejscowoscTV, typTV, godzrozpTV, datazakTV, godzzakTV, limitTV, oplataTV, opisTV, katTV, kat;
	Intent intent4 = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	TurningOnGPS gpssx;
	String error, error1, ret, ret1, whichList1, ID, idX, kategoria, ktore_zawody= "";
	DialogCommunications comm = new DialogCommunications(context);
	ImageView typIV;
	Spinner mSpinner;
	final userDTO user1 = Login.user;
	String ID_usera = user1.getID_uzytkownika();
	TimeValidation tv = new TimeValidation();
	boolean flaga1, flaga2, mappc, mapoi, matrase, mozezapisac, focus;
	int inn, flow, row;
	Spanned spanned;
	Intent intent, intent1, intent2, intent3, intent5, intentmapa, intentlista;
	String success1, success, wieloetap;
	ArrayList<String> category = new ArrayList<String>();
	ArrayList<String> description = new ArrayList<String>();
	ArrayList<String> stringArray = new ArrayList<String>();
	ArrayList<String> stringArray1 = new ArrayList<String>();
	ArrayList<String> stringArray2 = new ArrayList<String>();
	ArrayList<String> stringArray3 = new ArrayList<String>();
	ArrayList<String> stringArray4 = new ArrayList<String>();
	ArrayList<String> stringArray5 = new ArrayList<String>();
	ArrayList<String> stringArray6 = new ArrayList<String>();
	ArrayList<String> stringArray7 = new ArrayList<String>();
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.competition);	
		Intent intentX = getIntent();
		gpssx = new TurningOnGPS(getApplicationContext());
		whichList1 = intentX.getExtras().getString("ktory");
		ID = intentX.getExtras().getString("ID");
		button2 = (Button) findViewById(R.id.Button2);	
		button1 = (Button) findViewById(R.id.Button1);
		datarozTV = (TextView) findViewById(R.id.TextView1);
		nazwaTV = (TextView) findViewById(R.id.TextView2);
		miejscowoscTV = (TextView) findViewById(R.id.TextView3);
		typTV = (TextView) findViewById(R.id.TextView5);
		godzrozpTV = (TextView) findViewById(R.id.TextView7);
		datazakTV = (TextView) findViewById(R.id.TextView9);
		godzzakTV = (TextView) findViewById(R.id.TextView11);
		limitTV = (TextView) findViewById(R.id.TextView13);
		oplataTV = (TextView) findViewById(R.id.TextView15);
		kat = (TextView) findViewById(R.id.TextView16);
		katTV = (TextView) findViewById(R.id.TextView17);
		opisTV = (TextView) findViewById(R.id.TextView19);
		typIV = (ImageView) findViewById(R.id.imageView1);
		intent2 = new Intent(CompInfo.this, CompList.class);
		intent3 = new Intent(CompInfo.this, MakingRoute.class);
		intent5 = new Intent(CompInfo.this, StartComp.class);
		intentlista = new Intent(CompInfo.this, CompetitorsList.class);
		intentmapa = new Intent(CompInfo.this, DrawRoute.class);
		intent = new Intent(this, CompInfo.class);
		intent1 = new Intent(this, ResultsList.class);
		//Toast.makeText(CompInfo.this, ID, Toast.LENGTH_LONG).show();
		String url="http://209785serwer.iiar.pwr.edu.pl/Rest1/rest/competition?id="+ID;
		sendHttpRequest(url, "GET", 0, false);


		new DownloadImageTask(typIV)
        .execute("http://209785serwer.iiar.pwr.edu.pl/RestImage/rest/competition/get/image?competition_id="+ID);

		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				CompInfo.this.finish();
				}
			});	
		button1.setOnClickListener(new OnClickListener() {			
			public void onClick(View arg1) {
				if (inn == 1){
					if(mozezapisac) {
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								context);
						alertDialogBuilder.setTitle("Zapis na zawody");
						alertDialogBuilder
								.setMessage("Zapisując się na zawody wyrażasz zgodze na przetwarzanie Twoich danych osobowych")
								.setCancelable(false)
								.setNegativeButton("Nie zgadzam się", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.cancel();
									}
								})
								.setPositiveButton("Zgadzam się", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										addSpinners();
									}
								});
						AlertDialog alertDialog = alertDialogBuilder.create();
						alertDialog.show();
					}
					else{
						comm.alertDialog("Zapis na zawody", "Organizator nie stworzył kategorii do tych zawodów. Nie możesz się na nie zapisać");
					}
					
				}
				if (inn == 2){
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							context);
						alertDialogBuilder.setTitle("Wypis z zawodów");
						alertDialogBuilder
							.setMessage("Czy na pewno chcesz się wypisać z zawodów?")
							.setCancelable(false)
								.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.cancel();
									}
								})
							.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									String url3 = "http://209785serwer.iiar.pwr.edu.pl/Rest1/rest/competition/event/leave?competition_id=" + ID + "&user_id=" + ID_usera;
									sendHttpRequest(url3, "DELETE", 0, true);
								}
							});

							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();										
				}
				if (inn == 3){
					if (gpssx.checkingGPSStatus()) {
						if (mappc && matrase && mapoi) {
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
									context);
							alertDialogBuilder.setTitle("Ustal trasę");
							alertDialogBuilder
									.setMessage("Te zawody posiadają już trasę. Czy chcesz ją edytować?")
									.setCancelable(false)
									.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int id) {
											dialog.cancel();
										}
									})
									.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int id) {
											intent3.putExtra("ID", ID);
											startActivity(intent3);
										}
									});
							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();
						}
						else {

							intent3.putExtra("ID", ID);
							startActivity(intent3);

						}
					} else {
						comm.alertDialog("Pobieranie lokalizacji", "Proszę włączyć usługę GPS");
					}
				}
				if (inn == 4){
					intentlista.putExtra("ID", ID);
					startActivity(intentlista);
				}
				
				}
			});
	}

	public void addSpinners(){
		LayoutInflater li = LayoutInflater.from(context);
		View promptsView = li.inflate(R.layout.my_dialog_layout, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setView(promptsView);

		alertDialogBuilder.setTitle("Wybierz kategorię");
		alertDialogBuilder.setIcon(R.mipmap.ic_launcher2);
		// create alert dialog
		final AlertDialog alertDialog = alertDialogBuilder.create();
		final Spinner mSpinner= (Spinner) promptsView
				.findViewById(R.id.spinner1);
		final Button mButton = (Button) promptsView
				.findViewById(R.id.btnSubmit);
		final Button bButton = (Button) promptsView
				.findViewById(R.id.btnBack);
		mSpinner.setOnItemSelectedListener(new OnSpinnerItemClicked());
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, category) {

			public View getView(int position, View convertView, ViewGroup parent) {

				View v = super.getView(position, convertView, parent);
				TextView tv = ((TextView) v);
				tv.setSingleLine();
				tv.setEllipsize(TextUtils.TruncateAt.END);
				tv.setTextSize(20);
				return v;
			}
		};
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(adapter);
		alertDialog.show();
		alertDialog.setCanceledOnTouchOutside(false);
		mButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String url2 = "http://209785serwer.iiar.pwr.edu.pl/Rest1/rest/competition/event?user_id=" + ID_usera + "&competition_id=" + ID +
						"&category_name=" + kategoria;
				sendHttpRequest(url2, "PUT", 2, true);
				alertDialog.cancel();
			}
		});
		bButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				alertDialog.cancel();
			}
		});

	}
	public class OnSpinnerItemClicked implements AdapterView.OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent,
								   View view, int pos, long id) {
			kategoria = parent.getItemAtPosition(pos).toString();
		}

		@Override
		public void onNothingSelected(AdapterView parent) {
			// Do nothing.
		}
	}

	public void addButton(final String x, final Intent y, int z) {
		TableLayout table = (TableLayout) findViewById(R.id.tableButtons);
		TableRow tableRow = new TableRow(this);
		tableRow.setLayoutParams(new TableLayout.LayoutParams(
				TableLayout.LayoutParams.MATCH_PARENT,
				TableLayout.LayoutParams.MATCH_PARENT
		));
		TableLayout.LayoutParams tableRowParams=
				new TableLayout.LayoutParams
						(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
		tableRowParams.setMargins(0, 0, 0, 10);
		tableRow.setLayoutParams(tableRowParams);
		table.addView(tableRow);
		Button button = new Button(this);
		button.setLayoutParams(new TableRow.LayoutParams(
				TableRow.LayoutParams.MATCH_PARENT,
				TableRow.LayoutParams.MATCH_PARENT
		));
		button.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		button.setBackgroundResource(z);
		button.setTextColor(getApplication().getResources().getColor(R.color.white));
		button.setText(x);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (y == null) {
					setCompTime();
				} else if (x.equals("START")) {
					if (gpssx.checkingGPSStatus()) {
						String url = "http://209785serwer.iiar.pwr.edu.pl/Rest1/rest/competition/event/user/start?competition_id="
								+ID+"&user_id="+ID_usera;
						sendHttpRequest(url, "PUT", 2, true);
					} else {
						comm.alertDialog("Pobieranie lokalizacji", "Proszę włączyć usługę GPS");
					}
				} else {
					y.putExtra("ID", ID);
					startActivity(y);
				}
			}

		});
		tableRow.addView(button);
	}

	public void setCompTime(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		final View arg2 = null;
		final EditText input = new EditText(CompInfo.this);
		input.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_NORMAL);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		input.setLayoutParams(lp);
		alertDialogBuilder.setView(input);
		alertDialogBuilder.setTitle("Zarządzanie zawodami");
		alertDialogBuilder
				.setMessage("Podaj godzinę startu zawodów:")
				.setCancelable(false)
				.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				})
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						String time = input.getText().toString();
						if (time.contains("-")) time = new String(time.replace("-", ":"));
						else if (time.contains("/")) time = new String(time.replace("/", ":"));
						if (!tv.validate(time)) {
							comm.alertDialog("Komunikat", "Wpisz poprawny format daty (hh:mm)");
						} else {
							String url4 = "http://209785serwer.iiar.pwr.edu.pl/Rest1/rest/competition/event/start?competition_id=" + ID +
									"&owner_id=" + ID_usera + "&time=" + time;
							sendHttpRequest(url4, "PUT", 1, true);
						}
					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public void sendHttpRequest(String url, final String operation, final int i, boolean showpd){
		RestController rc = new RestController(this){
			@Override
			public void onResponseReceived(String result) {
				if (operation == "GET"&& i==0){
					//pobierz dane zawodow

					try {
						parsingJSON(result);
					} catch (JSONException e) {

					}
					String url1 ="http://209785serwer.iiar.pwr.edu.pl/Rest1/rest/competition/category/list?competition_id="+ID;
					//pobierz kategorie
					sendHttpRequest(url1, "GET", 1, false);
					if (whichList1.equals("OGOLNE") && wieloetap.length()==1){
						button1.setText("Zapisz się na zawody");
						button1.setBackgroundResource(R.color.navyblue);
						inn = 1;
					}
					else if (whichList1.equals("OGOLNE") && wieloetap.length()>1){
						button1.setVisibility(View.INVISIBLE);
					}
					else if (whichList1.equals("OSOBISTE") && wieloetap.length()==1){
						button1.setText("Wypisz się z zawodów");
						button1.setBackgroundResource(R.color.navyblue);
						inn = 2;
					}
					else if (whichList1.equals("OSOBISTE") && wieloetap.length()>1){
						button1.setVisibility(View.INVISIBLE);
					}
					else if (whichList1.equals("ORG") && !wieloetap.equals("1")){
						button1.setText("Ustal trasę");
						button1.setBackgroundResource(R.color.navyblue);
						inn = 3;
					}
					else if (whichList1.equals("ORG") && wieloetap.equals("1")){
						button1.setVisibility(View.INVISIBLE);
					}
					else if (whichList1.equals("OBSERW")){
						button1.setText("Zobacz listę uczestników");
						button1.setBackgroundResource(R.color.navyblue);
						inn = 4;
					}

					if(wieloetap.equals("1")){
						String urlx = "http://209785serwer.iiar.pwr.edu.pl/Rest1/rest/competition/all?type=&name=&place=&wieloetapowe="+ID;
						sendHttpRequest(urlx, "GET", 2, true);
					}
				}
				else if (operation == "GET"&& i == 2){
					try {
						parsingJSON1(result);
					} catch (JSONException e) {

					}
				}
				else if (operation == "GET"&& i == 1){
					//pobranie kategorii
					try {
						getCategory(result);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} //wypisz sie z zawodow
				else if (operation == "DELETE"){
					checkResponseSignOutAndSetTime(result);
				} //ustalanie godziny
				else if (operation == "PUT" && i == 1){
					checkResponseSignOutAndSetTime(result);
				} //zapisywanie na zawody
				else if (i == 2){
					checkResponseSignUpAndCheckStart(result);
				}
			}
		};
		rc.setAddress(url);
		rc.setOperation(operation);
		if(showpd) rc.setShowPD(true);
		rc.execute();
	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	    ImageView bmImage;

	    public DownloadImageTask(ImageView bmImage) {
	        this.bmImage = bmImage;
	    }

	    protected Bitmap doInBackground(String... urls) {
	        String urldisplay = urls[0];
	        Bitmap mIcon11 = null;
	        try {
	            InputStream in = new java.net.URL(urldisplay).openStream();
	            mIcon11 = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            Log.e("Error", e.getMessage());
	            e.printStackTrace();
	        }
	        return mIcon11;
	    }

	    protected void onPostExecute(Bitmap result) {
	    	if (result!=null)
	        bmImage.setImageBitmap(result);
	    }
	}

	public void checkResponseSignUpAndCheckStart(String wejscie)
    {
    	String komunikat="";
    	flaga1 = true;				  		
    		if (wejscie.contains("Ok")){
				flaga1 = true;
				success = "Udało Ci się zapisać na zawody!";
			}
			else if (wejscie.contains("Juz zapisany na te zawody")){
				flaga1 = false;
				error = "Jesteś już zapisany na te zawody!";
			}
			else if (wejscie.contains("Brak wolnych miejsc")){
				flaga1 = false;
				error = "Brak wolnych miejsc!";
			}
			else if (wejscie.contains("Zawody juz sie odbyly")){
				flaga1 = false;
				error = "Zawody już się odbyły. Nie możesz się na nie zapisać";
			}
			else if (wejscie.contains("Competition disactivated")){
				flaga1 = false;
				error = "Zawody zostały dezaktywowane przez organizatora";
			}
			if (wejscie.contains("Ok!")){
				flaga1 = true;
				success = "Jesteś gotowy aby wziąć udział w zawodach. Przejdź do panelu pomiaru czasu";
			}
			else if (wejscie.contains("Already timed. Action forbidden")){
				flaga1 = false;
				error = "Juz wziąłeś udział w tych zawodach";
			}
			else if (wejscie.contains("User set as ready")){
				String url = "http://209785serwer.iiar.pwr.edu.pl/Rest1/rest/competition/checkpart?user_id="+ID_usera+
						"&competition_id="+ID;
				sendHttpRequest(url, "GET", 2, false);
				flaga1=true;
			}
			else if (wejscie.contains("Competition don't have start time set yet")){
				flaga1 = false;
				error = "Zawody nie mają jeszcze ustalonej dokładnej godziny startu";
			}
			else if (wejscie.contains("Competition already started")){
				flaga1 = false;
				error = "Start zawodów już się odbył. Spóźniłeś się";
			}
			else if (wejscie.contains("Number not set")){
				flaga1 = false;
				error = "Nie masz jeszcze nadanego numeru zawodnika";
			}
			if (wejscie.length()==0){
				flaga1 = false;
				error = "Wystąpił problem w połączeniu z serwerem. Spróuj ponownie później";
			}

		if (flaga1 == false) {
			ret = error;
		} else {
			ret = success;
		}
		final String tt = wejscie;
		Context context2 = this;
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context2);
		alertDialogBuilder.setTitle("Komunikat");
		alertDialogBuilder
				.setMessage(ret)
			.setCancelable(false)
			.setNeutralButton("OK",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
					if ((ret1==success1) && tt.contains("Ok!")) {
						intent5.putExtra("ID", ID);
						startActivity(intent5);
					}
				}});
			AlertDialog alertDialog1 = alertDialogBuilder.create();
			if(!tt.contains("User set as ready")) alertDialog1.show();
    }
	public void checkResponseSignOutAndSetTime(String wejscie)
	{
		String komunikat1="";
    	flaga2 = true;				
   		
    		if (wejscie.contains("Ok")){
			flaga2 = true;
				success1 = "Udało Ci się wypisać z zawodów!";
				komunikat1 = "Udany wypis z zawodów";
			}
			else if (wejscie.contains("Start set")){
				flaga2 = true;
				success1 = "Udało Ci się ustalić godzinę startu zawodów";
				komunikat1 = "Ustalanie godziny startu";
			}
			else if (wejscie.contains("No such record")){
    			flaga2 = false;
    			error1 = "Nie jesteś zapisany na te zawody";
    			}
			else if (wejscie.contains("Zawody juz sie odbyly")){
    			flaga2 = false;
    			error1 = "Zawody już się odbyły. Nie możesz sie z nich wypisać";
    			}
			else if (wejscie.length()==0){
				flaga1 = false;
				error = "Wystąpił problem w połączeniu z serwerem. Spróuj ponownie później";
			}
    		
			if (flaga2==false){
				ret1=error1;
			}
			else{ 
				ret1=success1;
			}
			final View arg0 = null;
		final String tt = wejscie;
    	Context context2 = this;   	
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context2);
		alertDialogBuilder.setTitle("Komunikat");
		alertDialogBuilder
			.setMessage(ret1)
			.setCancelable(false)
			.setNeutralButton("OK",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
				if ((ret1==success1) && tt.contains("Ok")){
					ktore_zawody = "OSOBISTE";
	    			intent2.putExtra("ktore", ktore_zawody);
	    			intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    			startActivity(intent2);					
				}

				}});
			AlertDialog alertDialog1 = alertDialogBuilder.create();
			alertDialog1.show();
	}

	public void parsingJSON1(String JSON) throws JSONException {
		//Toast.makeText(CompList.this, JSON, Toast.LENGTH_LONG).show();
		stringArray.clear();
		stringArray1.clear();
		stringArray2.clear();
		stringArray3.clear();
		stringArray4.clear();
		stringArray5.clear();
		stringArray6.clear();
		stringArray7.clear();
		int i;
		JSONArray jsonarray = new JSONArray(JSON);
		for (i = 0; i < jsonarray.length(); i++) {

			JSONObject obj = jsonarray.getJSONObject(i);
			stringArray.add(obj.getString("DATA_ROZP"));
			stringArray1.add(obj.getString("NAME"));
			stringArray2.add(obj.getString("MIEJSCOWOSC"));
			stringArray3.add(obj.getString("COMPETITION_ID"));
			stringArray4.add(obj.getString("TYP"));
			if(obj.toString().contains("ILE_OSOB"))
				stringArray5.add(obj.getString("ILE_OSOB"));
			else stringArray5.add("");
			if(obj.toString().contains("ROUTE_ID"))
				stringArray6.add("1");
			else stringArray6.add("0");
			stringArray7.add(obj.getString("WIELOETAPOWE"));
		}
		populateButtons(i, stringArray, stringArray1, stringArray2, stringArray3, stringArray4, stringArray5, stringArray6, stringArray7);
	}

	public void parsingJSON(String JSON) throws JSONException
	{
		if(JSON.contains("ROUTE_ID")) mappc=true;
		if(JSON.contains("ROUTEPOI_ID")) mapoi=true;
		if(JSON.contains("TRACK_ID")) matrase=true;

		JSONObject obj = new JSONObject(JSON);		
		String naz = obj.getString("NAME");
		String miej = obj.getString("MIEJSCOWOSC");
		String typ = obj.getString("TYP");
		String datro = obj.getString("DATA_ROZP");
		String godzro = obj.getString("CZAS_ROZP");
		String datza = obj.getString("DATA_ZAK");
		String godzza = obj.getString("CZAS_ZAK");
		String lim = obj.getString("LIMIT_UCZ");
		String opl = obj.getString("OPLATA");
		String opis = obj.getString("OPIS");
		wieloetap = obj.getString("WIELOETAPOWE");
		if (whichList1.equals("OSOBISTE")){
			if(matrase&&mappc && !wieloetap.equals("1"))
				addButton("START", intent5, R.color.navyblue);
		}
		if (mappc || matrase && !wieloetap.equals("1")) addButton("Zobacz trasę", intentmapa, R.color.navyblue);
		if (whichList1.equals("ORG")){
			if(matrase && mappc && !wieloetap.equals("1"))
				addButton("Ustal godzinę startu", null, R.color.navyblue);
		}
		datarozTV.setText(datro);
		nazwaTV.setText(naz);
		miejscowoscTV.setText(miej);
		typTV.setText(typ);
		godzrozpTV.setText(godzro);
		datazakTV.setText(datza);
		godzzakTV.setText(godzza);
		limitTV.setText(lim);
		oplataTV.setText(opl);
		spanned = Html.fromHtml(opis);
		opisTV.setText(spanned);

		int ilosc_linii=(int)(spanned.length()/25);
		++ilosc_linii;
		if (ilosc_linii==0) ilosc_linii=2;
		opisTV.setLines(ilosc_linii);

		if (typ.contains("arciars")){
			typIV.setImageResource(R.mipmap.ic_narciarskie);
			}
		else if (typ.contains("Kolarstwo") || typ.contains("kolarstwo")){
			typIV.setImageResource(R.mipmap.ic_rowerowe);
			} 
		else if 
			(typ.contains("Bieg") || typ.contains("Chód") || typ.contains("bieg")){
			typIV.setImageResource(R.mipmap.ic_biegi);			
			}
		else if (typ.equals("Wyścig samolotów") || typ.equals("Wyścig balonów")){
			typIV.setImageResource(R.mipmap.ic_powietrzne);
			}
		else if (typ.contains("Wyścig") || typ.contains("Karting")){
			typIV.setImageResource(R.mipmap.ic_motorowe);
			}
		else if (typ.contains("Kajakarstwo") || typ.contains("Wioślarstwo")){
			typIV.setImageResource(R.mipmap.ic_lodzie);
			}
		else
		 typIV.setImageResource(R.mipmap.ic_inne);
	}
	public void getCategory(String JSON) throws JSONException {

		if(JSON.length()>3) {
			mozezapisac = true;
			JSONArray jsonarray = new JSONArray(JSON);
			kat.setLines(jsonarray.length());
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject obj = jsonarray.getJSONObject(i);
				category.add(obj.getString("NAME"));
				description.add(obj.getString("DESCRIPTION"));
				katTV.append(category.get(i) + ": " + description.get(i) + "\n");
			}
			if(spanned.length()!=0) opisTV.setLines(25);
		}
	}
	private void populateButtons(int i, ArrayList<String> data, ArrayList<String> nazwa, ArrayList<String> miasto,
								 final ArrayList<String> id, ArrayList<String> typ, ArrayList<String> ileOsob,
								 ArrayList<String> pcc, ArrayList<String> wieloetapowe) {
		TableLayout table = (TableLayout) findViewById(R.id.tableButtons1);
		table.removeAllViews();
		for (row = 0; row < i; row++) {
			TableRow tableRow = new TableRow(this);
			tableRow.setLayoutParams(new TableLayout.LayoutParams(
					TableLayout.LayoutParams.MATCH_PARENT,
					TableLayout.LayoutParams.MATCH_PARENT
			));
			table.addView(tableRow);
			Button button = new Button(this);
			button.setLayoutParams(new TableRow.LayoutParams(
					TableRow.LayoutParams.WRAP_CONTENT,
					TableRow.LayoutParams.WRAP_CONTENT
			));
			final String id_zawodow = id.get(row);
			button.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

			if (typ.get(row).contains("arciars")) {
				button.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_narciarskie, 0, 0, 0);
			} else if (typ.get(row).contains("Kolarstwo") || typ.get(row).contains("kolarstwo")) {
				button.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_rowerowe, 0, 0, 0);
			} else if
					(typ.get(row).contains("Bieg") || typ.get(row).contains("Chód") || typ.get(row).contains("bieg")) {
				button.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_biegi, 0, 0, 0);
			} else if (typ.get(row).equals("Wyścig samolotów") || typ.get(row).equals("Wyścig balonów")) {
				button.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_powietrzne, 0, 0, 0);
			} else if (typ.get(row).contains("Wyścig") || typ.get(row).contains("Karting")) {
				button.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_motorowe, 0, 0, 0);
			} else if (typ.get(row).contains("Kajakarstwo") || typ.get(row).contains("Wioślarstwo")) {
				button.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_lodzie, 0, 0, 0);
			} else
				button.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_inne, 0, 0, 0);
			button.setTextColor(getApplication().getResources().getColor(R.color.navyblue));
			button.setCompoundDrawablePadding(30);
			if(wieloetapowe.get(row).equals("0"))
				button.setBackground(getResources().getDrawable(R.drawable.rounded_border_comp1));
			else if(wieloetapowe.get(row).equals("1"))
				button.setBackground(getResources().getDrawable(R.drawable.rounded_border_comp2));
			else button.setBackground(getResources().getDrawable(R.drawable.rounded_border_comp));
			button.setText(data.get(row) + ",\n" + nazwa.get(row) + ",\n" + miasto.get(row));
			DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy");
			DateTime eventDate = formatter.parseDateTime(data.get(row));
			eventDate = formatter.parseDateTime(data.get(row));
			if (!focus) {
				if (!eventDate.isBeforeNow()) {
					focus = true;
					flow = row + 4;
				}

			} else {
				if (row == flow) {
					button.setFocusable(true);
					button.setFocusableInTouchMode(true);
					button.requestFocus();
				}
			}
			final int tmp = row;
			if(whichList1.contains("RESULTS")) {
				button.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						//comp1.setID_zawodow(id_zawodow);
						//intent.putExtra("ktory", whichList);
						//CompInfo.this.finish();
						idX = id_zawodow;
						intent1.putExtra("ID", idX);
						startActivity(intent1);
					}

				});
			}
			else {
				button.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						//Toast.makeText(CompInfo.this, id_zawodow, Toast.LENGTH_LONG).show();
						//comp1.setID_zawodow(id_zawodow);
						intent.putExtra("ktory", whichList1);
						idX = id_zawodow;
						intent.putExtra("ID", idX);
						//CompInfo.this.finish();
						startActivity(intent);
					}

				});
			}
			if(whichList1.contains("OGOLNERESULTS") || whichList1.contains("OBSERWRESULTS"))
			{
				if(!(ileOsob.get(row).equals("0"))&&(pcc.get(row).equals("1")))
					tableRow.addView(button);
			}
			else //if (wieloetapowe.get(row).length()>1 && wieloetapowe.get(row).equals(ID))
				tableRow.addView(button);
		}
	}

}

