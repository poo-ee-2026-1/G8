import java.awt.*;

public class Personagem extends Entidade {

    double velY = 0;

    boolean noChao = true;

    double gravidade = 0.8;

    Personagem(int x, int y) {

        super(x, y, 96, 96);
    }

    void pular() {

        if (noChao) {

            velY = -12;

            noChao = false;
        }
    }

    @Override
    void atualizar() {

        velY += gravidade;

        y += velY;

        if (y >= 250) {

            y = 250;

            velY = 0;

            noChao = true;
        }
    }

    @Override
    void desenhar(Graphics g) {

        g.setColor(Color.GREEN);

        g.fillRect(x, y, largura, altura);
    }
}
