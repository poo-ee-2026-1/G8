
CLASSE Elefante
class Elefante {
    private int x, y;
    private int width = 35, height = 45;
    private double ySpeed = 0;
    private boolean isJumping = false;
    private boolean isDucking = false;
    private int normalHeight = 45;
    private int duckHeight = 25;
    private static final double GRAVITY = 0.8;
    private static final int GROUND_Y = 400;
    private int animationFrame = 0;
    
    public Elefante() {
        x = 100;
        y = GROUND_Y - height;
    }
    
    public void jump() {
        if (!isJumping && !isDucking) {
            isJumping = true;
            ySpeed = -12;
        }
    }
    
    public void duck() {
        if (!isJumping) {
            isDucking = true;
            height = duckHeight;
            y = GROUND_Y - height;
        }
    }
    
    public void stand() {
        if (!isJumping) {
            isDucking = false;
            height = normalHeight;
            y = GROUND_Y - height;
        }
    }
    
    public void update() {
        animationFrame++;
        
        if (isJumping) {
            y += ySpeed;
            ySpeed += GRAVITY;
            
            if (y >= GROUND_Y - height) {
                y = GROUND_Y - height;
                isJumping = false;
                ySpeed = 0;
            }
        }
    }
    
    public void draw(Graphics g) {
        // Corpo do dinossauro
        g.setColor(new Color(80, 80, 80));
        
        if (!isDucking) {
            // Corpo principal
            g.fillRect(x, y, width, height);
            
            // Pescoço e cabeça
            g.fillRect(x + width - 10, y - 15, 15, 20);
            g.fillOval(x + width - 8, y - 20, 18, 18);
            
            // Olho
            g.setColor(Color.WHITE);
            g.fillOval(x + width - 5, y - 18, 8, 8);
            g.setColor(Color.BLACK);
            g.fillOval(x + width - 3, y - 16, 4, 4);
            
            // Dentes (se estiver pulando)
            if (isJumping) {
                g.setColor(Color.WHITE);
                g.fillRect(x + width - 3, y - 12, 3, 5);
            }
            
            // Pernas
            g.setColor(new Color(60, 60, 60));
            if (animationFrame % 40 < 20) {
                g.fillRect(x + 5, y + height - 10, 8, 12);
                g.fillRect(x + 20, y + height - 15, 8, 17);
            } else {
                g.fillRect(x + 5, y + height - 15, 8, 17);
                g.fillRect(x + 20, y + height - 10, 8, 12);
            }
            
            // Rabo
            g.fillRect(x - 10, y + height - 20, 12, 8);
        } else {
            // Versão agachada
            g.fillRect(x, y, width, height);
            g.fillOval(x + width - 10, y + 5, 12, 12);
            
            
            g.setColor(Color.WHITE);
            g.fillOval(x + width - 8, y + 7, 6, 6);
            g.setColor(Color.BLACK);
            g.fillOval(x + width - 6, y + 8, 3, 3);
        }
        
        
        g.setColor(new Color(50, 50, 50));
        for (int i = 0; i < 3; i++) {
            g.fillRect(x + 5 + i*8, y - 5, 4, 8);
        }
    }
    
    public Rectangle getBounds() {
        if (isDucking) {
            return new Rectangle(x, y + 10, width, height - 10);
        }
        return new Rectangle(x, y, width, height);
    }
}

 CLASSE Obstaculo 
abstract class obstaculo {
    protected int x, y, width, height;
    protected int speed;
    
    public obstaculo(int startX, int groundY) {
        this.x = startX;
        this.speed = 8;
    }
    
    public void update() {
        x -= speed;
    }
    
    public abstract void draw(Graphics g);
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
    public boolean isOffScreen() {
        return x + width < 0;
    }
}

