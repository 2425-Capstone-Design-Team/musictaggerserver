package com.tftf.musictaggerserver.controller;

import com.tftf.musictaggerserver.db.PlayHistoryDAO;
import com.tftf.util.PlayHistory;
import com.tftf.util.PlayInform;
import com.tftf.util.Surroundings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping(value="data_generate")
public class DataGenerateController {
    @Autowired
    private PlayHistoryDAO playHistoryDAO;

    static Random random = new Random(System.currentTimeMillis());

    public String getRandomString(char start, int bound, int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=0; i<length; i++) {
            stringBuilder.append((char)(start + random.nextInt(bound)));
        }
        return stringBuilder.toString();
    }

    public String getRandomEmail() {
        int engLength = 4 + random.nextInt(4);
        int numLength = 2 + random.nextInt(4);
        int domainLength = 4 + random.nextInt(4);

        String engPart = getRandomString('a', 26, engLength);
        String numPart = getRandomString('0', 10, numLength);
        String domainPart = getRandomString('a', 26, domainLength);

        return engPart + numPart + '@' + domainPart + ".com";
    }

    public Surroundings getRandomSurroundings() {
        Surroundings surroundings = new Surroundings();

        String[] categoryList = { "시간", "날씨", "계절", "요일" };
        String[][] tagList = {
                {"새벽", "이른 아침", "늦은 아침", "이른 오후", "늦은 오후", "밤"},
                {"맑음", "구름", "소나기", "비", "천둥", "눈", "안개"},
                {"봄", "여름", "가을", "겨울"},
                {"월", "화", "수", "목", "금", "토", "일"}
        };

        for (int i=0; i<categoryList.length; i++) {
            String category = categoryList[i];
            int tagIdx = random.nextInt(tagList[i].length);
            surroundings.infoMap.replace(category, tagList[i][tagIdx]);
        }

        return surroundings;
    }

    @GetMapping(value = "/history")
    public boolean generate_history(@RequestParam int count) {

        for (int t = 0; t < count; t++) {
            String userID = getRandomEmail();
            int musicID = 1000 + random.nextInt(42);

            int generateCount = 3 + random.nextInt(4);
            for (int i = 0; i < generateCount; i++) {
                Surroundings surroundings = getRandomSurroundings();
                Long playedTime = 30000 + random.nextLong(70000);

                PlayHistory history = playHistoryDAO.select(userID, musicID);

                if (history == null) {
                    history = new PlayHistory(userID, musicID);
                    history.cumulatePlayedTime(playedTime, surroundings);
                    playHistoryDAO.insert(history);
                } else {
                    history.cumulatePlayedTime(playedTime, surroundings);
                    playHistoryDAO.update(history);
                }
            }
        }

        return true;
    }
}