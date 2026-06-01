import java.awt.*;

public abstract class Entidade {

    int x, y, largura, altura;
    private final Rectangle bounds = new Rectangle();

    Entidade(int x, int y, int w, int h) {

        this.x = x;
        this.y = y;
        this.largura = w;
        this.altura = h;
    }

    abstract void atualizar();

    abstract void desenhar(Graphics g);

    Rectangle getBounds() {
        bounds.setBounds(x, y, largura, altura);
        return bounds;
    }
}