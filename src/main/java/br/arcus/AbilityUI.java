package br.arcus;

import org.bukkit.entity.Player;

import Br.API.GUI.Ex.BaseUI;
import Br.API.GUI.Ex.Item;
import Br.API.GUI.Ex.SnapshotFactory;

public class AbilityUI extends BaseUI {
    private SnapshotFactory factory = SnapshotFactory.getDefaultSnapshotFactory();
    private Item[] contains = new Item[54];

    {
        int index = 0;
        for (AbilityType at : AbilityType.values()) {
            contains[index] = Item.getNewInstance(at.getDisplayItem());
            int it = index + 1;
            for (int lv = 0; lv < 9; lv++) {
                Ability a = at.getAbility(lv);
                if (a != null) {
                    contains[it + lv] = a.getDisplayItem();
                }
            }
            index += 9;
        }
    }

    @Override
    public Item getItem(Player player, int i) {
        return null;
    }

    @Override
    public SnapshotFactory getSnapshotFactory() {
        return factory;
    }
}
