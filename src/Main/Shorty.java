package Main;

import Environment.Status;
import net.sf.jsefa.csv.annotation.CsvDataType;
import net.sf.jsefa.csv.annotation.CsvField;
import java.util.Date;

@CsvDataType()
public class Shorty extends Person implements Comparable {
    @CsvField(pos = 2)
    private Integer power;
    @CsvField(pos = 3)
    private Date date;
    private boolean isWinner;
    private int foodCount;
    @CsvField(pos = 1)
    private String name;
    @CsvField(pos=4)
    private String CN;
    @CsvField(pos=5)
    private int CHP;

    private String getName(){
        return  this.name;
    }

    public Shorty(){}

    Shorty(String name, int height, int weight, String CName, Integer hp){
        super(height, CName, hp);
        power = weight*height;
        this.name = name;
        CN = CName;
        CHP = hp;
    }

    public void dontWorryImDressed(){
        cloth = new Cloth(CN,CHP);
    }

    void Beat(Shorty shorty){
        isWinner = false;
        if(shorty.getStatus()!= Status.REPAIRING){
            shorty.recieveDamage();
            if (shorty.getStatus() != Status.NONE) shorty.recieveDamage();
            if (shorty.cloth.getIsBroke()) {
                Laugh(shorty);
                System.out.println(this.getName()+" засмеялся над "+shorty.getName());
            }
        }
        System.out.println(this.getName()+" ударил "+shorty.getName());
    }

    private void recieveDamage() throws NullPointerException{
        cloth.Break();
    }

    void Laugh(Human human){
        System.out.println(this.getName()+" засмеялся над "+human.getName());
    }

    private void Laugh(Shorty shorty){
        System.out.println(shorty.getName()+" начал ремонтировать "+shorty.cloth.getName());
        shorty.setStatus(Status.REPAIRING);
        this.setStatus(Status.LAUGHING);
        isWinner = true;
    }

    void Buzz(Shorty shorty){
        if (this.getStatus() != Status.REPAIRING) {
            this.setStatus(Status.BUZZING);
            System.out.println(this.getName()+" одобрительно загудел в сторону "+shorty.getName());
        }
    }

    boolean getIsWinner(){
        return isWinner;
    }

    public int getPower(){
        return power;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public int getFoodCount(){
        return foodCount;
    }

    @Override
    void takeFood(Furnace furnace){
        furnace.foodTaken();
        foodCount++;
        System.out.println(this.getName()+" взял "+furnace.getName());
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
//+