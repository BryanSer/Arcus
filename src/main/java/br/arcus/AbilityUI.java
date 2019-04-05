package br.arcus;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;

import Br.API.GUI.Ex.BaseUI;
import Br.API.GUI.Ex.Item;
import Br.API.GUI.Ex.SnapshotFactory;
import br.arcus.potential.GemPotential;
import br.arcus.potential.Line1Kt;

public class AbilityUI extends BaseUI {
    private SnapshotFactory factory = SnapshotFactory.getDefaultSnapshotFactory();
    private Item[] contains = new Item[54];

    {
        super.Name = "AR_AUI";
        super.AllowShift = false;
        super.Rows = 6;
        super.DisplayName = "§6能力与潜能";
    }

    {
        int index = 0;
        for (AbilityType at : AbilityType.values()) {
            contains[index] = Item.getNewInstance(at.getDisplayItem());
            int it = index + 1;
            for (int lv = 0; lv < 8; lv++) {
                Ability a = at.getAbility(lv);
                if (a != null) {
                    contains[it + lv] = a.getDisplayItem();
                }
            }
            index += 9;
        }

        index = 27;
        for (Potential p : Data.registeredPotential.values()) {
            contains[index + p.getIndex()] = p.getDisplayItem();
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
