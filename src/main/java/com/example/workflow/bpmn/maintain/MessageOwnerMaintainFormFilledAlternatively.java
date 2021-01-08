package com.example.workflow.bpmn.maintain;

import static com.example.workflow.bpmn.maintain.MaintainProcessBusinessKeyPrefix.MAINTAIN_PROCESS_OWNER;
import static com.example.workflow.bpmn.maintain.MaintainProcessMessageNames.MSG_OWNER_MAINTAIN_FORM_FILLED_ALTER;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

@Slf4j
public class MessageOwnerMaintainFormFilledAlternatively implements JavaDelegate {

  @Override
  public void execute(DelegateExecution delegateExecution) throws Exception {
    log.info("业主维修单客服已代填-消息事件触发");

    RuntimeService runtimeService = delegateExecution.getProcessEngine().getRuntimeService();

    delegateExecution
        .setVariable("alternativelyFilledBy", delegateExecution.getVariable("starter"));

    Map<String, Object> variables = new HashMap<>(delegateExecution.getVariables());
    variables.put("starter", "ownerName");

    runtimeService
        .startProcessInstanceByMessage(MSG_OWNER_MAINTAIN_FORM_FILLED_ALTER,
            MAINTAIN_PROCESS_OWNER+delegateExecution.getCurrentActivityId(),
            variables);
  }
}
