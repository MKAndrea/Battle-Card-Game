package battleCardGame;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Player {
	private String playerName;
	private int turn=1;
	private Card[] hand = new Card[5];
	private Deck deck;
	private Card[] field = new Card[5];
	
	public Player(String playerName, Deck deck) {
		super();
		this.playerName = playerName;
		this.deck = deck;
	}
	
	public Card playCard(int index) {
        if (index >= 0 && index < hand.length && hand[index] != null) {
            Card chosen = hand[index];
            hand[index] = null; // Lo slot ora è libero per pescare ancora
            return chosen;
        }
        System.out.println("Indice non valido o slot vuoto!");
        return null;
    }

	
	public void playerTurn() {
		 String turn_player=(turn%2==1)? playerName:playerName;
		 System.out.println(turn_player+" è il tuo turno");
		 turn++;
	}
	
	
	public void showHand() {
	    System.out.println("\n--- MANO DI " + playerName.toUpperCase() + " ---");
	    
	    for (int i = 0; i < hand.length; i++) {
	        if (hand[i] != null) {
	            // Prendiamo le info essenziali dalla carta
	            String nome = hand[i].getName();
	            int hp = hand[i].getLife();
	            int atk = hand[i].getDamage();
	            String elem = hand[i].getElement().toString();
	            String abilName=hand[i].getAbility().getName();
	            String abilDescription=hand[i].getAbility().getDescription();
	            
	            // Stampiamo una riga semplice: [0] Nome - HP: 50 | ATK: 30 | ELEM: FIRE
	            System.out.println("[" + i + "] " + nome + " - HP: " + hp + " | ATK: " + atk + " | ELEM: " + elem+
	            		" | ABILITY NAME: "+abilName+" | ABILITY DESC: "+abilDescription);
	        } else {
	            System.out.println("[" + i + "] --- SLOT VUOTO ---");
	        }
	    }
	    System.out.println("------------------------------------------");
	}
	
	public void showCard(Card card_played) {
		System.out.println(card_played.toString());
	}
	
	// Pesca le carte per riempire la mano all'inizio
    public void fillHand() {
        for (int i = 0; i < hand.length; i++) {
            if (hand[i] == null) { // Pesca solo se lo slot è vuoto
                hand[i] = deck.drawCard();
            }
        }
    }
    
    public void attack(int myIndex, int opponentIndex, Player opponent) {
        // 1. Recupero la mia carta dal mio campo
        Card myCard = this.field[myIndex];
        
        // 2. Recupero la carta del nemico dal campo dell'oggetto 'opponent'
        Card opponentCard = opponent.getField()[opponentIndex];

        // 3. Controllo di sicurezza: entrambi gli slot devono avere una carta
        if (myCard == null) {
            System.out.println("Errore: Non c'è nessuna carta nel tuo slot " + myIndex);
            return;
        }
        if (opponentCard == null) {
            System.out.println("Errore: Il nemico non ha una carta nello slot " + opponentIndex);
            return;
        }

        // 4. Calcolo del danno (usando la tua logica: Danno - Armatura)
        System.out.println(myCard.getName() + " attacca " + opponentCard.getName() + "!");
        
        int damageDone = myCard.getDamage() - opponentCard.getArmor();
        if (damageDone < 0) damageDone = 0; // L'armatura non può "curare"

        // 5. Applico il danno alla carta nemica
        int remainingLife = opponentCard.getLife() - damageDone;
        opponentCard.setLife(remainingLife);

        System.out.println("Danno inflitto: " + damageDone);

        // 6. Controllo morte della carta nemica
        if (opponentCard.getLife() <= 0) {
            System.out.println("La carta " + opponentCard.getName() + " è stata distrutta!");
            opponent.getField()[opponentIndex] = null; // Rimuovo la carta dal campo nemico
        } else {
            System.out.println(opponentCard.getName() + " sopravvive con " + opponentCard.getLife() + " HP.");
        }
    }
	
}
