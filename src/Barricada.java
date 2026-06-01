import java.awt.*;

public class Barricada extends Obstaculo {

    public Barricada(int x, int y, double multiplicador) {
        super(x, y, 48, 40, multiplicador);
    }

    @Override
    void desenhar(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        // If suspended, rotate around its center for the flying/spinning effect
        if (suspenso) {
            g2d.translate(x + largura / 2, y + altura / 2);
            g2d.rotate(rotacao);
            g2d.translate(-largura / 2, -altura / 2);
        } else {
            g2d.translate(x, y);
        }

        // Draw Support Legs (Wood Brown)
        g2d.setColor(new Color(150, 110, 80)); 
        // Left Leg
        g2d.fillRect(8, 20, 6, 20);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(8, 20, 6, 20);

        // Right Leg
        g2d.setColor(new Color(150, 110, 80));
        g2d.fillRect(34, 20, 6, 20);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(34, 20, 6, 20);

        // Draw Board (Yellow Base)
        g2d.setColor(new Color(255, 204, 0)); // Warning Yellow
        g2d.fillRect(0, 4, 48, 16);

        // Draw Slanted Black Stripes
        g2d.setColor(Color.BLACK);
        for (int offset = -16; offset < 48; offset += 20) {
            int[] xs = { offset, offset + 10, offset + 20, offset + 10 };
            int[] ys = { 4, 4, 20, 20 };
            g2d.fillPolygon(xs, ys, 4);
        }

        // Draw Board Border
        g2d.drawRect(0, 4, 48, 16);

        g2d.dispose();
    }
}
