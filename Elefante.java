import java.awt.*;

public class Elefante extends Personagem {

    boolean habilidadeAtiva = false;

    boolean intangivel = false;

    int blocosAtravessados = 0;

    int ticks = 0;

    Elefante(int x, int y) {

        super(x, y);
    }

    void ativarHabilidade() {

        habilidadeAtiva = true;

        intangivel = true;

        blocosAtravessados = 0;
    }

    @Override
    void atualizar() {
        super.atualizar();
        if (noChao) {
            ticks++;
        } else {
            ticks = 0;
        }
    }

    @Override
    Rectangle getBounds() {

        return new Rectangle(
                x + 18,
                y + 18,
                52,
                70);
    }

    @Override
    void desenhar(Graphics g) {

        int offsetPerna1 = 24;
        int offsetPerna2 = 54;
        int offsetCaudaX = 10;
        int offsetTufoX = 9;
        int offsetTrombaH = 24;
        int offsetPontaX = 58;
        int offsetPontaY = 58;
        int offsetPontaW = 20;

        if (noChao) {
            if ((ticks / 8) % 2 == 0) {
                offsetPerna1 = 20;
                offsetPerna2 = 58;
                offsetCaudaX = 10;
                offsetTufoX = 9;
                offsetTrombaH = 24;
                offsetPontaX = 58;
                offsetPontaY = 58;
                offsetPontaW = 20;
            } else {
                offsetPerna1 = 28;
                offsetPerna2 = 50;
                offsetCaudaX = 6;
                offsetTufoX = 5;
                offsetTrombaH = 20;
                offsetPontaX = 60;
                offsetPontaY = 54;
                offsetPontaW = 20;
            }
        } else {
            offsetPerna1 = 20;
            offsetPerna2 = 50;
            offsetCaudaX = 10;
            offsetTufoX = 9;
            offsetTrombaH = 24;
            offsetPontaX = 58;
            offsetPontaY = 58;
            offsetPontaW = 20;
        }

        // ================= CAUDA =================
        // Cor da base da cauda (cinza ligeiramente mais escuro que o corpo principal)
        g.setColor(new Color(150, 155, 175));
        // Conexão horizontal ao corpo (começa em offsetCaudaX e conecta ao corpo em
        // x+18)
        g.fillRect(x + offsetCaudaX, y + 34, 18 - offsetCaudaX, 4);
        // Parte vertical pendente da cauda
        g.fillRect(x + offsetCaudaX, y + 38, 4, 16);
        // Tufo da cauda (pelo preto no final da cauda)
        g.setColor(new Color(80, 80, 80));
        g.fillRect(x + offsetTufoX, y + 54, 6, 8);

        // Contornos da Cauda
        g.setColor(Color.BLACK);
        g.drawRect(x + offsetCaudaX, y + 34, 18 - offsetCaudaX, 4);
        g.drawRect(x + offsetCaudaX, y + 38, 4, 16);
        g.drawRect(x + offsetTufoX, y + 54, 6, 8);

        // Limpeza do contorno de sobreposição dentro da cauda
        g.setColor(new Color(150, 155, 175));
        g.fillRect(x + offsetCaudaX + 1, y + 35, 18 - offsetCaudaX - 2, 2);
        g.fillRect(x + offsetCaudaX + 1, y + 37, 2, 2);

        // ================= CORPO E CABEÇA =================
        g.setColor(new Color(170, 175, 195));
        // Corpo Arredondado
        g.fillRoundRect(x + 18, y + 18, 52, 44, 16, 16);
        // Cabeça Arredondada
        g.fillRoundRect(x + 56, y + 22, 28, 32, 10, 10);

        // ================= ORELHA =================
        g.setColor(new Color(150, 155, 175));
        // Orelha Arredondada
        g.fillRoundRect(x + 36, y + 10, 22, 34, 12, 12);

        // ================= PERNAS =================
        g.setColor(new Color(170, 175, 195));
        // Perna 1
        g.fillRect(x + offsetPerna1, y + 56, 14, 34);
        // Perna 2
        g.fillRect(x + offsetPerna2, y + 56, 14, 34);

        // ================= TROMBA E QUEIXO =================
        g.fillRect(x + 66, y + 44, 12, offsetTrombaH);
        g.fillRect(x + offsetPontaX, y + offsetPontaY, offsetPontaW, 10);

        // ================= UNHAS DAS PATAS =================
        g.setColor(Color.WHITE);
        // Unhas para a Perna 1
        g.fillRect(x + offsetPerna1 + 2, y + 84, 4, 6);
        g.fillRect(x + offsetPerna1 + 8, y + 84, 4, 6);
        // Unhas para a Perna 2
        g.fillRect(x + offsetPerna2 + 2, y + 84, 4, 6);
        g.fillRect(x + offsetPerna2 + 8, y + 84, 4, 6);

        // ================= MARFIM =================
        g.setColor(Color.WHITE);
        g.fillRect(x + 74, y + 44, 10, 6);
        g.fillRect(x + 80, y + 38, 6, 6);

        // ================= CONTORNOS =================
        g.setColor(Color.BLACK);
        g.drawRoundRect(x + 18, y + 18, 52, 44, 16, 16);
        g.drawRoundRect(x + 56, y + 22, 28, 32, 10, 10);
        g.drawRoundRect(x + 36, y + 10, 22, 34, 12, 12);

        g.drawRect(x + offsetPerna1, y + 56, 14, 34);
        g.drawRect(x + offsetPerna2, y + 56, 14, 34);

        g.drawRect(x + 66, y + 44, 12, offsetTrombaH);
        g.drawRect(x + offsetPontaX, y + offsetPontaY, offsetPontaW, 10);

        // Contornos das Unhas
        g.drawRect(x + offsetPerna1 + 2, y + 84, 4, 6);
        g.drawRect(x + offsetPerna1 + 8, y + 84, 4, 6);
        g.drawRect(x + offsetPerna2 + 2, y + 84, 4, 6);
        g.drawRect(x + offsetPerna2 + 8, y + 84, 4, 6);

        // Contornos do Marfim
        g.drawRect(x + 74, y + 44, 10, 6);
        g.drawRect(x + 80, y + 38, 6, 6);

        // Limpeza do Contorno do Marfim
        g.setColor(Color.WHITE);
        g.fillRect(x + 75, y + 45, 8, 4);
        g.fillRect(x + 79, y + 40, 2, 8);

        // ================= OLHO EXPRESSIVO =================
        // Branco do Olho
        g.setColor(Color.WHITE);
        g.fillRect(x + 70, y + 28, 6, 8);
        // Contorno do Olho
        g.setColor(Color.BLACK);
        g.drawRect(x + 70, y + 28, 6, 8);
        // Pupila do Olho
        g.fillRect(x + 73, y + 30, 3, 4);

        // ================= ESCUDO DE INTANGIBILIDADE =================
        if (intangivel) {

            g.setColor(
                    new Color(0, 255, 255, 80));

            g.fillOval(
                    x - 10,
                    y - 10,
                    120,
                    120);
        }
    }
}