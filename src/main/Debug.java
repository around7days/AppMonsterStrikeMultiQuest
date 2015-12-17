package main;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;

/**
 * Debugクラス
 * @author 7days
 */
public class Debug {

    /** logger */
    private static final Logger logger = Logger.getLogger(Debug.class);

    /**
     * デバッグ用
     * @param args 引数
     */
    public static void main(String[] args) {
        logger.info("--------------- start ---------------");
        try {

            new Debug().execute();
        } catch (Exception e) {
            logger.error("system err", e);
        }
        logger.info("---------------- end ----------------");
    }

    /**
     * メイン処理
     * @throws IOException
     * @throws ServletException
     */
    private void execute() throws IOException, ServletException {

        String bbsType = "3";

        String json = new MainServlet().accessMonsterStrikeGameWith(bbsType);

        System.out.println(json);
    }
}
