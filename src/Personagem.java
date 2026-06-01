import java.awt.*;

public abstract class Personagem extends Entidade {

    protected double velY = 0;
    protected boolean noChao = true;
    protected double gravidade = 0.8;

    // Common ability properties pushed up to base class
    public boolean habilidadeAtiva = false;
    public boolean intangivel = false;
    public int obstaculosAtravessados = 0;
    protected int ticks = 0;

    Personagem(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    void pular() {
        if (noChao) {
            velY = -12;
            noChao = false;
        }
    }

    // Abstract ability activation method (OCP/LSP)
    public abstract void ativarHabilidade();

    @Override
    void atualizar() {
        velY += gravidade;
        y += velY;

        if (y >= 250) {
            y = 250;
            velY = 0;
            noChao = true;
        }

        // Common animation tick update logic
        if (noChao) {
            ticks++;
        } else {
            ticks = 0;
        }
    }

    @Override
    void desenhar(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(x, y, largura, altura);
    }
}
