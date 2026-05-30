import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

// ================= MAIN =================
public class Main extends JPanel implements ActionListener, KeyListener {

    Timer timer;

    Personagem jogador;  

    ArrayList<Entidade> entidades;

    Random random = new Random();

    AudioManager audio = new AudioManager();

    boolean rodando = true;

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

    long ultimoPontoTempo = System.currentTimeMillis();

    // ================= TEMPO =================
    long inicioJogo;

    // ================= VELOCIDADE =================
    double multiplicadorVelocidade = 1.0;

    // ================= BALANCEAMENTO =================
    int distanciaMinimaObstaculos = 220;

    // ================= MENU =================
    int opcaoMenu = 0;

    String[] menuPrincipal = {
            "Jogar",
            "Configurações"
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

        timer = new Timer(16, this);

        timer.start();

       audio.tocarMusica();
    }

    // ================= INICIAR JOGO =================
    void iniciarJogo() {
    // Criar o personagem baseado na escolha
    if (opcaoPersonagem == 0) {
        jogador = new Elefante(100, 250);
    } else {
        jogador = new Advogado(100, 250);
    }
    
    entidades = new ArrayList<>();
    entidades.add(jogador);
    
    pontuacao = 0;
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

        if (estadoAtual == Estado.PLAYING) {

            if (rodando) {

                atualizar();
            }
        }

        repaint();
    }

    // ================= ATUALIZAR =================
    void atualizar() {

        // ================= ATUALIZAR NUVENS =================
        for (int i = 0; i < nuvensX.length; i++) {
            nuvensX[i] -= nuvensVel[i] * multiplicadorVelocidade;
            if (nuvensX[i] < -120) {
                nuvensX[i] = 800 + random.nextInt(100);
                nuvensY[i] = 30 + random.nextInt(80);
            }
        }

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

                entidades.add(
                        new Obstaculo(
                                800,
                                300,
                                multiplicadorVelocidade));
            }
        }

        // ================= SPAWN RAIO =================

        if (random.nextInt(700) < 1) {

            entidades.add(
                    new Raio(
                            800,
                            250,
                            multiplicadorVelocidade));
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
        // Verificar se o jogador é Elefante ou Advogado
        if (jogador instanceof Elefante && ((Elefante) jogador).intangivel) {
            Elefante ele = (Elefante) jogador;
            ele.blocosAtravessados++;
            audio.tocarEfeito("Explosion.WAV");
            if (ele.blocosAtravessados >= 3) {
                ele.intangivel = false;
            }
            it.remove();
            continue;
        } else if (jogador instanceof Advogado && ((Advogado) jogador).intangivel) {
            Advogado adv = (Advogado) jogador;
            adv.processosGanhos++;
            audio.tocarEfeito("Explosion.WAV");
            if (adv.processosGanhos >= 3) {
                adv.intangivel = false;
            }
            it.remove();
            continue;
        }
        
        audio.tocarEfeito("Fail.WAV");
        rodando = false;
    }
    
    // ================= RAIO =================
    if (ent instanceof Raio) {
        ((Raio) ent).coletar(jogador);
        audio.tocarEfeito("Thunder.WAV");
        it.remove();
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

        // ================= ADVOGADO =================
        if (estadoAtual == Estado.LAWYER_DEVELOPING) {
            desenharLawyer(g);
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

    // ================= MENU =================
    void desenharMenu(Graphics g) {

        g.setColor(Color.BLACK);

        g.fillRect(0, 0, 800, 400);

        g.setColor(Color.WHITE);

        g.setFont(new Font("Arial", Font.BOLD, 42));

        g.drawString("CORRIDA DO ELEFANTE", 180, 100);

        g.setFont(new Font("Arial", Font.BOLD, 28));

        for (int i = 0; i < menuPrincipal.length; i++) {

            if (i == opcaoMenu) {

                g.setColor(Color.YELLOW);

            } else {

                g.setColor(Color.WHITE);
            }

            g.drawString(
                    menuPrincipal[i],
                    340,
                    180 + (i * 60));
        }

        g.setFont(new Font("Arial", Font.PLAIN, 18));

        g.setColor(Color.LIGHT_GRAY);

        g.drawString(
                "Use ↑ ↓ e ENTER",
                300,
                340);
    }

    // ================= SELEÇÃO =================
    void desenharSelecao(Graphics g) {

        g.setColor(new Color(20, 20, 20));

        g.fillRect(0, 0, 800, 400);

        g.setColor(Color.WHITE);

        g.setFont(new Font("Arial", Font.BOLD, 36));

        g.drawString(
                "Escolha o Personagem",
                210,
                90);

        g.setFont(new Font("Arial", Font.BOLD, 28));

        for (int i = 0; i < personagens.length; i++) {

            if (i == opcaoPersonagem) {

                g.setColor(Color.CYAN);

            } else {

                g.setColor(Color.WHITE);
            }

            g.drawString(
                    personagens[i],
                    320,
                    180 + (i * 60));
        }

        g.setFont(new Font("Arial", Font.PLAIN, 18));

        g.setColor(Color.LIGHT_GRAY);

        g.drawString(
                "ESC para voltar",
                310,
                340);
    }

    // ================= CONFIGURAÇÕES =================
    void desenharSettings(Graphics g) {

        g.setColor(Color.DARK_GRAY);

        g.fillRect(0, 0, 800, 400);

        g.setColor(Color.WHITE);

        g.setFont(new Font("Arial", Font.BOLD, 40));

        g.drawString("CONFIGURAÇÕES", 250, 100);

        g.setFont(new Font("Arial", Font.BOLD, 26));

        for (int i = 0; i < opcoesSettings.length; i++) {

            if (i == opcaoSettings) {

                g.setColor(Color.YELLOW);

            } else {

                g.setColor(Color.WHITE);
            }

            g.drawString(
                    opcoesSettings[i],
                    250,
                    190 + (i * 50));
        }

        g.setFont(new Font("Arial", Font.PLAIN, 18));

        g.setColor(Color.LIGHT_GRAY);

        g.drawString(
                "ENTER para alterar",
                270,
                300);

        g.drawString(
                "ESC para voltar",
                290,
                330);
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

         g.setFont(new Font("Arial", Font.BOLD, 24));
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

       if (jogador instanceof Elefante && ((Elefante) jogador).intangivel) {
    g.setColor(Color.BLACK);
    g.setFont(new Font("Arial", Font.BOLD, 18));
    g.drawString(
        "INTANGÍVEL: " + (3 - ((Elefante) jogador).blocosAtravessados) + " blocos restantes",
        20, 100);
} else if (jogador instanceof Advogado && ((Advogado) jogador).intangivel) {
    g.setColor(Color.BLACK);
    g.setFont(new Font("Arial", Font.BOLD, 18));
    g.drawString(
        "INTANGÍVEL: " + (3 - ((Advogado) jogador).processosGanhos) + " bloqueios restantes",
        20, 100);
}

        if (!rodando) {

            g.setColor(Color.WHITE);

            g.setFont(new Font("Arial", Font.BOLD, 32));

            g.drawString("FIM DE JOGO", 280, 170);

            g.setFont(new Font("Arial", Font.PLAIN, 22));

            g.drawString(
                    "Pressione ESPAÇO para Reiniciar",
                    220,
                    220);
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

        else if (estadoAtual == Estado.LAWYER_DEVELOPING) {

            if (tecla == KeyEvent.VK_ESCAPE) {

                estadoAtual = Estado.CHARACTER_SELECT;
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
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}