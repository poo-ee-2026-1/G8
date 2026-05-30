import javax.sound.sampled.*;
import java.net.URL; // 1. ADICIONE ESTA IMPORTAÇÃO AQUI NO TOPO

public class AudioManager {

    private Clip musica;
    private boolean musicaMutada = false;

    public void tocarMusica() {
        try {
            // 2. SUBSTITUA A LINHA DO 'NEW FILE' POR ESTA ABAIXO:
            URL urlAudio = getClass().getResource("/Assets/Audio/MusicaJogo.WAV");

            // Segurança: Se você errar o nome do arquivo, o Java avisa aqui antes de quebrar
            if (urlAudio == null) {
                System.out.println("ERRO: O arquivo de áudio não foi encontrado! Verifique o nome na pasta Assets.");
                return;
            }

            // 3. PASSE A VARIÁVEL 'urlAudio' AQUI DENTRO:
            AudioInputStream audio = AudioSystem.getAudioInputStream(urlAudio);

            musica = AudioSystem.getClip();
            musica.open(audio);
            musica.loop(Clip.LOOP_CONTINUOUSLY);
            musica.start();

        } catch (Exception e) {
            System.out.println("Erro ao tocar música: " + e.getMessage());
            e.printStackTrace(); // Isso vai te mostrar o erro detalhado se algo der errado
        }
    }

    public void alternarMute() {
        musicaMutada = !musicaMutada;
        atualizarEstadoMusica();
    }

    public boolean estaMutada() {
        return musicaMutada;
    }

    private void atualizarEstadoMusica() {
        if (musicaMutada) {
            if (musica != null) {
                musica.stop();
            }
        } else {
            if (musica != null && !musica.isRunning()) {
                musica.loop(Clip.LOOP_CONTINUOUSLY);
                musica.start();
            }
        }
    }

    public void tocarEfeito(String nomeArquivo) {
        // Se a configuração estiver mutada, também não toca os efeitos
        if (musicaMutada) return; 

        try {
            URL urlAudio = getClass().getResource("/Assets/Audio/Sfx/" + nomeArquivo);
            
            if (urlAudio == null) {
                System.out.println("ERRO: Efeito " + nomeArquivo + " não encontrado na pasta Assets/Audio.");
                return;
            }

            AudioInputStream audio = AudioSystem.getAudioInputStream(urlAudio);
            Clip efeito = AudioSystem.getClip();
            efeito.open(audio);
            efeito.start(); // Toca apenas uma vez (sem loop)

        } catch (Exception e) {
            System.out.println("Erro ao tocar efeito: " + e.getMessage());
        }
    } 

}