package br.com.buscaenderecocep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import br.com.buscaenderecocep.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main );

        binding.btnBuscar.setOnClickListener(view -> {


            if (!binding.editCep.getText().toString().isEmpty()){

                binding.linearDados.setVisibility(View.VISIBLE);
                MyTask myTask = new MyTask();
                String cep = binding.editCep.getText().toString();
                String urlCep = "https://viacep.com.br/ws/" + cep + "/json/";
                myTask.execute(urlCep);

            }{
                Toast.makeText(getApplicationContext(), "Prencha o CEP sem traços. Ex.: 80430210", Toast.LENGTH_SHORT).show();
            }

        });
    }

    class MyTask extends AsyncTask<String , Void, String>{

        @Override
        protected String doInBackground(String... strings) {

            String stringUrl = strings[0];
            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            StringBuffer buffer = null;

            try {

                URL url = new URL(stringUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);

                BufferedReader reader = new BufferedReader(inputStreamReader);

                buffer = new StringBuffer();
                String linha = "";

                while ((linha = reader.readLine()) != null){
                    buffer.append(linha);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);

            String rua = null;
            String complemento = null;
            String bairro = null;
            String cidade = null;
            String estado = null;


            try {
                JSONObject jsonObject = new JSONObject(resultado);
                rua = jsonObject.getString("logradouro");
                complemento = jsonObject.getString("complemento");
                bairro = jsonObject.getString("bairro");
                cidade = jsonObject.getString("localidade");
                estado = jsonObject.getString("uf");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            binding.editRua.setText(rua);
            binding.editComplemento.setText(complemento);
            binding.editBairro.setText(bairro);
            binding.editCidade.setText(cidade);
            binding.editEstado.setText(estado);

        }
    }
}