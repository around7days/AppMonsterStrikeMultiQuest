package main.bean;

/**
 * マルチクエスト情報Bean
 * @author 7days
 */
public class MultiQuestInfoBean {

    /** ID */
    private String id;

    /** 時間 */
    private String time;

    /** クエストメッセージ */
    private String questMsg;

    /** クエストタイトル */
    private String questTitle;

    /** クエストURL */
    private String questUrl;

    /**
     * IDを取得します。
     * @return ID
     */
    public String getId() {
        return id;
    }

    /**
     * IDを設定します。
     * @param id ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 時間を取得します。
     * @return 時間
     */
    public String getTime() {
        return time;
    }

    /**
     * 時間を設定します。
     * @param time 時間
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * クエストメッセージを取得します。
     * @return クエストメッセージ
     */
    public String getQuestMsg() {
        return questMsg;
    }

    /**
     * クエストメッセージを設定します。
     * @param questMsg クエストメッセージ
     */
    public void setQuestMsg(String questMsg) {
        this.questMsg = questMsg;
    }

    /**
     * クエストタイトルを取得します。
     * @return クエストタイトル
     */
    public String getQuestTitle() {
        return questTitle;
    }

    /**
     * クエストタイトルを設定します。
     * @param questTitle クエストタイトル
     */
    public void setQuestTitle(String questTitle) {
        this.questTitle = questTitle;
    }

    /**
     * クエストURLを取得します。
     * @return クエストURL
     */
    public String getQuestUrl() {
        return questUrl;
    }

    /**
     * クエストURLを設定します。
     * @param questUrl クエストURL
     */
    public void setQuestUrl(String questUrl) {
        this.questUrl = questUrl;
    }
}
