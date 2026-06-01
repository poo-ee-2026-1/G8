import java.awt.*;

public class Processo extends Item {

    double velocidade;

    public Processo(int x, int y, double multiplicador) {
        super(x, y);
        this.velocidade = 5 * multiplicador;
    }

    @Override
    void atualizar() {
        this.velocidade = 5 * Main.multiplicadorVelocidade;
        x -= velocidade;
    }

    @Override
    void coletar(Personagem p) {
        p.ativarHabilidade();
    }

    @Override
    void desenhar(Graphics g) {
        // Draw Sheets of Paper peeking out (White)
        g.setColor(Color.WHITE);
        g.fillRect(x + 4, y - 4, 12, 8);
        g.setColor(Color.BLACK);
        g.drawRect(x + 4, y - 4, 12, 8);

        // Draw Manila Folder Base (Beige)
        g.setColor(new Color(240, 210, 160));
        // Main Body
        g.fillRect(x, y + 4, 20, 16);
        // Folder Tab
        g.fillRect(x, y, 8, 4);

        // Contours
        g.setColor(Color.BLACK);
        g.drawRect(x, y + 4, 20, 16);
        g.drawRect(x, y, 8, 4);

        // Folder Tab clean up overlap line
        g.setColor(new Color(240, 210, 160));
        g.fillRect(x + 1, y + 3, 6, 2);

        // Draw an Official Red Seal
        g.setColor(new Color(220, 40, 40)); 
        g.fillOval(x + 10, y + 10, 6, 6);
        g.setColor(Color.BLACK);
        g.drawOval(x + 10, y + 10, 6, 6);
    }
}
