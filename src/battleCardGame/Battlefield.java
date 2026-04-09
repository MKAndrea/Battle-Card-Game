package battleCardGame;

import java.util.Scanner;

public class Battlefield {
    private Player p1;
    private Player p2;
    private Scanner scanner;
    private int totalTurns = 0;

    public Battlefield(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.scanner = new Scanner(System.in);
    }

    // Avvia e gestisce l'intera partita fino alla fine
    public void gameLoop() {
        p1.fillHand();
        p2.fillHand();

        Player attacker = p1;
        Player defender = p2;

        while (!isGameOver()) {
            totalTurns++;
            attacker.resetCardsReady(); // Riattiva le carte per il nuovo turno
            
            showBattle();
            System.out.println("\n>>> TURNO DI: " + attacker.getPlayerName().toUpperCase() + " (Round: " + totalTurns + ")");

            attacker.fillHand(); // Pesca carte se ne mancano
            handlePlayPhase(attacker); // Fase di schieramento

            // L'attacco è permesso solo dopo il primo turno di entrambi
            if (totalTurns > 2) {
                handleAttackPhase(attacker, defender);
            } else {
                System.out.println("\n[INFO] Primo turno: attacco non disponibile.");
            }

            if (isGameOver()) break;

            // Scambio dei ruoli tra chi attacca e chi difende
            Player temp = attacker;
            attacker = defender;
            defender = temp;
            
            System.out.println("\n" + "~".repeat(60) + "\n");
        }
        
        printWinner();
    }

    // Gestisce la scelta e il posizionamento di una carta dalla mano al campo
    private void handlePlayPhase(Player player) {
    	if (player.isFieldFull()) {
            System.out.println("\n[INFO] Il tuo campo è pieno! Salto la fase di schieramento.");
            return; // Esce dal metodo e torna al gameLoop, che proseguirà con l'attacco
        }
        player.showHand();
        System.out.print("Scegli carta da schierare (1-" + player.getHand().size() + ") o 0 per saltare: ");
        int handIdx = scanner.nextInt();
        
        if (handIdx > 0 && handIdx <= player.getHand().size()) {
            System.out.print("Scegli slot del campo (1-5): ");
            int fieldSlot = scanner.nextInt();
            player.playCardToField(handIdx - 1, fieldSlot - 1);
        }
    }

    // Permette al giocatore di effettuare attacchi multipli finché ha carte pronte
    private void handleAttackPhase(Player attacker, Player defender) {
        while (true) {
            showBattle();
            System.out.println("--- FASE DI ATTACCO ---");
            System.out.print("Quale carta attacca? (1-5) o 0 per FINIRE IL TURNO: ");
            int myIdx = scanner.nextInt();
            
            if (myIdx == 0) break;

            Card chosenCard = attacker.getField()[myIdx - 1];
            if (chosenCard == null || chosenCard.isHasAttacked()) {
                System.out.println("[!] Carta non valida o già usata!");
                continue;
            }

            System.out.print("Scegli bersaglio nemico (1-5): ");
            int targetIdx = scanner.nextInt();

            resolveTurn(attacker, defender, myIdx - 1, targetIdx - 1);
            chosenCard.setHasAttacked(true); // Segna la carta come utilizzata

            if (isGameOver()) break;
        }
    }

    // Stampa la grafica del campo di battaglia con le statistiche delle carte
    public void showBattle() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println(String.format("%45s", "STATO DEL CAMPO"));
        System.out.println("=".repeat(80));

        System.out.println("GIOCATORE 1: " + p1.getPlayerName() + " | HP: " + p1.getPlayerHP());
        printDetailedField(p1);
        
        System.out.println("\n" + " ".repeat(30) + "VS" + "\n");
        
        printDetailedField(p2);
        System.out.println("GIOCATORE 2: " + p2.getPlayerName() + " | HP: " + p2.getPlayerHP());
        System.out.println("=".repeat(80));
    }

    // Mostra i dettagli di ogni carta presente negli slot del giocatore
    private void printDetailedField(Player p) {
        for (int i = 0; i < p.getField().length; i++) {
            Card c = p.getField()[i];
            if (c != null) {
                String ready = c.isHasAttacked() ? "STREMA" : "PRONTA";
                System.out.print(String.format("[%d:%s HP:%d ATK:%d EL:%s ARM:%d AB:%s %s]  ", 
                    (i + 1), c.getName(), c.getLife(), c.getDamage(), c.getElement(), c.getArmor(),
                    c.getAbility().getName(), ready));
            } else {
                System.out.print("[" + (i + 1) + ": --- VUOTO --- ]  ");
            }
            if ((i + 1) % 2 == 0) System.out.println(); 
        }
        System.out.println();
    }

    // Calcola se l'attacco va alla carta nemica o direttamente ai punti vita del giocatore
    public void resolveTurn(Player attacker, Player defender, int myIdx, int targetIdx) {
        Card attackerCard = attacker.getField()[myIdx];
        if (attackerCard == null) return;

        if (defender.getField()[targetIdx] == null) {
            int dmg = attackerCard.getDamage();
            defender.setPlayerHP(defender.getPlayerHP() - dmg);
            System.out.println("\n>>> ATTACCO DIRETTO! " + dmg + " danni a " + defender.getPlayerName());
        } else {
            attacker.attack(myIdx, targetIdx, defender);
        }
    }

    // Controlla se le condizioni di fine partita sono soddisfatte
    public boolean isGameOver() {
        return p1.getPlayerHP() <= 0 || p2.getPlayerHP() <= 0 || p1.getDeck().isEmpty() || p2.getDeck().isEmpty();
    }

    // Stampa il messaggio finale con il nome del vincitore
    private void printWinner() {
        System.out.println("\n" + "*".repeat(80));
        
        if (p1.getPlayerHP() <= 0) {
            System.out.println(">>> " + p1.getPlayerName() + " ha esaurito tutti i suoi HP!");
            System.out.println(">>> VINCITORE: " + p2.getPlayerName() + "!");
        } else if (p2.getPlayerHP() <= 0) {
            System.out.println(">>> " + p2.getPlayerName() + " ha esaurito tutti i suoi HP!");
            System.out.println(">>> VINCITORE: " + p1.getPlayerName() + "!");
        } else {
            // Caso in cui la partita finisce per mancanza di carte nel mazzo
            System.out.println(">>> La partita termina per esaurimento delle carte nel mazzo.");
            System.out.println(">>> IL MATCH FINISCE IN PAREGGIO O PER RESA TECNICA!");
        }
        
        System.out.println("*".repeat(80) + "\n");
    }
}