package util.seqExecFunction;

import org.apache.log4j.Logger;

/**
 * 顺序执行的业务逻辑
 * 方法必须以exec开头，数字x表示顺序，数字越小，执行顺序越靠前.
 *
 * @author jiaolong
 * @date 2024/12/16 15:56
 **/
public class ExecServiceOperate {
    private static final Logger logger = Logger.getLogger(ExecServiceOperate.class);
    private LoadDao load = new LoadDao();

    public boolean exec1() {
        load.loadData();
        logger.info("a");
        return true;
    }

    public boolean exec2() {
        logger.info("b");
        return false;
    }

    public boolean exec3() {
        logger.info("c");
        return true;
    }
}

class LoadDao {
    public void loadData() {
        System.out.println("LoadDao.loadData(), load data init ....");
    }
}
