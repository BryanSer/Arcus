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
        for (AbilityType abilityType : AbilityType.values()) {
            contains[index] = Item.getNewInstance(abilityType.getDisplayItem());
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
