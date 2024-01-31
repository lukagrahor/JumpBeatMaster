package com.lukagrahor.jumpbeat.Items;

import com.badlogic.gdx.math.Vector2;

public class ItemDef {
    public Vector2 position;
    public Class<?> type;

    public ItemDef(Vector2 position,Class<?>type){//ta razred uporabljamo za definiranje objekta in ga v PlayScreen-u shranimo v vrsto
        this.position=position;
        this.type=type;
    }
}
