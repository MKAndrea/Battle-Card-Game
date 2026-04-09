package battleCardGame;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Deck deck = new Deck();
	    deck.generateDeck();
	    Scanner scanner = new Scanner(System.in);

	    // Creiamo il giocatore passandogli il mazzo
	    Player player1 = new Player("Player1", deck);

	    // È il giocatore che deve riempire la sua mano!
	    player1.fillHand(); 

	    // Ora showHand() troverà le carte e non dirà più VUOTO
	    player1.showHand();
	    
	    System.out.println("Quale carta vuoi giocare? Inserisci indice:");
	    int scelta = scanner.nextInt();
	    Card cartaInGioco = player1.playCard(scelta);
	    System.out.println("Hai giocato: " + cartaInGioco);
	}

}
