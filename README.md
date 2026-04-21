# G8
Discentes: Jean Gabriel, Gabriel Arthur e Davi Mariano. 

Classes detalhadas

1. GameObject (classe abstrata ou interface)

Função: Define o contrato para todos os objetos do jogo
Métodos: update(), draw(Graphics2D g), getBounds(), isOffScreen()
 Herança: Será estendida por Dinosaur, Obstacle, Ground
Polimorfismo: GamePanel pode tratar todos como GameObject e chamar update()/draw() sem saber o tipo específico

---

2. Obstacle (classe abstrata)

Função: Base para todos os obstáculos
Atributos: x, y, width, height, speed, type
Métodos abstratos: getCollisionBox() (pode variar entre cactus e pássaro)
Herança: Pai concreto de Cactus e Bird
 Polimorfismo: ObstacleManager armazena List<Obstacle> e chama métodos polimórficos

---

3. Cactus (estende Obstacle)

Função: Cactos no chão (3 tamanhos diferentes)
Métodos específicos: setSize(int tipo)
Herança: Implementa Obstacle e sobrescreve getCollisionBox()

---

4. Bird (estende Obstacle)

Função: Pássaros voando (altura variável)
Atributos: wingPosition (para animação de asa), yOffset
Métodos específicos: animateWings(), changeAltitude()
 Herança: Implementa Obstacle com lógica de movimento vertical diferente do Cactus

---

5. Dinosaur (implementa GameObject)

Função: Personagem principal controlado pelo jogador
Atributos: x, y, velocityY, isJumping, isDucking (agachar)
Métodos: jump(), duck(), standUp(), applyGravity()
 Polimorfismo: Se Dinosaur também fosse Obstacle (modo noturno reverso?), poderia ser tratado como obstáculo pelo colisor

---

6. Ground (implementa GameObject)

Função: Chão móvel (efeito de esteira)
Atributos: x1, x2 (duas imagens lado a lado para rolagem infinita), yPosition
Métodos: scroll(), resetPosition()
Polimorfismo: GamePanel desenha junto com outros GameObject

---

7. ObstacleManager

Função: Gerencia criação, remoção e atualização de obstáculos
Atributos: List<Obstacle> obstacles, spawnTimer, lastSpawnTime
Métodos: spawnRandomObstacle(), updateAll(), removeOffscreen()
 Polimorfismo: Usa List<Obstacle> — ao chamar update() de cada item, o comportamento varia (pássaro voa diferente do cactus)

---

8. CollisionDetector

Função: Detecta colisões entre dinossauro e obstáculos
Métodos: checkCollision(Dinosaur dino, Obstacle obs), isPixelPerfect() (opcional)
 Polimorfismo: Recebe qualquer Obstacle — usa getCollisionBox() que pode ser retângulo (cactus) ou elipse (pássaro) se sobrescrito

---

9. ScoreManager

Função: Pontuação e recordes
Atributos: currentScore, highScore, comboMultiplier
Métodos: incrementScore(int points), saveHighScore(), reset()

---

10. GamePanel (estende JPanel)

Função: Loop principal, renderização e entrada do usuário
Atributos: Dinosaur dino, ObstacleManager obsManager, GameState state, Timer gameLoop
Métodos: startGame(), gameOver(), paintComponent(), keyPressed()
Polimorfismo: Desenha todos GameObject via List<GameObject> — cada um tem seu próprio draw()

---

11. GameState (enum)

Função: Estados do jogo
Valores: MENU, RUNNING, GAME_OVER, PAUSED

---

12. SoundManager (singleton)

Função: Gerencia efeitos sonoros (pulo, colisão, pontuação)
