package Server;

import Server.Background.CSV;
import Server.Background.JSON;
import Server.Background.Shorty;
import Server.Background.Status;
import net.sf.jsefa.DeserializationException;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Scanner;

class Collection {

    private static PriorityQueue<Shorty> collection = new PriorityQueue<>();
    Collection.Commands commands;

    Collection(){
        commands = new Collection.Commands();
        commands.initialise("");
    }

    static void Rest(){
        for (Shorty x : collection) {
            if (x.getStatus() == Status.REPAIRING) x.repairCloth();
            if (x.getStatus() != Status.NONE || x.getStatus() != Status.REPAIRING) {
                x.setStatus(Status.NONE);
            }
        }
    }

    Shorty getBest(){
        Shorty best = new Shorty();
        for (Shorty x : collection) {
            if (x.getFoodCount() > best.getFoodCount()) {
                best = x;
            }
        }
        return best;
    }

    private float getMax(){
        float max = 0;
        while (collection.iterator().hasNext()){
            Shorty x = collection.iterator().next();
            if(x.getPower()>max){
                max = x.getPower();
            }
        }
        return max;
    }

    private String iDate(){
        final DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        for (Shorty x : collection) {
            if (x.getDate().before(date)) {
                date = x.getDate();
            }
        }
        return sdf.format(date);
    }

    Shorty getRandomMember(){
        int it = 0;
        Shorty [] x = new Shorty[collection.size()];
        for (Shorty shorty : collection) {
            x[it] = shorty;
            it++;
        }
        float min = Float.MIN_VALUE;
        float max = 0;
        int c = 2;
        float comp;
        int res;

        for (Shorty shorty : x) {
            if (shorty.getHeight() > max) {
                max = shorty.getHeight();
            }
            if (shorty.getHeight() < min) {
                min = shorty.getHeight();
            }
        }
        while (c>0){
            comp = (float) Math.random()*max;
            for(int i = 0; i<x.length;i++){
                if (x[i]!=null){
                    if (x[i].getHeight()<comp){
                        x[i] = null;
                    }
                }
            }
            c--;
        }
        do{
            res = (int)Math.round(Math.random()*(x.length-1));
            if(x[res]!=null){
                return x[res];
            }
        }
        while (x[res]!=null);
        return null;
    }



    public class Commands {

        void initialise(String key){
            try {
                collection = new PriorityQueue<>();
                Scanner reader = new Scanner(getClass().getResourceAsStream("/Server/collectionStorage.csv"));
                while (reader.hasNext()){
                    Date date = new Date();
                    String current = reader.nextLine();
                    if(!key.equals("-l")){
                         CSV.fromCSV(current).setDate(date);
                         CSV.fromCSV(current).dontWorryImDressed();
                    }
                    collection.add(CSV.fromCSV(current));
                }
                reader.close();
            }
            catch (DeserializationException e){
                System.err.println("Ошибка представления данных в файле!");
            }
        }

        void load(){
            initialise("-l");
        }

        void add(String jsonObject){
            try{
                if(JSON.fromJSON(jsonObject).getDate()==null){
                    throw new NullPointerException();
                }
                collection.add(JSON.fromJSON(jsonObject));
            }
            catch (NullPointerException e){
                System.err.println("Неправильно задан объект!");
            }
        }

        void addIfMax(String jsonObject){
            try {
                if (JSON.fromJSON(jsonObject).getPower() > getMax()) {
                    add(jsonObject);
                }
            }
            catch (NullPointerException e){
                System.err.println("Неправильно задан объект!");
            }
        }

        void remove(String jsonObject){
            collection.remove(JSON.fromJSON(jsonObject));
        }

        void removeLast() {
            int i = collection.size();
            try {
                if (i == 0) {
                    throw new Exception("");
                } else {
                    for (Iterator<Shorty> iterator = collection.iterator(); iterator.hasNext(); ) {
                        iterator.next();
                        if (i == 1) {
                            iterator.remove();
                        }
                        i--;
                    }
                }
            } catch (Exception e) {
                System.err.println("Коллекция пуста!");
            }
        }

        String show(){
            return collection.toString();
        }

        String info(){
            return ("Shorty"+"/n"+collection.size()+"/n"+iDate());
        }

        void save(){
            try{
                BufferedOutputStream bfos=new BufferedOutputStream(new FileOutputStream("collectionStorage.csv", true));
                for (Shorty x : collection) {
                    byte[] b = CSV.toCSV(x).getBytes();
                    bfos.write(b);
                }
                bfos.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
            System.out.println("Let the battle begins!");
        }
    }
}
//+