package com.example.workflow.bpmn.maintain;

import static com.example.workflow.bpmn.maintain.MaintainProcessBusinessKeyPrefix.MAINTAIN_PROCESS_MAINTAINER;
import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.MAINTAINER_PROCESS_BUSINESS_KEY;
import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.MAINTAINER_PROCESS_INSTANCE_ID;
import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.MAINTAIN_DEPT_MANAGER_PROCESS_BUSINESS_KEY;
import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.MAINTAIN_DEPT_MANAGER_PROCESS_INSTANCE_ID;
import static com.example.workflow.bpmn.maintain.MaintainProcessMessageNames.MSG_MAINTAIN_DEPT_MANAGER_ASSIGN;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.ProcessInstance;

@Slf4j
public class MessageMaintainDeptManagerAssign implements JavaDelegate {

  @Override
  public void execute(DelegateExecution delegateExecution) throws Exception {
    log.info("维修部主管已分配工作-消息事件触发");

    RuntimeService runtimeService = delegateExecution.getProcessEngine().getRuntimeService();

    delegateExecution
        .setVariable(MAINTAIN_DEPT_MANAGER_PROCESS_INSTANCE_ID, delegateExecution.getProcessInstanceId());
    delegateExecution
        .setVariable(MAINTAIN_DEPT_MANAGER_PROCESS_BUSINESS_KEY, delegateExecution.getProcessBusinessKey());

    ProcessInstance maintainerProcessInstance = runtimeService
        .startProcessInstanceByMessage(MSG_MAINTAIN_DEPT_MANAGER_ASSIGN, MAINTAIN_PROCESS_MAINTAINER+delegateExecution.getCurrentActivityId(),
            delegateExecution.getVariables());

    delegateExecution
        .setVariable(MAINTAINER_PROCESS_INSTANCE_ID, maintainerProcessInstance.getProcessInstanceId());
    delegateExecution
        .setVariable(MAINTAINER_PROCESS_BUSINESS_KEY, maintainerProcessInstance.getBusinessKey());

  }
}
