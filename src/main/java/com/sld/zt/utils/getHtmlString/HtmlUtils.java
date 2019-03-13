package com.sld.zt.utils.getHtmlString;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HtmlUtils
{
    /**
          * TODO 下载文件到本地
          * @author nadim  ；4      * @date Sep 11, 2015 11:45:31 AM
          * @param fileUrl 远程地址
          * @param fileLocal 本地路径
          * @throws Exception ；8      */
    public static void downloadFile(String fileUrl,String fileLocal) throws Exception {
             URL url = new URL(fileUrl);
             HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
             urlCon.setConnectTimeout(6000);
             urlCon.setReadTimeout(6000);
             int code = urlCon.getResponseCode();
             if (code != HttpURLConnection.HTTP_OK)
                     throw new Exception("文件读取失败");

             //读文件流
            DataInputStream in = new DataInputStream(urlCon.getInputStream());
             DataOutputStream out = new DataOutputStream(new FileOutputStream(fileLocal));
             byte[] buffer = new byte[2048];
             int count = 0;
             while ((count = in.read(buffer)) > 0) {
                     out.write(buffer, 0, count);


         }
        out.close();
        in.close();
    }
    public static void main(String[] args) {
        /*String HTMLText="<p>我的<br/>评论</p>";
        String t1 = "&lt;p&gt;我的&lt;br/&gt;评论&lt;/p&gt";
        String text=htmlEncode(HTMLText);
        System.out.println(text);*/
        try{
            HtmlUtils.downloadFile("https://webduu.ehshig.net/duu/smallmp3files/Nomin%20Talst%20-%2013%20Hairiin%20deed%20hani%20mini.mp3","D:\\test\\1.mp3");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static String htmlEncode(String source) {
        if(source == null) {
            return "";
        }
        String html = "";
        StringBuffer buffer = new StringBuffer();
        for(int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            switch (c) {
                case '<':
                    buffer.append("<");
                    break;
                case '>':
                    buffer.append(">");
                    break;
                case '&':
                    buffer.append("&");
                    break;
                case '"':
                    buffer.append("\"");
                    break;
                case 10:
                case 13:
                    break;
                default:
                    buffer.append(c);
            }
        }
        html = buffer.toString();
        return html;
    }


}
