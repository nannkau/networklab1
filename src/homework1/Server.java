package homework1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class Server {
    private Socket socket = null;
    private ServerSocket server = null;
    BufferedWriter out = null;
    BufferedReader in = null;
    public Server(int port)
    {
        try
        {
            server = new ServerSocket(port);
            System.out.println("Server started");
            System.out.println("Waiting for a client ...");
            socket = server.accept();
            System.out.println("Client accepted");
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = "";
            Map<String, String> map= getMap("dictionary.txt");
            while (true)
            {
                try
                {
                    line = in.readLine().toLowerCase();
                    System.out.println("Server received: " + line);
                    if (line.equals("bye"))
                        break;
                   MapDTO mapDTO= getData(line,map);
                   map=mapDTO.getStringMap();
//                    String word=map.get(line)!=null ?line+":"+map.get(line): "cannot find word in dictionary";
                    out.write(mapDTO.getNotification());
                    out.newLine();
                    out.flush();
                }
                catch(IOException i)
                {
                    System.err.println(i);
                }
            }
            System.out.println("Write data to file");
            writeToFile(map);
            System.out.println("Closing connection");

            in.close();
            out.close();
            socket.close();
            server.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }
    public MapDTO getData(String line,Map<String,String> map){
        MapDTO mapDTO= new MapDTO();
        mapDTO.setNotification("cannot find word in dictionary");
        mapDTO.setStringMap(map);
        String keyPattern=  "^[a-zA-Z ]+$";
        String valuePattern="^[a-zA-Z_ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶ" +
                "ẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợ" +
                "ụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s]+$";
        StringTokenizer st = new StringTokenizer(line, ";");
        String keyword=st.nextToken();

        if("add".equals(keyword)){

            String key=st.nextToken();
            String value=st.nextToken();
            if(Pattern.matches(keyPattern,key) && Pattern.matches(valuePattern,value)){
                map.put(key,value);
                mapDTO.setNotification("successful");
                mapDTO.setStringMap(map);
            }
            else {
                mapDTO.setNotification("can not add");
                mapDTO.setStringMap(map);
            }

        }
        else {
            if ("del".equals(keyword)){
                String key=st.nextToken();
                if (Pattern.matches(keyPattern,key)){
                    map.remove(key);
                    mapDTO.setNotification("successful");
                    mapDTO.setStringMap(map);
                }
                else {
                    mapDTO.setNotification("can not delete");
                    mapDTO.setStringMap(map);
                }
            }
            else {
                if(map.get(line)!=null){
                    String data=line+":"+map.get(line);
                    mapDTO.setNotification(data.toString());
                    mapDTO.setStringMap(map);
                }
                else {
                    map.forEach((key,value)->{
                        if(value.equals(line)) {
                            String data=line+":"+key;
                            mapDTO.setNotification(data.toString());
                            mapDTO.setStringMap(map);
                        }


                    });
                }

            }
        }
        return mapDTO;
    }
    public Map<String, String> getMap(String url){
        Map<String, String> map= new HashMap<String,String>();
        try
        {

            FileInputStream fis=new FileInputStream(url);
            Scanner sc=new Scanner(fis);

            while(sc.hasNextLine())
            {
                String line=sc.nextLine();
                StringTokenizer st = new StringTokenizer(line, ";");
                String key=st.nextToken();
                String value=st.nextToken();
                map.put(key, value);
            }
            sc.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return map;
    }
    public static void writeToFile(Map<String,String> map) {
        try {
            File file=new File("dictionary.txt");
            if (file.exists()) {
                file.delete(); //you might want to check if delete was successfull
            }
            file.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            for (Map.Entry<String, String> entry : map.entrySet()) {
               String line=entry.getKey() + ";" + entry.getValue();
                bw.write(line);
                bw.newLine();
            }
            bw.close();
        } catch (Exception e) {
        }
    }
    public static void main(String args[])
    {
        Server server = new Server(6000);
    }
}