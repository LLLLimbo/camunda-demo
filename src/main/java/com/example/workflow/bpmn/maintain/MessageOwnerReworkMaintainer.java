package com.example.workflow.bpmn.maintain;

import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.MAINTAINER_PROCESS_BUSINESS_KEY;
import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.MAINTAINER_PROCESS_INSTANCE_ID;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.ProcessInstance;

@Slf4j
public class MessageOwnerReworkMaintainer implements JavaDelegate {

  @Override
  public void execute(DelegateExecution delegateExecution) throws Exception {
    log.info("业主验收未通过(维修工)-消息事件触发");

    RuntimeService runtimeService = delegateExecution.getProcessEngine().getRuntimeService();

    ProcessInstance maintainerProcessInstance = runtimeService
        .startProcessInstanceByMessage("MessageOwnerReworkMaintainer", "MaintainProcess_Maintain",
            delegateExecution.getVariables());

    delegateExecution
        .setVariable(MAINTAINER_PROCESS_INSTANCE_ID,
            maintainerProcessInstance.getProcessInstanceId());
    delegateExecution
        .setVariable(MAINTAINER_PROCESS_BUSINESS_KEY, maintainerProcessInstance.getBusinessKey());
  }
}
