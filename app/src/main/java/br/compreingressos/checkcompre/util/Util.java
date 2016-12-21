package br.compreingressos.checkcompre.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Edicarlos on 04/03/2015.
 */
public class Util {

    public String makeRequest(String urlAddress){
        HttpURLConnection conn = null;
        URL url = null;
        String response = null;
        try{
            url = new URL(urlAddress);
            conn = (HttpURLConnection) url.openConnection();
            response = readStream(conn.getInputStream());
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            conn.disconnect();
        }
        return response;
    }

    public String makeRequest(String urlAddress, String data){
        HttpURLConnection conn = null;
        URL url = null;
        String response = null;
        try{
            url = new URL(urlAddress);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

            writer.write(data);
            writer.flush();

            response = readStream(conn.getInputStream());
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            conn.disconnect();
        }
        return response;
    }

    public String readStream(InputStream in){
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try{
            reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while((line = reader.readLine()) != null){
                builder.append(line + "\n");
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            if(reader != null){
                try{
                    reader.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }
}
