import java.awt.*;
public class Raio extends Item {

    double velocidade;

    Raio(
            int x,
            int y,
            double multiplicador) {

        super(x, y);

        velocidade = 5 * multiplicador;
    }

    @Override
    void atualizar() {

        x -= velocidade;
    }

    @Override
    void desenhar(Graphics g) {

        g.setColor(Color.YELLOW);

        int[] xs = {
                x + 10,
                x + 4,
                x + 12,
                x + 8,
                x + 16,
                x + 10
        };

        int[] ys = {
                y,
                y + 10,
                y + 10,
                y + 20,
                y + 20,
                y + 30
        };

        g.fillPolygon(xs, ys, xs.length);
    }

   @Override
              void coletar(Personagem p) {
                     if (p instanceof Elefante) {
                    
                ((Elefante) p).ativarHabilidade();
            }
                
                else if (p instanceof Advogado){
             ((Advogado) p).ativarHabilidade();
             }
                                            }
}
