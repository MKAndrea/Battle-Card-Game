package battleCardGame;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Deck {
	private Card[] cards=new Card[20];
	private int number_card_deck=cards.length;
	
	public void printCardRemaining(){
		number_card_deck--;
		System.out.println("number of cards remaining "+number_card_deck);
	}
	
	public Card drawCard() {
	    if (number_card_deck > 0) {
	        // Prendiamo la carta corrente prima di scalare l'indice
	        Card cardDraw = cards[number_card_deck - 1];
	        
	        // Chiamiamo il tuo metodo che stampa e sottrae
	        printCardRemaining(); 
	        
	        return cardDraw;
	    } else {
	        System.out.println("The deck is empty!");
	        return null;
	    }
	}
	
	public void generateDeck() {
	    String[] name = {
	        "Drago Primordiale", "Cavaliere di Sangue", "Guardiano del Crepuscolo", 
	        "Golem di Ossidiana", "Spirito della Tempesta", "Assassino Ombra", 
	        "Sacerdotessa della Luna", "Gigante di Ghiaccio", "Fenice di Fuoco", 
	        "Signore delle Rune", "Cacciatore di Demoni", "Alchimista Folle", 
	        "Guerriero di Terracotta", "Sirena Abissale", "Ent Millenario", 
	        "Lupo Invernale", "Arciere Arcano", "Mago del Caos", 
	        "Paladino della Luce", "Necromante Errante"
	    };

	    for (int i = 0; i < cards.length; i++) {
	        int indexName = (int) (Math.random() * name.length);
	        String chosenName = name[indexName];
	        cards[i] = new Card(chosenName);
	    }
	    
	}
}
	
