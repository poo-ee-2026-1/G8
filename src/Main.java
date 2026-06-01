import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.imageio.ImageIO;



// ================= MAIN =================
public class Main extends JPanel implements ActionListener, KeyListener {

    Timer timer;

    Personagem jogador;  

    ArrayList<Entidade> entidades;
 
    AudioManager audio = new AudioManager();

    boolean rodando = true;

     Random random = new Random();



    private Image[] spritesElefante;
private Image[] spritesAdvogado;

private int ticksMenu = 0;

private double flutuarElefante = 0;
private double flutuarAdvogado = Math.PI; 





    // ================= EASTER EGG PHRASES =================
    private static final String[] frasesElefante = {
        "Blindagem Eletrotática 😨",
        "Esse é o poder da gambiarra 😎",
        "Vai UFG 😍"
    };
    
    private static final String[] frasesAdvogado = {
        "Vou destruir esses cones golpistas 😡",
        "Tenho a legislação em mim 😋",
        "Vai UFG 🤤😝"
    };
    
    // ================= EASTER EGG (BALÃO) =================
private String mensagemEasterEgg = null;
private long tempoMensagem = 0;
private static final long DURACAO_MENSAGEM = 3000; // 3 segundos







    

    // ================= NUVENS =================
    double[] nuvensX = { 100, 400, 700 };

    double[] nuvensY = { 50, 90, 40 };

    double[] nuvensVel = { 0.2, 0.4, 0.3 };

    // ================= ESTRADA =================
    double estradaOffset = 0;

    // ================= MÚSICA =================
    
    int opcaoSettings = 0;

    String[] opcoesSettings = {
            "Mutar Música: NÃO"
    };

    Estado estadoAtual = Estado.MENU;

    // ================= PONTUAÇÃO =================
    int pontuacao = 0;

    // ================= ITENS COLETADOS =================
    int itensColetados = 0;

    // ================= RECORDES =================
    int recorde = 0;

    long ultimoPontoTempo = System.currentTimeMillis();

    // ================= TEMPO =================
    long inicioJogo;

    // ================= VELOCIDADE =================
    public static double multiplicadorVelocidade = 1.0;

    // ================= BALANCEAMENTO =================
    int distanciaMinimaObstaculos = 220;

    // ================= MENU =================
    int opcaoMenu = 0;

    String[] menuPrincipal = {
            "Jogar",
            "Configurações",
            "Devs"
    };

    int opcaoPersonagem = 0;

    String[] personagens = {
            "Elefante",
            "Advogado"
    };

    public Main() {

        setPreferredSize(new Dimension(800, 400));

        addKeyListener(this);

        setFocusable(true);

         carregarSpritesMenu();

        timer = new Timer(16, this);

        timer.start();

       audio.tocarMusica();

        carregarRecorde();
    }

    private void carregarRecorde() {
        File arquivo = new File("highscore.dat");
        if (arquivo.exists()) {
            try (DataInputStream in = new DataInputStream(new FileInputStream(arquivo))) {
                recorde = in.readInt();
            } catch (Exception e) {
                System.out.println("Erro ao carregar recorde: " + e.getMessage());
            }
        }
    }

    private void salvarRecorde() {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream("highscore.dat"))) {
            out.writeInt(recorde);
        } catch (Exception e) {
            System.out.println("Erro ao salvar recorde: " + e.getMessage());
        }
    }


