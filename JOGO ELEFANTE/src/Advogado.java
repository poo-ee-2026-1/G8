import java.awt.*;

public class Advogado extends Personagem {
    
    // ================= PROPRIEDADES =================
    boolean habilidadeAtiva = false;
    boolean intangivel = false;
    int processosGanhos = 0;
    int ticks = 0;
    
    // ================= CONSTRUTOR =================
    Advogado(int x, int y) {
        super(x, y);
        this.largura = 88;
        this.altura = 96;
    }
    
    // ================= HABILIDADE =================
    void ativarHabilidade() {
        habilidadeAtiva = true;
        intangivel = true;
        processosGanhos = 0;
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
        return new Rectangle(x + 15, y + 20, 58, 68);
    }
    
    @Override
    void desenhar(Graphics g) {
        
        // ================= ANIMAÇÃO SIMPLES DAS PERNAS =================
        int offsetPernaEsq = 20;
        int offsetPernaDir = 58;
        
        if (noChao) {
            if ((ticks / 8) % 2 == 0) {
                offsetPernaEsq = 18;
                offsetPernaDir = 60;
            } else {
                offsetPernaEsq = 24;
                offsetPernaDir = 54;
            }
        } else {
            offsetPernaEsq = 18;
            offsetPernaDir = 54;
        }
        
        // ================= CORPO (TERNO) =================
        // Paletó
        g.setColor(new Color(25, 25, 112)); // Azul marinho
        g.fillRoundRect(x + 15, y + 25, 60, 55, 12, 12);
        
        // Lapelas
        g.setColor(new Color(30, 30, 120));
        int[] lapelaEsq = {x + 30, x + 20, x + 30, x + 38};
        int[] lapelaEsqY = {y + 30, y + 45, y + 50, y + 38};
        g.fillPolygon(lapelaEsq, lapelaEsqY, 4);
        
        int[] lapelaDir = {x + 60, x + 70, x + 60, x + 52};
        int[] lapelaDirY = {y + 30, y + 45, y + 50, y + 38};
        g.fillPolygon(lapelaDir, lapelaDirY, 4);
        
        // Botões
        g.setColor(new Color(192, 192, 192));
        g.fillOval(x + 42, y + 45, 6, 6);
        g.fillOval(x + 42, y + 58, 6, 6);
        
        // Gravata
        g.setColor(new Color(220, 20, 60));
        int[] gravataX = {x + 42, x + 48, x + 42, x + 36};
        int[] gravataY = {y + 40, y + 55, y + 70, y + 55};
        g.fillPolygon(gravataX, gravataY, 4);
        g.fillRect(x + 39, y + 38, 12, 6);
        
        // Calças
        g.setColor(new Color(40, 40, 80));
        g.fillRect(x + offsetPernaEsq, y + 72, 16, 28);
        g.fillRect(x + offsetPernaDir, y + 72, 16, 28);
        
        // Sapatos
        g.setColor(new Color(20, 20, 20));
        g.fillRect(x + offsetPernaEsq, y + 92, 18, 8);
        g.fillRect(x + offsetPernaEsq + 12, y + 88, 8, 4);
        g.fillRect(x + offsetPernaDir, y + 92, 18, 8);
        g.fillRect(x + offsetPernaDir + 12, y + 88, 8, 4);
        
        // Braços
        g.setColor(new Color(25, 25, 112));
        g.fillRoundRect(x + 15, y + 35, 14, 35, 8, 8);
        g.fillRoundRect(x + 65, y + 35, 14, 35, 8, 8);
        
        // Mãos
        g.setColor(new Color(255, 220, 180));
        g.fillOval(x + 17, y + 65, 10, 10);
        g.fillOval(x + 67, y + 65, 10, 10);
        
        // Pasta de processos
        g.setColor(new Color(160, 100, 60));
        g.fillRect(x + 73, y + 58, 14, 18);
        g.setColor(new Color(200, 140, 80));
        g.fillRect(x + 75, y + 60, 10, 14);
        
        // ================= CABEÇA CARECA =================
        // Pele
        g.setColor(new Color(255, 220, 180));
        g.fillRoundRect(x + 30, y + 8, 30, 32, 14, 14);
        
        // Careca (cabeça lisa sem cabelo)
        // Apenas um leve brilho na careca
        g.setColor(new Color(245, 225, 200));
        g.fillOval(x + 35, y + 4, 20, 14);
        
        // Olhos
        g.setColor(Color.WHITE);
        g.fillOval(x + 36, y + 24, 5, 4);
        g.fillOval(x + 50, y + 24, 5, 4);
        
        g.setColor(Color.BLACK);
        g.fillOval(x + 38, y + 25, 2, 2);
        g.fillOval(x + 52, y + 25, 2, 2);
        
        // Sobrancelhas
        g.setColor(new Color(40, 40, 50));
        g.fillRect(x + 34, y + 20, 8, 2);
        g.fillRect(x + 48, y + 20, 8, 2);
        
        // Nariz
        g.setColor(new Color(235, 200, 160));
        g.fillOval(x + 44, y + 30, 4, 3);
        
        // Boca
        g.setColor(new Color(180, 100, 80));
        g.fillArc(x + 40, y + 34, 12, 6, 0, -180);
        
        // Óculos
        g.setColor(Color.BLACK);
        g.drawOval(x + 32, y + 20, 12, 10);
        g.drawOval(x + 46, y + 20, 12, 10);
        g.drawLine(x + 44, y + 25, x + 46, y + 25);
        g.drawLine(x + 32, y + 22, x + 26, y + 18);
        g.drawLine(x + 58, y + 22, x + 64, y + 18);
        
        // Brilho das lentes
        g.setColor(new Color(200, 230, 255, 100));
        g.fillOval(x + 33, y + 21, 10, 8);
        g.fillOval(x + 47, y + 21, 10, 8);
        
        // ================= CONTORNOS =================
        g.setColor(Color.BLACK);
        g.drawRoundRect(x + 15, y + 25, 60, 55, 12, 12);
        g.drawRoundRect(x + 30, y + 8, 30, 32, 14, 14);
        g.drawRect(x + offsetPernaEsq, y + 72, 16, 28);
        g.drawRect(x + offsetPernaDir, y + 72, 16, 28);
        g.drawRoundRect(x + 15, y + 35, 14, 35, 8, 8);
        g.drawRoundRect(x + 65, y + 35, 14, 35, 8, 8);
        
        // ================= ESCUDO DE INTANGIBILIDADE =================
        if (intangivel) {
            g.setColor(new Color(0, 255, 255, 80));
            g.fillOval(x - 15, y - 15, 120, 120);
        }
    }
}