package com.mygdx.game.components;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.mygdx.game.GameSettings;

import java.util.ArrayList;

public class RecordListView extends TextView{

    public RecordListView(BitmapFont font, float x, float y) {
        super(font, x, y,"");
    }
    public void setRecords(ArrayList<Integer> recordsList) {
        text = "";
        int countOfRows = Math.min(recordsList.size(), 5);
        for (int i = 0; i < countOfRows; i++) {
            System.out.println(recordsList.get(i));
            text += (i + 1) + ". - " + recordsList.get(i) + "\n";
        }

        GlyphLayout glyphLayout = new GlyphLayout(font, text);
        x = (GameSettings.SCREEN_WIDTH - glyphLayout.width) / 2;
    }

}
