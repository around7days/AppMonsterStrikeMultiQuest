package main.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.base.AjaxErrorException;
import main.bean.MultiQuestInfoBean;

import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

/**
 * Logicクラス
 * @author 7days
 */
public class Logic {

    /** logger */
    private static final Logger logger = Logger.getLogger(Logic.class);

    /**
     * マルチクエスト情報取得処理
     * @param bbsType 掲示板タイプ
     * @return マルチ情報リスト
     * @throws IOException IOException
     * @throws AjaxErrorException AjaxErrorException
     */
    public List<MultiQuestInfoBean> multiQuestData(String bbsType) throws IOException, AjaxErrorException {

        // モンストマルチクエスト情報リストの生成
        List<MultiQuestInfoBean> resultList = new ArrayList<>();

        /*
         * 接続
         */
        // URLの生成
        String url = "http://xn--eckwa2aa3a9c8j8bve9d.gamewith.jp/bbs/matching/threads/show/" + bbsType;
        // String url = "http://モンスターストライク.gamewith.jp/bbs/matching/threads/show/3";
        // String url = Utils.encodeUrl("http://モンスターストライク.gamewith.jp/bbs/matching/threads/show/3");

        // コネクションの生成
        Connection connection = Jsoup.connect(url);
        // タイムアウト設定
        connection.timeout(/* ms */1 * 1000); // 1秒

        // 接続
        Document document = null;
        for (int cnt = 0; cnt < /* retryCnt */3; cnt++) {
            try {
                document = connection.get();
            } catch (IOException e) {
                logger.warn("connect time out");
            }
        }

        // 接続チェック
        String title = document.select("title").text();
        if (!title.startsWith("【モンスト】")) {
            // タイトルが「【モンスト】」で始まらない場合
            // 異常
            return resultList;
        }

        /*
         * 結果取得
         */
        // ユーザー名の取得 ※class="playerName"内の<a>タグから値を取得
        Elements bbsList = document.select("#bbs-posts>.bbs-content");
        for (Element bbs : bbsList) {
            try {
                // 値取得
                String id = bbs.select(".bbs-post-number").text();
                String time = bbs.select(".bbs-posted-time").text();
                String questUrl = bbs.select(".bbs-post-body>a").first().text();
                if (bbs.select(".bbs-post-body>a").isEmpty()) {
                    continue;
                }

                String body = bbs.select(".bbs-post-body").text();
                logger.debug(body);
                String questMsg = body.split("「")[0].replaceAll("モンストでマルチしない？", "").trim();
                String questTitle = body.split("「|」")[1];

                // 結果結果格納
                MultiQuestInfoBean multiQuestInfo = new MultiQuestInfoBean();
                multiQuestInfo.setId(id);
                multiQuestInfo.setTime(time);
                multiQuestInfo.setQuestMsg(questMsg);
                multiQuestInfo.setQuestTitle(questTitle);
                multiQuestInfo.setQuestUrl(questUrl);

                // リストに追加
                resultList.add(multiQuestInfo);

            } catch (Exception e) {
                logger.error(e);
            }

        }

        // 正常
        return resultList;
    }

    /**
     * Json形式に変換
     * @param multiQuestInfoList クエスト情報一覧
     * @return json
     */
    public String convertJson(List<MultiQuestInfoBean> multiQuestInfoList) {

        // Json変換対象をマップに格納
        HashMap<String, Object> map = new HashMap<>();
        map.put("multiQuestInfoList", multiQuestInfoList);

        // 変換
        Gson gson = new Gson();
        String json = gson.toJson(multiQuestInfoList);
        logger.debug(json);

        return json;
    }
}
