package com.example.workflow.bpmn.maintain;

import static com.example.workflow.bpmn.maintain.MaintainProcessBusinessKeyPrefix.MAINTAIN_PROCESS_ADMIN_DEPT;
import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.ADMIN_DEPT_PROCESS_BUSINESS_KEY;
import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.ADMIN_DEPT_PROCESS_INSTANCE_ID;
import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.OWNER_MAINTAIN_PROCESS_BUSINESS_KEY;
import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.OWNER_MAINTAIN_PROCESS_INSTANCE_ID;
import static com.example.workflow.bpmn.maintain.MaintainProcessMessageNames.MSG_OWNER_MAINTAIN_FORM_FILLED;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.ProcessInstance;

@Slf4j
public class MessageOwnerMaintainFormFilled implements JavaDelegate {

  @Override
  public void execute(DelegateExecution delegateExecution) {
    log.info("业主维修单已填写-消息事件触发");

    RuntimeService runtimeService = delegateExecution.getProcessEngine().getRuntimeService();

    delegateExecution
        .setVariable(OWNER_MAINTAIN_PROCESS_INSTANCE_ID, delegateExecution.getProcessInstanceId());
    delegateExecution
        .setVariable(OWNER_MAINTAIN_PROCESS_BUSINESS_KEY, delegateExecution.getProcessBusinessKey());

    ProcessInstance adminDeptStageInstance = runtimeService
        .startProcessInstanceByMessage(MSG_OWNER_MAINTAIN_FORM_FILLED, MAINTAIN_PROCESS_ADMIN_DEPT+delegateExecution.getCurrentActivityId(),
            delegateExecution.getVariables());

    delegateExecution
        .setVariable(ADMIN_DEPT_PROCESS_INSTANCE_ID, adminDeptStageInstance.getId());
    delegateExecution
        .setVariable(ADMIN_DEPT_PROCESS_BUSINESS_KEY, adminDeptStageInstance.getBusinessKey());
  }

}
