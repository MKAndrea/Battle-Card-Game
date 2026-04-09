package battleCardGame;

public class Main {

	public static void main(String[] args) {
		// 1. Inizializzazione dei mazzi per entrambi i giocatori
        Deck deck1 = new Deck();
        deck1.generateDeck(); // Crea e mescola le carte per il Player 1
        
        Deck deck2 = new Deck();
        deck2.generateDeck(); // Crea e mescola le carte per il Player 2

        // 2. Creazione dei giocatori e assegnazione dei rispettivi mazzi
        Player p1 = new Player("Player 1", deck1);
        Player p2 = new Player("Player 2", deck2);

        // 3. Creazione del campo di battaglia
        // Passiamo i due giocatori al Battlefield che farà da "arbitro" e motore del gioco
        Battlefield battlefield = new Battlefield(p1, p2);

        // 4. Avvio del ciclo di gioco
        // Questo metodo gestisce internamente turni, fasi di attacco e input dell'utente
        battlefield.gameLoop();
        
        // Il programma terminerà automaticamente quando gameLoop() uscirà dal ciclo
	}
}
