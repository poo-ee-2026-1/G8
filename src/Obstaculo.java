import java.awt.*;

public class Obstaculo extends Entidade {

    double velocidade;
    public boolean suspenso = false;
    protected double velY = 0;
    protected double rotacao = 0; // rotation angle for a premium flying/spin effect

    Obstaculo(int x, int y, int w, int h, double multiplicador) {
        super(x, y, w, h);
        velocidade = 5 * multiplicador;
    }

    Obstaculo(
            int x,
            int y,
            double multiplicador) {

        this(x, y, 30, 40, multiplicador);
    }

    @Override
    void atualizar() {
        velocidade = 5 * Main.multiplicadorVelocidade;
        x -= velocidade;

        if (suspenso) {
            y += velY;
            velY += 0.5; // positive gravity pulls it down (creating an arc!)
            rotacao += 0.1; // spins while flying
        }
    }

    public void suspender() {
        this.suspenso = true;
        this.velY = -12; // upward launch kick
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

        // ================= BASE DO CONE =================
        g2d.setColor(new Color(40, 40, 40)); // Charcoal base
        g2d.fillRect(0, 34, 30, 6);

        // Contorno da Base
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 34, 30, 6);

        // ================= CORPO DO CONE =================
        // Polígono Principal do Corpo Laranja
        int[] coneX = { 11, 19, 27, 3 };
        int[] coneY = { 0, 0, 34, 34 };

        g2d.setColor(new Color(255, 80, 0)); // Safety Orange
        g2d.fillPolygon(coneX, coneY, 4);

        // Faixa Branca Refletiva
        int[] bandX = { 8, 22, 24, 6 };
        int[] bandY = { 12, 22, 22, 12 };
        g2d.setColor(Color.WHITE);
        g2d.fillPolygon(bandX, bandY, 4);

        // Contornos
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(coneX, coneY, 4);
        g2d.drawPolygon(bandX, bandY, 4);

        // ================= ABERTURA SUPERIOR =================
        g2d.setColor(new Color(40, 40, 40));
        g2d.fillOval(11, -2, 8, 4);
        g2d.setColor(Color.BLACK);
        g2d.drawOval(11, -2, 8, 4);

        g2d.dispose();
    }
}