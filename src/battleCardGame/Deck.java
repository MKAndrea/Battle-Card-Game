package battleCardGame;

import java.util.ArrayList;
import java.util.Collections; // Necessario per shuffle

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Deck {
    // Usiamo ArrayList: non serve dichiarare la dimensione subito
    private ArrayList<Card> cards = new ArrayList<>();

    public Card drawCard() {
        // isEmpty() delle Collection è perfetto qui
        if (!cards.isEmpty()) {
            // remove(0) prende la prima carta, la restituisce e la TOGLIE dalla lista
            // Tutto in una riga, senza dover gestire indici o null!
            return cards.remove(0);
        } else {
            // Rimosso il print per mantenere la console pulita come volevi
            return null;
        }
    }

    public void generateDeck() {
        // Puliamo il mazzo se per caso fosse già pieno
        cards.clear();

        String[] names = {
            "Drago Primordiale", "Cavaliere di Sangue", "Guardiano del Crepuscolo", 
            "Golem di Ossidiana", "Spirito della Tempesta", "Assassino Ombra", 
            "Sacerdotessa della Luna", "Gigante di Ghiaccio", "Fenice di Fuoco", 
            "Signore delle Rune", "Cacciatore di Demoni", "Alchimista Folle", 
            "Guerriero di Terracotta", "Sirena Abissale", "Ent Millenario", 
            "Lupo Invernale", "Arciere Arcano", "Mago del Caos", 
            "Paladino della Luce", "Necromante Errante"
        };

        // Creiamo 20 carte
        for (int i = 0; i < 20; i++) {
            int indexName = (int) (Math.random() * names.length);
            String chosenName = names[indexName];
            // cards.add() aggiunge la carta in fondo alla lista
            cards.add(new Card(chosenName));
        }

        // --- LA MAGIA DELLE COLLECTION ---
        // Mescola l'intero mazzo casualmente
        Collections.shuffle(cards);
    }

    public boolean isEmpty() {
        // Sfruttiamo il metodo nativo della ArrayList
        return cards.isEmpty();
    }
}