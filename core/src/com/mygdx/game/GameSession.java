package com.mygdx.game;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.managers.MemoryManager;
import com.mygdx.game.screens.ScreenGame;
import com.mygdx.game.screens.SettingsScreen;

import java.util.ArrayList;

public class GameSession {
public GameState state;
ScreenGame screenGame;
SettingsScreen settingsScreen;
    long nextTrashSpawnTime;
    long sessionStartTime;
    long pauseStartTime;

    public GameSession() {
    }

    public void startGame() {
        state = GameState.PLAYING;
        screenGame.counter = 0;
        sessionStartTime = TimeUtils.millis();
        nextTrashSpawnTime = sessionStartTime + (long) (GameSettings.STARTING_TRASH_APPEARANCE_COOL_DOWN
                * getTrashPeriodCoolDown());
    }
    public void pauseGame() {
        state = GameState.PAUSED;
        pauseStartTime = TimeUtils.millis();
    }
    public void resumeGame() {
        state = GameState.PLAYING;
        sessionStartTime += TimeUtils.millis() - pauseStartTime;
    }
    public boolean shouldSpawnTrash() {
        if (nextTrashSpawnTime <= TimeUtils.millis()) {
            nextTrashSpawnTime = TimeUtils.millis() + (long) (GameSettings.STARTING_TRASH_APPEARANCE_COOL_DOWN
                    * getTrashPeriodCoolDown());
            return true;
        }
        return false;
    }

    private float getTrashPeriodCoolDown() {
        return (float) Math.exp(-0.001 * (TimeUtils.millis() - sessionStartTime + 1) / 1000);
    }
    public void endGame() {
        state = GameState.ENDED; // устанавливаем новое состояние для сессии
        ArrayList<Integer> recordsTable = MemoryManager.loadRecordsTable();// загружаем таблицу рекордов из памяти
        if (recordsTable == null) { // делаем проверку для случая, когда ранее не было создано таблицы в игре
            recordsTable = new ArrayList<>(); // создаём пустой массив
        }
        int foundIdx = 0;
        for (; foundIdx < recordsTable.size(); foundIdx++) {
            if (recordsTable.get(foundIdx) < screenGame.counter) break;
        }
        recordsTable.add(foundIdx, screenGame.counter);
        MemoryManager.saveTableOfRecords(recordsTable);
    }
}