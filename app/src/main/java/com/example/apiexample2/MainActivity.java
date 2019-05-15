package com.example.apiexample2;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        new DownloadJSonAsyncTask().execute("https://jsonplaceholder.typicode.com/users");

    }

    public class DownloadJSonAsyncTask extends AsyncTask<String, Void, ArrayList<Usuario>>{

        ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(MainActivity.this,
                    "Aguarde", "Fazendo download das " +
                            "informações...");
        }

        @Override
        protected ArrayList<Usuario> doInBackground(String... strings) {
            String urlString = strings[0];
            URL url;

            try {
                url = new URL(urlString);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(15000000);
                httpURLConnection.connect();

                InputStream resposta = httpURLConnection.getInputStream();
                String texto = new Scanner(resposta).useDelimiter("\\A").next();

                if (texto!=null) {
                    ArrayList<Usuario> usuarioArrayList = getDados(texto);
                    return usuarioArrayList;

                }else
                    return null;



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        private ArrayList<Usuario> getDados(String texto) {

            ArrayList<Usuario> usuarioArrayList = new ArrayList<>();

            try {
                JSONArray jsonArray = new JSONArray(texto);

                for (int i = 0; jsonArray.length() > i; i++) {
                    JSONObject usuarioJSObj = jsonArray.getJSONObject(i);

                    Usuario usuario = new Usuario();
                    usuario.setId(usuarioJSObj.getString("id"));
                    usuario.setNome(usuarioJSObj.getString("name"));
                    usuario.setSobrenome(usuarioJSObj.getString("username"));
                    usuario.setEmail(usuarioJSObj.getString("email"));

                    usuario.setEndereco_rua(usuarioJSObj.getJSONObject("address").
                            getString("street"));
                    usuario.setEndereco_complemento(usuarioJSObj.getJSONObject("address").
                            getString("suite"));
                    usuario.setEndereco_cidade(usuarioJSObj.getJSONObject("address").
                            getString("city"));
                    usuario.setEndereco_cep(usuarioJSObj.getJSONObject("address").
                            getString("zipcode"));
                        usuario.setGeo_lat(usuarioJSObj.getJSONObject("address").
                                getJSONObject("geo").getString("lat"));
                        usuario.setGeo_lon(usuarioJSObj.getJSONObject("address").
                                getJSONObject("geo").getString("lng"));

                    usuario.setTel(usuarioJSObj.getString("phone"));
                    usuario.setSite(usuarioJSObj.getString("website"));
                    usuario.setEmpresa_nome(usuarioJSObj.getJSONObject("company").
                            getString("name"));
                    usuario.setEmpresa_slogan(usuarioJSObj.getJSONObject("company").
                            getString("catchPhrase"));
                    usuario.setEmpresa_bs(usuarioJSObj.getJSONObject("company").
                            getString("bs"));

                    usuarioArrayList.add(usuario);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


            return usuarioArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<Usuario> usuarios) {
            super.onPostExecute(usuarios);

            dialog.dismiss();

            ArrayAdapter<Usuario> adapter = new ArrayAdapter<>(
              MainActivity.this,
              android.R.layout.simple_list_item_1,
              usuarios
            );

            setListAdapter(adapter);

        }
    }

}
