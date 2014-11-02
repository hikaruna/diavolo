package jp.diavolo.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    public static List<String[]> load(InputStream inputStream){
        List<String[]> ret = new ArrayList<String[]>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while(true){
                String line = reader.readLine();
                if(line == null){
                    break;
                }
                line = line.trim();
                if(line.startsWith("//")) continue;
                String[] columns = line.split(",",-1);
                ArrayList<String> r = new ArrayList<String>();
                String t = null;
                final String quate = "\"";
                for(String s : columns){
                    if(t == null){
                        if(!s.startsWith(quate)){
                            r.add(s);
                            continue;
                        }
                        if(s.endsWith(quate)){
                            //ここで終わり。
                            r.add(s.substring(0, s.length() - quate.length()));
                        }else{
                            //引用が続く
                            t = new String(s.substring(1, s.length()));
                        }
                    }else{
                        if(!s.endsWith(quate)){
                            t += "," + s;
                            continue;
                        }
                        t += "," + s.substring(0, s.length()-1);
                        r.add(t);
                        t = null;
                    }
                }
                ret.add(r.toArray(new String[r.size()]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
