import java.awt.*;
public class Obstaculo extends Entidade {

    double velocidade;

    Obstaculo(
            int x,
            int y,
            double multiplicador) {

        super(x, y, 30, 40);

        velocidade = 5 * multiplicador;
    }

    @Override
    void atualizar() {

        x -= velocidade;
    }

    @Override
    void desenhar(Graphics g) {

        // ================= BASE DO CONE =================
        g.setColor(new Color(40, 40, 40)); // Base de carvão escuro
        g.fillRect(x, y + 34, 30, 6);

        // Contorno da Base
        g.setColor(Color.BLACK);
        g.drawRect(x, y + 34, 30, 6);

        // ================= CORPO DO CONE =================
        // Polígono Principal do Corpo Laranja
        int[] coneX = { x + 11, x + 19, x + 27, x + 3 };
        int[] coneY = { y, y, y + 34, y + 34 };

        g.setColor(new Color(255, 80, 0)); // Laranja de Segurança
        g.fillPolygon(coneX, coneY, 4);

        // Faixa Branca Refletiva
        int[] bandX = { x + 8, x + 22, x + 24, x + 6 };
        int[] bandY = { y + 12, y + 22, y + 22, y + 12 };
        g.setColor(Color.WHITE);
        g.fillPolygon(bandX, bandY, 4);

        // Contornos
        g.setColor(Color.BLACK);
        g.drawPolygon(coneX, coneY, 4);
        g.drawPolygon(bandX, bandY, 4);

        // ================= ABERTURA SUPERIOR =================
        g.setColor(new Color(40, 40, 40));
        g.fillOval(x + 11, y - 2, 8, 4);
        g.setColor(Color.BLACK);
        g.drawOval(x + 11, y - 2, 8, 4);
    }
}