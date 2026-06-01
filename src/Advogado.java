import java.awt.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;

public class Advogado extends Personagem {
    
    private Image[] spritesCorrida;
    private boolean spritesCarregados = false;
    
    // ================= CONSTRUTOR =================
    Advogado(int x, int y) {
        super(x, y, 88, 96);
        carregarSprites();
    }
    
    private void carregarSprites() {
        try {
            spritesCorrida = new Image[6];
            for (int i = 0; i < 6; i++) {
                URL imgURL = getClass().getResource("/Assets/Sprites/advogado-frame-" + (i + 1) + ".png");
                if (imgURL != null) {
                    spritesCorrida[i] = ImageIO.read(imgURL);
                } else {
                    System.out.println("ERRO: Sprite frame " + (i + 1) + " não encontrado.");
                }
            }
            spritesCarregados = true;
        } catch (IOException e) {
            System.out.println("Erro ao carregar sprites do advogado: " + e.getMessage());
            spritesCarregados = false;
        }
    }
    
    // ================= HABILIDADE =================
    @Override
    public void ativarHabilidade() {
        habilidadeAtiva = true;
        intangivel = true;
        obstaculosAtravessados = 0;
    }
    
    @Override
    Rectangle getBounds() {
        // Tight, centered hitbox (width 40, height 72) fitting the lawyer sprite perfectly
        return new Rectangle(x + 24, y + 20, 40, 72);
    }
    
    @Override
    void desenhar(Graphics g) {
        if (spritesCarregados && spritesCorrida != null) {
            int frameIdx = (ticks / 6) % 6;
            Image sprite = spritesCorrida[frameIdx];
            if (sprite != null) {
                g.drawImage(sprite, x, y, largura, altura, null);
                
                // Draw the floating law book and aura if active
                desenharLivroFlutuante(g);
                return;
            }
        }
        
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
        desenharLivroFlutuante(g);
    }

    private void desenharLivroFlutuante(Graphics g) {
        if (!intangivel) return;

        // 1. Warm Golden Aura Cast over the Lawyer
        g.setColor(new Color(255, 215, 0, 45)); // Translucent gold
        g.fillOval(x - 12, y - 8, largura + 24, altura + 16);

        // 2. Floating Vade Mecum (Law Book) math
        int bobOffset = (int) (Math.sin(ticks * 0.1) * 5); // Smooth sine wave bobbing
        int bookWidth = 26;
        int bookHeight = 18;
        int bookX = x + (largura / 2) - (bookWidth / 2);
        int bookY = y - 26 + bobOffset;

        // Draw Pages (White edges)
        g.setColor(Color.WHITE);
        g.fillRect(bookX + 22, bookY + 2, 4, bookHeight - 4); // right pages
        g.fillRect(bookX + 2, bookY + 14, bookWidth - 4, 4); // bottom pages

        // Draw Leather Cover (Dark Burgundy/Brown)
        g.setColor(new Color(128, 0, 0)); // burgundy
        g.fillRect(bookX + 2, bookY, bookWidth - 4, bookHeight - 2); // front cover
        g.fillRect(bookX, bookY, 4, bookHeight); // spine

        // Contours & Page Separation Lines
        g.setColor(Color.BLACK);
        g.drawRect(bookX, bookY, bookWidth, bookHeight); // outer border
        g.drawRect(bookX + 22, bookY + 2, 4, bookHeight - 4); // pages boundary right
        g.drawRect(bookX + 2, bookY + 14, bookWidth - 4, 4); // pages boundary bottom

        // Draw Gold Trim / "LEI" lettering on the cover
        g.setColor(new Color(255, 215, 0)); // Gold
        g.fillRect(bookX + 1, bookY + 2, 2, bookHeight - 4); // gold spine stripes
        g.setFont(new Font("Arial", Font.BOLD, 8));
        g.drawString("LEI", bookX + 6, bookY + 12);
    }
}