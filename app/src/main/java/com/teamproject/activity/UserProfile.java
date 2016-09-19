package com.teamproject.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.teamproject.functions.DialogCommunications;
import com.teamproject.functions.RestController;
import com.teamproject.models.userDTO;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UserProfile extends Activity {	
	private Button przyciskWyjscia, przyciskZmianyHasla, przyciskPotw;
	private ImageButton przyciskEdycji, przyciskUsuwania;
	final Context context = this;
	 private EditText imieET, nazwiskoET, loginET, emailET, wiekET, plecET, klubET, obywET, nrtelET, ICEET;
	public static final String costam1  = Login.SPF_NAME;
	Intent intent, intent1;
	String error, ret, success, success1;
	boolean flaga,flaga1, czyklik;
	final userDTO user1 = Login.user;
	String ID_usera = user1.getID_uzytkownika();
	DialogCommunications comm;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_profile);
		success = "Udało Ci się zmienić swoje dane!";
		success1 = "Udało Ci się usunąć swoje konto!";
		intent = new Intent(this, ChangePass.class);
		intent1 = new Intent(this, Login.class);		
		przyciskEdycji = (ImageButton) findViewById(R.id.imageButton1);	
	    przyciskUsuwania = (ImageButton) findViewById(R.id.imageButton2);
		przyciskWyjscia = (Button) findViewById(R.id.buttonAlert);
		przyciskZmianyHasla = (Button) findViewById(R.id.button1);
		przyciskPotw = (Button) findViewById(R.id.ConfirmButton);
		imieET = (EditText) findViewById(R.id.editText1);
		nazwiskoET = (EditText) findViewById(R.id.editText2);
		loginET = (EditText) findViewById(R.id.editText3);
		emailET = (EditText) findViewById(R.id.editText4);
		wiekET = (EditText) findViewById(R.id.editText5);
		plecET = (EditText) findViewById(R.id.editText6);
		klubET = (EditText) findViewById(R.id.editText7);
		obywET = (EditText) findViewById(R.id.editText8);
		nrtelET = (EditText) findViewById(R.id.editText9);
		ICEET = (EditText) findViewById(R.id.editText10);
		comm = new DialogCommunications(context);
		String url="http://209785serwer.iiar.pwr.edu.pl/Rest1/rest/user?id="+ID_usera;
		sendHttpRequest(url, "GET");
		przyciskWyjscia.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				UserProfile.this.finish();
				}
			});
		przyciskZmianyHasla.setOnClickListener(new OnClickListener() {			
			public void onClick(View arg0) {
				startActivity(intent);
				}
			});	
		przyciskUsuwania.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View arg1) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					context);
				alertDialogBuilder.setTitle("Usuwanie konta");
				alertDialogBuilder
					.setMessage("W związku z likwidacją konta zostaną usunięte wszystkie dane użytkownika "
							+ "z wyłączeniem imienia, nazwiska oraz nazwy klubu, "
							+ "które będą prezentowane jedynie na listach wyników zawodów, "
							+ "w których zawodnik brał udział.\n\nCzy napewno chcesz usunąć swoje konto?")
					.setCancelable(false)
						.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						})
					.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							positiveResponse();
						}
					});
					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();
				}
			});
		przyciskEdycji.setOnClickListener(new OnClickListener() {			
			public void onClick(View arg0) {
				czyklik = true;
				przyciskEdycji.setImageResource(R.drawable.ic_edit2);
				Toast.makeText(context, "Teraz możesz edytować swoje dane", Toast.LENGTH_LONG).show();
				editData();
				}
			});	
		przyciskPotw.setOnClickListener(new OnClickListener() {			
			public void onClick(View arg0) {
				if (czyklik){
					String url1 = "";
					try {
						url1 = validation(user1.getID_uzytkownika());
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (url1.length()!=0)
					{
						sendHttpRequest(url1, "POST");
					}
				}
				else
					Toast.makeText(context, "Aby potwierdzić zmianę danych, proszę je najpierw zedytować, klikając przycisk na górze (lewy)", Toast.LENGTH_LONG).show();
				}
			});
	}
	public void sendHttpRequest(String url, final String operation){
		RestController rc = new RestController(this){
			@Override
			public void onResponseReceived(String result) {
				if (operation == "GET"){
					try {
						parsingJSON(result);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				if (operation == "POST"){
					checkResponseChangeData(result);
				}
				if (operation == "DELETE")
				{
					checkResponseDelete(result);
				}
			}
		};
		rc.setAddress(url);
		rc.setOperation(operation);
		rc.setShowPD(true);
		rc.execute();
	}
	public void positiveResponse(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
			final View arg2 = null;
			final EditText input = new EditText(UserProfile.this);
			input.setInputType(InputType.TYPE_CLASS_TEXT |
				    InputType.TYPE_TEXT_VARIATION_PASSWORD);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
			input.setLayoutParams(lp);
			alertDialogBuilder.setView(input);
			alertDialogBuilder.setTitle("Usuwanie konta");
			alertDialogBuilder
				.setMessage("Podaj swoje hasło")
				.setCancelable(false)
					.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					})
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						String passwd = input.getText().toString();
						String url3 = "http://209785serwer.iiar.pwr.edu.pl/Rest1/rest/user/delete?user_id=" + ID_usera + "&password=" + passwd;
						sendHttpRequest(url3, "DELETE");
					}
				});

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
	}
	public String validation(String id) throws UnsupportedEncodingException {
    	String url="";
    	flaga = true;
        String imie = imieET.getText().toString(); 
        String nazwisko = nazwiskoET.getText().toString();
        String email = emailET.getText().toString();
        String wiek = wiekET.getText().toString();
        String klub = klubET.getText().toString();   
        String obywatelstwo = obywET.getText().toString();
        String nr_tel = nrtelET.getText().toString();
        String ICE = ICEET.getText().toString();
        String ID = id;
    	if ((nazwisko.matches("[A-Za-z]+") == false))
    	{
    		flaga = false;
    		error = "Nazwisko może składać się tylko z liter (bez polskich znaków)";
    	}
    	if ((imie.matches("[A-Za-z]+") == false))
    	{
    		flaga = false;
    		error = "Imię możee składać się tylko z liter (bez polskich znaków)";
    	}
    	if ((imie.length() == 0 || nazwisko.length() == 0 || 
        		 email.length() == 0 || wiek.length()==0))
        {
        	flaga = false;
        	error = "Proszę wypełnić wszystkie wymagane pola (imię, nazwisko, e-mail, wiek)";
        }
    	if (flaga == false){
			comm.alertDialog("Komunikat o błędzie", error);
		}
    	else {	
    		url = URLaddress(imie, nazwisko, email, ID, wiek, klub, nr_tel, ICE, obywatelstwo); 
    	}
    	return url;
    }
    
	public String URLaddress(String imieS, String nazwiskoS, String emailS, String ID, String wiekS, String klubS, String nrtel, String ICE, String obywS) throws UnsupportedEncodingException
	{
		String klub = URLEncoder.encode(klubS,"UTF-8");
		String obyw = URLEncoder.encode(obywS,"UTF-8");
		String URL = "http://209785serwer.iiar.pwr.edu.pl/Rest1/rest/user?"
				+ "name="+imieS+
				"&surname="+nazwiskoS+
				"&email="+emailS+
				"&user_id="+ID+	
				"&age="+wiekS+
				"&club="+klub+
				"&nr_tel="+nrtel+
				"&ICE="+ICE+
				"&nationality="+obyw+"";	
		return URL;
	}
	public void editData()
	{
		imieET.setInputType(InputType.TYPE_CLASS_TEXT);
		nazwiskoET.setInputType(InputType.TYPE_CLASS_TEXT);
		emailET.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		wiekET.setInputType(InputType.TYPE_CLASS_NUMBER);
		klubET.setInputType(InputType.TYPE_CLASS_TEXT);
		obywET.setInputType(InputType.TYPE_CLASS_TEXT);
		nrtelET.setInputType(InputType.TYPE_CLASS_NUMBER);
		ICEET.setInputType(InputType.TYPE_CLASS_NUMBER);
	}
	public void parsingJSON(String JSON) throws JSONException
	{		
		JSONObject obj = new JSONObject(JSON);		
		String imie = obj.getString("IMIE");
		String nazwisko = obj.getString("NAZWISKO");
		String login = obj.getString("LOGIN");
		String email = obj.getString("EMAIL");
		String wiek = obj.getString("WIEK");
		String plec = obj.getString("PLEC");
		String klub = obj.getString("KLUB");
		String obywatelstwo = obj.getString("OBYWATELSTWO");
		String nrtel = obj.getString("NR_TEL");
		String ICE = obj.getString("ICE");
			
		imieET.setText(imie);
		nazwiskoET.setText(nazwisko);
		loginET.setText(login);
		emailET.setText(email);
		wiekET.setText(wiek);
		plecET.setText(plec);
		klubET.setText(klub);
		obywET.setText(obywatelstwo);
		nrtelET.setText(nrtel);
		ICEET.setText(ICE);
		
	}

	public void checkResponseChangeData(String wejscie)
    {
    	String komunikat="";
    	flaga1 = true;
			if (wejscie.contains("Profile updated")){
			flaga1 = true;
			}	
			else{
				flaga1 = false;
				error = "Wystąpił nieoczekiwany błąd - spróbuj ponownie później";
				}
		
			if (flaga1==false){
				ret=error;
				komunikat = "Komunikat";
			}
			else{ 
				ret=success;
				komunikat = "Udana edycja danych";
			}

		comm.alertDialog(komunikat, ret);

	}
	public void checkResponseDelete(String wejscie)
	{
	    	String komunikat="";
	    	flaga1 = false;
				if (wejscie.contains("Account deleted")){
				flaga1 = true;
				}					
	    		if (wejscie.contains("Wrong password")){
	    		flaga1 = false;
				error = "Podałeś złe hasło!";
				}	
				if (flaga1==false){
					ret=error;
					komunikat = "Komunikat";
				}
				else{ 
					ret=success1;
					komunikat = "Udane usunięcie konta";
				}
	    	Context context2 = this;   	
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context2);
			alertDialogBuilder.setTitle(komunikat);
			alertDialogBuilder
				.setMessage(ret)
				.setCancelable(false)
				.setNeutralButton("OK",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
					if (ret==success1){
						logOut();
						startActivity(intent1);
						UserProfile.this.finish();
					}
					}});
				AlertDialog alertDialog1 = alertDialogBuilder.create();
				alertDialog1.show();
	}
	public void logOut(){
		    	{
		            SharedPreferences loginPreferences = UserProfile.this.getSharedPreferences(costam1, Context.MODE_PRIVATE);
		            loginPreferences.edit().clear().commit();
		        }
		        Toast.makeText(context, "Zostałeś wylogowany", Toast.LENGTH_LONG ).show();;
		    }
}

