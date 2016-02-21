package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.base.AjaxErrorException;
import main.bean.MultiQuestInfoBean;
import main.consts.Consts;
import main.logic.Logic;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class MainServlet
 */
@WebServlet("/main")
public class MainServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /** logger */
    private static final Logger logger = Logger.getLogger(MainServlet.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainServlet() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        logger.info("------ start[MonsterStrikeMultiQuest] -----");
        try {
            execute(request, response);
        } catch (AjaxErrorException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("System Error", e);
            throw e;
        } finally {
            logger.info("------ end  [MonsterStrikeMultiQuest] -----");
        }
    }

    /**
     * メイン処理
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    private void execute(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        /*
         * リクエスト情報の取得
         */
        String bbsType = request.getParameter("bbsType");

        /*
         * モンストマルチクエスト情報の取得（Json形式）
         */
        String json = accessMonsterStrikeGameWith(bbsType);

        /*
         * レスポンス返却
         */
        response.setContentType("application/json");
        response.getWriter().write(json);
    }

    /**
     * モンストマルチクエスト情報の取得（Json形式）
     * @param type
     * @return ダーツライブ情報
     * @throws IOException
     * @throws ServletException
     */
    protected String accessMonsterStrikeGameWith(String bbsType) throws ServletException, IOException {

        // ロジッククラスの生成
        Logic logic = new Logic();

        /*
         * マルチ情報の取得処理 ※値が取得できるまで指定回数繰り返す
         */
        List<MultiQuestInfoBean> multiInfoList = new ArrayList<>();
        for (int loopCnt = 0; loopCnt < Consts.LOOP_CNT_MAX; loopCnt++) {
            multiInfoList = logic.multiQuestData(bbsType);
            if (!multiInfoList.isEmpty()) {
                break;
            }
        }

        /*
         * Json形式に変換
         */
        String json = logic.convertJson(multiInfoList);

        return json;
    }
}
