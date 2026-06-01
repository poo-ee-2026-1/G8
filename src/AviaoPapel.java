import java.awt.*;

public class AviaoPapel extends Obstaculo {

    public AviaoPapel(int x, int y, double multiplicador) {
        super(x, y, 36, 16, multiplicador);
    }

    @Override
    void desenhar(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Turn on antialiasing for smooth paper fold lines
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // If suspended, rotate around its center for the flying/spinning effect
        if (suspenso) {
            g2d.translate(x + largura / 2, y + altura / 2);
            g2d.rotate(rotacao);
            g2d.translate(-largura / 2, -altura / 2);
        } else {
            g2d.translate(x, y);
        }

        // 1. Draw High-Speed Motion Trails behind the paper plane (translucent white)
        g2d.setColor(new Color(255, 255, 255, 140));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawLine(40, 3, 48, 3);
        g2d.drawLine(38, 8, 44, 8);
        g2d.drawLine(40, 13, 47, 13);

        // 2. Draw Bottom Folder Crease (Shadowed Grey)
        g2d.setColor(new Color(210, 215, 225));
        int[] bottomX = { 12, 36, 24 };
        int[] bottomY = { 8, 0, 8 };
        g2d.fillPolygon(bottomX, bottomY, 3);
        
        // 3. Draw Main Wing Body (Pure White)
        g2d.setColor(Color.WHITE);
        int[] wingX = { 0, 36, 24 };
        int[] wingY = { 8, 0, 16 };
        g2d.fillPolygon(wingX, wingY, 3);

        // 4. Outlines (Precise and Sleek)
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1.0f));
        g2d.drawPolygon(bottomX, bottomY, 3);
        g2d.drawPolygon(wingX, wingY, 3);

        g2d.dispose();
    }
}
