public abstract class Item extends Entidade {

    Item(int x, int y) {

        super(x, y, 20, 20);
    }

    abstract void coletar(Personagem p);
}
