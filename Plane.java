import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

//for debug
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Plane extends Entity
{
    private ArrayList<Gun> guns;
    
    private Group displayGroup;
    private ImageView img;
    private Image planeImage;
    
    private ArrayList<Rectangle> hitbox;
    public boolean displayHitbox;
    private Circle debugCircle;
    
    //important stats
    private int maxHealth;
    private int health;
    private int money;

    public Plane(double xStart, double yStart, double speed, double turn, String imageName, int startHealth)
    {
        super(xStart, yStart, Math.PI/-2, speed, turn);
        displayGroup = new Group();
        
        planeImage = new Image(imageName);
        img = new ImageView(planeImage);
        displayGroup.getChildren().add(img);

        guns = new ArrayList<Gun>();
        
        
        /*
         * generating hitbox
         */
        hitbox = new ArrayList<Rectangle>();
        displayHitbox = true;
        
        //mess with these variables to change the size of the hitbox
        int planeRearLength = 58;
        int planeFrontLength = 53;
        int planeWidth = 20;
        int engineSpan = 55;
        int engineLength = 31;
        int wingWidth = 22;
        int wingRearLength = 2;
        int wingForwardLength = 17;
        int wingDistance = engineSpan/2;
        
        /*
         * DON'T mess with the next part:
         * 
         * the way these vairables are used may be confusing or counterintuitive, but that
         * is because the hitbox starts SIDEWAYS in relation to the plane texture
         */
        Rectangle bodyHitbox = new Rectangle(xPos()-planeRearLength, yPos()-planeWidth/2, planeRearLength+planeFrontLength, planeWidth);
        hitbox.add(bodyHitbox);
        displayGroup.getChildren().add(bodyHitbox);
        
        Rectangle engineWing = new Rectangle(xPos(), yPos()-engineSpan/2, engineLength, engineSpan);
        hitbox.add(engineWing);
        displayGroup.getChildren().add(engineWing);
        
        Rectangle wingLeft = new Rectangle(xPos()-wingRearLength, yPos()-wingDistance-wingWidth, wingForwardLength+wingRearLength, wingWidth);
        hitbox.add(wingLeft);
        displayGroup.getChildren().add(wingLeft);
        
        Rectangle wingRight = new Rectangle(xPos()-wingRearLength, yPos()+wingDistance, wingForwardLength+wingRearLength, wingWidth);
        hitbox.add(wingRight);
        displayGroup.getChildren().add(wingRight);
        
        if(displayHitbox){
            debugCircle = new Circle(xPos(), yPos(), 10);
            displayGroup.getChildren().add(debugCircle);
        }
        
        
        maxHealth = startHealth;
        health = startHealth;
        money = 0;
    }

    @Override
    public void update(){
        super.update();
        
        for (Rectangle r : hitbox){
            r.setX(r.getX()+dX());
            r.setY(r.getY()+dY());
            r.getTransforms().clear();
            r.getTransforms().add(new Rotate(Math.toDegrees(angle()), xPos(), yPos()));
        }
    }
    
    public boolean containsPoint(Point2D p){
        boolean cont = false;
        for(Rectangle r : hitbox){
            cont = cont || r.contains(p);
        }
        return cont;
    }
    public boolean collide(Entity e){
        boolean col = false;
        for(Rectangle r : hitbox){
            col = col || e.containsPoint(new Point2D(r.getX(), r.getY()));
            col = col || e.containsPoint(new Point2D(r.getX()+r.getWidth(), r.getY()));
            col = col || e.containsPoint(new Point2D(r.getX(), r.getY()+r.getHeight()));
            col = col || e.containsPoint(new Point2D(r.getX()+r.getWidth(), r.getY()+r.getHeight()));
        }
        return col;
    }
    
    public Node display(){
        img.setRotate(Math.toDegrees(angle()));
        img.setX(xPos() - (planeImage.getWidth()/2));
        img.setY(yPos() - (planeImage.getHeight()/2));
        
        //for debugging
        if(displayHitbox){
            for(Rectangle r : hitbox){
                r.setStroke(Color.BLACK);
                r.setFill(Color.TRANSPARENT);
                r.setStrokeWidth(2.0);
            }
            debugCircle.setCenterX(xPos());
            debugCircle.setCenterY(yPos());
        }

        return displayGroup;
    }

    public void addGun(Gun gun){
        if(guns.size() < 2){        //max number of guns is currently 2
            guns.add(gun);
            if(guns.size() == 2){
                guns.get(0).setXOffset(10);
                guns.get(1).setXOffset(-10);
            }
        }
    }

    public ArrayList<Bullet> fireGuns(){
        ArrayList<Bullet> bullets = new ArrayList<Bullet>();

        for(Gun g : guns){
            Bullet b = g.fire(xPos(), yPos(), angle());
            if(b != null){
                bullets.add(b);
            }
        }
        return bullets;
    }
    
    public int getHealth(){
        return health;
    }
    public void setHealth(int amount){
        health = amount;
        if(health < 0){
            health = 0;
        }
        else if(health > maxHealth){
            health = maxHealth;
        }
    }
    public void addHealth(int amount){
        health += amount;
        if(health < 0){
            health = 0;
        }
        else if(health > maxHealth){
            health = maxHealth;
        }
    }
    public void subHealth(int amount){
        health -= amount;
        if(health < 0){
            health = 0;
        }
        else if(health > maxHealth){
            health = maxHealth;
        }
    }
    
    public int getMaxHealth(){
        return maxHealth;
    }
    
    public int getMoney(){
        return money;
    }
    public void setMoney(int amount){
        money = amount;
        if(money < 0)
            money = 0;
    }
    public void addMoney(int amount){
        money += amount;
        if(money < 0)
            money = 0;
    }
    public void subMoney(int amount){
        money -= amount;
        if(money < 0)
            money = 0;
    }
    
    public int getAmmo(){
        int totalAmmo = 0;
        for(Gun g : guns){
            totalAmmo += g.getAmmo();
        }
        return totalAmmo;
    }
    
    public int getMaxAmmo(){
        int totalAmmo = 0;
        for(Gun g : guns){
            totalAmmo += g.getMaxAmmo();
        }
        return totalAmmo;
    }
}