// ================= EASTER EGG (BALÃO) =================
private void showRandomPhrase() {
    if (jogador == null) return;
    
    Random rand = new Random();
    if (jogador instanceof Elefante) {
        mensagemEasterEgg = frasesElefante[rand.nextInt(frasesElefante.length)];
    } else if (jogador instanceof Advogado) {
        mensagemEasterEgg = frasesAdvogado[rand.nextInt(frasesAdvogado.length)];
    } else {
        return;
    }
    tempoMensagem = System.currentTimeMillis();
}


    // ================= INICIAR JOGO =================
    void iniciarJogo() {

        mensagemEasterEgg = null;
        tempoMensagem = 0;

        // Criar o personagem baseado na escolha
        if (opcaoPersonagem == 0) {
        jogador = new Elefante(100, 250);
    } else {
        jogador = new Advogado(100, 250);
    }
    
    entidades = new ArrayList<>();
    entidades.add(jogador);
    
    pontuacao = 0;
    itensColetados = 0;
    ultimoPontoTempo = System.currentTimeMillis();
    inicioJogo = System.currentTimeMillis();
    multiplicadorVelocidade = 1.0;
    rodando = true;
    estadoAtual = Estado.PLAYING;
}

    public static void main(String[] args) {

        JFrame frame = new JFrame("Jogo do Elefante");

        Main jogo = new Main();

        frame.add(jogo);

        frame.pack();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

              ticksMenu++;

            flutuarElefante += 0.04;
            flutuarAdvogado += 0.04;


        if (estadoAtual == Estado.PLAYING) {

            if (rodando) {

                atualizar();
            }
        }

        repaint();
    }

    // ================= ATUALIZAR =================
    void atualizar() {

       if (mensagemEasterEgg != null && System.currentTimeMillis() - tempoMensagem >= DURACAO_MENSAGEM) {
           mensagemEasterEgg = null;}


        // ================= ATUALIZAR ESTRADA =================
        estradaOffset -= 5 * multiplicadorVelocidade;
        if (estradaOffset < -80) {
            estradaOffset += 80;
        }

        long tempoAtual = System.currentTimeMillis();

        long segundos = (tempoAtual - inicioJogo) / 1000;

        // ================= VELOCIDADE DINÂMICA =================

        if (segundos <= 10) {

            multiplicadorVelocidade = 1.0 + (segundos / 10.0);

        } else if (segundos <= 60) {

            multiplicadorVelocidade = 2.0 + ((segundos - 10) / 50.0) * 2.0;

        } else {

            multiplicadorVelocidade = 4.0 + Math.log(segundos - 59) * 0.7;
        }

        // ================= PONTUAÇÃO =================

        if (tempoAtual - ultimoPontoTempo >= 1000) {

            pontuacao++;

            ultimoPontoTempo = tempoAtual;
        }

        // ================= SPAWN OBSTÁCULO =================

        if (random.nextInt(100) < 2) {

            boolean podeSpawnar = true;

            for (Entidade ent : entidades) {

                if (ent instanceof Obstaculo) {

                    if (ent.x > 800 - distanciaMinimaObstaculos) {

                        podeSpawnar = false;

                        break;
                    }
                }
            }

            if (podeSpawnar) {
                if (jogador instanceof Elefante) {
                    // 50% chance of ground traffic cone, 50% chance of a hanging traffic light!
                    if (random.nextBoolean()) {
                        entidades.add(
                                new Obstaculo(
                                        800,
                                        300,
                                        multiplicadorVelocidade));
                    } else {
                        entidades.add(
                                new Semaforo(
                                        800,
                                        210,
                                        multiplicadorVelocidade));
                    }
                } else {
                    // 50% chance of a ground barricade, 50% chance of a flying paper plane!
                    if (random.nextBoolean()) {
                        entidades.add(
                                new Barricada(
                                        800,
                                        300,
                                        multiplicadorVelocidade));
                    } else {
                        entidades.add(
                                new AviaoPapel(
                                        800,
                                        220,
                                        multiplicadorVelocidade));
                    }
                }
            }
        }

        // ================= SPAWN ITEM =================

        if (random.nextInt(700) < 1) {
            if (jogador instanceof Elefante) {
                entidades.add(
                        new Raio(
                                800,
                                250,
                                multiplicadorVelocidade));
            } else {
                entidades.add(
                        new Processo(
                                800,
                                250,
                                multiplicadorVelocidade));
            }
        }

        Iterator<Entidade> it = entidades.iterator();

        while (it.hasNext()) {

            Entidade ent = it.next();

            ent.atualizar();

            if (ent.x < -100) {

                it.remove();

                continue;
            }

// ================= COLISÃO =================
if (ent != jogador && ent.getBounds().intersects(jogador.getBounds())) {
    
    // ================= OBSTÁCULO =================
    if (ent instanceof Obstaculo) {
        if (((Obstaculo) ent).suspenso) {
            continue;
        }

        if (jogador.intangivel) {
            jogador.obstaculosAtravessados++;
            audio.tocarEfeito("Explosion.WAV");
            if (jogador.obstaculosAtravessados >= 3) {
                jogador.intangivel = false;
            }
            
            ((Obstaculo) ent).suspender();
            continue;
        }
        
        audio.tocarEfeito("Fail.WAV");
        rodando = false;
        if (pontuacao > recorde) {
            recorde = pontuacao;
            salvarRecorde();
        }
    }
     

    



    
    // ================= ITEM =================
    if (ent instanceof Item) {
        ((Item) ent).coletar(jogador);
        itensColetados++;
        audio.tocarEfeito("Thunder.WAV");
        it.remove();
        showRandomPhrase();
    }
}
        }
    }

  // ================= RENDER =================
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // 1. Pegar o tamanho real atual da janela
        int larguraAtual = getWidth();
        int alturaAtual = getHeight();

        // 2. Definir a resolução base original do seu jogo
        double larguraBase = 800.0;
        double alturaBase = 400.0;

        // 3. Calcular a escala (Math.min garante que o jogo caiba inteiro na tela sem achatar)
        double escala = Math.max(larguraAtual / larguraBase, alturaAtual / alturaBase);

        // 4. Calcular o deslocamento para centralizar a tela do jogo
        int offsetX = (int) ((larguraAtual - (larguraBase * escala)) / 2);
        int offsetY = (int) ((alturaAtual - (alturaBase * escala)) / 2);

        // 5. Pintar o fundo real de preto (para as bordas que sobrarem fora da proporção)
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, larguraAtual, alturaAtual);

        // 6. Aplicar as transformações!
        g2d.translate(offsetX, offsetY);
        g2d.scale(escala, escala);

        // 7. Cortar o que vazar da resolução base (evita falhas visuais nas bordas)
        g2d.clipRect(0, 0, (int) larguraBase, (int) alturaBase);
    
        // ================= MENU =================
        if (estadoAtual == Estado.MENU) {
            desenharMenu(g);
            return;
        }

        // ================= SELEÇÃO =================
        if (estadoAtual == Estado.CHARACTER_SELECT) {
            desenharSelecao(g);
            return;
        }

        // ================= CONFIGURAÇÕES =================
        if (estadoAtual == Estado.SETTINGS) {
            desenharSettings(g);
            return;
        }


          
        if (estadoAtual == Estado.DEVELOPERS) {
    desenharDevelopers(g);
    return;
}



       

        // ================= JOGO =================
        if (estadoAtual == Estado.PLAYING) {
            desenharJogo(g);
            return;
        }

        // Fallback (caso ocorra algum estado inesperado)
        g.setColor(Color.RED);
        g.fillRect(0, 0, (int) larguraBase, (int) alturaBase);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Estado inválido: " + estadoAtual, 300, 200);
    }


   void desenharDevelopers(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    GradientPaint gp = new GradientPaint(0, 0, new Color(20, 20, 40), 0, 400, new Color(10, 10, 20));
    g2d.setPaint(gp);
    g2d.fillRect(0, 0, 800, 400);
    
    // ===== TÍTULO CENTRALIZADO =====
    g.setFont(new Font("Arial", Font.BOLD, 36));
    String titulo = "Desenvolvedores";
    FontMetrics fmTitulo = g.getFontMetrics();
    int larguraTitulo = fmTitulo.stringWidth(titulo);
    int xTitulo = (800 - larguraTitulo) / 2;
    
    // Sombra
    g.setColor(Color.BLACK);
    g.drawString(titulo, xTitulo + 2, 102);
    // Texto principal dourado
    g.setColor(new Color(255, 215, 0));
    g.drawString(titulo, xTitulo, 100);
    
    // ===== NOMES CENTRALIZADOS =====
    g.setFont(new Font("Arial", Font.BOLD, 28));
    String[] nomes = {"Davi Mariano", "Jean Gabriel", "Gabriel Arthur"};
    int yInicial = 180;
    int espacoEntreNomes = 60;
    
    for (int i = 0; i < nomes.length; i++) {
        int larguraNome = g.getFontMetrics().stringWidth(nomes[i]);
        int xNome = (800 - larguraNome) / 2;
        
        // Sombra
        g.setColor(Color.BLACK);
        g.drawString(nomes[i], xNome + 2, yInicial + i * espacoEntreNomes + 2);
        // Texto principal
        g.setColor(new Color(220, 220, 255));
        g.drawString(nomes[i], xNome, yInicial + i * espacoEntreNomes);
    }
    
    // ===== INSTRUÇÃO CENTRALIZADA =====
    g.setFont(new Font("Arial", Font.PLAIN, 18));
    g.setColor(Color.LIGHT_GRAY);
    String voltar = "ESC para voltar";
    int larguraVoltar = g.getFontMetrics().stringWidth(voltar);
    int xVoltar = (800 - larguraVoltar) / 2;
    g.drawString(voltar, xVoltar, 350);
}





