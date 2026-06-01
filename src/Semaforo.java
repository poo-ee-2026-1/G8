import java.awt.*;

public class Semaforo extends Obstaculo {

    public Semaforo(int x, int y, double multiplicador) {
        super(x, y, 18, 42, multiplicador);
    }

    @Override
    void desenhar(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Turn on antialiasing for smooth round lights
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. Draw Hanging Wire/Cable from the top of screen to the traffic light
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawLine(x + 9, 0, x + 9, y);

        // 2. Draw Traffic Light Housing Box (Dark Construction Charcoal)
        g2d.setColor(new Color(40, 40, 40));
        g2d.fillRect(x, y, 18, 42);
        
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1.0f));
        g2d.drawRect(x, y, 18, 42);

        // 3. Draw Lens Visors/Hoods (Slightly overlapping semi-circular tabs above each light)
        g2d.setColor(Color.BLACK);
        g2d.drawArc(x + 2, y + 1, 14, 6, 0, 180);
        g2d.drawArc(x + 2, y + 13, 14, 6, 0, 180);
        g2d.drawArc(x + 2, y + 25, 14, 6, 0, 180);

        // 4. Draw Red Light (Bright glowing active state)
        g2d.setColor(new Color(255, 0, 0)); // Active Red
        g2d.fillOval(x + 3, y + 4, 12, 10);
        
        // Red glow shine reflection
        g2d.setColor(new Color(255, 150, 150, 200));
        g2d.fillOval(x + 5, y + 6, 4, 3);
        
        g2d.setColor(Color.BLACK);
        g2d.drawOval(x + 3, y + 4, 12, 10);

        // 5. Draw Yellow Light (Dark inactive state)
        g2d.setColor(new Color(80, 70, 0)); // Dark amber
        g2d.fillOval(x + 3, y + 16, 12, 10);
        g2d.setColor(Color.BLACK);
        g2d.drawOval(x + 3, y + 16, 12, 10);

        // 6. Draw Green Light (Dark inactive state)
        g2d.setColor(new Color(0, 70, 20)); // Dark forest green
        g2d.fillOval(x + 3, y + 28, 12, 10);
        g2d.setColor(Color.BLACK);
        g2d.drawOval(x + 3, y + 28, 12, 10);

        g2d.dispose();
    }
}
