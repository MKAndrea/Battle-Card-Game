package battleCardGame;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Card {
    // Attributi principali della carta
    private String name;
    private int life;
    private int damage;
    private int armor;
    
    // Possibili tipi elementali
    public enum Elements { FIRE, WATER, WIND, EARTH };
    private Elements element;
    
    private Ability ability;
    private boolean hasAttacked = false; // Indica se la carta ha già agito nel turno attuale
    
    // Elenco delle abilità speciali con nome e descrizione
    @Getter
    @AllArgsConstructor
    public enum Ability {
        NONE("Nessuna", "Poteri particolari assenti."),
        LIFE_STEAL("Ruba Vita", "Recupera HP pari al 50% del danno inflitto."),
        PIERCING("Perforante", "Ignora l'armatura dell'avversario."),
        THORNS("Spine", "Riflette il 30% del danno ricevuto."),
        BERSERK("Furia", "Aumenta l'attacco di +5 quando colpita."),
        DIVINE_SHIELD("Scudo Divino", "Ignora il primo attacco ricevuto."),
        POISON("Veleno", "Riduce l'armatura nemica di 2 ogni colpo."),
        LUCKY_STRIKE("Colpo Critico", "25% di probabilità di danno doppio.");

        private final String name;
        private final String description;
    }
    
    // Costruttore che genera una carta con statistiche, elemento e abilità casuali
    public Card(String name) {
        this.name = name;
        
        // Generazione casuale delle statistiche base
        this.life = (int) (Math.random() * 50) + 1;
        this.damage = (int) (Math.random() * 50) + 1;
        this.armor = (int) (Math.random() * 17);
        
        // Assegnazione casuale di un elemento dall'enum Elements
        this.element = Elements.values()[(int) (Math.random() * Elements.values().length)];
        
        // Assegnazione casuale di un'abilità dall'enum Ability
        this.ability = Ability.values()[(int) (Math.random() * Ability.values().length)];
    }
}