//Menu
    void desenharMenu(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    // Gradiente azul escuro (igual às outras telas)
    GradientPaint gp = new GradientPaint(0, 0, new Color(20, 20, 40), 0, 400, new Color(10, 10, 20));
    g2d.setPaint(gp);
    g2d.fillRect(0, 0, 800, 400);
    desenharPersonagensMenu(g);
    
    // Título principal centralizado
    g.setFont(new Font("Arial", Font.BOLD, 42));
    String titulo = "CORRIDA DO ELEFANTE";
    FontMetrics fmTitulo = g.getFontMetrics();
    int larguraTitulo = fmTitulo.stringWidth(titulo);
    int xTitulo = (800 - larguraTitulo) / 2;
    g.setColor(Color.WHITE);
    g.drawString(titulo, xTitulo, 100);
    
    // Recorde centralizado
    g.setFont(new Font("Arial", Font.BOLD, 20));
    String recordeTexto = "Recorde: " + recorde;
    int larguraRecorde = g.getFontMetrics().stringWidth(recordeTexto);
    int xRecorde = (800 - larguraRecorde) / 2;
    g.setColor(new Color(255, 215, 0)); // Gold
    g.drawString(recordeTexto, xRecorde, 150);
    
    // Opções do menu centralizadas
    g.setFont(new Font("Arial", Font.BOLD, 28));
    int yInicial = 200;
    int espaco = 60;
    for (int i = 0; i < menuPrincipal.length; i++) {
        int larguraOpcao = g.getFontMetrics().stringWidth(menuPrincipal[i]);
        int xOpcao = (800 - larguraOpcao) / 2;
        if (i == opcaoMenu) {
            g.setColor(Color.YELLOW);
        } else {
            g.setColor(Color.WHITE);
        }
        g.drawString(menuPrincipal[i], xOpcao, yInicial + (i * espaco));
    }
    
    // Instrução centralizada
    g.setFont(new Font("Arial", Font.PLAIN, 18));
    g.setColor(Color.LIGHT_GRAY);
    String instrucao = "Use ↑ ↓ e ENTER";
    int larguraInst = g.getFontMetrics().stringWidth(instrucao);
    int xInst = (800 - larguraInst) / 2;
    g.drawString(instrucao, xInst, 360);
}


