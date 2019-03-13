package com.sld.zt.utils.getHtmlString;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class GetHtmlString {
    public static String getOneHtml(String urlString)throws Exception{
        InputStreamReader in = new InputStreamReader(new URL(urlString).openStream());
        // read contents into string buffer
        StringBuilder input = new StringBuilder();
        int ch;
        while ((ch = in.read()) != -1) input.append((char) ch);
        //System.out.println(input);
        return input.toString();
    }
    public static String t1(String urls){
       try{
            URL url = new URL(urls);
            URLConnection URLconnection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) URLconnection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.err.println("成功");
                InputStream in = httpConnection.getInputStream();
                InputStreamReader isr = new InputStreamReader(in);
                BufferedReader bufr = new BufferedReader(isr);
                StringBuilder result = new StringBuilder();
                String str;
                while ((str = bufr.readLine()) != null) {
                    System.out.println(str);
                    result.append(str);
                }
                bufr.close();
                return result.toString();
            } else {
                System.err.println("失败");
                return null;
            }
        } catch (Exception e) {
        e.printStackTrace();
        return null;
        }
    }


    public static void main(String[] args) {
        try{
            //String content = GetHtmlString.getOneHtml("http://192.168.100.56:81/upload/htmlpage/20180802/54fd07cda0984cee8efa610bc677d7eb.html");

            String content = GetHtmlString.t1("http://192.168.100.56:81/upload/htmlpage/20180802/54fd07cda0984cee8efa610bc677d7eb.html");
            //String content = GetHtmlString.t1("https://www.51kawang.com/upload/htmlpage/20180815/5b95ff4a4ed842fc864bbf1eea78e580.html");
            System.out.println(content);
            Map map = new HashMap<>();
            String[] a1 = content.split("</style></head><body><div class=\"w-e-text\"><h1 id=\"gdTitle\">");
            String[] a4 = content.split("</head><body><div class=\"w-e-text\">");
            if(a1.length>1){
                String[] a2 = a1[1].split("</h1><p id=\"defaultHh\"><br></p>");
                String title = a2[0];
                String[] a3 = a2[1].split("</div></body></html>");
                System.out.println("title=-------"+title);
                content = a3[0];
                content.replaceAll("\r|\n*","");
                System.out.println(content);
                map.put("title",title);
                map.put("content",content);
            }else{
                String[] a2 = a4[1].split("</div></body></html>");
                content = a2[0];
                map.put("title","");
                map.put("content",content);
                System.out.println(content);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
