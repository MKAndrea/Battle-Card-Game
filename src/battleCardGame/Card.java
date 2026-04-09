package battleCardGame;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Card {
	private String name;
	private int life;
	private int damage;
	private int armor;
	public enum Elements{FIRE,WATER,WIND,EARTH};
	private Elements element;
	private Ability ability;
	
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
	
	public Card(String name) {
        this.name = name;
        this.life = (int) (Math.random() * 50) + 1;
        this.damage = (int) (Math.random() * 50) + 1;
        this.armor = (int) (Math.random() * 17);
        
        // Elemento Random
        this.element = Elements.values()[(int) (Math.random() * Elements.values().length)];
        
        // Abilità Random
        this.ability = Ability.values()[(int) (Math.random() * Ability.values().length)];
    }
	
	
	@Override
    public String toString() {
        return String.format(
            "----------------------\n" +
            " CARD: %s\n" +
            "----------------------\n" +
            " Element: %s\n" +
            " Ability: %s\n" +
            " Effect:  %s\n" +
            " Life:    %d HP\n" +
            " Damage:  %d\n" +
            " Armor:   %d DEF\n" +
            "----------------------",
            getName().toUpperCase(), getElement(), ability.getName(), ability.getDescription(), getLife(),
            getDamage(), getArmor());
    }
	
	
}