//Seleção

  void desenharSelecao(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    // Gradiente azul escuro (igual ao menu)
    GradientPaint gp = new GradientPaint(0, 0, new Color(20, 20, 40), 0, 400, new Color(10, 10, 20));
    g2d.setPaint(gp);
    g2d.fillRect(0, 0, 800, 400);
    
    // Título centralizado
    g.setFont(new Font("Arial", Font.BOLD, 36));
    String titulo = "Escolha o Personagem";
    FontMetrics fmTitulo = g.getFontMetrics();
    int larguraTitulo = fmTitulo.stringWidth(titulo);
    int xTitulo = (800 - larguraTitulo) / 2;
    g.setColor(Color.WHITE);
    g.drawString(titulo, xTitulo, 100);
    
    // Opções centralizadas (cores iguais ao menu)
    g.setFont(new Font("Arial", Font.BOLD, 28));
    int yInicial = 180;
    int espaco = 70;
    for (int i = 0; i < personagens.length; i++) {
        int larguraOpcao = g.getFontMetrics().stringWidth(personagens[i]);
        int xOpcao = (800 - larguraOpcao) / 2;
        if (i == opcaoPersonagem) {
            g.setColor(Color.YELLOW);   // selecionado
        } else {
            g.setColor(Color.WHITE);    // não selecionado
        }
        g.drawString(personagens[i], xOpcao, yInicial + (i * espaco));
    }
    
    // Instrução centralizada
    g.setFont(new Font("Arial", Font.PLAIN, 18));
    g.setColor(Color.LIGHT_GRAY);
    String instrucao = "ESC para voltar";
    int larguraInst = g.getFontMetrics().stringWidth(instrucao);
    int xInst = (800 - larguraInst) / 2;
    g.drawString(instrucao, xInst, 360);
}


    //Configurações
   void desenharSettings(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    // Gradiente igual ao da tela de desenvolvedores (azul escuro)
    GradientPaint gp = new GradientPaint(0, 0, new Color(20, 20, 40), 0, 400, new Color(10, 10, 20));
    g2d.setPaint(gp);
    g2d.fillRect(0, 0, 800, 400);
    
    // Título centralizado
    g.setFont(new Font("Arial", Font.BOLD, 40));
    String titulo = "CONFIGURAÇÕES";
    FontMetrics fmTitulo = g.getFontMetrics();
    int larguraTitulo = fmTitulo.stringWidth(titulo);
    int xTitulo = (800 - larguraTitulo) / 2;
    g.setColor(Color.WHITE);
    g.drawString(titulo, xTitulo, 100);
    
    // Opção de mute centralizada
    g.setFont(new Font("Arial", Font.BOLD, 26));
    for (int i = 0; i < opcoesSettings.length; i++) {
        int larguraOpcao = g.getFontMetrics().stringWidth(opcoesSettings[i]);
        int xOpcao = (800 - larguraOpcao) / 2;
        if (i == opcaoSettings) {
            g.setColor(Color.YELLOW);
        } else {
            g.setColor(Color.WHITE);
        }
        g.drawString(opcoesSettings[i], xOpcao, 190 + (i * 50));
    }
    
    // Instruções centralizadas
    g.setFont(new Font("Arial", Font.PLAIN, 18));
    g.setColor(Color.LIGHT_GRAY);
    String enterMsg = "ENTER para alterar";
    String escMsg = "ESC para voltar";
    int larguraEnter = g.getFontMetrics().stringWidth(enterMsg);
    int larguraEsc = g.getFontMetrics().stringWidth(escMsg);
    int xEnter = (800 - larguraEnter) / 2;
    int xEsc = (800 - larguraEsc) / 2;
    g.drawString(enterMsg, xEnter, 300);
    g.drawString(escMsg, xEsc, 330);
}

    // ================= ADVOGADO =================
    void desenharLawyer(Graphics g) {

        g.setColor(Color.BLACK);

        g.fillRect(0, 0, 800, 400);

        g.setColor(Color.ORANGE);

        g.setFont(new Font("Arial", Font.BOLD, 42));

        g.drawString(
                "Desenvolvendo...",
                230,
                180);

        g.setFont(new Font("Arial", Font.PLAIN, 24));

        g.setColor(Color.WHITE);

        g.drawString(
                "Pressione ESC para voltar",
                240,
                250);
    }

    // ================= JOGO =================
    void desenharJogo(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        // Sky Gradient (Gradiente do Céu)
        GradientPaint gradienteCeu = new GradientPaint(
                0, 0, new Color(50, 130, 245),
                0, 340, new Color(175, 215, 255));
        g2d.setPaint(gradienteCeu);
        g2d.fillRect(0, 0, 800, 340);

        // Stylized Sun (Sol Estilizado Estático)
        int sunCx = 670;
        int sunCy = 70;
        int rCore = 28;
        int rOuter = 46;
        int numRays = 12;
        double rotationOffset = 0;

        // Draw Ray Polygons (Desenha os Raios de Sol)
        for (int i = 0; i < numRays; i++) {
            double angle = i * (2 * Math.PI / numRays) + rotationOffset;
            double angleLeft = angle - (Math.PI / 24);
            double angleRight = angle + (Math.PI / 24);

            int[] rx = {
                    (int) (sunCx + rOuter * Math.cos(angle)),
                    (int) (sunCx + rCore * Math.cos(angleLeft)),
                    (int) (sunCx + rCore * Math.cos(angleRight))
            };
            int[] ry = {
                    (int) (sunCy + rOuter * Math.sin(angle)),
                    (int) (sunCy + rCore * Math.sin(angleLeft)),
                    (int) (sunCy + rCore * Math.sin(angleRight))
            };

            // Ray fill (Preenchimento do Raio - Amarelo/Laranja)
            g.setColor(new Color(255, 180, 0));
            g.fillPolygon(rx, ry, 3);

            // Ray outline (Contorno do Raio)
            g.setColor(Color.BLACK);
            g.drawPolygon(rx, ry, 3);
        }

        // Sun Glow Layer (Brilho do Sol)
        g.setColor(new Color(255, 140, 0, 100)); // transparent orange glow
        g.fillOval(sunCx - rCore - 8, sunCy - rCore - 8, (rCore + 8) * 2, (rCore + 8) * 2);

        // Sun Core fill (Núcleo do Sol - Laranja)
        g.setColor(new Color(255, 100, 0));
        g.fillOval(sunCx - rCore, sunCy - rCore, rCore * 2, rCore * 2);

        // Sun Core outline (Contorno do Núcleo)
        g.setColor(Color.BLACK);
        g.drawOval(sunCx - rCore, sunCy - rCore, rCore * 2, rCore * 2);

        // Moving Clouds (Nuvens)
        for (int i = 0; i < nuvensX.length; i++) {
            int cx = (int) nuvensX[i];
            int cy = (int) nuvensY[i];

            // Cloud Shadow (Sombra da Nuvem)
            g.setColor(new Color(225, 235, 250, 160));
            g.fillOval(cx - 2, cy + 12, 54, 30);
            g.fillOval(cx + 18, cy + 2, 64, 40);
            g.fillOval(cx + 48, cy + 12, 44, 30);
            g.fillOval(cx + 8, cy + 22, 84, 20);

            // Cloud Body (Corpo da Nuvem)
            g.setColor(new Color(255, 255, 255, 230));
            g.fillOval(cx, cy + 10, 50, 30);
            g.fillOval(cx + 20, cy, 60, 40);
            g.fillOval(cx + 50, cy + 10, 40, 30);
            g.fillOval(cx + 10, cy + 20, 80, 20);
        }

        // Road Ground (Chão de Estrada)
        g.setColor(new Color(50, 50, 50));
        g.fillRect(0, 340, 800, 60);

        // White solid top road border (Borda Superior da Estrada)
        g.setColor(Color.WHITE);
        g.drawLine(0, 340, 800, 340);

        // Scrolling Road Stripes (Faixas da Estrada Animadas)
        for (int rx = (int) estradaOffset; rx < 800; rx += 80) {
            g.fillRect(rx, 368, 40, 4);
        }

 // ================= EASTER EGG (BALÃO) =================
if (mensagemEasterEgg != null) {
    long agora = System.currentTimeMillis();
    if (agora - tempoMensagem < DURACAO_MENSAGEM) {
        // Escolher fonte com emoji
        Font fonteEmoji;
        try {
            fonteEmoji = new Font("Segoe UI Emoji", Font.BOLD, 16);
        } catch (Exception e) {
            fonteEmoji = new Font("Arial", Font.BOLD, 16);
        }
        g.setFont(fonteEmoji);
        FontMetrics fm = g.getFontMetrics();
        int larguraTexto = fm.stringWidth(mensagemEasterEgg);
        int alturaTexto = fm.getHeight();
        
        // Largura do personagem (compatível com getBounds() se não existir largura)
        int larguraPersonagem;
        if (jogador.largura != 0) {
            larguraPersonagem = jogador.largura;
        } else {
            larguraPersonagem = jogador.getBounds().width;
        }
        
        // Posição centralizada acima do personagem
        int centroX = jogador.x + (larguraPersonagem / 2);
        int balaoY = jogador.y - 40;  // acima da cabeça
        int balaoLargura = larguraTexto + 30;
        int balaoAltura = alturaTexto + 20;
        int balaoX = centroX - (balaoLargura / 2);
        
        // Evitar que saia da tela
        if (balaoX < 5) balaoX = 5;
        if (balaoX + balaoLargura > 800) balaoX = 800 - balaoLargura;
        
        // Desenhar balão
        g.setColor(Color.WHITE);
        g.fillRoundRect(balaoX, balaoY, balaoLargura, balaoAltura, 20, 20);
        g.setColor(Color.BLACK);
        g.drawRoundRect(balaoX, balaoY, balaoLargura, balaoAltura, 20, 20);
        
        // Desenhar texto
        g.drawString(mensagemEasterEgg, balaoX + 15, balaoY + alturaTexto - 2);
        
        // Triângulo apontando para o personagem
        int pontaX = centroX;
        int pontaY = balaoY + balaoAltura;
        int[] triX = {pontaX - 10, pontaX, pontaX + 10};
        int[] triY = {pontaY, pontaY + 10, pontaY};
        g.setColor(Color.WHITE);
        g.fillPolygon(triX, triY, 3);
        g.setColor(Color.BLACK);
        g.drawPolygon(triX, triY, 3);
    }
}



         g.setFont(new Font("Arial", Font.BOLD, 24)); //pontuação
        g.setColor(Color.BLACK);
         g.drawString("Pontuação: " + pontuacao, 20, 40);

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.setColor(Color.BLACK);
         g.drawString(
                         "Velocidade: x"
                                     + String.format("%.1f", multiplicadorVelocidade),
                         20,
                      70);

        for (Entidade e : entidades) {

            e.desenhar(g);
        }

        // Draw Itens Coletados HUD
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.setColor(Color.BLACK);
        String labelItemHUD = (jogador instanceof Elefante) ? "Raios: " : "Processos: ";
        g.drawString(labelItemHUD + itensColetados, 20, 95);

        if (jogador.intangivel) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.drawString(
                "INTANGÍVEL: " + (3 - jogador.obstaculosAtravessados) + " proteções restantes",
                20, 120);
        }

        if (!rodando) {
            // Draw semi-transparent background box in the center for superior QoL contrast
            g.setColor(new Color(0, 0, 0, 190));
            g.fillRoundRect(190, 80, 420, 240, 16, 16);
            g.setColor(Color.WHITE);
            g.drawRoundRect(190, 80, 420, 240, 16, 16);

            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.setColor(new Color(255, 60, 60)); // Vibrant red
            g.drawString("FIM DE JOGO", 295, 130);

            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.setColor(Color.WHITE);
            g.drawString("Pontuação Final: " + pontuacao, 230, 180);

            String labelItemGameOver = (jogador instanceof Elefante) ? "Raios Coletados: " : "Processos Coletados: ";
            g.drawString(labelItemGameOver + itensColetados, 230, 215);

            // Show if it is a new record!
            if (pontuacao >= recorde && recorde > 0) {
                g.setColor(new Color(255, 215, 0)); // Gold
                g.drawString("¡NOVO RECORDE GLOBAL!", 230, 250);
            } else {
                g.setColor(Color.LIGHT_GRAY);
                g.drawString("Recorde Pessoal: " + recorde, 230, 250);
            }

            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.setColor(Color.LIGHT_GRAY);
            g.drawString("ESPAÇO para Reiniciar | ESC para o Menu", 220, 295);
        }
    }

    // ================= CONTROLES =================
    @Override
    public void keyPressed(KeyEvent e) {

        int tecla = e.getKeyCode();

        if (estadoAtual == Estado.MENU) {

            if (tecla == KeyEvent.VK_UP) {

                opcaoMenu--;

                audio.tocarEfeito("Select.WAV");

                if (opcaoMenu < 0) {

                    opcaoMenu = menuPrincipal.length - 1;
                }
            }

            if (tecla == KeyEvent.VK_DOWN) {

                opcaoMenu++;
                audio.tocarEfeito("Select.WAV");

                if (opcaoMenu >= menuPrincipal.length) {

                    opcaoMenu = 0;
                }
            }

            if (tecla == KeyEvent.VK_ENTER) {

                audio.tocarEfeito("Select.WAV");

                if (opcaoMenu == 0) {

                    estadoAtual = Estado.CHARACTER_SELECT;
                }

                if (opcaoMenu == 1) {

                    estadoAtual = Estado.SETTINGS;
                }

                 if (opcaoMenu == 2) {
                    estadoAtual = Estado.DEVELOPERS;
                         }
            }
        }

        else if (estadoAtual == Estado.CHARACTER_SELECT) {

            if (tecla == KeyEvent.VK_UP) {

                opcaoPersonagem--;

                if (opcaoPersonagem < 0) {

                    opcaoPersonagem = personagens.length - 1;
                }
            }

            if (tecla == KeyEvent.VK_DOWN) {

                opcaoPersonagem++;

                if (opcaoPersonagem >= personagens.length) {

                    opcaoPersonagem = 0;
                }
            }

            if (tecla == KeyEvent.VK_ENTER) {

                if (opcaoPersonagem == 0) {

                    audio.tocarEfeito("Select.WAV");

                    iniciarJogo();
                }

                if (opcaoPersonagem == 1) {

                    audio.tocarEfeito("Select.WAV");

                    iniciarJogo();
                }
            }

            if (tecla == KeyEvent.VK_ESCAPE) {

                estadoAtual = Estado.MENU;
            }
        }

        else if (estadoAtual == Estado.SETTINGS) {

            if (tecla == KeyEvent.VK_UP) {

                opcaoSettings--;
                audio.tocarEfeito("Select.WAV");

                if (opcaoSettings < 0) {

                    opcaoSettings = opcoesSettings.length - 1;
                }
            }

            if (tecla == KeyEvent.VK_DOWN) {

                opcaoSettings++;
                audio.tocarEfeito("Select.WAV");

                if (opcaoSettings >= opcoesSettings.length) {

                    opcaoSettings = 0;
                }
            }

            if (tecla == KeyEvent.VK_ENTER) {

                audio.tocarEfeito("Select.WAV");

                if (opcaoSettings == 0) {

               audio.alternarMute();

          if (audio.estaMutada()) {
          opcoesSettings[0] = "Mutar Música: SIM";
            } 
            else {
             opcoesSettings[0] = "Mutar Música: NÃO";
            }
                }
            }

            if (tecla == KeyEvent.VK_ESCAPE) {
                audio.tocarEfeito("Select.WAV");

                estadoAtual = Estado.MENU;
            }
        }


         else if (estadoAtual == Estado.DEVELOPERS) {
    if (tecla == KeyEvent.VK_ESCAPE) {
        audio.tocarEfeito("Select.WAV");
        estadoAtual = Estado.MENU;
            }
        }


        

        else if (estadoAtual == Estado.PLAYING) {

            if (tecla == KeyEvent.VK_SPACE) {

                if (!rodando) {

                    iniciarJogo();

                    return;
                }

                jogador.pular();
            }

            if (tecla == KeyEvent.VK_ESCAPE) {
                estadoAtual = Estado.MENU;
                rodando = false;
                audio.tocarEfeito("Select.WAV");
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }







       private void desenharPersonagensMenu(Graphics g) {

    if (spritesElefante == null ||
        spritesAdvogado == null) {
        return;
    }

    int frameElefante =
        (ticksMenu / 12) % spritesElefante.length;

    int frameAdvogado =
        (ticksMenu / 12) % spritesAdvogado.length;

    int yElefante =
        170 +
        (int)(10 * Math.sin(flutuarElefante));

    int yAdvogado =
        170 +
        (int)(10 * Math.sin(flutuarAdvogado));

    g.drawImage(
        spritesElefante[frameElefante],
        40,
        yElefante,
        140,
        140,
        null
    );

    g.drawImage(
        spritesAdvogado[frameAdvogado],
        620,
        yAdvogado,
        140,
        140,
        null
    );
}

  //CarregarSprites
private void carregarSpritesMenu() {
    try {

        spritesElefante = new Image[6];

        for (int i = 0; i < 6; i++) {

            spritesElefante[i] = ImageIO.read(
                getClass().getResource(
                    "/Assets/Sprites/elefante-frame-" + (i + 1) + ".png"
                )
            );
        }

        spritesAdvogado = new Image[6];

        for (int i = 0; i < 6; i++) {

            spritesAdvogado[i] = ImageIO.read(
                getClass().getResource(
                    "/Assets/Sprites/advogado-frame-" + (i + 1) + ".png"
                )
            );
        }

    } catch (Exception e) {

        e.printStackTrace();
    }
}








}
