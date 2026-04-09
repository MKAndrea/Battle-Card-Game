package battleCardGame;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Player {
    private String playerName;
    private int playerHP = 100;
    
    // Lista dinamica per la mano: si ridimensiona da sola quando aggiungi o togli carte
    private List<Card> hand = new ArrayList<>();
    
    // Array fisso per il campo: rappresenta i 5 slot fisici sul terreno di gioco
    private Card[] field = new Card[5];
    private Deck deck;

    public Player(String playerName, Deck deck) {
        this.playerName = playerName;
        this.deck = deck;
    }

    // Sposta una carta dalla mano a uno slot libero sul campo
    public boolean playCardToField(int handIndex, int fieldSlot) {
        // Verifica che l'indice della mano scelto esista
        if (handIndex >= 0 && handIndex < hand.size()) {
            // Verifica che lo slot sul campo sia valido e libero
            if (fieldSlot >= 0 && fieldSlot < field.length && field[fieldSlot] == null) {
                // Rimuove la carta dalla lista (mano) e la mette nell'array (campo)
                Card cardToPlay = hand.remove(handIndex);
                field[fieldSlot] = cardToPlay;
                return true;
            }
            System.out.println("[!] Slot del campo occupato o non valido!");
        } else {
            System.out.println("[!] Indice mano non valido!");
        }
        return false;
    }

    // Pesca carte dal mazzo fino ad averne 5 in mano
    public void fillHand() {
        while (hand.size() < 5 && !deck.isEmpty()) {
            Card drawn = deck.drawCard();
            if (drawn != null) {
                hand.add(drawn);
            }
        }
    }

    // Mostra a video le carte presenti in mano con tutte le loro statistiche
    public void showHand() {
        System.out.println("\n--- MANO DI " + playerName.toUpperCase() + " ---");
        if (hand.isEmpty()) {
            System.out.println("La tua mano è vuota!");
        } else {
            for (int i = 0; i < hand.size(); i++) {
                Card c = hand.get(i);
                System.out.println("[" + (i + 1) + "] " + c.getName() + 
                                   " - HP: " + c.getLife() + 
                                   " | ARMOR: " + c.getArmor() + 
                                   " | ATK: " + c.getDamage() + 
                                   " | EL: " + c.getElement() + 
                                   " | AB: " + c.getAbility().getName());
            }
        }
    }

    public void attack(int myIndex, int opponentIndex, Player opponent) {
        Card myCard = this.field[myIndex];
        Card opponentCard = opponent.getField()[opponentIndex];
        
        if (myCard == null || opponentCard == null) return;

        System.out.println("\n--- SCONTRO: " + myCard.getName() + " VS " + opponentCard.getName() + " ---");

        // 1. GESTIONE SCUDO DIVINO
        if (opponentCard.getAbility() == Card.Ability.DIVINE_SHIELD) {
            System.out.println("[!] ABILITÀ: Lo Scudo Divino di " + opponentCard.getName() + " ha parato il colpo!");
            opponentCard.setAbility(Card.Ability.NONE); 
            return; 
        }

        int baseAtk = myCard.getDamage();

        // 2. GESTIONE CONTROLLO LUCKY STRIKE ---
        if (myCard.getAbility() == Card.Ability.LUCKY_STRIKE && Math.random() < 0.25) {
            baseAtk *= 2;
            System.out.println("[⭐] ABILITÀ: Colpo Critico! Il danno base raddoppia a " + baseAtk);
        }

        // 3. CALCOLO MOLTIPLICATORE ELEMENTALE
        double multiplier = calculateMultiplier(myCard.getElement(), opponentCard.getElement());
        
        if (multiplier > 1.0) {
            System.out.println("[🔥] VANTAGGIO ELEMENTALE! " + myCard.getElement() + " vs " + opponentCard.getElement() + " (Danno x2.0)");
        }

        int totalAtk = (int) (baseAtk * multiplier);

        // 4. CALCOLO DANNO FINALE (Armor o Piercing)
        int damageDone;
        if (myCard.getAbility() == Card.Ability.PIERCING) {
            damageDone = totalAtk;
            System.out.println("[!] ABILITÀ: Perforazione! Ignora l'armatura.");
        } else {
            damageDone = Math.max(0, totalAtk - opponentCard.getArmor());
            if (opponentCard.getArmor() > 0 && damageDone > 0) {
                System.out.println("[V] L'armatura riduce il danno di " + opponentCard.getArmor());
            }
        }

        // 5. APPLICAZIONE DANNO
        opponentCard.setLife(opponentCard.getLife() - damageDone);
        System.out.println(">>> " + myCard.getName() + " infligge " + damageDone + " danni.");

        // 6. EFFETTI SECONDARI E CONTROLLO MORTI
        applyOnHitEffects(myCard, opponentCard, damageDone);
        checkDeaths(myIndex, opponentIndex, opponent);
    }

    // Gestisce le abilità che si attivano nel momento in cui viene inflitto un danno
    private void applyOnHitEffects(Card attacker, Card defender, int damage) {
        if (attacker.getAbility() == Card.Ability.POISON && damage > 0) {
            defender.setArmor(Math.max(0, defender.getArmor() - 2));
            System.out.println("[!] ABILITÀ: Veleno! Corazza ridotta.");
        }
        if (defender.getAbility() == Card.Ability.BERSERK && damage > 0 && defender.getLife() > 0) {
            defender.setDamage(defender.getDamage() + 5);
            System.out.println("[!] ABILITÀ: Berserk! Attacco aumentato.");
        }
        if (attacker.getAbility() == Card.Ability.LIFE_STEAL && damage > 0) {
            int heal = damage / 2;
            attacker.setLife(attacker.getLife() + heal);
            System.out.println("[!] ABILITÀ: Life Steal! Curato di " + heal);
        }
        if (defender.getAbility() == Card.Ability.THORNS && damage > 0) {
            int recoil = (int)(damage * 0.3);
            attacker.setLife(attacker.getLife() - recoil);
            System.out.println("[!] ABILITÀ: Spine! Riflessi " + recoil + " danni.");
        }
    }

    // Calcola il bonus di danno (x2) basato sul tipo elementale
    private double calculateMultiplier(Card.Elements a, Card.Elements d) {
        if ((a == Card.Elements.FIRE && d == Card.Elements.WIND) ||
            (a == Card.Elements.WIND && d == Card.Elements.EARTH) ||
            (a == Card.Elements.EARTH && d == Card.Elements.WATER) ||
            (a == Card.Elements.WATER && d == Card.Elements.FIRE)) {
            return 2.0;
        }
        return 1.0;
    }
    
    // Controlla la vita delle carte e le rimuove dal campo se arrivano a 0
    private void checkDeaths(int myIndex, int opponentIndex, Player opponent) {
        if (opponent.getField()[opponentIndex].getLife() <= 0) {
            System.out.println(">>> " + opponent.getField()[opponentIndex].getName() + " distrutta!");
            opponent.getField()[opponentIndex] = null;
        }
        if (this.field[myIndex] != null && this.field[myIndex].getLife() <= 0) {
            System.out.println(">>> " + this.field[myIndex].getName() + " caduta!");
            this.field[myIndex] = null;
        }
    }
    
    // Ripristina la possibilità di attacco per tutte le carte sul campo a inizio turno
    public void resetCardsReady() {
        for (Card c : field) {
            if (c != null) {
                c.setHasAttacked(false);
            }
        }
    }
    
 // Controlla se tutti gli slot del campo sono occupati
    public boolean isFieldFull() {
        for (Card c : field) {
            if (c == null) return false; // Se trova anche solo uno slot null, non è pieno
        }
        return true;
    }
}