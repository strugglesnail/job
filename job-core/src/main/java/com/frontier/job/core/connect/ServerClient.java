package com.frontier.job.core.connect;


import com.frontier.job.core.model.IdleBeatParam;
import com.frontier.job.core.model.KillParam;
import com.frontier.job.core.model.ReturnT;
import com.frontier.job.core.model.TriggerParam;

/**
 * Created by w
 */
public interface ServerClient {

    /**
     * beat
     * @return
     */
    ReturnT<String> beat();

    /**
     * idle beat
     *
     * @param idleBeatParam
     * @return
     */
    ReturnT<String> idleBeat(IdleBeatParam idleBeatParam);

    /**
     * run
     * @param triggerParam
     * @return
     */
    ReturnT<String> run(TriggerParam triggerParam);

    ReturnT<String> add(TriggerParam triggerParam);

    // 更新任务
    ReturnT<String> update(TriggerParam triggerParam);

    // 暂停任务
    ReturnT<String> pause(TriggerParam triggerParam);

    // 恢复任务
    ReturnT<String> resume(TriggerParam triggerParam);



    /**
     * kill
     * @param killParam
     * @return
     */
    ReturnT<String> kill(KillParam killParam);

    /**
     * log
     * @param logParam
     * @return
     */
//    ReturnT<LogResult> log(LogParam logParam);

}
